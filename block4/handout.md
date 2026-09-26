# Handout Block 4 -- Embeddings, Vector Stores und RAG-Grundlagen

> **Technische Basis:** Java 21, Spring Boot 4, Spring AI 2, Maven und
> Ollama. Als Chat-Modell dient `llama3.2`, für Embeddings
> `nomic-embed-text`.

## Lernziel

Dieses Handout vertieft die Inhalte von Block 4. Es dient sowohl zur
Begleitung der Übungen als auch als Nachschlageunterlage nach dem
Seminar.

## 1. RAG-Vorbereitung

Eigene Dokumente werden nicht automatisch Teil des Modellwissens. Für
semantische Suche werden sie gelesen, in Chunks zerlegt, eingebettet und
in einem VectorStore gespeichert.

## 2. Embeddings

Ein Embedding ist eine numerische Repräsentation eines Textes.
Semantisch ähnliche Inhalte sollen im Vektorraum nahe beieinanderliegen.
Im Seminar verwenden wir `nomic-embed-text`.

## 3. Document und Metadaten

Spring AI repräsentiert Inhalte als Document. Metadaten wie Quelle,
Abteilung oder Dokumenttyp helfen später bei Filterung,
Zugriffskontrolle und Quellenangaben.

## 4. Chunking

Zu große Chunks enthalten viel irrelevanten Kontext; zu kleine verlieren
Zusammenhänge. Chunk-Größe und Überlappung sind deshalb wichtige
Qualitätsparameter und müssen zum Dokumenttyp passen.

## 5. VectorStore und Indexierung

Der Ablauf lautet: Dokument lesen → splitten → Metadaten ergänzen →
Embeddings erzeugen → speichern. Die Indexierung findet typischerweise
außerhalb des eigentlichen Chat-Requests statt.

## 6. Similarity Search

Top-K bestimmt die Zahl der Treffer, ein Similarity Threshold die
erforderliche Mindestähnlichkeit. Retrieval sollte zunächst ohne LLM
geprüft werden: Schlechte Treffer können später keine gute RAG-Antwort
ergeben.

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
