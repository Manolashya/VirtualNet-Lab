
package com.virtualnetlab.network;

import java.util.Objects;

/**
 * Represents a connection between two network devices.
 */
public class Link {

    private final Node source;
    private final Node destination;
    private final double bandwidthBps;
    private final double distanceMeters;
    private final double propagationSpeedMps;

    public Link(Node source, Node destination,
                double bandwidthBps,
                double distanceMeters,
                double propagationSpeedMps) {

        this.source = Objects.requireNonNull(
                source, "Source node cannot be null.");

        this.destination = Objects.requireNonNull(
                destination, "Destination node cannot be null.");

        if (source.equals(destination)) {
            throw new IllegalArgumentException(
                    "Source and destination cannot be the same node.");
        }

        if (bandwidthBps <= 0 || !Double.isFinite(bandwidthBps)) {
            throw new IllegalArgumentException(
                    "Bandwidth must be a positive finite value.");
        }

        if (distanceMeters < 0 || !Double.isFinite(distanceMeters)) {
            throw new IllegalArgumentException(
                    "Distance must be a non-negative finite value.");
        }

        if (propagationSpeedMps <= 0
                || !Double.isFinite(propagationSpeedMps)) {
            throw new IllegalArgumentException(
                    "Propagation speed must be a positive finite value.");
        }

        this.bandwidthBps = bandwidthBps;
        this.distanceMeters = distanceMeters;
        this.propagationSpeedMps = propagationSpeedMps;
    }

    public Node getSource() {
        return source;
    }

    public Node getDestination() {
        return destination;
    }

    public double getBandwidthBps() {
        return bandwidthBps;
    }

    public double getDistanceMeters() {
        return distanceMeters;
    }

    public double getPropagationSpeedMps() {
        return propagationSpeedMps;
    }

    @Override
    public String toString() {
        return source.getName() + " -> " + destination.getName()
                + " (" + bandwidthBps + " bps)";
    }
}
