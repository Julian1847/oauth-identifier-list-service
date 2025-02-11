package org.example.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

/**
 * Repräsentiert eine Identifier-Liste in der Datenbank.
 *
 * Diese Entität wird in der Tabelle `identifier_lists` gespeichert.
 *
 * @param id Die eindeutige ID der Identifier-Liste.
 * Diese ist optional und wird automatisch generiert, wenn nicht gesetzt.
 */
@Table("identifier_lists")
data class IdentifierList(
    @Id
    val id: Long? = null
)