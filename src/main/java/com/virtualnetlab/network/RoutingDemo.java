package com.virtualnetlab.network;

import java.util.List;

public class RoutingDemo {

    public static void main(String[] args) {

        // Create network devices
        Node computer1 = new Node(
                "N1", "Computer 1", Node.NodeType.COMPUTER);

        Node computer2 = new Node(
                "N2", "Computer 2", Node.NodeType.COMPUTER);

        Node networkSwitch = new Node(
                "N3", "Switch 1", Node.NodeType.SWITCH);

        Node router = new Node(
                "N4", "Router 1", Node.NodeType.ROUTER);

        Node server = new Node(
                "N5", "Server 1", Node.NodeType.SERVER);

        // Create topology
        NetworkTopology topology = new NetworkTopology();

        topology.addNode(computer1);
        topology.addNode(computer2);
        topology.addNode(networkSwitch);
        topology.addNode(router);
        topology.addNode(server);

        // Add bidirectional connections
        addConnection(topology, computer1, networkSwitch, 100_000_000);
        addConnection(topology, computer2, networkSwitch, 100_000_000);
        addConnection(topology, networkSwitch, router, 1_000_000_000);
        addConnection(topology, router, server, 1_000_000_000);

        // Find route using BFS
        NetworkSimulator simulator = new NetworkSimulator();

        List<Link> route = simulator.findRoute(
                topology, computer1, server);

        // Display route
        System.out.println("===== PACKET ROUTING SIMULATION =====");

        if (route.isEmpty()) {
            System.out.println("No route found.");
        } else {

            System.out.println("\nRoute from Computer 1 to Server 1:");

            System.out.println(computer1.getName());

            for (Link link : route) {
                System.out.println("   |");
                System.out.println("   v");
                System.out.println(link.getDestination().getName());
            }

            System.out.println("\nTotal links used: " + route.size());
        }
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