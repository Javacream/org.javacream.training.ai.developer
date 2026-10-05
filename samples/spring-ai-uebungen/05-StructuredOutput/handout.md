# Handout: StructuredOutput

*Projekt 05-StructuredOutput*

Dieses Handout erläutert die 6 Controller des Projekts mit Lernziel, didaktischem Sinn und gezielten Übungen. Die Beispiele werden am tatsächlichen Aufrufpfad untersucht: REST-Eingabe, Spring-AI-Verarbeitung und Ergebnis. Die folgenden Codeauszüge zeigen den Kern der jeweiligen execute-Methode.

## Vorbereitung und gemeinsame Konfiguration

Benötigt werden JDK 17 oder neuer, Maven 3.9+ und ein laufendes Ollama. Die Modellintegration verwendet normalerweise llama3.2 mit Temperature 0.2; Modelle werden nicht automatisch heruntergeladen. Weitere Voraussetzungen stehen in requirements.md des Projekts.

```bash
ollama pull llama3.2
mvn spring-boot:run
```

Die Anwendung läuft auf Port 8085. Swagger UI: http://localhost:8085/swagger-ui.html. Die Proxy-Weboberfläche läuft bei dir auf Port 9082. Für Ollama-Aufrufe über den Proxy:

```bash
OLLAMA_BASE_URL=http://localhost:11435 mvn spring-boot:run
```

## Überblick über die Lernfolge

BeanOutputController – Ein fachliches Ergebnis in einen Java Record überführen.

GenericOutputController – Generische Ergebnisstrukturen erhalten.

ListOutputController – Eine einfache Liste aus Modelltext erzeugen.

MapOutputController – Flexible Schlüssel-Wert-Ergebnisse verarbeiten.

ProviderStructuredOutputController – Das Ausgabeformat auf Provider-Ebene vorgeben.

SchemaValidationController – Ausgabe anhand eines Schemas prüfen lassen.

## 1 BeanOutputController

POST /api/bean-output

Eingabe: Text im Body (Content-Type: text/plain).

**Lernziel **Ein fachliches Ergebnis in einen Java Record überführen.

**Didaktischer Sinn **BeanOutputConverter beschreibt Filmography mit actor und movies. getFormat() wird ausdrücklich an die Frage angehängt; entity(converter) übernimmt die Umwandlung.

```java
var converter = new BeanOutputConverter<>(Filmography.class);
return chatClient.prompt()
.user(message + "\n" + converter.getFormat())
.call()
.entity(converter);
```

**Gezielte Übung **Frage nach drei Filmen von Tom Hanks. Erweitere Filmography um ein Feld und beobachte Formatvorgabe und Ergebnis.

**Worauf es ankommt **Eine erfolgreiche Umwandlung bestätigt keine Filmangabe. Die explizite Formatvorgabe und die durch entity verwendete Formatsteuerung sollten im Prompt gemeinsam untersucht werden.

## 2 GenericOutputController

POST /api/generic-output

Eingabe: Text im Body (Content-Type: text/plain).

**Lernziel **Generische Ergebnisstrukturen erhalten.

**Didaktischer Sinn **ParameterizedTypeReference<List<Filmography>> bewahrt den Elementtyp, den List.class allein nicht beschreiben würde.

```java
return chatClient.prompt()
.user(message)
.call()
.entity(new ParameterizedTypeReference<List<Filmography>>() {});
```

**Gezielte Übung **Frage nach Filmografien für zwei Schauspieler. Prüfe die Liste und die Typen der Elemente.

**Worauf es ankommt **Die Typinformation hilft der Konvertierung. Sie erzwingt keine richtige Anzahl von Schauspielern oder faktisch richtige Inhalte.

## 3 ListOutputController

POST /api/list-output

Eingabe: Text im Body (Content-Type: text/plain).

**Lernziel **Eine einfache Liste aus Modelltext erzeugen.

**Didaktischer Sinn **ListOutputConverter zeigt einen leichteren Ausgabe-Vertrag als ein verschachteltes JSON-Objekt. Die Formatanweisung wird durch entity eingebunden.

```java
return chatClient.prompt()
.user(message)
.call()
.entity(new ListOutputConverter());
```

**Gezielte Übung **Fordere fünf Java-Begriffe an. Untersuche im Proxy die Formatvorgabe und vergleiche die Java-Liste.

**Worauf es ankommt **Kommagetrennte Inhalte sind weniger ausdrucksstark als typisierte Objekte. Begriffe mit eingebetteten Kommas verdienen besondere Aufmerksamkeit.

## 4 MapOutputController

POST /api/map-output

Eingabe: Text im Body (Content-Type: text/plain).

**Lernziel **Flexible Schlüssel-Wert-Ergebnisse verarbeiten.

**Didaktischer Sinn **MapOutputConverter ermöglicht eine dynamische Struktur ohne vorherigen Record. Die Übung zeigt den Komfort und den Verlust fachlicher Typsicherheit.

```java
return chatClient.prompt()
.user(message)
.call()
.entity(new MapOutputConverter());
```

**Gezielte Übung **Frage nach name, capital und population für Deutschland. Prüfe Schlüssel und tatsächliche Werttypen.

**Worauf es ankommt **Eine Map ersetzt kein fachliches Schema. Erwartete Schlüssel, Pflichtwerte und Datentypen müssen für nachfolgende Verarbeitung geprüft werden.

## 5 ProviderStructuredOutputController

POST /api/provider-structured-output

Eingabe: Text im Body (Content-Type: text/plain).

**Lernziel **Das Ausgabeformat auf Provider-Ebene vorgeben.

**Didaktischer Sinn **useProviderStructuredOutput nutzt den Provider-Vertrag anstelle einer bloßen Textbitte. Die Übung vergleicht die Formatebene im Request mit BeanOutput.

```java
return chatClient.prompt()
.user(message)
.call()
.entity(Filmography.class, spec -> spec.useProviderStructuredOutput());
```

**Gezielte Übung **Sende dieselbe Filmfrage an beide Controller und untersuche das format-Feld im Ollama-Request.

**Worauf es ankommt **Die Unterstützung hängt vom Provider und Modell ab. Ein erzwungenes Schema garantiert Struktur, aber keine sachliche Richtigkeit.

## 6 SchemaValidationController

POST /api/schema-validation

Eingabe: Text im Body (Content-Type: text/plain).

**Lernziel **Ausgabe anhand eines Schemas prüfen lassen.

**Didaktischer Sinn **validateSchema aktiviert Validierung und bei Schemafehlern Wiederholungen mit Rückmeldung. Die Übung behandelt Fehler als Teil der Verarbeitung.

```java
return chatClient.prompt()
.user(message)
.call()
.entity(Filmography.class, spec -> spec.validateSchema());
```

**Gezielte Übung **Verwende passende und widersprüchliche Filmfragen. Beobachte mögliche zusätzliche Modellaufrufe im Proxy.

**Worauf es ankommt **Validierung und provider-native Strukturierung sind getrennte Optionen. Validierung ist keine Faktenprüfung; Wiederholungen können Laufzeit und Token-Verbrauch erhöhen.

## Transfer und Vertiefung

1. Welche Teile laufen in der Anwendung, welche im Modell und welche in einem externen Dienst? Ordne den wichtigsten Controller-Aufruf jeder Ebene zu.

2. Was zeigt die REST-Antwort, und welche zusätzlichen Informationen erhältst du im Debugger oder HTTP-Proxy? Unterscheide Datenstruktur, fachlichen Inhalt und technischen Ablauf.

3. Formuliere für zwei Controller eine passende und eine unpassende Eingabe. Erkläre, ob du eine andere Antwort, eine leere Ergebnismenge oder einen technischen Fehler erwartest.

4. Welche Aufgabe wird vom gezeigten Controller tatsächlich erledigt, welche wird nur vorbereitet? Identifiziere wiederverwendbare Komponenten und Unterschiede zur Produktionsimplementierung.

## Technische Referenz

Spring AI Reference, StructuredOutput. Die Übungen verwenden die im Projekt eingebundenen APIs. Vertiefung:

https://docs.spring.io/spring-ai/reference/
