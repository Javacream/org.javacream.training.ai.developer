# Handout Block 2 -- ChatClient, Prompts und strukturierte Ausgaben

> **Technische Basis:** Java 21, Spring Boot 4, Spring AI 2, Maven und
> Ollama. Als Chat-Modell dient `llama3.2`, für Embeddings
> `nomic-embed-text`.

## Lernziel

Dieses Handout vertieft die Inhalte von Block 2. Es dient sowohl zur
Begleitung der Übungen als auch als Nachschlageunterlage nach dem
Seminar.

## 1. ChatModel und ChatClient

ChatModel bildet den grundlegenden Modellzugriff ab. ChatClient stellt
darüber eine Fluent API bereit und ist für viele Anwendungen der
bequemere Einstieg.

## 2. System- und User-Nachrichten

System-Nachrichten definieren Rolle, Regeln und Rahmen; User-Nachrichten
enthalten die konkrete Aufgabe. Diese Trennung verbessert
Wiederverwendbarkeit und Wartbarkeit von Prompts.

## 3. Prompt Templates

Variable Inhalte sollten als Parameter eines Templates modelliert werden
statt durch unübersichtliche String-Konkatenation. Prompts sind
Anwendungsartefakte und sollten versioniert und getestet werden.

## 4. Structured Output

Freitext ist für Menschen bequem, aber für Programme schwer
weiterzuverarbeiten. Spring AI kann Antworten auf Java-Typen abbilden,
etwa mit `.entity(KnowledgeCard.class)`. Das schafft eine technische
Schnittstelle, ersetzt aber keine fachliche Validierung.

## 5. REST und Streaming

Synchron wartet der Client auf die vollständige Antwort. Mit
`.stream().content()` können Teilantworten als Stream verarbeitet
werden. Streaming verbessert die wahrgenommene Reaktionszeit, macht
Fehlerbehandlung und Client-Verarbeitung jedoch komplexer.

## 6. Fehlerbehandlung

Nicht erreichbare Modelle, Timeouts und ungültige Antworten müssen
kontrolliert behandelt werden. Technische Exceptions sollten nicht
ungefiltert an REST-Clients gelangen.

## Praktische Orientierung

Arbeiten Sie bei Problemen schrittweise: zuerst Infrastruktur und
Konfiguration prüfen, dann die Spring-AI-Abstraktion, anschließend die
tatsächlich an das Modell übergebenen Daten. Trennen Sie klassische
Anwendungslogik konsequent von generativem Verhalten.

## Merksätze

Die AI-Komponente ist Teil einer normalen Spring-Anwendung und
unterliegt denselben Anforderungen an Struktur, Testbarkeit,
Fehlerbehandlung und Sicherheit. Modellantworten sind grundsätzlich als
nicht vollständig vertrauenswürdige externe Ergebnisse zu behandeln.
