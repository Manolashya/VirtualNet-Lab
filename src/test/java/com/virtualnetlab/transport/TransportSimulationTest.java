package com.virtualnetlab.transport;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TransportSimulationTest {

    @Test
    void tcpDeliversAllDataWhenThereIsNoLoss() {
        TransportResult result = new TcpSimulation().run("HELLO WORLD", 0);
        assertTrue(result.isDelivered());
        assertEquals(0, result.getRetransmissions());
        assertEquals(result.getPacketsSent(), result.getPacketsDelivered());
    }

    @Test
    void udpDoesNotRetransmitWhenAllDatagramsAreLost() {
        TransportResult result = new UdpSimulation().run("HELLO WORLD", 100);
        assertFalse(result.isDelivered());
        assertEquals(0, result.getPacketsDelivered());
        assertEquals(0, result.getRetransmissions());
    }

    @Test
    void tcpRetriesWhenAllPacketsAreLost() {
        TransportResult result = new TcpSimulation().run("HELLO", 100);
        assertFalse(result.isDelivered());
        assertEquals(3, result.getRetransmissions());
    }

    @Test
    void rejectsInvalidPacketLoss() {
        assertThrows(IllegalArgumentException.class,
                () -> new UdpSimulation().run("HELLO", 120));
    }

    @Test
    void rejectsEmptyMessage() {
        assertThrows(IllegalArgumentException.class,
                () -> new TcpSimulation().run(" ", 0));
    }
}
