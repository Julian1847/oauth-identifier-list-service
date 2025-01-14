package org.example.web


import org.example.config.AppConfiguration
import org.example.service.IdentifierListService
import org.example.service.IdentifierService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/identifier-list")
class IdentifierController(
    private val identifierService: IdentifierService,
    private val identifierListService: IdentifierListService,
    private val config: AppConfiguration
) {

    @PostMapping("/new-reference")
    fun createIdentifier(): ResponseEntity<ReferenceResponse> {
        val identifier = identifierService.createNewIdentifier()
        val response = ReferenceResponse(
            uri = "${config.publicUrl}/identifier-list/${identifier.listId}",
            identifierUuid = identifier.id
        )
        return ResponseEntity.ok(response)
    }

    @PatchMapping("/update")
    fun updateStatus(@RequestBody updateStatusRequest: UpdateStatusRequest): ResponseEntity<Void> {
        // Extrahiere die Daten aus der Anfrage
        val uri = updateStatusRequest.uri
        val identifierUuid = updateStatusRequest.identifierUuid
        val newStatus = updateStatusRequest.value

        identifierListService.updateIdentifierStatus(uri, identifierUuid, newStatus)

        return ResponseEntity.noContent().build()
    }
}

data class ReferenceResponse(
    val uri: String,
    val identifierUuid: UUID
)

data class UpdateStatusRequest(
    val uri: String,
    val identifierUuid: UUID,
    val value: Int
)