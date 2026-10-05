# Handout zur ChatClient API

*Projekt 01 ChatClient*

Die neun Controller zeigen schrittweise, wie eine Spring-Boot-Anwendung mit einem AI-Modell kommuniziert. Ausgangspunkt ist ein einfacher Textaufruf. Danach untersuchen wir Antworttypen, Streaming und die Gestaltung wiederverwendbarer Clients. Ziel ist, eine passende API-Variante bewusst zu wählen und ihr Verhalten erklären zu können.

## Gemeinsamer Ausgangspunkt

Das Projekt verwendet Ollama mit dem Chat-Modell llama3.2. ChatConfiguration erzeugt den gemeinsamen ChatClient aus dem automatisch konfigurierten Builder. Die Controller erhalten ihn per Constructor Injection. Das Java-Paket lautet org.javacream.training.spring.ai.chatclient.

Vorbereitung: Ollama starten, das Modell laden und im Projektverzeichnis die Anwendung starten. Weitere Voraussetzungen stehen in requirements.md.

```bash
ollama pull llama3.2
mvn spring-boot:run
```

Swagger UI: http://localhost:8081/swagger-ui.html. Alle neun Endpunkte verwenden POST und erwarten im Body einen Text. Für REST-Aufrufe Content-Type: text/plain verwenden. Die Standardports und das Modell können über Umgebungsvariablen überschrieben werden.

## Die Lernfolge

| Controller | Schwerpunkt |
| --- | --- |
| SimpleChatController | Minimaler synchroner Textaufruf |
| FluentApiController | User und System Message kombinieren |
| ChatResponseController | Antwort samt Metadaten untersuchen |
| EntityResponseController | Antwort in einen Java Record überführen |
| StreamingChatController | Text schrittweise empfangen |
| PromptParameterController | Prompt Templates mit Parametern füllen |
| MessageMetadataController | Anwendungskontext an Messages hängen |
| ChatClientDefaultsController | Wiederverwendbare Vorgaben festlegen |
| ChatClientMutationController | Client konfigurativ ableiten |

## 1 SimpleChatController

```java
POST /api/simple-chat
```

**Lernziel **Den kürzesten vollständigen Weg von einer REST-Anfrage zur Modellantwort verstehen.

**Didaktischer Sinn **Das Beispiel hält die Infrastruktur bewusst klein. Der Body der HTTP-Anfrage wird zur User Message. Nach der synchronen Modellverarbeitung erhält die Anwendung den Antworttext. Dadurch lassen sich REST-Schicht, Spring-AI-Aufruf und Modellverhalten getrennt betrachten.

```java
chatClient.prompt()
    .user(message)
    .call()
    .content();
```

**Gezielte Übung **Sende „Erkläre Dependency Injection in einem Satz.“ Wiederhole den Aufruf und vergleiche die Formulierungen. Sende anschließend „Mein Name ist Ada.“ und in einem neuen Request „Wie heiße ich?“.

**Worauf es ankommt **Der gemeinsame Client stellt hier kein Chat Memory bereit. Vorherige REST-Aufrufe werden nicht automatisch Teil des nächsten Prompts. content() liefert einen String. Die API-Kette wird bis zur Auswahl des Ergebnisses aufgebaut; .call() allein ist noch kein ausgelesenes Ergebnis.

## 2 FluentApiController

```java
POST /api/fluent-api
```

**Lernziel **Die Fluent API als Beschreibung einer Anfrage lesen und die Rollen von User und System Message unterscheiden.

**Didaktischer Sinn **Der Controller beginnt mit prompt(message) und ergänzt die feste System-Anweisung „Antworte kurz auf Deutsch.“ Die Kurzform setzt den User-Text. Die System Message formuliert dagegen Vorgaben der Anwendung für die Antwort. Der Vergleich mit SimpleChat zeigt, wie wenig Code dafür zusätzlich nötig ist.

```java
chatClient.prompt(message)
    .system("Antworte kurz auf Deutsch.")
    .call()
    .content();
```

**Gezielte Übung **Sende an beide Controller dieselbe englische Frage, etwa „What is dependency injection?“ Vergleiche Sprache und Länge der Antworten. Ändere anschließend die System-Anweisung im Code.

**Worauf es ankommt **Die Methodenreihenfolge ist kein Gesprächsprotokoll: Die Kette konfiguriert eine Anfrage mit unterschiedlichen Message-Rollen. Eine System-Anweisung steuert das Modell, garantiert aber keine bestimmte Wortzahl oder Formulierung.

## 3 ChatResponseController

```java
POST /api/chat-response
```

**Lernziel **Zwischen dem reinen Antworttext und der vollständigen Spring-AI-Antwort unterscheiden.

**Didaktischer Sinn **Dieser Controller verändert nicht die Frage, sondern die Art, wie die Antwort ausgewertet wird. chatResponse() erhält das Spring-AI-Antwortobjekt mit Generations und Metadaten. Das ist der Einstieg in Token-Auswertung, Finish Reasons und die Untersuchung des Modellaufrufs.

```java
chatClient.prompt().user(message)
    .call().chatResponse();
```

**Gezielte Übung **Sende dieselbe Frage an SimpleChat und ChatResponse. Suche in der JSON-Antwort den generierten Text und die Usage-Metadaten. Formuliere dann eine längere Frage und vergleiche die Token-Angaben, soweit Ollama sie liefert.

**Worauf es ankommt **Eine ChatResponse ist das Spring-AI-Modell der Antwort, kein unverändertes HTTP-Paket von Ollama. Umfang und Inhalt der Metadaten hängen vom Provider ab. Für eine reine Textanzeige reicht oft content(); für technische Auswertung sind zusätzliche Daten sinnvoll.

## 4 EntityResponseController

```java
POST /api/entity-response
```

**Lernziel **Modellantworten in einen fachlich definierten Java-Datentyp überführen.

**Didaktischer Sinn **Der Record Filmography beschreibt das erwartete Ergebnis mit einem Schauspielernamen und einer Filmliste. Der Controller übergibt diesen Typ an entity(...). Damit wird die Modellantwort für anschließende Java-Verarbeitung nutzbar, statt erst eigene String-Operationen für die Auswertung schreiben zu müssen.

```java
public record Filmography(
    String actor, List<String> movies) {}

chatClient.prompt().user(message)
    .call().entity(Filmography.class);
```

**Gezielte Übung **Sende „Nenne drei Filme von Tom Hanks.“ Untersuche die Felder actor und movies. Erweitere anschließend den Record um ein weiteres fachliches Feld und passe die Anfrage an.

**Worauf es ankommt **Die technische Umwandlung in einen Record bestätigt keine Filmangabe. Das Beispiel aktiviert weder Schema-Validierung noch provider-native Structured Output ausdrücklich. Unpassende Modellantworten können zu Konvertierungsfehlern führen. Diese Zuverlässigkeitsfragen werden später im Projekt StructuredOutput vertieft.

**Hinweis zum Java-Code **Viele Controller deklarieren ihren REST-Rückgabewert als Object. Didaktisch entscheidend ist trotzdem der tatsächlich erzeugte Wert: hier ein Filmography, beim vorigen Controller eine ChatResponse und bei content() ein String.

## 5 StreamingChatController

```java
POST /api/streaming-chat
```

**Lernziel **Den Unterschied zwischen einer vollständigen synchronen Antwort und einem Strom von Textteilen verstehen.

**Didaktischer Sinn **Mit stream() liefert Spring AI einen Flux<String>. Der REST-Endpunkt verwendet text/event-stream, sodass eine geeignete HTTP-Anwendung die Textteile während der Generierung anzeigen kann. Die Übung erklärt, warum sich Streaming für längere Antworten anders anfühlt als ein blockierender Aufruf.

```java
chatClient.prompt().user(message)
    .stream().content();
// REST-Rückgabetyp: Flux<String>
// Response: text/event-stream
```

**Gezielte Übung **Bitte um eine längere Erklärung und rufe den Endpoint mit einem Client auf, der Streaming nicht puffert. Unter Windows kannst du beispielsweise curl.exe -N verwenden. Vergleiche den Zeitpunkt des ersten sichtbaren Textes mit SimpleChat.

**Worauf es ankommt **Ein Flux-Element entspricht nicht zwingend genau einem Token, Wort oder Satz. HTTP-Clients können Inhalte puffern; eine verzögerte Anzeige beweist daher noch kein fehlendes Streaming. Auch eine frühe Ausgabe verkürzt nicht automatisch die gesamte Generierungsdauer.

## 6 PromptParameterController

```java
POST /api/prompt-parameter
```

**Lernziel **Feste Prompt-Struktur und variable Eingabedaten voneinander trennen.

**Didaktischer Sinn **Hier ist der Request-Body ein Thema und keine fertig ausformulierte Frage. Derselbe Wert wird in zwei Templates eingesetzt: in die System Message für die Expertenrolle und in die User Message für den Arbeitsauftrag. Die Anwendung bestimmt damit die Struktur der Anfrage.

```java
.system(s -> s.text("Du bist Experte für {topic}.")
    .param("topic", message))
.user(u -> u.text("Erkläre {topic} in drei Sätzen.")
    .param("topic", message))
```

**Gezielte Übung **Sende nur „Dependency Injection“ und danach „REST“. Untersuche im Debugger oder Log die erzeugten Messages. Ergänze als Erweiterung einen zweiten Parameter, beispielsweise für die Zielgruppe.

**Worauf es ankommt **Die Platzhalter werden in der Anwendung durch die angegebenen Werte ersetzt. Das Modell bekommt den daraus erzeugten Text. Parametrisierung verbessert die Wiederverwendbarkeit, ist aber keine Sicherheitsgrenze gegen Anweisungen in Benutzereingaben.

## 7 MessageMetadataController

```java
POST /api/message-metadata
```

**Lernziel **Den Inhalt einer Message von zusätzlichem Anwendungskontext unterscheiden.

**Didaktischer Sinn **Der Controller ergänzt die User Message um exercise und eine neu erzeugte requestId. Zusätzlich gibt er diese Metadaten zusammen mit der Antwort im REST-Ergebnis zurück. So können Teilnehmende nachvollziehen, welche Daten zur Zuordnung eines Aufrufs dienen und welche Daten der eigentliche Prompt sind.

```java
.user(u -> u.text(message).metadata(metadata))
// REST-Ergebnis enthält metadata und answer
```

**Gezielte Übung **Rufe den Controller zweimal mit demselben Text auf. Vergleiche die requestId und den festen Wert von exercise. Frage das Modell anschließend nach seiner Request-ID und untersuche, welche Information überhaupt an Ollama übertragen wurde.

**Worauf es ankommt **Die zusätzliche ID erscheint im REST-Ergebnis, weil der Controller sie dort ausdrücklich ausgibt. Daraus folgt nicht, dass Ollama sie kennt. Message Metadata werden in diesem Beispiel nicht automatisch als Prompt-Text übertragen. Sie sind außerdem nicht dasselbe wie Response Metadata des Providers.

## 8 ChatClientDefaultsController

```java
POST /api/chat-client-defaults
```

**Lernziel **Wiederkehrende Anweisungen und Modelloptionen als Vorgaben eines Clients festlegen.

**Didaktischer Sinn **Das Beispiel leitet aus dem gemeinsamen Client einen Builder ab und setzt einen Standard-Systemtext sowie die Temperature. Der anschließend gebaute Client benötigt beim einzelnen Aufruf nur noch den User-Text. So wird verständlich, wie sich wiederholte Konfiguration aus dem eigentlichen Aufruf entfernen lässt.

```java
var client = chatClient.mutate()
    .defaultSystem("Antworte in der Stimme eines Piraten.")
    .defaultOptions(ChatOptions.builder().temperature(0.2))
    .build();
```

**Gezielte Übung **Sende dieselbe Frage an SimpleChat und ChatClientDefaults. Vergleiche vor allem den Sprachstil. Ändere anschließend die Default-System-Anweisung und wiederhole den Vergleich.

**Worauf es ankommt **Die Temperature ist bereits in application.properties auf 0.2 gesetzt. Im aktuellen Beispiel ist die neue Vorgabe deshalb vor allem eine API-Demonstration und kein kontrollierter Temperature-Vergleich. Der abgeleitete Client wird hier pro Request gebaut; für eine produktive, wiederkehrende Konfiguration bietet sich ein eigener Bean an.

## 9 ChatClientMutationController

```java
POST /api/chat-client-mutation
```

**Lernziel **Aus einem vorhandenen Client eine Variante ableiten, ohne dessen gemeinsame Konfiguration zu überschreiben.

**Didaktischer Sinn **Der Controller baut mit mutate() einen Client mit der Default-Anweisung „Antworte nur auf Englisch.“ Anschließend führt er dieselbe Anfrage einmal mit dem ursprünglichen und einmal mit dem abgeleiteten Client aus. Die Antwort-Map macht beide Ergebnisse unmittelbar vergleichbar.

```java
var derived = chatClient.mutate()
    .defaultSystem("Antworte nur auf Englisch.")
    .build();
// Zwei Aufrufe: chatClient und derived
// REST-Ergebnis: original und derived
```

**Gezielte Übung **Sende „Was ist Dependency Injection?“ und vergleiche original mit derived. Rufe danach SimpleChat auf. Überprüfe im Debugger, dass der ursprüngliche Client nicht die neue Default-System-Anweisung erhalten hat.

**Worauf es ankommt **Es werden zwei Modellaufrufe ausgeführt; die REST-Anfrage wartet auf beide. „original“ bezeichnet den ursprünglichen Client und bedeutet nicht „deutsche Antwort“. Die neue Variante verwendet weiterhin dieselbe Ollama-Anbindung. Unterschiedliche Formulierungen allein belegen daher weder einen Modellwechsel noch eine Veränderung des ursprünglichen Clients.

## Transferfragen

1. Welche Antwortform würdest du für eine einfache Chat-Anzeige, für Token-Auswertung und für nachfolgende fachliche Java-Verarbeitung wählen? Begründe jeweils die Wahl.

2. Soll eine Einstellung für jeden Aufruf gelten oder nur für eine einzelne Anfrage? Ordne System-Anweisung, Prompt-Parameter und Default-Konfiguration entsprechend ein.

3. Warum sind ein gemeinsamer ChatClient und mehrere nacheinander ausgeführte REST-Aufrufe noch kein Gespräch mit dauerhaftem Kontext?

## Bezug zur Referenz

Die Erläuterungen beziehen sich auf die neun Implementierungen des Projekts 01-ChatClient. Die folgenden Kapitel zu Advisors, Chat Memory und Structured Output bauen darauf auf. Technische Vertiefung: Spring AI Reference, Chat Client API.

https://docs.spring.io/spring-ai/reference/api/chatclient.html
