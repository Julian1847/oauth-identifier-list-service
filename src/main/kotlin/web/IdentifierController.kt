package org.example.web


import org.example.config.AppConfiguration
import org.example.service.IdentifierService
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.UNAUTHORIZED
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import java.security.MessageDigest
import java.util.*

@RestController
@RequestMapping("/identifier-list")
class IdentifierController(
    private val identifierService: IdentifierService,
    private val config: AppConfiguration
) {

    @PostMapping("/new-reference")
    fun createIdentifier(
        @RequestHeader(required = false, value = "X-Api-Key") xApiKey: String?,
        ): ResponseEntity<ReferenceResponse> {
        authenticate(config, xApiKey)
        val identifier = identifierService.createNewIdentifier()
        val response = ReferenceResponse(
            uri = "${config.publicUrl}/identifier-list/${identifier.listId}",
            identifierUuid = identifier.id
        )
        return try {
            ResponseEntity.ok().body(response) // Ensure the response is returned
        } catch (e: ResponseStatusException) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null) // Handle errors properly
        }
    }

    @PatchMapping("/update")
    fun updateStatus(@RequestBody updateStatusRequest: UpdateStatusRequest,
                     @RequestHeader(required = false, value = "X-Api-Key") xApiKey: String?): ResponseEntity<Void> {
        // Extrahiere die Daten aus der Anfrage
        val uri = updateStatusRequest.uri
        val identifierUuid = updateStatusRequest.id
        val newStatus = updateStatusRequest.value
        authenticate(config, xApiKey)
        try {
            identifierService.updateIdentifierStatus(uri, identifierUuid, newStatus)
            return ResponseEntity.noContent().build()
        } catch (e: Error) {
            return ResponseEntity.notFound().build()
        }
    }

    @GetMapping("/{list-id}")
    fun getIdentifierStatusList(@PathVariable("list-id") listId: Int): ResponseEntity<IdentifierListResponse> {
        return try {
            val response = identifierService.getIdentifierList(listId)
            ResponseEntity.ok(response)
        } catch (e: Exception) {
            throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred while fetching data", e)
        }
    }

    @OptIn(ExperimentalStdlibApi::class)
    private fun authenticate(appConfiguration: AppConfiguration, apiKey: String?) {
        if (apiKey == null) {
            throw ResponseStatusException(UNAUTHORIZED, "API key is missing")
        }
        if (!appConfiguration.apiKeyHash.contains(
                    MessageDigest.getInstance("SHA-256").digest(apiKey.toByteArray()).toHexString())) {
                throw ResponseStatusException(UNAUTHORIZED, "Invalid API key")
            }
    }
}

data class ReferenceResponse(
    val uri: String,
    val identifierUuid: UUID
)

data class UpdateStatusRequest(
    val id: UUID,
    val uri: String,
    val value: Int
)

data class IdentifierListResponse(
    val identifierList: Map<String, IdentifierStatus>
)

data class IdentifierStatus(
    val status: Int
)