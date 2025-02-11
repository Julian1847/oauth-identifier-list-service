package org.example.web

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.example.config.AppConfiguration
import org.example.entity.Identifier
import org.example.service.IdentifierListService
import org.example.service.IdentifierService
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.security.MessageDigest
import java.util.*

@WebMvcTest(IdentifierController::class)
class IdentifierControllerTest(@Autowired val mockMvc: MockMvc) {

    @MockkBean lateinit var appConfig: AppConfiguration
    @MockkBean lateinit var identifierService: IdentifierService
    @MockkBean lateinit var identifierListService: IdentifierListService

    private val baseUrl = "http://localhost:8080"
    private val listId = 1

    private val contentTypeJson = "application/json"
    private val apiKey = "366A9069-2965-4667-9AD2-5C51D71046D9"

    @OptIn(ExperimentalStdlibApi::class)
    private val apiKeyHash = mutableListOf<String>()
        .apply {
            apiKey.let { add(it) }
        }
        .map { MessageDigest.getInstance("SHA-256").digest(it.toByteArray()).toHexString() }

    @Test
    fun `should return identifier list response`() {
        every { appConfig.publicUrl } returns baseUrl
        every { identifierListService.getIdentifierList(listId) } returns IdentifierListResponse(
            identifierList = mapOf(
                "8247c5c5-4809-47be-a413-e599dbdd551c" to IdentifierStatus(status = 1)
            )
        )
        mockMvc
            .perform(MockMvcRequestBuilders.get("/identifier-list/$listId").accept(contentTypeJson))
            .andExpect(status().isOk)
            .andExpect(content().contentType(contentTypeJson))
            .andExpect(jsonPath("$.identifierList").isMap)
            .andExpect(jsonPath("$.identifierList['8247c5c5-4809-47be-a413-e599dbdd551c'].status").value(1))
    }

    @Test
    fun `should create a new identifier and return response`() {
        val identifierUuid = UUID.randomUUID()
        val response = ReferenceResponse(
            uri = "$baseUrl/identifier-list/$listId",
            identifierUuid = identifierUuid
        )

        every { appConfig.publicUrl } returns baseUrl
        every { appConfig.apiKeyHash } returns apiKeyHash

        every { identifierService.createNewIdentifier() } returns Identifier(
            id = identifierUuid,
            listId = 1L,
            status = 0,
            statusChangeCount = 0
        )

        mockMvc
            .perform(MockMvcRequestBuilders.post("/identifier-list/new-reference")
                .header("x-api-key", apiKey)
                .accept(contentTypeJson)
                .contentType(contentTypeJson))
            .andExpect(status().isOk)
            .andExpect(content().contentType(contentTypeJson))
            .andExpect(jsonPath("$.uri").value(response.uri))
            .andExpect(jsonPath("$.identifierUuid").value(identifierUuid.toString()))
    }

    @Test
    fun `should update identifier status`() {
        val uri = "$baseUrl/identifier-list/$listId"
        val identifierUuid = UUID.randomUUID()
        every { appConfig.publicUrl } returns baseUrl
        every { appConfig.apiKeyHash } returns apiKeyHash
        every { identifierService.updateIdentifierStatus(uri, identifierUuid, 1) } returns Unit

        mockMvc
            .perform(
                MockMvcRequestBuilders.patch("/identifier-list/update")
                    .header("x-api-key", apiKey)
                    .contentType(contentTypeJson)
                    .content(
                        """
                        {
                         "identifierUuid": "$identifierUuid",
                         "uri": "$uri",
                         "value": 1}
                    """.trimIndent())
                    .accept(contentTypeJson)
            )
            .andExpect(status().isNoContent)
    }
}
