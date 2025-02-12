package org.example.data

import io.mockk.*
import io.mockk.impl.annotations.MockK
import org.assertj.core.api.Assertions.assertThat

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.JdbcTemplate

class IdentifierListRepositoryTest {

    @MockK
    private lateinit var jdbcTemplate: JdbcTemplate
    private lateinit var repository: IdentifierListRepository

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        repository = IdentifierListRepository(jdbcTemplate)
    }

    @Test
    fun `should return latest list id when available`() {
        every { jdbcTemplate.queryForObject("SELECT id FROM identifier_lists ORDER BY id DESC LIMIT 1", Long::class.java) } returns 10L

        val latestListId = repository.findLatestListId()

        assertThat(latestListId).isEqualTo(10L)
    }

    @Test
    fun `should return null when no latest list id exists`() {
        every { jdbcTemplate.queryForObject("SELECT id FROM identifier_lists ORDER BY id DESC LIMIT 1", Long::class.java) } throws EmptyResultDataAccessException(1)

        val latestListId = repository.findLatestListId()

        assertThat(latestListId).isNull()
    }

    @Test
    fun `should create new list and return id`() {
        every { jdbcTemplate.queryForObject("INSERT INTO identifier_lists DEFAULT VALUES RETURNING id", Long::class.java) } returns 42L

        val newListId = repository.createNewList()

        assertThat(newListId).isEqualTo(42L)
    }

    @Test
    fun `should return list of revoked identifiers by list id`() {
        val listId = 5
        val expectedIdentifiers = listOf(Identifier("id1", 1), Identifier("id2", 1))
        every { jdbcTemplate.query(any<String>(), any<IdentifierRowMapper>(), eq(listId)) } returns expectedIdentifiers

        val identifiers = repository.findByListId(listId)

        assertThat(identifiers).isEqualTo(expectedIdentifiers)
    }
}