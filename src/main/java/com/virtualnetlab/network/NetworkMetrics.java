
package com.virtualnetlab.network;

/**
 * Calculates important network performance metrics.
 */
public class NetworkMetrics {

    /**
     * Calculates the time required to transmit a packet.
     * Returns delay in seconds.
     */
    public static double calculateTransmissionDelay(
            Packet packet, Link link) {

        if (packet == null || link == null) {
            throw new IllegalArgumentException(
                    "Packet and link cannot be null.");
        }

        return (double) packet.getSizeBits()
                / link.getBandwidthBps();
    }

    /**
     * Calculates the time required for a signal to travel.
     * Returns delay in seconds.
     */
    public static double calculatePropagationDelay(Link link) {

        if (link == null) {
            throw new IllegalArgumentException(
                    "Link cannot be null.");
        }

        return link.getDistanceMeters()
                / link.getPropagationSpeedMps();
    }

    /**
     * Calculates total network delay in seconds.
     */
    public static double calculateTotalDelay(
            Packet packet, Link link) {

        double transmissionDelay =
                calculateTransmissionDelay(packet, link);

        double propagationDelay =
                calculatePropagationDelay(link);

        return transmissionDelay + propagationDelay;
    }

    /**
     * Calculates throughput in bits per second.
     */
    public static double calculateThroughput(
            long deliveredBits, double elapsedSeconds) {

        if (deliveredBits < 0) {
            throw new IllegalArgumentException(
                    "Delivered bits cannot be negative.");
        }

        if (elapsedSeconds <= 0
                || !Double.isFinite(elapsedSeconds)) {
            throw new IllegalArgumentException(
                    "Elapsed time must be positive and finite.");
        }

        return deliveredBits / elapsedSeconds;
    }

    /**
     * Calculates packet loss percentage.
     */
    public static double calculatePacketLoss(
            int totalPackets, int lostPackets) {

        if (totalPackets <= 0) {
            throw new IllegalArgumentException(
                    "Total packets must be greater than zero.");
        }

        if (lostPackets < 0 || lostPackets > totalPackets) {
            throw new IllegalArgumentException(
                    "Lost packets must be between zero and total packets.");
        }

        return (double) lostPackets / totalPackets * 100;
    }
}
