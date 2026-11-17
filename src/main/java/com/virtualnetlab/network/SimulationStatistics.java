
package com.virtualnetlab.network;

/**
 * Tracks network performance across multiple packets.
 */
public class SimulationStatistics {

    private int totalPackets;
    private int deliveredPackets;
    private int lostPackets;

    private long deliveredBits;
    private double totalElapsedSeconds;

    /**
     * Records the result of one packet transmission.
     */
    public void recordResult(
            Packet packet,
            TransmissionResult result) {

        if (packet == null || result == null) {
            throw new IllegalArgumentException(
                    "Packet and result cannot be null.");
        }

        totalPackets++;

        totalElapsedSeconds += result.getTotalDelaySeconds();

        if (result.isDelivered()) {

            deliveredPackets++;

            deliveredBits += packet.getSizeBits();

        } else {

            lostPackets++;
        }
    }

    public int getTotalPackets() {
        return totalPackets;
    }

    public int getDeliveredPackets() {
        return deliveredPackets;
    }

    public int getLostPackets() {
        return lostPackets;
    }

    public double getPacketLossPercentage() {

        if (totalPackets == 0) {
            return 0;
        }

        return (double) lostPackets / totalPackets * 100;
    }

    public double getThroughputBps() {

        if (totalElapsedSeconds <= 0) {
            return 0;
        }

        return deliveredBits / totalElapsedSeconds;
    }

    public long getDeliveredBits() {
        return deliveredBits;
    }

    public double getTotalElapsedSeconds() {
        return totalElapsedSeconds;
    }
}
