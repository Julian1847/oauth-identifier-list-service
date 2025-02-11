package org.example.service

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.config.AppConfiguration
import org.example.data.IdentifierListRepository
import org.example.data.IdentifierRepository
import org.example.entity.Identifier
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.*

class IdentifierServiceTest {

    private val identifierListRepository: IdentifierListRepository = mockk(relaxed = true)
    private val identifierRepository: IdentifierRepository = mockk(relaxed = true)
    private val config: AppConfiguration = mockk {
        every { listSize } returns 100
    }

    private val identifierService = IdentifierService(identifierListRepository, identifierRepository, config)

    @Test
    fun `should create new identifier in existing list`() {
        val latestListId = 1L
        every { identifierListRepository.findLatestListId() } returns latestListId
        every { identifierRepository.countByListId(latestListId) } returns 50
        val newIdentifier = Identifier(UUID.randomUUID(), latestListId, 0, 0)
        every { identifierRepository.save(any()) } returns newIdentifier

        val result = identifierService.createNewIdentifier()

        assertEquals(latestListId, result.listId)
        verify(exactly = 1) { identifierRepository.save(any()) }
    }

    @Test
    fun `should create new identifier in new list when latest list is full`() {
        val newListId = 2L
        every { identifierListRepository.findLatestListId() } returns 1L
        every { identifierRepository.countByListId(1L) } returns 100
        every { identifierListRepository.createNewList() } returns newListId
        val newIdentifier = Identifier(UUID.randomUUID(), newListId, 0, 0)
        every { identifierRepository.save(any()) } returns newIdentifier

        val result = identifierService.createNewIdentifier()

        assertEquals(newListId, result.listId)
        verify(exactly = 1) { identifierListRepository.createNewList() }
        verify(exactly = 1) { identifierRepository.save(any()) }
    }

    @Test
    fun `should update identifier status`() {

        val uri = "http://localhost:8080/identifier-list/3"
        val identifierUuid = UUID.randomUUID()
        val listId = 3L

        identifierService.updateIdentifierStatus(uri, identifierUuid, 1)

        verify(exactly = 1) { identifierRepository.updateIdentifierStatus(listId, identifierUuid, 1) }
    }
}
