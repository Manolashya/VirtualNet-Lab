package com.virtualnetlab.transport;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Educational UDP-like simulation. UDP does not establish a connection
 * and does not automatically retransmit lost datagrams.
 */
public class UdpSimulation {
    private static final int DATAGRAM_SIZE = 5;

    public TransportResult run(String message, int lossPercent) {
        validate(message, lossPercent);

        List<String> steps = new ArrayList<>();
        steps.add("UDP: No connection setup is performed.");

        int datagramCount = (message.length() + DATAGRAM_SIZE - 1) / DATAGRAM_SIZE;
        int packetsSent = 0;
        int packetsDelivered = 0;
        Random random = new Random(12345);

        for (int i = 0; i < datagramCount; i++) {
            int datagramNumber = i + 1;
            String part = message.substring(i * DATAGRAM_SIZE,
                    Math.min(message.length(), (i + 1) * DATAGRAM_SIZE));
            packetsSent++;
            steps.add("UDP: Send datagram '" + part + "' (datagram "
                    + datagramNumber + ").");

            boolean lost = random.nextInt(100) < lossPercent;
            if (lost) {
                steps.add("UDP: Datagram " + datagramNumber
                        + " lost. No automatic retransmission.");
            } else {
                packetsDelivered++;
                steps.add("UDP: Datagram " + datagramNumber + " delivered.");
            }
        }

        boolean delivered = packetsDelivered == datagramCount;
        steps.add(delivered
                ? "UDP: All datagrams delivered."
                : "UDP: Some datagrams were lost; UDP does not resend them automatically.");

        return new TransportResult("UDP", delivered, packetsSent,
                packetsDelivered, 0, steps);
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
