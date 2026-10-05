# Handout: Multimodality

*Projekt 06-Multimodality*

Dieses Handout erläutert die 4 Controller des Projekts mit Lernziel, didaktischem Sinn und gezielten Übungen. Die Beispiele werden am tatsächlichen Aufrufpfad untersucht: REST-Eingabe, Spring-AI-Verarbeitung und Ergebnis. Die folgenden Codeauszüge zeigen den Kern der jeweiligen execute-Methode.

## Vorbereitung und gemeinsame Konfiguration

Benötigt werden JDK 17 oder neuer, Maven 3.9+ und ein laufendes Ollama. Die Modellintegration verwendet normalerweise llama3.2 mit Temperature 0.2; Modelle werden nicht automatisch heruntergeladen. Weitere Voraussetzungen stehen in requirements.md des Projekts.

```bash
ollama pull llama3.2
mvn spring-boot:run
```

Die Anwendung läuft auf Port 8086. Swagger UI: http://localhost:8086/swagger-ui.html. Die Proxy-Weboberfläche läuft bei dir auf Port 9082. Für Ollama-Aufrufe über den Proxy:

```bash
OLLAMA_BASE_URL=http://localhost:11435 mvn spring-boot:run
```

**Vision-Modell **Bildanalyse verwendet in diesem Projekt llava:7b; die spätere Model-Property überschreibt den allgemeinen Chat-Modellwert. OLLAMA_VISION_MODEL kann das Vision-Modell ersetzen. Die Upload-Grenzen betragen 10 MB pro Datei und 25 MB pro Request. Alle Beispiele deklarieren PNG-Medien.

```bash
ollama pull llava:7b
```

## Überblick über die Lernfolge

ImageAnalysisController – Text und hochgeladene Bilddaten gemeinsam übergeben.

MediaResourceController – Ein festes Bild vom Classpath verwenden.

MediaUriController – Ein Medium aus einer URL-Ressource einbinden.

MultipleMediaController – Mehrere Bilder in einer Anfrage vergleichen.

## 1 ImageAnalysisController

POST /api/image-analysis

Eingabe: Multipart-Upload; message als Request-Parameter und Bilddatei(en) als Part. Verwende die in Swagger angezeigten Part-Namen.

**Lernziel **Text und hochgeladene Bilddaten gemeinsam übergeben.

**Didaktischer Sinn **MultipartFile wird in Bytes umgewandelt und als PNG-Medium an die User Message gehängt. Die REST-Schicht und der multimodale Prompt werden getrennt sichtbar.

```java
return chatClient.prompt()
.user(u -> u.text(message).media(MimeTypeUtils.IMAGE_PNG, new
ByteArrayResource(bytes(image))))
.call()
.content();
```

**Gezielte Übung **Lade ein PNG hoch und frage nach drei sichtbaren Merkmalen. Vergleiche eine allgemeine mit einer gezielten Frage.

**Worauf es ankommt **Alle Bilder werden als image/png deklariert. Nutze tatsächliche PNG-Dateien. Die Analyse benötigt das konfigurierte Vision-Modell, nicht das textbasierte llama3.2.

## 2 MediaResourceController

POST /api/media-resource

Eingabe: Text im Body (Content-Type: text/plain).

**Lernziel **Ein festes Bild vom Classpath verwenden.

**Didaktischer Sinn **images/sample.png wird als ClassPathResource eingebunden. Das schafft einen reproduzierbaren Einstieg ohne Dateiupload.

```java
return chatClient.prompt()
.user(u -> u.text(message).media(MimeTypeUtils.IMAGE_PNG, new
ClassPathResource("images/sample.png")))
.call()
.content();
```

**Gezielte Übung **Sende unterschiedliche Fragen zum gleichen Testbild. Prüfe, welche Antworten am Bild belegt werden können.

**Worauf es ankommt **Das Bild bleibt gleich; geändert wird der Textauftrag. Dies ist Bildanalyse, keine Bildgenerierung.

## 3 MediaUriController

POST /api/media-uri

Eingabe: Query-Parameter gemäß Swagger.

**Lernziel **Ein Medium aus einer URL-Ressource einbinden.

**Didaktischer Sinn **UrlResource kapselt die eingegebene URI. Die Anwendung stellt die Ressource für den Modellrequest bereit und macht die Herkunft von Bilddaten austauschbar.

```java
var resource = new UrlResource(java.net.URI.create(uri));
return chatClient.prompt()
.user(u -> u.text(message).media(MimeTypeUtils.IMAGE_PNG, resource))
.call()
.content();
```

**Gezielte Übung **Verwende die URL eines erreichbaren PNG. Beobachte den Unterschied zwischen URL-Eingabe und tatsächlich übertragenem Modellrequest.

**Worauf es ankommt **Die URL muss von der Anwendung erreichbar sein. In einer produktiven Schnittstelle müssen erlaubte Quellen begrenzt werden; Dateityp und Fehlerbehandlung sind hier vereinfacht.

## 4 MultipleMediaController

POST /api/multiple-media

Eingabe: Multipart-Upload; message als Request-Parameter und Bilddatei(en) als Part. Verwende die in Swagger angezeigten Part-Namen.

**Lernziel **Mehrere Bilder in einer Anfrage vergleichen.

**Didaktischer Sinn **Der Controller hängt jedes hochgeladene Bild an dieselbe User Message. Der Vergleich wird damit ein gemeinsamer multimodaler Auftrag.

```java
return chatClient.prompt()
.user(u -> { u.text(message);
for (var image : images) u.media(MimeTypeUtils.IMAGE_PNG, new
ByteArrayResource(bytes(image))); })
.call()
.content();
```

**Gezielte Übung **Lade zwei PNG-Bilder hoch und frage nach Gemeinsamkeiten und Unterschieden. Ändere anschließend die Reihenfolge.

**Worauf es ankommt **Die Bilder gehören zu einer einzigen Anfrage. Fähigkeiten, Kontextgröße und Ressourcenbedarf des Vision-Modells begrenzen das Ergebnis.

## Transfer und Vertiefung

1. Welche Teile laufen in der Anwendung, welche im Modell und welche in einem externen Dienst? Ordne den wichtigsten Controller-Aufruf jeder Ebene zu.

2. Was zeigt die REST-Antwort, und welche zusätzlichen Informationen erhältst du im Debugger oder HTTP-Proxy? Unterscheide Datenstruktur, fachlichen Inhalt und technischen Ablauf.

3. Formuliere für zwei Controller eine passende und eine unpassende Eingabe. Erkläre, ob du eine andere Antwort, eine leere Ergebnismenge oder einen technischen Fehler erwartest.

4. Welche Aufgabe wird vom gezeigten Controller tatsächlich erledigt, welche wird nur vorbereitet? Identifiziere wiederverwendbare Komponenten und Unterschiede zur Produktionsimplementierung.

## Technische Referenz

Spring AI Reference, Multimodality. Die Übungen verwenden die im Projekt eingebundenen APIs. Vertiefung:

https://docs.spring.io/spring-ai/reference/
