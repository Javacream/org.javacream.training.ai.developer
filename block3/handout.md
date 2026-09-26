# Handout Block 3 -- Advisors und Conversation Memory

> **Technische Basis:** Java 21, Spring Boot 4, Spring AI 2, Maven und
> Ollama. Als Chat-Modell dient `llama3.2`, für Embeddings
> `nomic-embed-text`.

## Lernziel

Dieses Handout vertieft die Inhalte von Block 3. Es dient sowohl zur
Begleitung der Übungen als auch als Nachschlageunterlage nach dem
Seminar.

## 1. Warum Memory nötig ist

Ein LLM kennt vorherige Aufrufe nicht automatisch. Benötigte
Gesprächshistorie muss von der Anwendung erneut in den Kontext
aufgenommen werden.

## 2. Chat Memory

MessageWindowChatMemory hält ein begrenztes Fenster relevanter
Nachrichten. Eine Begrenzung verhindert, dass der Gesprächsverlauf das
Kontextfenster unbegrenzt belegt.

## 3. Conversation ID

Jedes Gespräch benötigt eine eigene Kennung. Andernfalls können sich
Historien verschiedener Benutzer oder Sessions vermischen.

## 4. Advisors

Advisors kapseln Querschnittslogik rund um Modellaufrufe.
MessageChatMemoryAdvisor ergänzt beispielsweise passende Historie
automatisch. Weitere Advisors können Logging oder später RAG übernehmen.

## 5. Advisor Chain

Mehrere Advisors bilden eine Verarbeitungskette. Ihre Reihenfolge kann
relevant sein, weil Requests und Responses verändert werden können.

## 6. Memory versus Wissen

Conversation Memory beantwortet die Frage, was in diesem Gespräch gesagt
wurde. RAG stellt dagegen externes fachliches Wissen bereit. Beides darf
konzeptionell nicht verwechselt werden.

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
