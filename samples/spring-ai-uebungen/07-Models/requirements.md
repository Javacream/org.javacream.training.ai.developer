# Voraussetzungen

- JDK 17 oder neuer (JDK 21 empfohlen), Maven 3.9+, Ollama installiert.
- Ollama starten: `ollama serve` (nur wenn nicht bereits als Dienst aktiv).
- Chat-Modell: `ollama pull llama3.2`.
- Standard: `http://localhost:11434`; abweichend über `OLLAMA_BASE_URL`.
- Kein externer API-Key erforderlich. Modellaufrufe benötigen lokal genügend RAM.
- Modellnamen lassen sich über `OLLAMA_CHAT_MODEL` ändern; Eigenschaften des Modells beeinflussen die Ergebnisse.

Embedding-Modell: `ollama pull nomic-embed-text`. Bei einem Modellwechsel muss der Vector Store mit zur Dimension passendem Schema neu angelegt werden.

Die vier Controller für ImageModel, TranscriptionModel, SpeechModel und ModerationModel melden HTTP 501: In dieser Spring-AI-Ollama-Integration stehen diese dedizierten Model-APIs nicht zur Verfügung. Sie werden nicht durch andere Provider oder simulierte Antworten ersetzt.
