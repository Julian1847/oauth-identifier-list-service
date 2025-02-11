package org.example.service

import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import org.example.data.Identifier
import org.example.data.IdentifierListRepository
import org.example.web.IdentifierListResponse
import org.example.web.IdentifierStatus
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class IdentifierListServiceTest {

    @RelaxedMockK
    private lateinit var identifierListRepository: IdentifierListRepository

    private val identifierListService: IdentifierListService by lazy {
        IdentifierListService(identifierListRepository)
    }

    @Test
    fun `should return identifier list response`() {
        val listId = 1
        val identifier = Identifier(id = "8247c5c5-4809-47be-a413-e599dbdd551c", status = 1)
        val identifierList = listOf(identifier)

        every { identifierListRepository.findByListId(listId) } returns identifierList

        val response = identifierListService.getIdentifierList(listId)

        val expectedResponse = IdentifierListResponse(
            identifierList = mapOf(identifier.id to IdentifierStatus(identifier.status))
        )
        assertEquals(expectedResponse, response)
    }

    @Test
    fun `should return empty identifier list response when no identifiers exist`() {
        val listId = 2
        every { identifierListRepository.findByListId(listId) } returns emptyList()

        val response = identifierListService.getIdentifierList(listId)

        val expectedResponse = IdentifierListResponse(identifierList = emptyMap())
        assertEquals(expectedResponse, response)
    }
}
