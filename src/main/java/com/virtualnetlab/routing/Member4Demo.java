package com.virtualnetlab.routing;

/** Console demonstration of the Member 4 Network Layer module. */
public class Member4Demo {
    public static void main(String[] args) {
        System.out.println("=== VIRTUALNET LAB: UNIT III - NETWORK LAYER ===");

        System.out.println("\n1) IPv4 and subnet example");
        System.out.println("192.168.1.10 valid? " +
                IPv4Utils.isValidIPv4("192.168.1.10"));
        System.out.println(IPv4Utils.calculateSubnet("192.168.1.10", 24));

        System.out.println("\n2) Router graph and Dijkstra");
        RouterGraph graph = new RouterGraph();
        graph.addLink("A", "B", 4);
        graph.addLink("A", "C", 2);
        graph.addLink("B", "C", 1);
        graph.addLink("B", "D", 5);
        graph.addLink("C", "D", 8);
        graph.addLink("C", "E", 10);
        graph.addLink("D", "E", 2);

        RoutingResult first = Dijkstra.findShortestPath(graph, "A", "E");
        System.out.println(first);

        System.out.println("\n3) Change a link cost and recalculate");
        graph.updateLinkCost("C", "D", 1);
        System.out.println(Dijkstra.findShortestPath(graph, "A", "E"));

        System.out.println("\n4) Disable a link and recalculate");
        graph.disableLink("D", "E");
        System.out.println(Dijkstra.findShortestPath(graph, "A", "E"));
    }
}
