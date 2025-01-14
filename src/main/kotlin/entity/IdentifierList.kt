package org.example.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("identifier_lists")
data class IdentifierList(
    @Id
    val id: Long? = null
)