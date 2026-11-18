package com.virtualnetlab.transport;

import java.util.List;

/**
 * Stores the result of one simplified transport-layer simulation.
 */
public class TransportResult {
    private final String protocol;
    private final boolean delivered;
    private final int packetsSent;
    private final int packetsDelivered;
    private final int retransmissions;
    private final List<String> steps;

    public TransportResult(String protocol, boolean delivered, int packetsSent,
                            int packetsDelivered, int retransmissions, List<String> steps) {
        this.protocol = protocol;
        this.delivered = delivered;
        this.packetsSent = packetsSent;
        this.packetsDelivered = packetsDelivered;
        this.retransmissions = retransmissions;
        this.steps = List.copyOf(steps);
    }

    public String getProtocol() { return protocol; }
    public boolean isDelivered() { return delivered; }
    public int getPacketsSent() { return packetsSent; }
    public int getPacketsDelivered() { return packetsDelivered; }
    public int getRetransmissions() { return retransmissions; }
    public List<String> getSteps() { return steps; }

    public String getSummary() {
        return protocol + " | delivered=" + delivered
                + " | packets sent=" + packetsSent
                + " | packets delivered=" + packetsDelivered
                + " | retransmissions=" + retransmissions;
    }
}
