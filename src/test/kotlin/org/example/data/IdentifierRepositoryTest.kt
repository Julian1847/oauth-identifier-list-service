package org.example.data

import io.mockk.*
import io.mockk.impl.annotations.MockK
import org.assertj.core.api.Assertions.assertThat
import org.example.config.AppConfiguration
import org.example.entity.Identifier
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.jdbc.core.JdbcTemplate
import java.util.*

class IdentifierRepositoryTest{

    @MockK private val config: AppConfiguration = mockk()
    @MockK private val jdbcTemplate: JdbcTemplate = mockk()

    private val repository = IdentifierRepository(jdbcTemplate)

    @BeforeEach
    fun setUp(){
        every { config.publicUrl } returns "http://localhost:8080"
    }

    @Test
    fun `should save identifier when provided a new Identifier`() {
        val identifier = Identifier(UUID.randomUUID(), 1L,0, 0)
        every { jdbcTemplate.update(any<String>(), any(), any(), any(), any()) } returns 1

        val savedIdentifier = repository.save(identifier)

        assertThat(savedIdentifier).isEqualTo(identifier)
        verify { jdbcTemplate.update("INSERT INTO identifiers (id, list_id, status, status_change_count) VALUES (?, ?, ?, ?)", identifier.id, identifier.listId, identifier.status, identifier.statusChangeCount) }
    }
    @Test
    fun `should return correct count when counting identifiers by listId`() {
        val listId = 1L
        every { jdbcTemplate.queryForObject("SELECT COUNT(*) FROM identifiers WHERE list_id = ?", Long::class.java, listId) } returns 2L

        val count = repository.countByListId(listId)

        assertThat(count).isEqualTo(2L)
        verify { jdbcTemplate.queryForObject("SELECT COUNT(*) FROM identifiers WHERE list_id = ?", Long::class.java, listId) }
    }

    @Test
    fun `should update identifier status and increment status change count`() {
        val identifierId = UUID.randomUUID()
        val listId = 3L
        val newStatus = 2

        every { jdbcTemplate.update("UPDATE identifiers SET status = ?, status_change_count = status_change_count + 1 WHERE list_id = ? AND id = ?", newStatus, listId, identifierId) } returns 1

        repository.updateIdentifierStatus(listId, identifierId, newStatus)

        verify { jdbcTemplate.update("UPDATE identifiers SET status = ?, status_change_count = status_change_count + 1 WHERE list_id = ? AND id = ?", newStatus, listId, identifierId) }
    }

}