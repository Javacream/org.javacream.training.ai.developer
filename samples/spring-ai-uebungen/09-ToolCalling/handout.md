# Handout: ToolCalling

*Projekt 09-ToolCalling*

Dieses Handout erläutert die 10 Controller des Projekts mit Lernziel, didaktischem Sinn und gezielten Übungen. Die Beispiele werden am tatsächlichen Aufrufpfad untersucht: REST-Eingabe, Spring-AI-Verarbeitung und Ergebnis. Die folgenden Codeauszüge zeigen den Kern der jeweiligen execute-Methode.

## Vorbereitung und gemeinsame Konfiguration

Benötigt werden JDK 17 oder neuer, Maven 3.9+ und ein laufendes Ollama. Die Modellintegration verwendet normalerweise llama3.2 mit Temperature 0.2; Modelle werden nicht automatisch heruntergeladen. Weitere Voraussetzungen stehen in requirements.md des Projekts.

```bash
ollama pull llama3.2
mvn spring-boot:run
```

Die Anwendung läuft auf Port 8089. Swagger UI: http://localhost:8089/swagger-ui.html. Die Proxy-Weboberfläche läuft bei dir auf Port 9082. Für Ollama-Aufrufe über den Proxy:

```bash
OLLAMA_BASE_URL=http://localhost:11435 mvn spring-boot:run
```

TrainingTools enthält today (aktuelles Datum), add (Integer-Addition) und tenant (Anwendungskontext). Tool-Registrierung bedeutet Angebot an das Modell, nicht garantierte Ausführung. Beobachte Tool-Aufrufe im Debugger und im Proxy.

## Überblick über die Lernfolge

ReturnDirectController – Ein Tool-Ergebnis direkt als Antwort verwenden.

SimpleToolController – Eine Java-Methode als Modellwerkzeug registrieren.

ToolCallbackController – Ein Tool funktional statt über Annotationen definieren.

ToolCallbackProviderController – Mehrere Tool-Callbacks als Provider bündeln.

ToolCallingAdvisorController – Die Tool-Schleife als Advisor konfigurieren.

ToolContextController – Vertrauenswürdigen Anwendungskontext an Tools geben.

ToolErrorHandlingController – Fehler an der Grenze zwischen Modell und Anwendung behandeln.

ToolParametersController – Modellargumente und Java-Parameter zuordnen.

ToolSearchController – Eine zusätzliche Tool-Suche als Konfigurationsthema einordnen.

UserControlledToolController – Die Tool-Schleife anwendungsseitig kontrollieren.

## 1 ReturnDirectController

POST /api/return-direct

Eingabe: Text im Body (Content-Type: text/plain).

**Lernziel **Ein Tool-Ergebnis direkt als Antwort verwenden.

**Didaktischer Sinn **DirectTools.code liefert SPRING-AI-TRAINING und markiert returnDirect=true. Das zeigt eine alternative Behandlung des Ergebnisses zur erneuten Formulierung durch das Modell.

```java
return chatClient.prompt()
.user(message)
.tools(new DirectTools())
.call()
.content();
```

**Gezielte Übung **Fordere den festen Trainingscode über das code-Tool an. Prüfe Ergebnis und Anzahl der Modellaufrufe im Proxy.

**Worauf es ankommt **returnDirect wirkt nur, wenn das Tool tatsächlich aufgerufen wird. Eine bloße Registrierung liefert den festen Text nicht automatisch.

## 2 SimpleToolController

POST /api/simple-tool

Eingabe: Text im Body (Content-Type: text/plain).

**Lernziel **Eine Java-Methode als Modellwerkzeug registrieren.

**Didaktischer Sinn **TrainingTools stellt today, add und tenant über @Tool bereit. Das Modell kann einen strukturierten Tool-Aufruf anfordern, den Java ausführt.

```java
return chatClient.prompt()
.user(message)
.tools(new TrainingTools())
.call()
.content();
```

**Gezielte Übung **Bitte ausdrücklich darum, das heutige Datum mit dem Tool abzurufen. Setze einen Haltepunkt in today und beobachte Requests im Proxy.

**Worauf es ankommt **Registrierung garantiert keinen Aufruf. Das Modell entscheidet anhand von Frage, Beschreibung und Fähigkeiten; eine korrekte Textantwort beweist keine Tool-Ausführung.

## 3 ToolCallbackController

POST /api/tool-callback

Eingabe: Text im Body (Content-Type: text/plain).

**Lernziel **Ein Tool funktional statt über Annotationen definieren.

**Didaktischer Sinn **FunctionToolCallback registriert multiply mit dem Eingabetyp Numbers. Ein Java Record beschreibt das Argumentobjekt; eine Lambda berechnet das Produkt.

```java
var tool = FunctionToolCallback.builder("multiply", (Numbers input) -> input.a() *
input.b()).description("Multiply two integers").inputType(Numbers.class).build();
return chatClient.prompt()
.user(message)
.tools(tool)
.call()
.content();
```

**Gezielte Übung **Fordere die Multiplikation zweier Zahlen ausdrücklich mit multiply an. Setze den Haltepunkt in die Lambda.

**Worauf es ankommt **Die Lambda läuft erst bei einer Tool-Anforderung. Ein Haltepunkt nur im Controller bestätigt Registrierung, nicht Ausführung. Die Integer-Multiplikation prüft hier keinen Überlauf.

## 4 ToolCallbackProviderController

POST /api/tool-callback-provider

Eingabe: Text im Body (Content-Type: text/plain).

**Lernziel **Mehrere Tool-Callbacks als Provider bündeln.

**Didaktischer Sinn **MethodToolCallbackProvider erzeugt aus TrainingTools eine Gruppe von Werkzeugen. Registrierung und Implementierung lassen sich dadurch organisatorisch trennen.

```java
var provider = MethodToolCallbackProvider.builder().toolObjects(new
TrainingTools()).build();
return chatClient.prompt()
.user(message)
.tools(provider)
.call()
.content();
```

**Gezielte Übung **Vergleiche die angebotenen Tool-Schemas mit SimpleTool und rufe das Datumswerkzeug auf.

**Worauf es ankommt **Ein Provider ist keine separate Modellfähigkeit. Die Werkzeuge müssen weiterhin zum Prompt passen und vom Modell aufgerufen werden.

## 5 ToolCallingAdvisorController

POST /api/tool-calling-advisor

Eingabe: Text im Body (Content-Type: text/plain).

**Lernziel **Die Tool-Schleife als Advisor konfigurieren.

**Didaktischer Sinn **ToolCallingAdvisor verwendet einen ToolCallingManager mit maxTotalToolCalls(5). Damit wird die automatische Verarbeitung explizit konfiguriert.

```java
return chatClient.prompt()
.user(message)
.tools(new TrainingTools())
.advisors(ToolCallingAdvisor.builder().toolCallingManager(ToolCallingManager.builder().maxTotalToolCalls(5).build()).build())
.call()
.content();
```

**Gezielte Übung **Verfolge einen Tool-Aufruf und die anschließende Modellantwort. Untersuche, wo der Manager die Begrenzung prüft.

**Worauf es ankommt **Die Grenze schützt die Ausführungsschleife. Sie erzwingt keinen ersten Tool-Aufruf und bedeutet nicht, dass jede Anfrage fünf Aufrufe ausführt.

## 6 ToolContextController

POST /api/tool-context

Eingabe: Text im Body (Content-Type: text/plain).

**Lernziel **Vertrauenswürdigen Anwendungskontext an Tools geben.

**Didaktischer Sinn **tenantId=training-acme wird über toolContext gesetzt und von tenant aus ToolContext gelesen. So wird serverseitiger Kontext von Modellargumenten getrennt.

```java
return chatClient.prompt()
.user(message)
.tools(new TrainingTools()).toolContext(Map.of("tenantId", "training-acme"))
.call()
.content();
```

**Gezielte Übung **Bitte das tenant-Tool aufzurufen. Vergleiche die Tool-Argumente im Proxy mit dem Kontext im Java-Debugger.

**Worauf es ankommt **ToolContext ist nicht automatisch Teil des Modellprompts. Erst das zurückgegebene Tool-Ergebnis kann die Tenant-ID an das Modell weitergeben.

## 7 ToolErrorHandlingController

POST /api/tool-error-handling

Eingabe: Text im Body (Content-Type: text/plain).

**Lernziel **Fehler an der Grenze zwischen Modell und Anwendung behandeln.

**Didaktischer Sinn **FailingTools wirft absichtlich eine IllegalArgumentException. Der Controller übersetzt durchgereichte RuntimeExceptions in HTTP 502.

```java
try { return chatClient.prompt()
.user(message)
.tools(new FailingTools())
.call()
.content(); } catch (RuntimeException e) { throw new
org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.BAD_GATEWAY,
"Tool-Ausführung fehlgeschlagen: " + e.getMessage(),e); }
```

**Gezielte Übung **Fordere ausdrücklich das fail-Tool an. Untersuche den Tool-Haltepunkt, die Fehlerbehandlung und den HTTP-Status.

**Worauf es ankommt **Das konkrete Verhalten hängt auch von der Tool-Fehlerstrategie ab. Wird der Fehler als Tool-Ergebnis behandelt, erreicht er den catch-Block möglicherweise nicht.

## 8 ToolParametersController

POST /api/tool-parameters

Eingabe: Text im Body (Content-Type: text/plain).

**Lernziel **Modellargumente und Java-Parameter zuordnen.

**Didaktischer Sinn **Die add-Methode beschreibt zwei Integer-Parameter mit @ToolParam. Die Übung zeigt das Schema, das dem Modell zur Argumentbildung übermittelt wird.

```java
return chatClient.prompt()
.user(message)
.tools(new TrainingTools())
.call()
.content();
```

**Gezielte Übung **Fordere eine Addition mit dem add-Tool an. Prüfe a und b im Debugger und das Arguments-JSON im Proxy.

**Worauf es ankommt **Der Controller verwendet dieselben TrainingTools wie SimpleTool. Math.addExact erkennt Integer-Überläufe; vom Modell gelieferte Argumente sind fachlich zu prüfen.

## 9 ToolSearchController

POST /api/tool-search

Eingabe: Text im Body (Content-Type: text/plain).

**Lernziel **Eine zusätzliche Tool-Suche als Konfigurationsthema einordnen.

**Didaktischer Sinn **Der Controller registriert dieselben TrainingTools wie SimpleTool. Das Profil tool-search aktiviert die zusätzliche Advisor-Funktion über eine Property.

```java
return chatClient.prompt()
.user(message)
.tools(new TrainingTools())
.call()
.content();
```

**Gezielte Übung **Starte mit und ohne Profil tool-search und vergleiche die angebotenen Werkzeuge sowie den Ausführungspfad.

**Worauf es ankommt **Der Controller enthält selbst keine Suche und keinen großen Tool-Katalog. Ein Unterschied ist über Profil und Advisor-Verhalten zu untersuchen, nicht aus dem Namen abzuleiten.

## 10 UserControlledToolController

POST /api/user-controlled-tool

Eingabe: Text im Body (Content-Type: text/plain).

**Lernziel **Die Tool-Schleife anwendungsseitig kontrollieren.

**Didaktischer Sinn **Die automatische Advisor-Registrierung wird deaktiviert. Die Anwendung prüft hasToolCalls, führt sie über ToolCallingManager aus und übergibt die erweiterte Historie erneut.

```java
var tools = ToolCallbacks.from(new TrainingTools());
var options = ToolCallingChatOptions.builder().toolCallbacks(tools).build();
var prompt = new Prompt(List.of(new UserMessage(message)),options);
var response = chatClient.prompt(prompt)
.advisors(AdvisorParams.toolCallingAdvisorAutoRegister(false))
.call().chatClientResponse();
// Weitere Verarbeitung siehe Controller-Quellcode.
```

**Gezielte Übung **Verfolge prompt und response über mehrere Runden. Vergleiche rounds mit dem automatischen Advisor-Beispiel.

**Worauf es ankommt **Die Grenze zählt hier Schleifenrunden, nicht zwingend einzelne Tools. Die Anwendung muss Abbruch, Fehler, Historie und direkte Ergebnisse selbst angemessen behandeln.

## Transfer und Vertiefung

1. Welche Teile laufen in der Anwendung, welche im Modell und welche in einem externen Dienst? Ordne den wichtigsten Controller-Aufruf jeder Ebene zu.

2. Was zeigt die REST-Antwort, und welche zusätzlichen Informationen erhältst du im Debugger oder HTTP-Proxy? Unterscheide Datenstruktur, fachlichen Inhalt und technischen Ablauf.

3. Formuliere für zwei Controller eine passende und eine unpassende Eingabe. Erkläre, ob du eine andere Antwort, eine leere Ergebnismenge oder einen technischen Fehler erwartest.

4. Welche Aufgabe wird vom gezeigten Controller tatsächlich erledigt, welche wird nur vorbereitet? Identifiziere wiederverwendbare Komponenten und Unterschiede zur Produktionsimplementierung.

## Technische Referenz

Spring AI Reference, ToolCalling. Die Übungen verwenden die im Projekt eingebundenen APIs. Vertiefung:

https://docs.spring.io/spring-ai/reference/
