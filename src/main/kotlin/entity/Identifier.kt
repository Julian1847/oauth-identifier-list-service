package org.example.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.util.*

@Table("identifiers")
data class Identifier(
    @Id
    val id: UUID = UUID.randomUUID(),
    val listId: Long,
    var status: Int = 0,
    var statusChangeCount: Int = 0
)