package org.example.service

import org.example.config.AppConfiguration
import org.example.data.IdentifierListRepository
import org.example.data.IdentifierRepository
import org.example.entity.Identifier

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class IdentifierService(
    private val identifierListRepository: IdentifierListRepository,
    private val identifierRepository: IdentifierRepository,
    private val config: AppConfiguration
) {

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

        // Create and save a new identifier
        val identifier = Identifier(
            id = UUID.randomUUID(),
            listId = listIdToUse,
            status = 0,
            statusChangeCount = 0
        )
        return identifierRepository.save(identifier)
    }

    @Transactional
    fun updateIdentifierStatus(uri: String, identifierUuid: UUID, newStatus: Int) {
        val listId = uri.substringAfterLast("/").toLong()
        identifierRepository.updateIdentifierStatus(listId, identifierUuid, newStatus)
    }

    fun getIdentifierStatus(listId: Long, identifierUuid: UUID): Int? {
        // Ruft den Status des Identifiers aus dem Repository ab
        return identifierRepository.getIdentifierStatus(listId, identifierUuid)
    }
}
