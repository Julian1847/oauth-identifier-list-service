package org.example.web

import org.example.config.AppConfiguration
import org.example.service.IdentifierListService
import org.example.service.IdentifierService
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.UNAUTHORIZED
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import java.security.MessageDigest
import java.util.*

/**
 * REST-Controller zur Verwaltung von Identifier-Listen.
 *
 * Dieser Controller stellt Endpunkte zur Erstellung, Aktualisierung und Abfrage von Identifiern bereit.
 *
 * @param identifierService Service für die Verwaltung einzelner Identifier.
 * @param identifierListService Service für die Verwaltung von Identifier-Listen.
 * @param config Anwendungskonfiguration.
 */
@RestController
@RequestMapping("/identifier-list")
class IdentifierController(
    private val identifierService: IdentifierService,
    private val identifierListService: IdentifierListService,
    private val config: AppConfiguration
) {

    /**
     * Erstellt eine neue Referenz-ID und gibt die zugehörige URI zurück.
     *
     * @param xApiKey API-Schlüssel zur Authentifizierung (Header `X-Api-Key`).
     * @return ResponseEntity mit einer [ReferenceResponse], die die neue Identifier-URI und UUID enthält.
     * @throws ResponseStatusException falls die Authentifizierung fehlschlägt.
     */
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
            ResponseEntity.ok().body(response)
        } catch (e: ResponseStatusException) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null)
        }
    }

    /**
     * Aktualisiert den Status eines Identifiers.
     *
     * @param updateStatusRequest Anfrage mit URI, Identifier-UUID und neuem Statuswert.
     * @param xApiKey API-Schlüssel zur Authentifizierung.
     * @return ResponseEntity ohne Inhalt, wenn erfolgreich, oder `404 Not Found`, falls der Identifier nicht existiert.
     * @throws ResponseStatusException falls die Authentifizierung fehlschlägt.
     */
    @PatchMapping("/update")
    fun updateStatus(
        @RequestBody updateStatusRequest: UpdateStatusRequest,
        @RequestHeader(required = false, value = "X-Api-Key") xApiKey: String?
    ): ResponseEntity<Void> {
        val uri = updateStatusRequest.uri
        val identifierUuid = updateStatusRequest.identifierUuid
        val newStatus = updateStatusRequest.value
        authenticate(config, xApiKey)
        return try {
            identifierService.updateIdentifierStatus(uri, identifierUuid, newStatus)
            ResponseEntity.noContent().build()
        } catch (e: Error) {
            ResponseEntity.notFound().build()
        }
    }

    /**
     * Ruft eine Liste aller Identifier und deren Statuswerte ab.
     *
     * @param listId ID der Identifier-Liste.
     * @return ResponseEntity mit einer [IdentifierListResponse], die die Identifier-Liste enthält.
     * @throws ResponseStatusException falls ein Fehler auftritt.
     */
    @GetMapping("/{list-id}")
    fun getIdentifierStatusList(@PathVariable("list-id") listId: Int): ResponseEntity<IdentifierListResponse> {
        return try {
            val response = identifierListService.getIdentifierList(listId)
            ResponseEntity.ok(response)
        } catch (e: Exception) {
            throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred while fetching data", e)
        }
    }

    /**
     * Authentifiziert eine Anfrage basierend auf dem API-Schlüssel.
     *
     * @param appConfiguration Anwendungskonfiguration mit dem Hash des gültigen API-Schlüssels.
     * @param apiKey API-Schlüssel zur Überprüfung.
     * @throws ResponseStatusException falls der API-Schlüssel fehlt oder ungültig ist.
     */
    @OptIn(ExperimentalStdlibApi::class)
    private fun authenticate(appConfiguration: AppConfiguration, apiKey: String?) {
        if (apiKey == null) {
            throw ResponseStatusException(UNAUTHORIZED, "API key is missing")
        }
        if (!appConfiguration.apiKeyHash.contains(
                MessageDigest.getInstance("SHA-256").digest(apiKey.toByteArray()).toHexString()
            )
        ) {
            throw ResponseStatusException(UNAUTHORIZED, "Invalid API key")
        }
    }
}

/**
 * Antwort-Datenklasse für einen neuen Identifier.
 *
 * @param uri Die URI der erstellten Identifier-Referenz.
 * @param identifierUuid Die UUID des erstellten Identifiers.
 */
data class ReferenceResponse(
    val uri: String,
    val identifierUuid: UUID
)

/**
 * Anfrage-Datenklasse zur Aktualisierung des Status eines Identifiers.
 *
 * @param identifierUuid Die UUID des zu aktualisierenden Identifiers.
 * @param uri Die URI der Identifier-Liste.
 * @param value Der neue Statuswert.
 */
data class UpdateStatusRequest(
    val identifierUuid: UUID,
    val uri: String,
    val value: Int
)

/**
 * Antwort-Datenklasse für eine Liste von Identifiern und deren Status.
 *
 * @param identifierList Eine Map von Identifier-URIs zu ihrem jeweiligen Status.
 */
data class IdentifierListResponse(
    val identifierList: Map<String, IdentifierStatus>
)

/**
 * Datenklasse für den Status eines Identifiers.
 *
 * @param status Der aktuelle Status des Identifiers.
 */
data class IdentifierStatus(
    val status: Int
)