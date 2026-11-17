package com.virtualnetlab.application;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EmailSimulationTest {
    @Test
    void sendAndReceiveAddsMessageToInbox() {
        EmailSimulation email = new EmailSimulation();
        EmailSimulation.EmailMessage message = new EmailSimulation.EmailMessage(
                "alice@example.com", "bob@example.com", "Hello", "Test message");

        EmailSimulation.EmailResult result = email.sendAndReceive(message);

        assertEquals(message, result.message());
        assertEquals(1, email.getInboxSize());
    }
}
