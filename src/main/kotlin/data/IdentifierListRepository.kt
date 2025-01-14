package org.example.data

import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class IdentifierListRepository(private val jdbcTemplate: JdbcTemplate) {

    fun findLatestListId(): Long? {
        return try {
            jdbcTemplate.queryForObject(
                "SELECT id FROM identifier_lists ORDER BY id DESC LIMIT 1",
                Long::class.java
            )
        } catch (e: EmptyResultDataAccessException) {
            null
        }
    }

    fun createNewList(): Long {
        return jdbcTemplate.queryForObject(
            "INSERT INTO identifier_lists DEFAULT VALUES RETURNING id",
            Long::class.java
        )!!
    }

    fun updateIdentifierStatus(listId: Long, identifierUuid: UUID, newStatus: Int) {
        jdbcTemplate.update(
            "UPDATE identifiers SET status = ?, " +
                    "status_change_count = status_change_count + 1 " +
                    "WHERE id = ? AND list_id = ?",
            newStatus, identifierUuid, listId
        )
    }
}