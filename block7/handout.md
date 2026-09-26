# Handout Block 7 -- MCP, Security und Guardrails

> **Technische Basis:** Java 21, Spring Boot 4, Spring AI 2, Maven und
> Ollama. Als Chat-Modell dient `llama3.2`, für Embeddings
> `nomic-embed-text`.

## Lernziel

Dieses Handout vertieft die Inhalte von Block 7. Es dient sowohl zur
Begleitung der Übungen als auch als Nachschlageunterlage nach dem
Seminar.

## 1. Model Context Protocol

MCP standardisiert, wie AI-Anwendungen externe Fähigkeiten und
Kontextquellen nutzen. Ein MCP Client verbindet sich mit einem MCP
Server, der beispielsweise Tools oder Resources anbietet.

## 2. Direkte Tools versus MCP

Direktes Tool Calling eignet sich für eng zur Anwendung gehörende
Funktionen. MCP wird interessant, wenn Fähigkeiten unabhängig
bereitgestellt oder von mehreren AI-Clients genutzt werden sollen. MCP
ersetzt Tool Calling nicht, sondern standardisiert die Integration.

## 3. Prompt Injection

Benutzer können versuchen, Regeln über manipulierte Eingaben zu
überschreiben. Prompts sind keine belastbare Sicherheitsgrenze;
kritische Regeln gehören in klassische Anwendungslogik.

## 4. Indirect Prompt Injection

Auch RAG-Dokumente können manipulative Instruktionen enthalten. Externe
Dokumente sind Daten, keine vertrauenswürdigen Befehle.

## 5. Tool- und RAG-Sicherheit

Tools benötigen Allowlisting, Validierung, Authentifizierung und
Autorisierung. Semantische Ähnlichkeit ist keine Zugriffsberechtigung:
RAG darf nur Dokumente liefern, die der aktuelle Benutzer lesen darf.

## 6. Guardrails und Human-in-the-loop

Output Validation, Ressourcenlimits, Tool-Grenzen und menschliche
Freigaben bilden gemeinsam Defense in Depth. Je autonomer ein Workflow
ist, desto wichtiger werden diese Grenzen.

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
