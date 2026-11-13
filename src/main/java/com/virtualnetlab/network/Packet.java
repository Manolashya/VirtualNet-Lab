
package com.virtualnetlab.network;

import java.util.Objects;

/**
 * Represents a data packet travelling through a network.
 */
public class Packet {

    private final String packetId;
    private final Node source;
    private final Node destination;
    private final int sizeBytes;

    public Packet(String packetId, Node source,
                  Node destination, int sizeBytes) {

        if (packetId == null || packetId.isBlank()) {
            throw new IllegalArgumentException(
                    "Packet ID cannot be empty.");
        }

        this.packetId = packetId;

        this.source = Objects.requireNonNull(
                source, "Source node cannot be null.");

        this.destination = Objects.requireNonNull(
                destination, "Destination node cannot be null.");

        if (source.equals(destination)) {
            throw new IllegalArgumentException(
                    "Source and destination cannot be the same node.");
        }

        if (sizeBytes <= 0) {
            throw new IllegalArgumentException(
                    "Packet size must be greater than zero.");
        }

        this.sizeBytes = sizeBytes;
    }

    public String getPacketId() {
        return packetId;
    }

    public Node getSource() {
        return source;
    }

    public Node getDestination() {
        return destination;
    }

    public int getSizeBytes() {
        return sizeBytes;
    }

    public int getSizeBits() {
        return sizeBytes * 8;
    }

    @Override
    public String toString() {
        return "Packet " + packetId
                + " | " + source.getName()
                + " -> " + destination.getName()
                + " | Size: " + sizeBytes + " bytes";
    }
}
