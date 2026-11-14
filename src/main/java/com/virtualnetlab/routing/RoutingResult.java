package com.virtualnetlab.routing;

import java.util.List;

/** Immutable output of a shortest-path calculation. */
public final class RoutingResult {
    private final String source;
    private final String destination;
    private final List<String> path;
    private final int totalCost;
    private final boolean reachable;

    public RoutingResult(String source, String destination,
                         List<String> path, int totalCost, boolean reachable) {
        this.source = source;
        this.destination = destination;
        this.path = List.copyOf(path);
        this.totalCost = totalCost;
        this.reachable = reachable;
    }

    public String getSource() { return source; }
    public String getDestination() { return destination; }
    public List<String> getPath() { return path; }
    public int getTotalCost() { return totalCost; }
    public boolean isReachable() { return reachable; }

    /** The first router after the source; null if no route or source == destination. */
    public String getNextHop() {
        return reachable && path.size() > 1 ? path.get(1) : null;
    }

    @Override
    public String toString() {
        if (!reachable) {
            return "No route from " + source + " to " + destination;
        }
        return "Route: " + String.join(" -> ", path) +
                "\nNext hop: " + (getNextHop() == null ? "N/A (same router)" : getNextHop()) +
                "\nTotal cost: " + totalCost;
    }
}
