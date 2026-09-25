package org.javacream.training.springai.block8;

import java.util.Optional;

public final class Guardrails {
    private Guardrails() {}
    public static Optional<String> rejectReason(String input) {
        if (input == null || input.isBlank()) return Optional.of("blank");
        if (input.length() > 4000) return Optional.of("too-long");
        return Optional.empty();
    }
}
