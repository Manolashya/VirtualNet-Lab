package com.virtualnetlab.routing;

import java.util.*;

/** Dijkstra shortest-path algorithm for graphs with non-negative link costs. */
public final class Dijkstra {
    private Dijkstra() {
    }

    public static RoutingResult findShortestPath(
            RouterGraph graph, String source, String destination) {
        Objects.requireNonNull(graph, "Graph cannot be null.");

        if (!graph.containsRouter(source)) {
            throw new IllegalArgumentException("Unknown source router: " + source);
        }
        if (!graph.containsRouter(destination)) {
            throw new IllegalArgumentException("Unknown destination router: " + destination);
        }

        Map<String, Integer> distance = new HashMap<>();
        Map<String, String> previous = new HashMap<>();
        Set<String> visited = new HashSet<>();

        for (String router : graph.getRouters()) {
            distance.put(router, Integer.MAX_VALUE);
        }
        distance.put(source, 0);

        PriorityQueue<RouterDistance> queue =
                new PriorityQueue<>(Comparator.comparingInt(RouterDistance::distance));
        queue.add(new RouterDistance(source, 0));

        while (!queue.isEmpty()) {
            RouterDistance current = queue.poll();
            String router = current.router();

            if (!visited.add(router)) continue;
            if (router.equals(destination)) break;

            for (Map.Entry<String, Integer> edge :
                    graph.getNeighbours(router).entrySet()) {
                String neighbour = edge.getKey();
                int newDistance = safeAdd(distance.get(router), edge.getValue());

                if (newDistance < distance.get(neighbour)) {
                    distance.put(neighbour, newDistance);
                    previous.put(neighbour, router);
                    queue.add(new RouterDistance(neighbour, newDistance));
                }
            }
        }

        int bestCost = distance.get(destination);
        if (bestCost == Integer.MAX_VALUE) {
            return new RoutingResult(source, destination, List.of(),
                    Integer.MAX_VALUE, false);
        }

        List<String> path = new ArrayList<>();
        String step = destination;
        while (step != null) {
            path.add(step);
            if (step.equals(source)) break;
            step = previous.get(step);
        }
        Collections.reverse(path);

        return new RoutingResult(source, destination, path, bestCost, true);
    }

    private static int safeAdd(int a, int b) {
        if (a == Integer.MAX_VALUE || b > Integer.MAX_VALUE - a) {
            return Integer.MAX_VALUE;
        }
        return a + b;
    }

    private record RouterDistance(String router, int distance) {
    }
}
