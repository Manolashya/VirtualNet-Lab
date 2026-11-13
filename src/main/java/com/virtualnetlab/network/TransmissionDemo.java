
package com.virtualnetlab.network;

import java.util.Random;

public class TransmissionDemo {

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

        // 2. Create topology
        NetworkTopology topology = new NetworkTopology();

        topology.addNode(computer);
        topology.addNode(networkSwitch);
        topology.addNode(router);
        topology.addNode(server);

        // 3. Add bidirectional connections
        addConnection(topology, computer, networkSwitch, 100_000_000);
        addConnection(topology, networkSwitch, router, 1_000_000_000);
        addConnection(topology, router, server, 1_000_000_000);

        // 4. Create packet
        Packet packet = new Packet(
                "P001", computer, server, 1000);

        // 5. Create simulator
        NetworkSimulator simulator =
                new NetworkSimulator(new Random(42));

        System.out.println("===== PACKET TRANSMISSION SIMULATION =====");

        System.out.println("\nPacket Details:");
        System.out.println(packet);

        // CASE 1: No packet loss
        System.out.println("\nCASE 1: Packet Loss Probability = 0%");

        TransmissionResult result1 =
                simulator.simulateTransmission(packet, topology, 0.0);

        displayResult(result1);

        // CASE 2: 100% packet loss
        System.out.println("\nCASE 2: Packet Loss Probability = 100%");

        TransmissionResult result2 =
                simulator.simulateTransmission(packet, topology, 1.0);

        displayResult(result2);
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

    private static void displayResult(TransmissionResult result) {

        if (result.isDelivered()) {
            System.out.println("Status: PACKET DELIVERED");
        } else {
            System.out.println("Status: PACKET LOST");
            System.out.println("Lost at: " + result.getLostAtLink());
        }

        System.out.println("Links traversed: " + result.getRoute().size());

        System.out.printf(
                "Total Delay: %.8f seconds%n",
                result.getTotalDelaySeconds());
    }
}
