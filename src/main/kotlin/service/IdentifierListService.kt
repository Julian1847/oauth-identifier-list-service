package org.example.service

import org.example.data.IdentifierListRepository
import org.example.web.IdentifierListResponse
import org.example.web.IdentifierStatus
import org.springframework.stereotype.Service

@Service
class IdentifierListService(
    private val identifierListRepository: IdentifierListRepository,
    ) {

    fun getIdentifierList(listId: Int): IdentifierListResponse {
        val identifierList = identifierListRepository.findByListId(listId)
        return IdentifierListResponse(
            identifierList = identifierList.associate { it.id to IdentifierStatus(it.status) }
        )
    }

}