
package com.virtualnetlab.network;

public class NetworkDemo {

    public static void main(String[] args) {

        // Step 1: Create network devices
        Node computer = new Node(
                "N1", "Computer 1", Node.NodeType.COMPUTER);

        Node router = new Node(
                "N2", "Router 1", Node.NodeType.ROUTER);

        // Step 2: Connect the devices
        Link link = new Link(
                computer,
                router,
                100_000_000,  // 100 Mbps
                500,          // 500 meters
                200_000_000   // Propagation speed
        );

        // Step 3: Create a data packet
        Packet packet = new Packet(
                "P001",
                computer,
                router,
                1000
        );

        // Step 4: Calculate network metrics
        double transmissionDelay =
                NetworkMetrics.calculateTransmissionDelay(packet, link);

        double propagationDelay =
                NetworkMetrics.calculatePropagationDelay(link);

        double totalDelay =
                NetworkMetrics.calculateTotalDelay(packet, link);

        double throughput =
                NetworkMetrics.calculateThroughput(8000, 0.001);

        double packetLoss =
                NetworkMetrics.calculatePacketLoss(10, 2);

        // Step 5: Display results
        System.out.println("===== VIRTUAL NETWORK SIMULATION =====");

        System.out.println("\nNetwork Connection:");
        System.out.println(link);

        System.out.println("\nPacket Details:");
        System.out.println(packet);

        System.out.println("\nNetwork Performance:");
        System.out.printf("Transmission Delay: %.6f seconds%n",
                transmissionDelay);

        System.out.printf("Propagation Delay: %.6f seconds%n",
                propagationDelay);

        System.out.printf("Total Delay: %.6f seconds%n",
                totalDelay);

        System.out.printf("Throughput: %.2f Mbps%n",
                throughput / 1_000_000);

        System.out.printf("Packet Loss: %.2f%%%n",
                packetLoss);
    }
}
