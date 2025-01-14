package org.example.config

import org.springframework.boot.context.properties.ConfigurationProperties
import java.nio.file.Files
import java.nio.file.Path

@ConfigurationProperties("app")
class AppConfiguration(
    val publicUrl: String,
    storageDirectory: String,
    val issuer: String,
    val listSize: Int
) {
    private val storageDirectory: Path = Path.of(storageDirectory)

    init {
        Files.createDirectories(this.storageDirectory)
    }

}
