package org.example

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableAsync

/**
 * Hauptklasse der Spring Boot Anwendung.
 *
 * Diese Klasse initialisiert und startet die Anwendung.
 * Sie verwendet folgende Annotationen:
 * - `@SpringBootApplication`: Markiert die Hauptklasse einer Spring Boot Anwendung.
 * - `@ConfigurationPropertiesScan`: Aktiviert das Scannen nach Konfigurationsklassen.
 * - `@EnableAsync`: Ermöglicht die asynchrone Verarbeitung.
 */
@SpringBootApplication
@ConfigurationPropertiesScan
@EnableAsync
class IdentifierListServiceApp

/**
 * Einstiegspunkt der Anwendung.
 *
 * Diese Methode startet die Spring Boot Anwendung.
 */
fun main() {
    runApplication<IdentifierListServiceApp>()
}