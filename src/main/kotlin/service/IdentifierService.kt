package org.example.service

import org.example.config.AppConfiguration
import org.example.data.IdentifierListRepository
import org.example.data.IdentifierRepository
import org.example.entity.Identifier
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

/**
 * Service-Klasse zur Verwaltung von Identifiern.
 *
 * Diese Klasse stellt Methoden zur Erstellung neuer Identifier und zur Aktualisierung
 * des Status bestehender Identifier bereit.
 *
 * @param identifierListRepository Repository für den Zugriff auf Identifier-Listen.
 * @param identifierRepository Repository für den Zugriff auf individuelle Identifier.
 * @param config Anwendungskonfiguration mit Konfigurationswerten für Identifier-Listen.
 */
@Service
class IdentifierService(
    private val identifierListRepository: IdentifierListRepository,
    private val identifierRepository: IdentifierRepository,
    private val config: AppConfiguration
) {

    /**
     * Erstellt einen neuen Identifier und fügt ihn einer bestehenden oder neuen Liste hinzu.
     *
     * Falls die neueste Identifier-Liste noch Platz hat, wird der neue Identifier dieser Liste zugeordnet.
     * Andernfalls wird eine neue Liste erstellt.
     *
     * @return Der erstellte [Identifier].
     */
    @Transactional
    fun createNewIdentifier(): Identifier {
        val latestListId = identifierListRepository.findLatestListId()
        val listIdToUse = if (latestListId != null &&
            identifierRepository.countByListId(latestListId) < config.listSize
        ) {
            latestListId
        } else {
            identifierListRepository.createNewList()
        }

        // Erstelle und speichere einen neuen Identifier
        val identifier = Identifier(
            id = UUID.randomUUID(),
            listId = listIdToUse,
            status = 0,
            statusChangeCount = 0
        )
        return identifierRepository.save(identifier)
    }

    /**
     * Aktualisiert den Status eines bestehenden Identifiers.
     *
     * @param uri Die URI der Identifier-Liste, aus der die Listen-ID extrahiert wird.
     * @param identifierUuid Die UUID des Identifiers, dessen Status aktualisiert werden soll.
     * @param newStatus Der neue Statuswert des Identifiers.
     */
    @Transactional
    fun updateIdentifierStatus(uri: String, identifierUuid: UUID, newStatus: Int) {
        val listId = uri.substringAfterLast("/").toLong()
        identifierRepository.updateIdentifierStatus(listId, identifierUuid, newStatus)
    }
}
