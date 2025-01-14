package org.example.service

import org.example.data.IdentifierListRepository
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class IdentifierListService(private val identifierListRepository: IdentifierListRepository) {

    fun updateIdentifierStatus(uri: String, identifierUuid: UUID, newStatus: Int) {
        val listId = uri.substringAfterLast("/").toLong()
        identifierListRepository.updateIdentifierStatus(listId, identifierUuid, newStatus)
    }
}