package com.virtualnetlab.transport;

/**
 * Small console demo to check the TCP and UDP simulations before UI integration.
 */
public class TransportDemo {
    public static void main(String[] args) {
        String message = "HELLO WORLD";
        int packetLoss = 30;

        TransportResult tcpResult = new TcpSimulation().run(message, packetLoss);
        TransportResult udpResult = new UdpSimulation().run(message, packetLoss);

        System.out.println("===== TCP SIMULATION =====");
        tcpResult.getSteps().forEach(System.out::println);
        System.out.println(tcpResult.getSummary());

        System.out.println();
        System.out.println("===== UDP SIMULATION =====");
        udpResult.getSteps().forEach(System.out::println);
        System.out.println(udpResult.getSummary());

        System.out.println();
        System.out.println("===== COMPARISON =====");
        System.out.println("TCP retransmissions: " + tcpResult.getRetransmissions());
        System.out.println("UDP retransmissions: " + udpResult.getRetransmissions());
        System.out.println("TCP delivered packets: " + tcpResult.getPacketsDelivered());
        System.out.println("UDP delivered packets: " + udpResult.getPacketsDelivered());
    }
}
