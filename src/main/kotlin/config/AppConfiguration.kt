package org.example.config

import org.springframework.boot.context.properties.ConfigurationProperties

import java.security.MessageDigest

@ConfigurationProperties("app")
class AppConfiguration(
    apiKey: String? = null,
    val publicUrl: String,
    val listSize: Int
) {

    @OptIn(ExperimentalStdlibApi::class)
    val apiKeyHash =
        mutableListOf<String>()
            .apply {
                apiKey?.let { add(it) }
            }
            .map { MessageDigest.getInstance("SHA-256").digest(it.toByteArray()).toHexString() }

    init {
        val apiKeyValuesSet = listOf(apiKey).count { it != null }
        check(apiKeyValuesSet == 1) { "api-key must be set" }
    }

}
