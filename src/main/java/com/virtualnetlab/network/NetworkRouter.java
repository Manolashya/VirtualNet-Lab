package com.virtualnetlab.network;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/**
 * Finds routes between network devices using BFS.
 */
public class NetworkRouter {

    public static List<Link> findPath(
            NetworkTopology topology,
            Node source,
            Node destination) {

        if (topology == null || source == null || destination == null) {
            throw new IllegalArgumentException(
                    "Topology and nodes cannot be null.");
        }

        if (topology.getNode(source.getId()) == null
                || topology.getNode(destination.getId()) == null) {
            throw new IllegalArgumentException(
                    "Source and destination must exist in the topology.");
        }

        // If source and destination are the same
        if (source.getId().equals(destination.getId())) {
            return Collections.emptyList();
        }

        Queue<Node> queue = new ArrayDeque<>();
        Set<String> visited = new HashSet<>();

        // Store how we reached each device
        Map<String, String> parent = new HashMap<>();
        Map<String, Link> parentLink = new HashMap<>();

        queue.add(topology.getNode(source.getId()));
        visited.add(source.getId());

        // BFS traversal
        while (!queue.isEmpty()) {

            Node current = queue.poll();

            if (current.getId().equals(destination.getId())) {
                break;
            }

            for (Link link : topology.getOutgoingLinks(current)) {

                Node next = link.getDestination();

                if (!visited.contains(next.getId())) {

                    visited.add(next.getId());

                    parent.put(next.getId(), current.getId());

                    parentLink.put(next.getId(), link);

                    queue.add(next);
                }
            }
        }

        // Destination was not reached
        if (!visited.contains(destination.getId())) {
            return Collections.emptyList();
        }

        // Reconstruct the path
        LinkedList<Link> path = new LinkedList<>();

        String currentId = destination.getId();

        while (!currentId.equals(source.getId())) {

            Link link = parentLink.get(currentId);

            path.addFirst(link);

            currentId = parent.get(currentId);
        }

        return Collections.unmodifiableList(
                new ArrayList<>(path));
    }
}