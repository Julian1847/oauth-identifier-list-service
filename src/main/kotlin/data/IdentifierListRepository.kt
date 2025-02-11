package org.example.data

import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Repository

/**
 * Repository-Klasse für den Zugriff auf Identifier-Listen in der Datenbank.
 *
 * Diese Klasse stellt Methoden zum Abrufen und Erstellen von Identifier-Listen bereit.
 *
 * @param jdbcTemplate Spring JDBC Template zur Kommunikation mit der Datenbank.
 */
@Repository
class IdentifierListRepository(private val jdbcTemplate: JdbcTemplate) {

    /**
     * Ruft die ID der neuesten Identifier-Liste aus der Datenbank ab.
     *
     * @return Die ID der neuesten Liste oder `null`, falls keine Liste existiert.
     */
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

    /**
     * Erstellt eine neue Identifier-Liste in der Datenbank.
     *
     * @return Die ID der neu erstellten Liste.
     */
    fun createNewList(): Long {
        return jdbcTemplate.queryForObject(
            "INSERT INTO identifier_lists DEFAULT VALUES RETURNING id",
            Long::class.java
        )!!
    }

    /**
     * Ruft alle revozierten Identifier einer bestimmten Liste aus der Datenbank ab.
     *
     * @param listId Die ID der Identifier-Liste.
     * @return Eine Liste von [Identifier]-Objekten mit ID und Status.
     */
    fun findByListId(listId: Int): List<Identifier> {
        val sql = "SELECT id, status FROM identifiers WHERE list_id = ? AND status = 1"
        return jdbcTemplate.query(sql, IdentifierRowMapper(), listId)
    }
}

/**
 * Datenklasse, die einen Identifier repräsentiert.
 *
 * @param id Die eindeutige ID des Identifiers.
 * @param status Der aktuelle Status des Identifiers.
 */
data class Identifier(
    val id: String,
    val status: Int
)

/**
 * Mapper-Klasse zur Umwandlung einer SQL-ResultSet-Zeile in ein [Identifier]-Objekt.
 */
class IdentifierRowMapper : RowMapper<Identifier> {
    override fun mapRow(rs: java.sql.ResultSet, rowNum: Int): Identifier {
        return Identifier(
            id = rs.getString("id"),
            status = rs.getInt("status")
        )
    }
}
