package org.example

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableAsync

@SpringBootApplication @ConfigurationPropertiesScan @EnableAsync
class IdentifierListServiceApp
fun main() {
    runApplication<IdentifierListServiceApp>()
}