
package com.virtualnetlab.network;

import java.util.Random;

public class StatisticsDemo {

    public static void main(String[] args) {

        // 1. Create network devices
        Node computer = new Node(
                "N1", "Computer 1", Node.NodeType.COMPUTER);

        Node networkSwitch = new Node(
                "N2", "Switch 1", Node.NodeType.SWITCH);

        Node router = new Node(
                "N3", "Router 1", Node.NodeType.ROUTER);

        Node server = new Node(
                "N4", "Server 1", Node.NodeType.SERVER);

        // 2. Create network topology
        NetworkTopology topology = new NetworkTopology();

        topology.addNode(computer);
        topology.addNode(networkSwitch);
        topology.addNode(router);
        topology.addNode(server);

        addConnection(topology, computer, networkSwitch, 100_000_000);
        addConnection(topology, networkSwitch, router, 1_000_000_000);
        addConnection(topology, router, server, 1_000_000_000);

        // 3. Create simulator and statistics tracker
        NetworkSimulator simulator =
                new NetworkSimulator(new Random(42));

        SimulationStatistics statistics =
                new SimulationStatistics();

        // 4. Configure simulation
        int numberOfPackets = 10;
        double lossProbability = 0.20;

        System.out.println("===== MULTIPLE PACKET SIMULATION =====");

        System.out.println("Total packets: " + numberOfPackets);
        System.out.println("Packet size: 1000 bytes");
        System.out.println("Packet loss probability: 20%");

        // 5. Transmit packets
        for (int i = 1; i <= numberOfPackets; i++) {

            Packet packet = new Packet(
                    "P" + String.format("%03d", i),
                    computer,
                    server,
                    1000
            );

            TransmissionResult result =
                    simulator.simulateTransmission(
                            packet, topology, lossProbability);

            statistics.recordResult(packet, result);

            System.out.println(
                    packet.getPacketId() + " : "
                    + (result.isDelivered()
                    ? "DELIVERED" : "LOST"));
        }

        // 6. Display network statistics
        System.out.println("\n===== NETWORK PERFORMANCE =====");

        System.out.println(
                "Total Packets: "
                + statistics.getTotalPackets());

        System.out.println(
                "Delivered Packets: "
                + statistics.getDeliveredPackets());

        System.out.println(
                "Lost Packets: "
                + statistics.getLostPackets());

        System.out.printf(
                "Packet Loss: %.2f%%%n",
                statistics.getPacketLossPercentage());

        System.out.println(
                "Delivered Data: "
                + statistics.getDeliveredBits() + " bits");

        System.out.printf(
                "Total Simulated Time: %.8f seconds%n",
                statistics.getTotalElapsedSeconds());

        System.out.printf(
                "Average Throughput: %.2f Mbps%n",
                statistics.getThroughputBps() / 1_000_000);
    }

    private static void addConnection(
            NetworkTopology topology,
            Node first,
            Node second,
            double bandwidth) {

        topology.addLink(new Link(
                first, second, bandwidth, 50, 200_000_000));

        topology.addLink(new Link(
                second, first, bandwidth, 50, 200_000_000));
    }
}
