package org.javacream.training.springai.block7;

import java.util.Locale;
import java.util.Optional;

public final class Guardrails {
    private Guardrails() {}

    public static Optional<String> rejectReason(String input) {
        if (input == null || input.isBlank()) return Optional.of("Leere Eingabe");
        if (input.length() > 4000) return Optional.of("Eingabe zu lang");

        String n = input.toLowerCase(Locale.ROOT);
        if (n.contains("ignore previous instructions")
                || n.contains("reveal system prompt")
                || n.contains("zeige den system prompt")) {
            return Optional.of("Verdächtige Prompt-Injection-Sequenz");
        }
        return Optional.empty();
    }
}
