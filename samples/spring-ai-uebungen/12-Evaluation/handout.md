# Handout: Evaluation

*Projekt 12-Evaluation*

Dieses Handout erläutert die 3 Controller des Projekts mit Lernziel, didaktischem Sinn und gezielten Übungen. Die Beispiele werden am tatsächlichen Aufrufpfad untersucht: REST-Eingabe, Spring-AI-Verarbeitung und Ergebnis. Die folgenden Codeauszüge zeigen den Kern der jeweiligen execute-Methode.

## Vorbereitung und gemeinsame Konfiguration

Benötigt werden JDK 17 oder neuer, Maven 3.9+ und ein laufendes Ollama. Die Modellintegration verwendet normalerweise llama3.2 mit Temperature 0.2; Modelle werden nicht automatisch heruntergeladen. Weitere Voraussetzungen stehen in requirements.md des Projekts.

```bash
ollama pull llama3.2
mvn spring-boot:run
```

Die Anwendung läuft auf Port 8092. Swagger UI: http://localhost:8092/swagger-ui.html. Die Proxy-Weboberfläche läuft bei dir auf Port 9082. Für Ollama-Aufrufe über den Proxy:

```bash
OLLAMA_BASE_URL=http://localhost:11435 mvn spring-boot:run
```

CustomEvaluator erwartet Text. Die beiden modellgestützten Evaluatoren erwarten JSON mit question, context und answer. FactCheckingEvaluator nutzt ausdrücklich das Zusatzmodell bespoke-minicheck, während RelevancyEvaluator den normalen ChatClient verwendet.

```bash
ollama pull bespoke-minicheck
```

## Überblick über die Lernfolge

CustomEvaluatorController – Eine deterministische Bewertungsregel implementieren.

FactCheckingEvaluatorController – Eine Antwort gegen vorgegebenen Kontext prüfen.

RelevancyEvaluatorController – Antwortrelevanz mit einem Modell bewerten.

## 1 CustomEvaluatorController

POST /api/custom-evaluator

Eingabe: Text im Body (Content-Type: text/plain); zusätzliche Query-Parameter gemäß Swagger.

**Lernziel **Eine deterministische Bewertungsregel implementieren.

**Didaktischer Sinn **Ein Evaluator prüft, ob die übergebene Antwort den required-Begriff enthält, und gibt Pass/Fail mit Score 1 oder 0 zurück. Die Bewertungs-API wird ohne Modellabhängigkeit sichtbar.

```java
Evaluator evaluator = request -> {
String answer=request.getResponseContent();
boolean pass=answer!=null && answer.contains(required);
return new EvaluationResponse(pass,pass?1.0f:0.0f,pass?"Required term found":"Required
term missing",Map.of());
```

**Gezielte Übung **Sende Antworten mit und ohne „Spring“. Ändere required über den Query-Parameter und teste Groß-/Kleinschreibung.

**Worauf es ankommt **contains ist eine einfache, case-sensitive Textprüfung. Sie bewertet weder fachliche Richtigkeit noch sinnvollen Zusammenhang; message ist die zu bewertende Antwort.

## 2 FactCheckingEvaluatorController

POST /api/fact-checking-evaluator

Eingabe: application/json mit question, context und answer.

**Lernziel **Eine Antwort gegen vorgegebenen Kontext prüfen.

**Didaktischer Sinn **Dieser Controller verwendet gezielt bespoke-minicheck mit Temperature 0 und numPredict 2. Der fachliche Kontext wird als Grundlage der Prüfung eingesetzt.

```java
var
builder=chatClient.mutate().defaultOptions(OllamaChatOptions.builder().model("bespoke-minicheck").temperature(0.0).numPredict(2));
return FactCheckingEvaluator.forBespokeMinicheck(builder).evaluate(new
EvaluationRequest(input.context(),List.of(),input.answer()));
```

**Gezielte Übung **Vergleiche eine belegte und eine widersprüchliche Antwort zu demselben Kontext. Lade das Zusatzmodell vorher.

**Worauf es ankommt **question ist im Record vorhanden, wird hier aber nicht verwendet. Es handelt sich um Kontexttreueprüfung, nicht um eine Recherche nach allgemein gültigen Fakten.

## 3 RelevancyEvaluatorController

POST /api/relevancy-evaluator

Eingabe: application/json mit question, context und answer.

**Lernziel **Antwortrelevanz mit einem Modell bewerten.

**Didaktischer Sinn **EvaluationRequest enthält question, ein Context-Document und answer. RelevancyEvaluator nutzt den ChatClient als Bewerter.

```java
var request=new EvaluationRequest(input.question(),List.of(new
Document(input.context())),input.answer());
return new RelevancyEvaluator(chatClient.mutate()).evaluate(request);
```

**Gezielte Übung **Sende passende und themenfremde Antworten zum gleichen Kontext. Wiederhole die Bewertung und vergleiche Scores und Begründungen.

**Worauf es ankommt **Eine modellgestützte Bewertung ist selbst fehlbar. Der Endpunkt erzeugt die ursprüngliche Antwort nicht, sondern bewertet das übergebene JSON.

## Transfer und Vertiefung

1. Welche Teile laufen in der Anwendung, welche im Modell und welche in einem externen Dienst? Ordne den wichtigsten Controller-Aufruf jeder Ebene zu.

2. Was zeigt die REST-Antwort, und welche zusätzlichen Informationen erhältst du im Debugger oder HTTP-Proxy? Unterscheide Datenstruktur, fachlichen Inhalt und technischen Ablauf.

3. Formuliere für zwei Controller eine passende und eine unpassende Eingabe. Erkläre, ob du eine andere Antwort, eine leere Ergebnismenge oder einen technischen Fehler erwartest.

4. Welche Aufgabe wird vom gezeigten Controller tatsächlich erledigt, welche wird nur vorbereitet? Identifiziere wiederverwendbare Komponenten und Unterschiede zur Produktionsimplementierung.

## Technische Referenz

Spring AI Reference, Evaluation. Die Übungen verwenden die im Projekt eingebundenen APIs. Vertiefung:

https://docs.spring.io/spring-ai/reference/
