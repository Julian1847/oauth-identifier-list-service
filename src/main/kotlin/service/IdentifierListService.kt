package org.example.service

import org.example.data.IdentifierListRepository
import org.example.web.IdentifierListResponse
import org.example.web.IdentifierStatus
import org.springframework.stereotype.Service

/**
 * Service-Klasse zur Verwaltung von Identifier-Listen.
 *
 * Diese Klasse stellt Methoden zur Abfrage von Identifier-Listen aus dem Repository bereit.
 *
 * @param identifierListRepository Repository für den Zugriff auf gespeicherte Identifier-Listen.
 */
@Service
class IdentifierListService(
    private val identifierListRepository: IdentifierListRepository,
) {

    /**
     * Ruft eine Identifier-Liste anhand der List-ID ab.
     *
     * @param listId Die ID der Identifier-Liste.
     * @return [IdentifierListResponse], die eine Map von Identifier-IDs zu ihren jeweiligen Statuswerten enthält.
     */
    fun getIdentifierList(listId: Int): IdentifierListResponse {
        val identifierList = identifierListRepository.findByListId(listId)
        return IdentifierListResponse(
            identifierList = identifierList.associate { it.id to IdentifierStatus(it.status) }
        )
    }
}
