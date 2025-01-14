package org.example.data

import org.example.entity.Identifier
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository

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
}