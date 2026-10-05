# Handout zur Advisors API

*Projekt 02-Advisors*

Advisors erweitern die Verarbeitung zwischen ChatClient und ChatModel. Sie können Requests untersuchen oder verändern, die Verarbeitung an den nächsten Advisor delegieren und Antworten nachbearbeiten. Dieses Projekt macht den Ablauf sichtbar, bevor in späteren Übungen Chat Memory und RAG hinzukommen.

## Gemeinsamer Ausgangspunkt

Die sechs Controller verwenden einen gemeinsamen ChatClient aus ChatConfiguration. Ollama erzeugt die Antworten mit llama3.2. Das Java-Paket lautet org.javacream.training.spring.ai.advisors. Die Beispiele registrieren ihre Advisors pro Aufruf; im gemeinsamen Bean sind keine Default-Advisors gesetzt.

```bash
ollama pull llama3.2
mvn spring-boot:run
```

Standardmäßig läuft das Projekt auf Port 8082. Swagger UI: http://localhost:8082/swagger-ui.html. Alle Endpunkte verwenden POST und erwarten einen Text im Request-Body; verwende Content-Type: text/plain. Das Modell wird nicht automatisch heruntergeladen.

**Betrieb mit HTTP-Proxy **Die mitmweb-Weboberfläche läuft auf Port 9082. Die Anwendung verwendet Port 8082. Der Proxy nimmt Ollama-Requests auf Port 11435 entgegen. Zum Starten über den Proxy:

```bash
OLLAMA_BASE_URL=http://localhost:11435 \
  mvn spring-boot:run
```

## Die Lernfolge

LoggingAdvisorController – Vorhandenen Advisor einsetzen und Logs untersuchen

CustomCallAdvisorController – Eigenen Advisor um einen synchronen Aufruf legen

AdvisorParameterController – Parameter in den Advisor-Kontext übergeben

AdvisorContextController – Kontext während der Verarbeitung erweitern

AdvisorChainController – Reihenfolge und Rückweg mehrerer Advisors verstehen

CustomStreamAdvisorController – Advisor-Verarbeitung auf einen Stream übertragen

Die Controller für Parameter und Kontext verwenden fast denselben Aufruf. Ihr Lernschwerpunkt unterscheidet sich: Parameter betrachtet die Eingabe in den Kontext, Context die Weitergabe und Erweiterung während der Verarbeitung.

## 1 LoggingAdvisorController

```java
POST /api/logging-advisor
```

**Lernziel **Einen fertigen Advisor einsetzen und den Chat-Aufruf außerhalb des Controllers beobachten.

**Didaktischer Sinn **SimpleLoggerAdvisor übernimmt eine wiederkehrende Querschnittsaufgabe. Der Controller beschreibt weiterhin nur die Frage und das gewünschte Ergebnis. Logging wird als austauschbarer Verarbeitungsschritt registriert. Das zeigt, wie technische Aufgaben vom fachlichen Aufruf getrennt werden können.

```java
chatClient.prompt().user(message)
    .advisors(new SimpleLoggerAdvisor())
    .call().content();
```

**Gezielte Übung **Sende „Hauptstadt von Deutschland“. Vergleiche den Request und die Response im Anwendungslog mit dem REST-Antworttext. Die erforderliche DEBUG-Konfiguration ist im Projekt bereits vorhanden.

**Worauf es ankommt **Der REST-Endpunkt liefert nur den Text. Die Logs zeigen Spring-AI-Objekte auf der Advisor-Ebene, nicht zwingend das ursprüngliche HTTP-JSON. Für die RAW-Ollama-Antwort verwendest du den HTTP-Proxy. Prompt- und Antworttexte können sensible Daten enthalten; protokolliere sie bewusst.

```java
logging.level.org.springframework.ai.chat.client.advisor.\
SimpleLoggerAdvisor=DEBUG
```

Die Property oben ist aus Platzgründen umgebrochen; in application.properties steht sie in einer einzigen Zeile.

## 2 CustomCallAdvisorController

```java
POST /api/custom-call-advisor
```

**Lernziel **Den Vertrag eines eigenen CallAdvisor und die Delegation an die nächste Stufe verstehen.

**Didaktischer Sinn **TraceAdvisor umschließt den synchronen Aufruf mit before und after. Der Controller registriert ihn unter dem Namen custom-call und übergibt exercise als Kontextparameter. Er gibt anschließend Antworttext und Kontext zurück. So wird das Ergebnis der eigenen Advisor-Verarbeitung unmittelbar sichtbar.

```java
.advisors(a -> a
    .advisors(new TraceAdvisor("custom-call", 0))
    .param("exercise", "CustomCallAdvisor"))
.call().chatClientResponse();
```

**Gezielte Übung **Setze Haltepunkte in adviseCall, before und after. Verfolge den Request bis nextCall und anschließend die zurückkehrende Response. Untersuche trace sowie custom-call:elapsedNs im REST-Ergebnis.

**Worauf es ankommt **Der Advisor verändert hier den Kontext, nicht den Prompt. Eine Textänderung ist daher kein erwartetes Ergebnis. nextCall setzt die Kette fort. Ohne Delegation müsste ein Advisor selbst eine Response erzeugen. after wird in dieser Implementierung nur bei erfolgreicher Rückkehr aufgerufen.

## Der gemeinsame TraceAdvisor

TraceAdvisor implementiert sowohl CallAdvisor als auch StreamAdvisor. Name und Order werden im Konstruktor festgelegt. Dieselbe Implementierung dient allen Beispielen außer dem Logging-Controller. Sie schreibt keine eigenen Logmeldungen: Beobachtet wird sie über Haltepunkte oder den zurückgegebenen Kontext.

```java
public ChatClientResponse adviseCall(
        ChatClientRequest request, CallAdvisorChain chain) {
    return after(chain.nextCall(before(request)));
}
```

Lies den Ausdruck von innen nach außen: before bereitet den Request vor, nextCall delegiert, after bearbeitet die Response. Diese Klammerung erklärt auch die umgekehrte Reihenfolge der Nachbearbeitung in einer Advisor-Kette.

## Was before und after tatsächlich tun

before kopiert die Kontext-Map und die vorhandene trace-Liste. Es ergänzt <name>:before, speichert <name>:start mit System.nanoTime() und baut einen abgeleiteten ChatClientRequest. Die Kopien vermeiden direkte Änderungen an der bisherigen Map und Liste.

after kopiert den Response-Kontext und trace, ergänzt <name>:after und berechnet <name>:elapsedNs. Das Ergebnis ist eine abgeleitete ChatClientResponse. Der Startwert dient nur der Differenzbildung; er ist kein Kalenderzeitpunkt.

```java
context.put(name + ":start", System.nanoTime());
// Nach der Delegation:
context.put(name + ":elapsedNs",
    System.nanoTime() - (Long) context.get(name + ":start"));
```

elapsedNs ist die verstrichene Zeit um die nachfolgenden Verarbeitungsschritte einschließlich Modellaufruf. Sie ist keine reine Ollama-Rechenzeit. Bei verschachtelten Advisors überlappen die Messbereiche; ihre Zeiten dürfen nicht als unabhängige Teilzeiten addiert werden. Eine Millisekunde entspricht 1.000.000 Nanosekunden.

## ChatResponse und ChatClientResponse

ChatResponse beschreibt die Modellantwort mit Generations und Metadaten. ChatClientResponse umschließt sie und ergänzt context(), also Daten aus der Advisor-Verarbeitung. Deshalb verwenden die synchronen Trace-Controller chatClientResponse(). Der REST-Code extrahiert daraus answer und context.

```java
var modelResponse = response.chatResponse();
var answer = modelResponse.getResult().getOutput().getText();
var context = response.context();
```

**Abgrenzung **Der Advisor-Kontext ist kein Chat Memory und kein zusätzlicher System-Prompt. Der Schlüssel exercise wird vom TraceAdvisor lediglich weitergegeben. Er steuert hier weder das Modell noch das Verhalten des Advisors. Eine neue REST-Anfrage beginnt mit einem neuen Ausführungskontext.

## 3 AdvisorParameterController

```java
POST /api/advisor-parameter
```

**Lernziel **Einem Advisor Aufrufparameter übergeben und sie von Prompt-Parametern unterscheiden.

**Didaktischer Sinn **Die Lambda-Variante von advisors kombiniert die Registrierung des TraceAdvisor mit param. exercise wird in den Ausführungskontext eingebracht. Das Beispiel zeigt zunächst den Transport eines Parameters; TraceAdvisor liest exercise nicht zur Steuerung seines Verhaltens aus.

```java
.advisors(a -> a
    .advisors(new TraceAdvisor("parameter", 0))
    .param("exercise", "AdvisorParameter"))
```

**Gezielte Übung **Prüfe im Debugger request.context() am Eingang von before. Suche exercise auch im REST-Ergebnis. Ändere den Wert im Controller und wiederhole den Aufruf. Als Erweiterung lasse den Advisor den Wert gezielt auslesen.

**Worauf es ankommt **Ein Advisor-Parameter ist kein Platzhalter im Prompt. Im Gegensatz zu user(...param(...)) aus 01-ChatClient ersetzt diese param-Methode keine Textvariable. Ein Parameter entfaltet erst eine Funktion, wenn ein Advisor ihn interpretiert.

## 4 AdvisorContextController

```java
POST /api/advisor-context
```

**Lernziel **Daten über die Verarbeitungsschritte eines Aufrufs hinweg weitergeben und erweitern.

**Didaktischer Sinn **Hier steht die Kontext-Map als gemeinsamer Kommunikationsraum im Vordergrund. Der Controller liefert sie ausdrücklich zurück. Der zuvor gesetzte Schlüssel exercise bleibt erhalten, während before und after trace und Zeitmesswerte hinzufügen. So lassen sich Eingabeparameter und während der Verarbeitung erzeugte Daten unterscheiden.

```java
.advisors(a -> a
    .advisors(new TraceAdvisor("context", 0))
    .param("exercise", "AdvisorContext"))
.call().chatClientResponse();
```

**Gezielte Übung **Vergleiche die Map vor before, nach before und nach after. Ordne jeden Schlüssel seinem Ursprung zu. Rufe den Controller erneut auf: Welche Werte bleiben gleich, welche werden neu berechnet?

**Worauf es ankommt **Parameter- und Kontext-Controller unterscheiden sich im aktuellen Code vor allem durch Advisor-Namen und exercise-Wert. Der didaktische Unterschied liegt in der Blickrichtung. Der Kontext wird nicht automatisch an Ollama übertragen. Er steht im REST-Ergebnis, weil der Controller context() ausdrücklich ausgibt.

## 5 AdvisorChainController

```java
POST /api/advisor-chain
```

**Lernziel **Die Reihenfolge mehrerer Advisors auf Hin- und Rückweg erklären.

**Didaktischer Sinn **Zwei TraceAdvisors machen die verschachtelte Verarbeitung sichtbar. first hat Order 0, second Order 10. Der kleinere Wert wird auf dem Request-Weg zuerst verarbeitet. Auf dem Response-Weg erfolgt die Nachbearbeitung in umgekehrter Reihenfolge.

```java
.advisors(a -> a.advisors(
    new TraceAdvisor("first", 0),
    new TraceAdvisor("second", 10))
    .param("exercise", "AdvisorChain"))
```

**Gezielte Übung **Sage vor dem Aufruf die Reihenfolge der trace-Einträge voraus. Vergleiche sie mit dem Ergebnis. Vertausche anschließend die Order-Werte. Ändere danach nur die Registrierungsreihenfolge und untersuche, was die Reihenfolge tatsächlich bestimmt.

**Worauf es ankommt **Bei unterschiedlichen Order-Werten entscheidet getOrder(). Bei gleichen Werten solltest du dich nicht auf eine bestimmte Reihenfolge verlassen. Die beiden Namen müssen verschieden sein, damit die Zeitmessschlüssel nicht kollidieren.

```java
// Erwartete trace bei erfolgreichem synchronem Aufruf:
["first:before", "second:before",
 "second:after", "first:after"]
```

## 6 CustomStreamAdvisorController

```java
POST /api/custom-stream-advisor
```

**Lernziel **Zwischen der Verarbeitung einer vollständigen Antwort und einzelner Stream-Elemente unterscheiden.

**Didaktischer Sinn **Der Controller registriert TraceAdvisor als StreamAdvisor und liefert Flux<String> über text/event-stream. adviseStream delegiert mit nextStream und wendet after über map auf jedes ChatClientResponse-Element an. after ist hier also keine einmalige Aktion am Ende des gesamten Streams.

```java
return chain.nextStream(before(request))
    .map(this::after);
```

**Gezielte Übung **Sende eine Frage, die eine längere Antwort auslöst. Setze einen Haltepunkt in after und beobachte die mehrfachen Aufrufe. Nutze für die Anzeige curl -N, damit der Client die Ausgabe nicht puffert.

**Worauf es ankommt **content() extrahiert Textteile; die Kontextdaten werden an diesem Endpunkt nicht mitgesendet. Ein Stream-Element ist nicht zwingend ein Token. elapsedNs misst hier die Zeit seit before bis zum jeweiligen Element, nicht die reine Bearbeitungszeit dieses Elements. Für einen einmaligen Abschluss benötigt man eine passende Abschlussbehandlung.

## Übungen mit REST und HTTP-Proxy

Bei einem Start auf Port 8082 lässt sich das Streaming-Beispiel unter Linux so aufrufen:

```java
curl -N http://localhost:8082/api/custom-stream-advisor \
  -H "Content-Type: text/plain" \
  -d "Erkläre den Ablauf einer Advisor-Kette ausführlich."
```

Für das synchrone Kettenbeispiel:

```java
curl http://localhost:8082/api/advisor-chain \
  -H "Content-Type: text/plain" \
  -d "Hauptstadt von Deutschland"
```

Vergleiche drei Perspektiven: Der REST-Client sieht answer und context. Der Debugger sieht die einzelnen Advisor-Schritte. Der HTTP-Proxy sieht den Prompt und die native Ollama-Antwort. trace, exercise und die Zeitmesswerte gehören hier zum Spring-AI-Kontext und erscheinen nicht automatisch im Ollama-Request.

## Transferfragen

1. Warum muss der TraceAdvisor chain.nextCall beziehungsweise chain.nextStream aufrufen, damit die nachfolgenden Stufen ausgeführt werden?

2. Warum lautet die Reihenfolge beim Rückweg second:after vor first:after? Welche Auswirkung haben vertauschte Order-Werte?

3. Welcher Unterschied besteht zwischen einem Prompt-Parameter, einem Advisor-Parameter und einem vom Advisor erzeugten Kontextwert?

4. Warum lässt sich aus den beiden elapsedNs-Werten keine Summe unabhängiger Verarbeitungszeiten bilden?

5. Welche Änderung wäre nötig, um die Kontextdaten auch im Streaming-Endpunkt zurückzugeben? Was wäre dann der Rückgabetyp?

6. Wo würdest du Logging, Chat Memory und die Ergänzung gefundener Dokumente ansiedeln? Welche dieser Funktionen zeigt dieses Projekt bereits tatsächlich?

## Einordnung und Vertiefung

Die Übungen führen vom Einsetzen eines fertigen Advisors zur eigenen Verarbeitungskette. TraceAdvisor dient der Beobachtung; er implementiert weder Chat Memory noch RAG und verändert keine Modellnachrichten. In späteren Projekten nutzen spezialisierte Advisors dieselben Mechanismen, um Gesprächsverlauf oder gefundene Dokumente in den Prompt aufzunehmen.

Technische Referenz: Spring AI, Advisors API und Chat Client API.

https://docs.spring.io/spring-ai/reference/api/advisors.html

https://docs.spring.io/spring-ai/reference/api/chatclient.html
