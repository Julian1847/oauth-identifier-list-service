package org.example.data

import org.example.entity.Identifier
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import java.util.*

/**
 * Repository-Klasse für den Zugriff auf Identifier in der Datenbank.
 *
 * Diese Klasse stellt Methoden zum Zählen, Speichern und Aktualisieren von Identifiern bereit.
 *
 * @param jdbcTemplate Spring JDBC Template zur Interaktion mit der Datenbank.
 */
@Repository
class IdentifierRepository(private val jdbcTemplate: JdbcTemplate) {

    /**
     * Zählt die Anzahl der Identifier in einer bestimmten Liste.
     *
     * @param listId Die ID der Liste, für die die Anzahl der Identifier ermittelt werden soll.
     * @return Die Anzahl der Identifier in der angegebenen Liste.
     */
    fun countByListId(listId: Long): Long {
        return jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM identifiers WHERE list_id = ?",
            Long::class.java,
            listId
        )
    }

    /**
     * Speichert einen neuen Identifier in der Datenbank.
     *
     * @param identifier Das zu speichernde [Identifier]-Objekt.
     * @return Das gespeicherte [Identifier]-Objekt.
     */
    fun save(identifier: Identifier): Identifier {
        jdbcTemplate.update(
            "INSERT INTO identifiers (id, list_id, status, status_change_count) VALUES (?, ?, ?, ?)",
            identifier.id, identifier.listId, identifier.status, identifier.statusChangeCount
        )
        return identifier
    }

    /**
     * Aktualisiert den Status eines Identifiers und erhöht den Status-Änderungszähler.
     *
     * @param listId Die ID der Liste, zu der der Identifier gehört.
     * @param identifierUuid Die UUID des zu aktualisierenden Identifiers.
     * @param newStatus Der neue Statuswert für den Identifier.
     */
    fun updateIdentifierStatus(listId: Long, identifierUuid: UUID, newStatus: Int) {
        jdbcTemplate.update(
            "UPDATE identifiers SET status = ?, " +
                    "status_change_count = status_change_count + 1 " +
                    "WHERE list_id = ? AND id = ?",
            newStatus, listId, identifierUuid
        )
    }
}