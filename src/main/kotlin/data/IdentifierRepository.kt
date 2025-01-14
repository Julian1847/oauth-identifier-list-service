package org.example.data

import org.example.entity.Identifier
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class IdentifierRepository(private val jdbcTemplate: JdbcTemplate) {

    fun countByListId(listId: Long): Long {
        return jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM identifiers WHERE list_id = ?",
            Long::class.java,
            listId
        )
    }

    fun save(identifier: Identifier): Identifier {
        jdbcTemplate.update(
            "INSERT INTO identifiers (id, list_id, status, status_change_count) VALUES (?, ?, ?, ?)",
            identifier.id, identifier.listId, identifier.status, identifier.statusChangeCount
        )
        return identifier
    }

    fun updateIdentifierStatus(listId: Long, identifierUuid: UUID, newStatus: Int) {
        jdbcTemplate.update(
            "UPDATE identifiers SET status = ?, " +
                    "status_change_count = status_change_count + 1 " +
                    "WHERE id = ? AND list_id = ?",
            newStatus, identifierUuid, listId
        )
    }

    fun getIdentifierStatus(listId: Long, identifierUuid: UUID): Int? {
        val sql = """
            SELECT status
            FROM identifiers
            WHERE list_id = ? AND id = ?
        """

        return jdbcTemplate.queryForObject(sql, Int::class.java, listId, identifierUuid)
    }
}