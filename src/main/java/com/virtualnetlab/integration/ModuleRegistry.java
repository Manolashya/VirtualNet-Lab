
package com.virtualnetlab.integration;

import com.virtualnetlab.network.Link;
import com.virtualnetlab.network.NetworkSimulator;
import com.virtualnetlab.network.NetworkTopology;
import com.virtualnetlab.network.Node;
import com.virtualnetlab.network.Packet;
import com.virtualnetlab.network.SimulationStatistics;
import com.virtualnetlab.network.TransmissionResult;

import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Integration point for all five CNIP modules.
 */
public final class ModuleRegistry {

    private ModuleRegistry() {
    }

    public static String runDemo(int unit, String input) {
        return switch (unit) {
            case 1 -> runNetworkFundamentals(input);
            case 2 -> runDataLink(input);
            case 3 -> runNetworkLayer(input);
            case 4 -> runTransport(input);
            case 5 -> runApplication(input);
            default -> "Unknown module.";
        };
    }

    /**
     * Runs the Unit I network fundamentals simulation.
     *
     * Input format:
     * packetSize=1000, bandwidthMbps=10, distanceMeters=1000
     */
    private static String runNetworkFundamentals(String input) {

        try {
            int packetSize = readInteger(input, "packetSize");
            double bandwidthMbps = readDouble(input, "bandwidthMbps");
            double distanceMeters = readDouble(input, "distanceMeters");

            if (packetSize <= 0 || packetSize > 1_000_000) {
                throw new IllegalArgumentException(
                        "Packet size must be between 1 and 1,000,000 bytes.");
            }

            if (bandwidthMbps <= 0 || bandwidthMbps > 100_000) {
                throw new IllegalArgumentException(
                        "Bandwidth must be greater than 0 and at most 100,000 Mbps.");
            }

            if (distanceMeters < 0 || distanceMeters > 100_000_000) {
                throw new IllegalArgumentException(
                        "Distance must be between 0 and 100,000,000 meters.");
            }

            int packetCount = 10;
            double packetLossProbability = 0.10;

            double bandwidthBps = bandwidthMbps * 1_000_000;

            // Create network devices.
            Node computer = new Node(
                    "N1", "Computer", Node.NodeType.COMPUTER);

            Node networkSwitch = new Node(
                    "N2", "Switch", Node.NodeType.SWITCH);

            Node router = new Node(
                    "N3", "Router", Node.NodeType.ROUTER);

            Node server = new Node(
                    "N4", "Server", Node.NodeType.SERVER);

            // Build network topology.
            NetworkTopology topology = new NetworkTopology();

            topology.addNode(computer);
            topology.addNode(networkSwitch);
            topology.addNode(router);
            topology.addNode(server);

            // Divide the configured distance across three links.
            double linkDistance = distanceMeters / 3.0;

            double propagationSpeed = 200_000_000;

            topology.addLink(new Link(
                    computer, networkSwitch,
                    bandwidthBps, linkDistance, propagationSpeed));

            topology.addLink(new Link(
                    networkSwitch, router,
                    bandwidthBps, linkDistance, propagationSpeed));

            topology.addLink(new Link(
                    router, server,
                    bandwidthBps, linkDistance, propagationSpeed));

            // Use a fixed seed for repeatable demonstration results.
            NetworkSimulator simulator =
                    new NetworkSimulator(new Random(42));

            SimulationStatistics statistics =
                    new SimulationStatistics();

            StringBuilder report = new StringBuilder();

            report.append("===== NETWORK FUNDAMENTALS SIMULATION =====\n\n");

            report.append("CONFIGURATION\n");
            report.append("------------------------------\n");
            report.append("Packet size: ")
                    .append(packetSize).append(" bytes\n");
            report.append("Bandwidth: ")
                    .append(bandwidthMbps).append(" Mbps\n");
            report.append("Total network distance: ")
                    .append(distanceMeters).append(" meters\n");
            report.append("Packets to transmit: ")
                    .append(packetCount).append("\n");
            report.append("Packet loss probability per link: 10%\n\n");

            report.append("NETWORK TOPOLOGY\n");
            report.append("------------------------------\n");
            report.append("Computer -> Switch -> Router -> Server\n");
            report.append("Devices: ")
                    .append(topology.getNodes().size()).append("\n");
            report.append("Links: ")
                    .append(topology.getLinks().size()).append("\n\n");

            report.append("PACKET TRANSMISSION\n");
            report.append("------------------------------\n");

            for (int i = 1; i <= packetCount; i++) {

                Packet packet = new Packet(
                        String.format(Locale.US, "P%03d", i),
                        computer,
                        server,
                        packetSize);

                TransmissionResult result =
                        simulator.simulateTransmission(
                                packet, topology, packetLossProbability);

                statistics.recordResult(packet, result);

                report.append(packet.getPacketId())
                        .append(" : ")
                        .append(result.isDelivered()
                                ? "DELIVERED" : "LOST");

                if (!result.isDelivered()) {
                    report.append(" at ")
                            .append(result.getLostAtLink().getSource().getName())
                            .append(" -> ")
                            .append(result.getLostAtLink().getDestination().getName());
                }

                report.append(" | Delay: ")
                        .append(String.format(
                                Locale.US, "%.8f",
                                result.getTotalDelaySeconds()))
                        .append(" seconds\n");
            }

            report.append("\nNETWORK PERFORMANCE\n");
            report.append("------------------------------\n");

            report.append("Total packets: ")
                    .append(statistics.getTotalPackets()).append("\n");

            report.append("Delivered packets: ")
                    .append(statistics.getDeliveredPackets()).append("\n");

            report.append("Lost packets: ")
                    .append(statistics.getLostPackets()).append("\n");

            report.append(String.format(
                    Locale.US,
                    "Packet loss: %.2f%%\n",
                    statistics.getPacketLossPercentage()));

            report.append(String.format(
                    Locale.US,
                    "Delivered data: %d bits\n",
                    statistics.getDeliveredBits()));

            report.append(String.format(
                    Locale.US,
                    "Average throughput: %.2f Mbps\n",
                    statistics.getThroughputBps() / 1_000_000));

            report.append("\nNote: This is an educational simulation. ");
            report.append("Packet loss is independently applied at each link.");

            return report.toString();

        } catch (IllegalArgumentException | IllegalStateException ex) {
            return "SIMULATION INPUT ERROR\n\n" + ex.getMessage()
                    + "\n\nExpected format:\n"
                    + "packetSize=1000, bandwidthMbps=10, distanceMeters=1000";
        }
    }

    private static int readInteger(String input, String key) {
        return Integer.parseInt(readValue(input, key));
    }

    private static double readDouble(String input, String key) {
        double value = Double.parseDouble(readValue(input, key));

        if (!Double.isFinite(value)) {
            throw new IllegalArgumentException(
                    key + " must be a finite number.");
        }

        return value;
    }

    private static String readValue(String input, String key) {

        if (input == null) {
            throw new IllegalArgumentException("Input cannot be empty.");
        }

        Pattern pattern = Pattern.compile(
                "(?i)(?:^|[,\\s])" + Pattern.quote(key)
                        + "\\s*=\\s*([^,\\s]+)");

        Matcher matcher = pattern.matcher(input);

        if (!matcher.find()) {
            throw new IllegalArgumentException(
                    "Missing or invalid parameter: " + key);
        }

        return matcher.group(1);
    }

    private static String runDataLink(String input) {
        return placeholder("Data Link Layer", input,
                "Member 3: connect Parity, CRC, Hamming and StopAndWait here.");
    }

    private static String runNetworkLayer(String input) {
        return placeholder("Network Layer", input,
                "Member 4: connect IPv4Utils and Dijkstra here.");
    }

    private static String runTransport(String input) {
        return placeholder("Transport Layer", input,
                "Member 5: connect TcpSimulation and UdpSimulation here.");
    }

    private static String runApplication(String input) {
        return placeholder("Application Layer", input,
                "Member 6: connect DnsSimulation and HttpSimulation here.");
    }

    private static String placeholder(
            String module, String input, String integrationNote) {

        return module + "\n"
                + "------------------------------\n"
                + "Received input: " + input + "\n\n"
                + "DEMO PLACEHOLDER — algorithm not connected yet.\n"
                + integrationNote;
    }
}
