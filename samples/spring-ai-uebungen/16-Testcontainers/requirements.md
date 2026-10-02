# Voraussetzungen

- JDK 17 oder neuer (JDK 21 empfohlen), Maven 3.9+, Ollama installiert.
- Ollama starten: `ollama serve` (nur wenn nicht bereits als Dienst aktiv).
- Chat-Modell: `ollama pull llama3.2`.
- Standard: `http://localhost:11434`; abweichend über `OLLAMA_BASE_URL`.
- Kein externer API-Key erforderlich. Modellaufrufe benötigen lokal genügend RAM.
- Modellnamen lassen sich über `OLLAMA_CHAT_MODEL` ändern; Eigenschaften des Modells beeinflussen die Ergebnisse.

Embedding-Modell: `ollama pull nomic-embed-text`. Bei einem Modellwechsel muss der Vector Store mit zur Dimension passendem Schema neu angelegt werden.

Docker Engine / Docker Desktop mit Compose v2. Cassandra 5 wird über `docker compose up -d --wait` gestartet. Port 9042 muss frei sein. Mindestens 4 GB RAM für Cassandra einplanen. Anschließend `docker compose run --rm cassandra-init`. Daten bleiben in einem Docker-Volume erhalten.

Die Integrationstests starten Cassandra selbst über Testcontainers. Compose vorher beenden, falls nicht benötigt. `mvn verify -Pintegration`: Docker muss erreichbar sein. Ein laufendes Ollama mit beiden Modellen ist erforderlich; Ollama bleibt auf dem Host.
