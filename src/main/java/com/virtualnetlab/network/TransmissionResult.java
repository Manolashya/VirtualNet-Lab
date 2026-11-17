package com.virtualnetlab.network;

import java.util.List;

/**
 * Stores the result of a packet transmission.
 */
public class TransmissionResult {

    private final boolean delivered;
    private final List<Link> route;
    private final double totalDelaySeconds;
    private final Link lostAtLink;

    public TransmissionResult(
            boolean delivered,
            List<Link> route,
            double totalDelaySeconds,
            Link lostAtLink) {

        this.delivered = delivered;
        this.route = List.copyOf(route);
        this.totalDelaySeconds = totalDelaySeconds;
        this.lostAtLink = lostAtLink;
    }

    public boolean isDelivered() {
        return delivered;
    }

    public List<Link> getRoute() {
        return route;
    }

    public double getTotalDelaySeconds() {
        return totalDelaySeconds;
    }

    public Link getLostAtLink() {
        return lostAtLink;
    }
}