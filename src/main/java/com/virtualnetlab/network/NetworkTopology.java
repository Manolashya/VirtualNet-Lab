
package com.virtualnetlab.network;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Manages network devices and their connections.
 */
public class NetworkTopology {

    private final Map<String, Node> nodes = new LinkedHashMap<>();
    private final List<Link> links = new ArrayList<>();

    // Add a device to the network.
    public void addNode(Node node) {

        if (node == null) {
            throw new IllegalArgumentException(
                    "Node cannot be null.");
        }

        if (nodes.containsKey(node.getId())) {
            throw new IllegalArgumentException(
                    "A node with this ID already exists.");
        }

        nodes.put(node.getId(), node);
    }

    // Add a connection between existing devices.
    public void addLink(Link link) {

        if (link == null) {
            throw new IllegalArgumentException(
                    "Link cannot be null.");
        }

        if (!nodes.containsKey(link.getSource().getId())
                || !nodes.containsKey(link.getDestination().getId())) {
            throw new IllegalArgumentException(
                    "Both link endpoints must exist in the topology.");
        }

        links.add(link);
    }

    // Return all devices.
    public Collection<Node> getNodes() {
        return Collections.unmodifiableCollection(nodes.values());
    }

    // Return all connections.
    public List<Link> getLinks() {
        return Collections.unmodifiableList(links);
    }

    // Find a device using its ID.
    public Node getNode(String id) {
        return nodes.get(id);
    }

    // Find outgoing connections from a device.
    public List<Link> getOutgoingLinks(Node node) {

        if (node == null || !nodes.containsKey(node.getId())) {
            throw new IllegalArgumentException(
                    "Node does not exist in this topology.");
        }

        List<Link> outgoingLinks = new ArrayList<>();

        for (Link link : links) {
            if (link.getSource().getId().equals(node.getId())) {
                outgoingLinks.add(link);
            }
        }

        return Collections.unmodifiableList(outgoingLinks);
    }
}
