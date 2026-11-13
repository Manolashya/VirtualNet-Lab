
package com.virtualnetlab.network;

import java.util.Objects;

/**
 * Represents a device in a computer network.
 */
public class Node {

    public enum NodeType {
        COMPUTER,
        SWITCH,
        ROUTER,
        SERVER
    }

    private final String id;
    private final String name;
    private final NodeType type;

    public Node(String id, String name, NodeType type) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Node ID cannot be empty.");
        }

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Node name cannot be empty.");
        }

        this.id = id;
        this.name = name;
        this.type = Objects.requireNonNull(type, "Node type cannot be null.");
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public NodeType getType() {
        return type;
    }

    @Override
    public String toString() {
        return name + " (" + type + ")";
    }
}
