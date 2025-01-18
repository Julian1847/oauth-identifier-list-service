package org.example.config

import org.springframework.boot.context.properties.ConfigurationProperties
import java.nio.file.Files
import java.nio.file.Path
import java.security.MessageDigest

@ConfigurationProperties("app")
class AppConfiguration(
    apiKey: String? = null,
    val publicUrl: String,
    storageDirectory: String,
    val issuer: String,
    val listSize: Int
) {
    private val storageDirectory: Path = Path.of(storageDirectory)

    @OptIn(ExperimentalStdlibApi::class)
    val apiKeyHash =
        mutableListOf<String>()
            .apply {
                apiKey?.let { add(it) }
            }
            .map { MessageDigest.getInstance("SHA-256").digest(it.toByteArray()).toHexString() }

    init {
        Files.createDirectories(this.storageDirectory)
        val apiKeyValuesSet = listOf(apiKey).count { it != null }
        check(apiKeyValuesSet == 1) { "api-key must be set" }
    }

}
