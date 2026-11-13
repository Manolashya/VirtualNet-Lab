package com.virtualnetlab.network;

public class TopologyDemo {

    public static void main(String[] args) {

        // 1. Create network devices
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

        // 2. Create the network topology
        NetworkTopology topology = new NetworkTopology();

        // 3. Add devices to the topology
        topology.addNode(computer1);
        topology.addNode(computer2);
        topology.addNode(networkSwitch);
        topology.addNode(router);
        topology.addNode(server);

        // 4. Create connections
        topology.addLink(new Link(
                computer1, networkSwitch,
                100_000_000, 10, 200_000_000));

        topology.addLink(new Link(
                networkSwitch, computer1,
                100_000_000, 10, 200_000_000));

        topology.addLink(new Link(
                computer2, networkSwitch,
                100_000_000, 15, 200_000_000));

        topology.addLink(new Link(
                networkSwitch, computer2,
                100_000_000, 15, 200_000_000));

        topology.addLink(new Link(
                networkSwitch, router,
                1_000_000_000, 50, 200_000_000));

        topology.addLink(new Link(
                router, networkSwitch,
                1_000_000_000, 50, 200_000_000));

        topology.addLink(new Link(
                router, server,
                1_000_000_000, 100, 200_000_000));

        topology.addLink(new Link(
                server, router,
                1_000_000_000, 100, 200_000_000));

        // 5. Display all devices
        System.out.println("===== VIRTUAL NETWORK TOPOLOGY =====");

        System.out.println("\nNetwork Devices:");

        for (Node node : topology.getNodes()) {
            System.out.println(node);
        }

        // 6. Display all connections
        System.out.println("\nNetwork Connections:");

        for (Link link : topology.getLinks()) {
            System.out.println(link);
        }

        // 7. Display outgoing connections
        System.out.println("\nConnections from Switch 1:");

        for (Link link : topology.getOutgoingLinks(networkSwitch)) {
            System.out.println(link);
        }
    }
}