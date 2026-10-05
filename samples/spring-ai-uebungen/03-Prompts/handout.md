# Handout: Prompts

*Projekt 03-Prompts*

Dieses Handout erläutert die 6 Controller des Projekts mit Lernziel, didaktischem Sinn und gezielten Übungen. Die Beispiele werden am tatsächlichen Aufrufpfad untersucht: REST-Eingabe, Spring-AI-Verarbeitung und Ergebnis. Die folgenden Codeauszüge zeigen den Kern der jeweiligen execute-Methode.

## Vorbereitung und gemeinsame Konfiguration

Benötigt werden JDK 17 oder neuer, Maven 3.9+ und ein laufendes Ollama. Die Modellintegration verwendet normalerweise llama3.2 mit Temperature 0.2; Modelle werden nicht automatisch heruntergeladen. Weitere Voraussetzungen stehen in requirements.md des Projekts.

```bash
ollama pull llama3.2
mvn spring-boot:run
```

Die Anwendung läuft auf Port 8083. Swagger UI: http://localhost:8083/swagger-ui.html. Die Proxy-Weboberfläche läuft bei dir auf Port 9082. Für Ollama-Aufrufe über den Proxy:

```bash
OLLAMA_BASE_URL=http://localhost:11435 mvn spring-boot:run
```

## Überblick über die Lernfolge

MessageRolesController – Die Rollen System, User und Assistant unterscheiden.

PromptController – Ein Prompt-Objekt explizit erstellen.

PromptTemplateController – Stabile Textstruktur und variable Daten trennen.

ResourcePromptController – Prompt-Texte außerhalb des Java-Codes pflegen.

SystemPromptTemplateController – Eine parametrisierte System Message erzeugen.

TemplateRendererController – Die Syntax der Template-Platzhalter gezielt konfigurieren.

## 1 MessageRolesController

POST /api/message-roles

Eingabe: Text im Body (Content-Type: text/plain).

**Lernziel **Die Rollen System, User und Assistant unterscheiden.

**Didaktischer Sinn **Eine feste System-Anweisung und ein vorgegebenes Frage-Antwort-Paar werden vor die aktuelle User-Nachricht gesetzt. Die Rollen transportieren Kontext und Aufgabenverteilung.

```java
var prompt = new Prompt(List.of(new SystemMessage("Du bist ein präziser Tutor."), new
UserMessage("Was ist Java?"), new AssistantMessage("Java ist eine
Programmiersprache."), new UserMessage(message)));
return chatClient.prompt(prompt)
.call()
.content();
```

**Gezielte Übung **Sende „Welche Vorteile hat diese Sprache?“. Untersuche, wie die vorangehende Java-Antwort die Referenz auf diese Sprache ermöglicht.

**Worauf es ankommt **Das Assistant-Beispiel ist von der Anwendung vorgegeben. Es wurde bei diesem Aufruf nicht vom Modell erzeugt; die Historie bleibt statisch.

## 2 PromptController

POST /api/prompt

Eingabe: Text im Body (Content-Type: text/plain).

**Lernziel **Ein Prompt-Objekt explizit erstellen.

**Didaktischer Sinn **Der Controller übergibt new Prompt(message) an den ChatClient. Er zeigt die Objektebene hinter der Fluent API und ermöglicht den späteren Aufbau komplexerer Nachrichtenfolgen.

```java
return chatClient.prompt(new org.springframework.ai.chat.prompt.Prompt(message))
.call()
.content();
```

**Gezielte Übung **Sende dieselbe Frage an diesen Controller und an SimpleChat aus Projekt 01. Vergleiche die Prompts im HTTP-Proxy.

**Worauf es ankommt **Ein Prompt-Objekt erzeugt noch keine Modellantwort und speichert keinen Gesprächsverlauf.

## 3 PromptTemplateController

POST /api/prompt-template

Eingabe: Text im Body (Content-Type: text/plain).

**Lernziel **Stabile Textstruktur und variable Daten trennen.

**Didaktischer Sinn **PromptTemplate setzt topic in „Erkläre {topic} mit einem Beispiel.“ ein. Die Anwendung formuliert den Auftrag; der Request-Body liefert das Thema.

```java
var template = new PromptTemplate("Erkläre {topic} mit einem Beispiel.");
return chatClient.prompt(template.create(Map.of("topic",message)))
.call()
.content();
```

**Gezielte Übung **Sende „Dependency Injection“ und „Transaktionen“. Untersuche jeweils den gerenderten Prompt.

**Worauf es ankommt **Das Template wird in Java gerendert. Das Modell sieht den fertigen Text, keine Platzhalter; Templates sind keine Sicherheitsgrenze.

## 4 ResourcePromptController

POST /api/resource-prompt

Eingabe: Text im Body (Content-Type: text/plain).

**Lernziel **Prompt-Texte außerhalb des Java-Codes pflegen.

**Didaktischer Sinn **Das Template kommt aus prompts/explain.st als ClassPathResource. Die Übung trennt fachliche Formulierungen von der Aufruflogik.

```java
var template = new PromptTemplate(new ClassPathResource("prompts/explain.st"));
return chatClient.prompt(template.create(Map.of("topic", message)))
.call()
.content();
```

**Gezielte Übung **Ändere die Ressource und starte die Anwendung neu. Vergleiche die erzeugten Texte bei gleicher Eingabe.

**Worauf es ankommt **Die Ressource muss auf dem Classpath liegen. Änderungen an einer gepackten Anwendung erfordern eine aktualisierte Ressource im Artefakt.

```java
// Ressource prompts/explain.st:
Erkläre {topic} für Java-Entwickler. Gib ein kleines Beispiel.
```

## 5 SystemPromptTemplateController

POST /api/system-prompt-template

Eingabe: Text im Body (Content-Type: text/plain).

**Lernziel **Eine parametrisierte System Message erzeugen.

**Didaktischer Sinn **Das Thema bestimmt die Expertenrolle; die User Message bleibt „Nenne drei zentrale Konzepte.“. Das verdeutlicht die Trennung von Rolle und Arbeitsauftrag.

```java
var system = new SystemPromptTemplate("Du bist Experte für
{topic}.").createMessage(Map.of("topic",message));
return chatClient.prompt(new Prompt(List.of(system, new UserMessage("Nenne drei
zentrale Konzepte."))))
.call()
.content();
```

**Gezielte Übung **Sende „Spring“ und „Datenbanken“. Vergleiche die Rollen im Proxy mit PromptTemplate.

**Worauf es ankommt **Die System Message steuert die Antwort, garantiert aber weder Faktenrichtigkeit noch vollständige Einhaltung.

## 6 TemplateRendererController

POST /api/template-renderer

Eingabe: Text im Body (Content-Type: text/plain).

**Lernziel **Die Syntax der Template-Platzhalter gezielt konfigurieren.

**Didaktischer Sinn **StTemplateRenderer verwendet hier <topic> anstelle von geschweiften Klammern. Das ist hilfreich, wenn der Prompt selbst JSON mit geschweiften Klammern enthält.

```java
return chatClient.prompt()
.user(u -> u.text("Erkläre <topic>.").param("topic",
message)).templateRenderer(StTemplateRenderer.builder().startDelimiterToken('<').endDelimiterToken('>').build())
.call()
.content();
```

**Gezielte Übung **Ergänze ein JSON-Beispiel im Prompt und vergleiche die Lesbarkeit mit der Standardsyntax.

**Worauf es ankommt **Der Renderer ist für diese ChatClient-Anfrage gesetzt. Die Konfiguration wirkt nicht automatisch auf interne Templates beliebiger Advisors.

## Transfer und Vertiefung

1. Welche Teile laufen in der Anwendung, welche im Modell und welche in einem externen Dienst? Ordne den wichtigsten Controller-Aufruf jeder Ebene zu.

2. Was zeigt die REST-Antwort, und welche zusätzlichen Informationen erhältst du im Debugger oder HTTP-Proxy? Unterscheide Datenstruktur, fachlichen Inhalt und technischen Ablauf.

3. Formuliere für zwei Controller eine passende und eine unpassende Eingabe. Erkläre, ob du eine andere Antwort, eine leere Ergebnismenge oder einen technischen Fehler erwartest.

4. Welche Aufgabe wird vom gezeigten Controller tatsächlich erledigt, welche wird nur vorbereitet? Identifiziere wiederverwendbare Komponenten und Unterschiede zur Produktionsimplementierung.

## Technische Referenz

Spring AI Reference, Prompts. Die Übungen verwenden die im Projekt eingebundenen APIs. Vertiefung:

https://docs.spring.io/spring-ai/reference/
