# Voraussetzungen

- JDK 17 oder neuer (JDK 21 empfohlen), Maven 3.9+, Ollama installiert.
- Ollama starten: `ollama serve` (nur wenn nicht bereits als Dienst aktiv).
- Chat-Modell: `ollama pull llama3.2`.
- Standard: `http://localhost:11434`; abweichend über `OLLAMA_BASE_URL`.
- Kein externer API-Key erforderlich. Modellaufrufe benötigen lokal genügend RAM.
- Modellnamen lassen sich über `OLLAMA_CHAT_MODEL` ändern; Eigenschaften des Modells beeinflussen die Ergebnisse.

Vision-Modell: `ollama pull llava:7b`. `OLLAMA_VISION_MODEL` überschreibt es. Ein kleines Testbild ist enthalten.
