package org.javacream.training.springai.block8;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class GuardrailsTest {
    @Test
    void rejectsBlankInput() {
        assertTrue(Guardrails.rejectReason("   ").isPresent());
    }

    @Test
    void acceptsNormalInput() {
        assertTrue(Guardrails.rejectReason("Erkläre Spring AI").isEmpty());
    }
}
