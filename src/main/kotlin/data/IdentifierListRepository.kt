package org.example.data

import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Repository

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


    fun findByListId(listId: Int): List<Identifier> {
        val sql = "SELECT id, status FROM identifiers WHERE list_id = ?"
        return jdbcTemplate.query(sql, IdentifierRowMapper(), listId)
    }
}

data class Identifier(
    val id: String,
    val status: Int
)

class IdentifierRowMapper : RowMapper<Identifier> {
    override fun mapRow(rs: java.sql.ResultSet, rowNum: Int): Identifier {
        return Identifier(
            id = rs.getString("id"),
            status = rs.getInt("status")
        )
    }
}