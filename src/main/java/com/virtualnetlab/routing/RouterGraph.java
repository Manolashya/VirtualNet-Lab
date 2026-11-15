package com.virtualnetlab.routing;

import java.util.*;

/**
 * Undirected weighted graph. Routers are vertices and links are edges.
 * A disabled link is retained in the graph but ignored by Dijkstra.
 */
public class RouterGraph {
    private final Map<String, Map<String, Integer>> adjacency = new LinkedHashMap<>();

    public void addRouter(String router) {
        validateName(router);
        adjacency.putIfAbsent(router, new LinkedHashMap<>());
    }

    public void addLink(String routerA, String routerB, int cost) {
        validateCost(cost);
        if (routerA.equals(routerB)) {
            throw new IllegalArgumentException("A router cannot have a link to itself.");
        }
        addRouter(routerA);
        addRouter(routerB);
        adjacency.get(routerA).put(routerB, cost);
        adjacency.get(routerB).put(routerA, cost);
    }

    /** Changes the cost of an existing link. */
    public void updateLinkCost(String routerA, String routerB, int newCost) {
        validateCost(newCost);
        requireLink(routerA, routerB);
        adjacency.get(routerA).put(routerB, newCost);
        adjacency.get(routerB).put(routerA, newCost);
    }

    /** Disables a link by removing it from the graph. */
    public void disableLink(String routerA, String routerB) {
        requireLink(routerA, routerB);
        adjacency.get(routerA).remove(routerB);
        adjacency.get(routerB).remove(routerA);
    }

    public boolean containsRouter(String router) {
        return adjacency.containsKey(router);
    }

    public Set<String> getRouters() {
        return Collections.unmodifiableSet(adjacency.keySet());
    }

    /** Returns a read-only view of the active neighbours and their costs. */
    public Map<String, Integer> getNeighbours(String router) {
        if (!adjacency.containsKey(router)) {
            throw new IllegalArgumentException("Unknown router: " + router);
        }
        return Collections.unmodifiableMap(adjacency.get(router));
    }

    private void requireLink(String a, String b) {
        if (!adjacency.containsKey(a) || !adjacency.get(a).containsKey(b)) {
            throw new IllegalArgumentException("Link does not exist: " + a + " - " + b);
        }
    }

    private static void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Router name cannot be empty.");
        }
    }

    private static void validateCost(int cost) {
        if (cost < 0) {
            throw new IllegalArgumentException("Link cost cannot be negative.");
        }
    }
}
