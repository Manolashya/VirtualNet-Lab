package com.virtualnetlab.transport;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Educational TCP-like simulation. This is not a real TCP implementation.
 * Packet loss is generated predictably using a fixed random seed.
 */
public class TcpSimulation {
    private static final int MAX_RETRANSMISSIONS = 3;
    private static final int SEGMENT_SIZE = 5;

    public TransportResult run(String message, int lossPercent) {
        validate(message, lossPercent);

        List<String> steps = new ArrayList<>();
        steps.add("TCP: Client sends SYN.");
        steps.add("TCP: Server replies with SYN-ACK.");
        steps.add("TCP: Client sends ACK. Three-way handshake completed.");

        int segmentCount = (message.length() + SEGMENT_SIZE - 1) / SEGMENT_SIZE;
        int packetsSent = 0;
        int packetsDelivered = 0;
        int retransmissions = 0;
        Random random = new Random(12345);

        for (int i = 0; i < segmentCount; i++) {
            int sequenceNumber = i + 1;
            String segment = message.substring(i * SEGMENT_SIZE,
                    Math.min(message.length(), (i + 1) * SEGMENT_SIZE));
            boolean received = false;
            int attempts = 0;

            while (!received && attempts <= MAX_RETRANSMISSIONS) {
                attempts++;
                packetsSent++;
                steps.add("TCP: Send data segment '" + segment
                        + "' (sequence number " + sequenceNumber + ").");

                boolean lost = random.nextInt(100) < lossPercent;
                if (lost) {
                    steps.add("TCP: Segment " + sequenceNumber
                            + " lost. Timeout waiting for ACK.");
                    if (attempts <= MAX_RETRANSMISSIONS) {
                        retransmissions++;
                        steps.add("TCP: Retransmitting segment " + sequenceNumber + ".");
                    }
                } else {
                    received = true;
                    packetsDelivered++;
                    steps.add("TCP: Receiver sends ACK " + (sequenceNumber + 1) + ".");
                }
            }

            if (!received) {
                steps.add("TCP: Maximum retransmissions reached for segment "
                        + sequenceNumber + ".");
                return new TransportResult("TCP", false, packetsSent,
                        packetsDelivered, retransmissions, steps);
            }
        }

        steps.add("TCP: All data segments delivered successfully.");
        return new TransportResult("TCP", true, packetsSent,
                packetsDelivered, retransmissions, steps);
    }

    private void validate(String message, int lossPercent) {
        if (message == null || message.isBlank()) {
            throw new IllegalArgumentException("Message must not be empty.");
        }
        if (lossPercent < 0 || lossPercent > 100) {
            throw new IllegalArgumentException("Packet loss must be between 0 and 100.");
        }
    }
}
