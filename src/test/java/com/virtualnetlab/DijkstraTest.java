package com.virtualnetlab.routing;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DijkstraTest {
    private RouterGraph sampleGraph() {
        RouterGraph graph = new RouterGraph();
        graph.addLink("A", "B", 4);
        graph.addLink("A", "C", 2);
        graph.addLink("B", "C", 1);
        graph.addLink("B", "D", 5);
        graph.addLink("C", "D", 8);
        graph.addLink("C", "E", 10);
        graph.addLink("D", "E", 2);
        return graph;
    }

    @Test
    void findsShortestPathAndCost() {
        RoutingResult result =
                Dijkstra.findShortestPath(sampleGraph(), "A", "E");

        assertTrue(result.isReachable());
        assertEquals(10, result.getTotalCost());
        assertEquals("C", result.getNextHop());
        assertEquals(java.util.List.of("A", "C", "B", "D", "E"),
                result.getPath());
    }

    @Test
    void recalculatesAfterLinkCostChange() {
        RouterGraph graph = sampleGraph();
        graph.updateLinkCost("C", "D", 1);

        RoutingResult result = Dijkstra.findShortestPath(graph, "A", "E");
        assertEquals(5, result.getTotalCost());
        assertEquals(java.util.List.of("A", "C", "D", "E"), result.getPath());
    }

    @Test
    void recalculatesAfterLinkFailure() {
        RouterGraph graph = sampleGraph();
        graph.disableLink("D", "E");

        RoutingResult result = Dijkstra.findShortestPath(graph, "A", "E");
        assertTrue(result.isReachable());
        assertEquals(12, result.getTotalCost());
        assertEquals(java.util.List.of("A", "C", "E"), result.getPath());
    }

    @Test
    void reportsUnreachableRouter() {
        RouterGraph graph = new RouterGraph();
        graph.addRouter("A");
        graph.addRouter("B");

        RoutingResult result = Dijkstra.findShortestPath(graph, "A", "B");
        assertFalse(result.isReachable());
        assertTrue(result.getPath().isEmpty());
        assertNull(result.getNextHop());
    }

    @Test
    void rejectsUnknownRouter() {
        RouterGraph graph = sampleGraph();
        assertThrows(IllegalArgumentException.class,
                () -> Dijkstra.findShortestPath(graph, "A", "Z"));
    }

    @Test
    void rejectsNegativeLinkCost() {
        RouterGraph graph = new RouterGraph();
        assertThrows(IllegalArgumentException.class,
                () -> graph.addLink("A", "B", -1));
    }
}
