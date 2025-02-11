package org.example.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.util.*

/**
 * Repräsentiert einen Identifier innerhalb einer Identifier-Liste.
 *
 * Diese Entität wird in der Datenbanktabelle `identifiers` gespeichert.
 *
 * @param id Die eindeutige UUID des Identifiers. Standardmäßig wird eine zufällige UUID generiert.
 * @param listId Die ID der Liste, zu der dieser Identifier gehört.
 * @param status Der aktuelle Status des Identifiers. Standardmäßig ist dieser 0.
 * @param statusChangeCount Die Anzahl der Statusänderungen dieses Identifiers. Standardmäßig ist dieser 0.
 */
@Table("identifiers")
data class Identifier(
    @Id
    val id: UUID = UUID.randomUUID(),
    val listId: Long,
    var status: Int = 0,
    var statusChangeCount: Int = 0
)