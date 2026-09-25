# Spring AI Seminar – Gesamtmaterial

Struktur pro Block:

- `trainerleitfaden.md` – didaktischer Leitfaden
- `uebungen.ipynb` – Aufgaben für Teilnehmende
- `musterloesung.ipynb` – ausgearbeitete Lösungsvorschläge
- `maven-project/` – eigenständig startbare Musterlösung

Technischer Zielstand:

- Spring AI 2.0.1
- Spring Boot 4.0.0
- Java 21

Für Modellaufrufe wird `OPENAI_API_KEY` als Umgebungsvariable erwartet.

Hinweis: Die Dateien wurden strukturell erzeugt und auf konsistente Imports/API-Verwendung ausgerichtet. In der Ausführungsumgebung stand Maven nicht zur Verfügung; daher konnte hier kein vollständiger `mvn test`-Build gegen Maven Central ausgeführt werden.

Maven GroupId: `org.javacream.training`
Java-Basispaket: `org.javacream.training.springai`
