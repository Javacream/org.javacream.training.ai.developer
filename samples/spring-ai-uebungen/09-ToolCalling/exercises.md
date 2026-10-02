# Übungsaufgaben

Die Controller enthalten lauffähige Beispiele. Verwende die Referenz für eigene Erweiterungen.

## SimpleToolController

Lernziel: Frage z.B. Welches Datum ist heute?.

1. Öffne `POST /api/simple-tool` in Swagger UI und lies den Controller.
2. Teste „Welches Datum ist heute? Nutze das Tool.“. Prüfe, ob das Modell das Tool auswählt und welche Argumente es liefert.
3. Implementiere eine kleine Erweiterung passend zum Lernziel und dokumentiere deren erwartetes Verhalten.

## ToolParametersController

Lernziel: Frage z.B. Addiere 17 und 25; @ToolParam.

1. Öffne `POST /api/tool-parameters` in Swagger UI und lies den Controller.
2. Teste „Addiere 17 und 25 mithilfe des Tools.“. Prüfe, ob das Modell das Tool auswählt und welche Argumente es liefert.
3. Implementiere eine kleine Erweiterung passend zum Lernziel und dokumentiere deren erwartetes Verhalten.

## ToolContextController

Lernziel: Frage nach der Tenant-ID; Context wird von der Anwendung gesetzt.

1. Öffne `POST /api/tool-context` in Swagger UI und lies den Controller.
2. Teste „Wie lautet meine Tenant-ID? Nutze das Tool.“. Prüfe, ob das Modell das Tool auswählt und welche Argumente es liefert.
3. Implementiere eine kleine Erweiterung passend zum Lernziel und dokumentiere deren erwartetes Verhalten.

## ToolCallbackController

Lernziel: ToolCallback.

1. Öffne `POST /api/tool-callback` in Swagger UI und lies den Controller.
2. Teste „Multipliziere 7 mit 8 mithilfe des Tools.“. Prüfe, ob das Modell das Tool auswählt und welche Argumente es liefert.
3. Implementiere eine kleine Erweiterung passend zum Lernziel und dokumentiere deren erwartetes Verhalten.

## ToolCallbackProviderController

Lernziel: ToolCallbackProvider.

1. Öffne `POST /api/tool-callback-provider` in Swagger UI und lies den Controller.
2. Teste „Addiere 4 und 5 und nenne das heutige Datum. Nutze die Tools.“. Prüfe, ob das Modell das Tool auswählt und welche Argumente es liefert.
3. Implementiere eine kleine Erweiterung passend zum Lernziel und dokumentiere deren erwartetes Verhalten.

## ReturnDirectController

Lernziel: ReturnDirect.

1. Öffne `POST /api/return-direct` in Swagger UI und lies den Controller.
2. Teste „Gib mir den Trainingscode mithilfe des Tools.“. Prüfe, ob das Modell das Tool auswählt und welche Argumente es liefert.
3. Implementiere eine kleine Erweiterung passend zum Lernziel und dokumentiere deren erwartetes Verhalten.

## ToolErrorHandlingController

Lernziel: Absichtlicher Tool-Fehler; abhängig vom ExceptionProcessor an Modell zurückgegeben oder HTTP 502.

1. Öffne `POST /api/tool-error-handling` in Swagger UI und lies den Controller.
2. Teste „Rufe das fail-Tool auf.“. Prüfe, ob das Modell das Tool auswählt und welche Argumente es liefert.
3. Implementiere eine kleine Erweiterung passend zum Lernziel und dokumentiere deren erwartetes Verhalten.

## ToolCallingAdvisorController

Lernziel: ToolCallingAdvisor.

1. Öffne `POST /api/tool-calling-advisor` in Swagger UI und lies den Controller.
2. Teste „Addiere 17 und 25 und nenne das heutige Datum. Nutze die Tools.“. Prüfe, ob das Modell das Tool auswählt und welche Argumente es liefert.
3. Implementiere eine kleine Erweiterung passend zum Lernziel und dokumentiere deren erwartetes Verhalten.

## UserControlledToolController

Lernziel: UserControlledTool.

1. Öffne `POST /api/user-controlled-tool` in Swagger UI und lies den Controller.
2. Teste „Addiere 17 und 25 und nenne das heutige Datum. Nutze die Tools.“. Prüfe, ob das Modell das Tool auswählt und welche Argumente es liefert.
3. Implementiere eine kleine Erweiterung passend zum Lernziel und dokumentiere deren erwartetes Verhalten.

## ToolSearchController

Lernziel: Mit Profil tool-search starten; automatische ToolSearchAdvisor-Registrierung.

1. Öffne `POST /api/tool-search` in Swagger UI und lies den Controller.
2. Teste „Addiere 17 und 25 mithilfe des passenden Tools.“. Prüfe, ob das Modell das Tool auswählt und welche Argumente es liefert.
3. Implementiere eine kleine Erweiterung passend zum Lernziel und dokumentiere deren erwartetes Verhalten.
