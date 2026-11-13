
package com.virtualnetlab.network;

import java.util.*;

public class NetworkSimulator {

    private final Random random;

    // Default constructor
    public NetworkSimulator() {
        this(new Random());
    }

    // Constructor for predictable testing
    public NetworkSimulator(Random random) {
        this.random = Objects.requireNonNull(random);
    }

    /**
     * Finds a route between two devices using BFS.
     */
    public List<Link> findRoute(
            NetworkTopology topology,
            Node source,
            Node destination) {

        if (topology == null || source == null || destination == null) {
            throw new IllegalArgumentException(
                    "Topology and nodes cannot be null.");
        }

        Node start = topology.getNode(source.getId());
        Node end = topology.getNode(destination.getId());

        if (start == null || end == null) {
            throw new IllegalArgumentException(
                    "Source and destination must exist in topology.");
        }

        if (start.getId().equals(end.getId())) {
            throw new IllegalArgumentException(
                    "Source and destination must be different.");
        }

        Queue<Node> queue = new ArrayDeque<>();
        Set<String> visited = new HashSet<>();
        Map<String, Link> previousLinks = new HashMap<>();

        queue.add(start);
        visited.add(start.getId());

        while (!queue.isEmpty()) {

            Node current = queue.poll();

            if (current.getId().equals(end.getId())) {
                break;
            }

            for (Link link : topology.getOutgoingLinks(current)) {

                Node next = link.getDestination();

                if (!visited.contains(next.getId())) {
                    visited.add(next.getId());
                    previousLinks.put(next.getId(), link);
                    queue.add(next);
                }
            }
        }

        if (!visited.contains(end.getId())) {
            return Collections.emptyList();
        }

        List<Link> route = new ArrayList<>();
        Node current = end;

        while (!current.getId().equals(start.getId())) {

            Link link = previousLinks.get(current.getId());

            route.add(link);
            current = link.getSource();
        }

        Collections.reverse(route);

        return route;
    }

    /**
     * Simulates packet transmission through the network.
     *
     * Packet loss probability is applied independently
     * at each link.
     */
    public TransmissionResult simulateTransmission(
            Packet packet,
            NetworkTopology topology,
            double packetLossProbability) {

        if (packet == null || topology == null) {
            throw new IllegalArgumentException(
                    "Packet and topology cannot be null.");
        }

        if (!Double.isFinite(packetLossProbability)
                || packetLossProbability < 0
                || packetLossProbability > 1) {

            throw new IllegalArgumentException(
                    "Packet loss probability must be between 0 and 1.");
        }

        List<Link> route = findRoute(
                topology,
                packet.getSource(),
                packet.getDestination());

        if (route.isEmpty()) {
            throw new IllegalStateException(
                    "No route exists between source and destination.");
        }

        List<Link> traversedRoute = new ArrayList<>();
        double totalDelay = 0;

        for (Link link : route) {

            // Calculate delay for this link
            totalDelay += NetworkMetrics.calculateTotalDelay(
                    packet, link);

            traversedRoute.add(link);

            // Check whether packet is lost
            if (random.nextDouble() < packetLossProbability) {

                return new TransmissionResult(
                        false,
                        traversedRoute,
                        totalDelay,
                        link
                );
            }
        }

        // Packet successfully reached destination
        return new TransmissionResult(
                true,
                traversedRoute,
                totalDelay,
                null
        );
    }
}
