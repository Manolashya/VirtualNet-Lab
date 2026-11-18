
package com.virtualnetlab.integration;

import com.virtualnetlab.network.*;
import com.virtualnetlab.application.DnsSimulation;
import com.virtualnetlab.application.EmailSimulation;
import com.virtualnetlab.application.HttpSimulation;
import com.virtualnetlab.application.StreamingSimulation;
import com.virtualnetlab.datalink.*;
import com.virtualnetlab.routing.IPv4Utils;
import com.virtualnetlab.routing.Dijkstra;
import com.virtualnetlab.routing.RouterGraph;
import com.virtualnetlab.routing.RoutingResult;
import com.virtualnetlab.transport.TcpSimulation;
import com.virtualnetlab.transport.UdpSimulation;
import com.virtualnetlab.transport.TransportResult;

import java.util.Random;
import java.util.List;

public final class ModuleRegistry {

    private static final DnsSimulation DNS_SIMULATION = new DnsSimulation();
    private static final HttpSimulation HTTP_SIMULATION =
        new HttpSimulation(DNS_SIMULATION);

    private ModuleRegistry() {}

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

    // UNIT I: NETWORK FUNDAMENTALS
    private static String runNetworkFundamentals(String input) {

        try {
            int packetSize = 1000;
            double bandwidthMbps = 10;
            double distanceMeters = 1000;

            for (String part : input.split(",")) {
                String[] pair = part.trim().split("=", 2);

                if (pair.length != 2) {
                    throw new IllegalArgumentException(
                            "Invalid input format. Use key=value.");
                }

                switch (pair[0].trim()) {
                    case "packetSize" ->
                            packetSize = Integer.parseInt(pair[1].trim());
                    case "bandwidthMbps" ->
                            bandwidthMbps = Double.parseDouble(pair[1].trim());
                    case "distanceMeters" ->
                            distanceMeters = Double.parseDouble(pair[1].trim());
                    default ->
                            throw new IllegalArgumentException(
                                    "Unknown input field: " + pair[0]);
                }
            }

            if (packetSize <= 0 || bandwidthMbps <= 0
                    || distanceMeters < 0
                    || !Double.isFinite(bandwidthMbps)
                    || !Double.isFinite(distanceMeters)) {
                throw new IllegalArgumentException(
                        "Packet size and bandwidth must be positive; "
                        + "distance cannot be negative.");
            }

            Node computer = new Node(
                    "N1", "Computer 1", Node.NodeType.COMPUTER);
            Node networkSwitch = new Node(
                    "N2", "Switch 1", Node.NodeType.SWITCH);
            Node router = new Node(
                    "N3", "Router 1", Node.NodeType.ROUTER);
            Node server = new Node(
                    "N4", "Server 1", Node.NodeType.SERVER);

            NetworkTopology topology = new NetworkTopology();

            topology.addNode(computer);
            topology.addNode(networkSwitch);
            topology.addNode(router);
            topology.addNode(server);

            double bandwidthBps = bandwidthMbps * 1_000_000;

            addConnection(topology, computer, networkSwitch,
                    bandwidthBps, distanceMeters / 3);
            addConnection(topology, networkSwitch, router,
                    bandwidthBps, distanceMeters / 3);
            addConnection(topology, router, server,
                    bandwidthBps, distanceMeters / 3);

            Packet packet = new Packet(
                    "P001", computer, server, packetSize);

            NetworkSimulator simulator =
                    new NetworkSimulator(new Random(42));

            StringBuilder result = new StringBuilder();

            result.append("NETWORK FUNDAMENTALS SIMULATION\n");
            result.append("--------------------------------\n");
            result.append("Packet size: ").append(packetSize)
                    .append(" bytes\n");
            result.append("Bandwidth: ").append(bandwidthMbps)
                    .append(" Mbps\n");
            result.append("Total distance: ").append(distanceMeters)
                    .append(" meters\n");
            result.append("Devices: ").append(topology.getNodes().size())
                    .append("\n");
            result.append("Connections: ").append(topology.getLinks().size())
                    .append("\n\n");

            result.append("ROUTE DISCOVERY (BFS)\n");

            var route = simulator.findRoute(
                    topology, computer, server);

            for (Link link : route) {
                result.append(link.getSource().getName())
                        .append(" -> ")
                        .append(link.getDestination().getName())
                        .append("\n");
            }

            result.append("\nCASE 1: PACKET LOSS = 0%\n");

            TransmissionResult success =
                    simulator.simulateTransmission(packet, topology, 0.0);

            appendTransmissionResult(result, success);

            result.append("\nCASE 2: PACKET LOSS = 100%\n");

            TransmissionResult failure =
                    simulator.simulateTransmission(packet, topology, 1.0);

            appendTransmissionResult(result, failure);

            return result.toString();

        } catch (IllegalArgumentException | IllegalStateException ex) {
            return "Network Fundamentals Error: " + ex.getMessage();
        }
    }

    private static void addConnection(
            NetworkTopology topology,
            Node first,
            Node second,
            double bandwidth,
            double distance) {

        topology.addLink(new Link(
                first, second, bandwidth, distance, 200_000_000));

        topology.addLink(new Link(
                second, first, bandwidth, distance, 200_000_000));
    }

    private static void appendTransmissionResult(
            StringBuilder result,
            TransmissionResult transmission) {

        result.append("Status: ")
                .append(transmission.isDelivered()
                        ? "PACKET DELIVERED" : "PACKET LOST")
                .append("\n");

        result.append("Links traversed: ")
                .append(transmission.getRoute().size())
                .append("\n");

        result.append(String.format(
                "Total delay: %.8f seconds%n",
                transmission.getTotalDelaySeconds()));

        if (!transmission.isDelivered()) {
            result.append("Lost at: ")
                    .append(transmission.getLostAtLink())
                    .append("\n");
        }
    }

    // UNIT II: DATA LINK LAYER
    private static String runDataLink(String input) {

        try {
            String data = input.trim();

            if (data.isEmpty()) {
                throw new IllegalArgumentException(
                        "Enter binary data, for example: 1011");
            }

            StringBuilder result = new StringBuilder();

            result.append("DATA LINK LAYER SIMULATION\n");
            result.append("===========================\n");
            result.append("Original binary data: ")
                    .append(data).append("\n\n");

            // 1. PARITY
            result.append("1. PARITY CHECK\n");
            result.append("---------------------------\n");

            String evenParity = Parity.generateEvenParity(data);
            String oddParity = Parity.generateOddParity(data);

            result.append("Even parity: ").append(evenParity).append("\n");
            result.append("Even parity valid: ")
                    .append(Parity.checkEvenParity(evenParity))
                    .append("\n");

            result.append("Odd parity: ").append(oddParity).append("\n");
            result.append("Odd parity valid: ")
                    .append(Parity.checkOddParity(oddParity))
                    .append("\n\n");

            // 2. CRC
            result.append("2. CYCLIC REDUNDANCY CHECK (CRC)\n");
            result.append("---------------------------\n");

            String generator = "1011";
            String codeword = CRC.generateCRC(data, generator);

            result.append("Generator: ").append(generator).append("\n");
            result.append("Generated codeword: ")
                    .append(codeword).append("\n");

            result.append("Verification: ")
                    .append(CRC.verifyCRC(codeword, generator)
                            ? "NO ERROR DETECTED"
                            : "ERROR DETECTED")
                    .append("\n\n");

            // 3. HAMMING CODE
            result.append("3. HAMMING CODE (7,4)\n");
            result.append("---------------------------\n");

            if (data.length() != 4) {
                result.append(
                        "Skipped: Hamming (7,4) requires exactly 4 bits.\n\n");
            } else {
                String encoded = HammingCode.encode(data);

                char[] receivedBits = encoded.toCharArray();
                receivedBits[5] =
                        receivedBits[5] == '0' ? '1' : '0';

                String received = new String(receivedBits);

                int errorPosition =
                        HammingCode.findErrorPosition(received);

                String corrected =
                        HammingCode.correctError(received);

                result.append("Encoded data: ")
                        .append(encoded).append("\n");
                result.append("Received with error: ")
                        .append(received).append("\n");
                result.append("Detected error position: ")
                        .append(errorPosition).append("\n");
                result.append("Corrected codeword: ")
                        .append(corrected).append("\n");
            }

            result.append("\n4. STOP-AND-WAIT ARQ\n");
            result.append("---------------------------\n");

            List<String> log = StopAndWait.simulate(3, 2, 0);

            for (String message : log) {
                result.append(message).append("\n");
            }

            return result.toString();

        } catch (IllegalArgumentException | IllegalStateException ex) {
            return "Data Link Layer Error: " + ex.getMessage();
        }
    }

    // UNIT III: NETWORK LAYER
private static String runNetworkLayer(String input) {

    try {
        String source = "A";
        String destination = "E";
        String ip = "192.168.1.10";
        int prefix = 24;

        for (String part : input.split(",")) {
            String[] pair = part.trim().split("=", 2);

            if (pair.length != 2) {
                throw new IllegalArgumentException(
                        "Invalid input format. Use key=value.");
            }

            String key = pair[0].trim();
            String value = pair[1].trim();

            if (value.isEmpty()) {
                throw new IllegalArgumentException(
                        "Value cannot be empty for: " + key);
            }

            switch (key) {
                case "source" -> source = value;
                case "destination" -> destination = value;
                case "ip" -> ip = value;
                case "prefix" -> prefix = Integer.parseInt(value);
                default -> throw new IllegalArgumentException(
                        "Unknown input field: " + key);
            }
        }

        if (!IPv4Utils.isValidIPv4(ip)) {
            throw new IllegalArgumentException(
                    "Invalid IPv4 address: " + ip);
        }

        var subnet = IPv4Utils.calculateSubnet(ip, prefix);

        RouterGraph graph = new RouterGraph();

        graph.addLink("A", "B", 4);
        graph.addLink("A", "C", 2);
        graph.addLink("B", "C", 1);
        graph.addLink("B", "D", 5);
        graph.addLink("C", "D", 8);
        graph.addLink("C", "E", 10);
        graph.addLink("D", "E", 2);

        StringBuilder result = new StringBuilder();

        result.append("NETWORK LAYER SIMULATION\n");
        result.append("=========================\n\n");

        result.append("1. IPv4 ADDRESS AND SUBNET\n");
        result.append("-------------------------\n");
        result.append("IPv4 address: ")
                .append(ip).append("\n");
        result.append("Valid IPv4: ")
                .append(IPv4Utils.isValidIPv4(ip))
                .append("\n");
        result.append(subnet).append("\n\n");

        result.append("2. DIJKSTRA SHORTEST PATH\n");
        result.append("-------------------------\n");

        RoutingResult initialRoute =
                Dijkstra.findShortestPath(
                        graph, source, destination);

        result.append("Initial route:\n");
        result.append(initialRoute).append("\n\n");

        result.append("3. UPDATE LINK COST\n");
        result.append("-------------------------\n");
        result.append("Changing link C-D cost from 8 to 1.\n");

        graph.updateLinkCost("C", "D", 1);

        RoutingResult updatedRoute =
                Dijkstra.findShortestPath(
                        graph, source, destination);

        result.append("Updated route:\n");
        result.append(updatedRoute).append("\n\n");

        result.append("4. DISABLE NETWORK LINK\n");
        result.append("-------------------------\n");
        result.append("Disabling link D-E.\n");

        graph.disableLink("D", "E");

        RoutingResult finalRoute =
                Dijkstra.findShortestPath(
                        graph, source, destination);

        result.append("Route after disabling link:\n");
        result.append(finalRoute).append("\n");

        return result.toString();

    } catch (IllegalArgumentException | IllegalStateException ex) {
        return "Network Layer Error: " + ex.getMessage();
    }
}


    // UNIT IV: TRANSPORT LAYER
private static String runTransport(String input) {

    String message = "HELLO WORLD";
    int lossPercent = 20;

    try {
        if (input != null && !input.isBlank()) {

            String[] fields = input.split(",");

            for (String field : fields) {
                String[] parts = field.split("=", 2);

                if (parts.length != 2) {
                    return "Transport Layer Error: Invalid input format. "
                            + "Use message=HELLO, lossPercent=20";
                }

                String key = parts[0].trim();
                String value = parts[1].trim();

                switch (key) {
                    case "message" -> message = value;
                    case "lossPercent" -> lossPercent = Integer.parseInt(value);
                    default -> {
                        return "Transport Layer Error: Unknown input field: " + key;
                    }
                }
            }
        }

        if (message.isBlank()) {
            return "Transport Layer Error: Message cannot be empty.";
        }

        if (lossPercent < 0 || lossPercent > 100) {
            return "Transport Layer Error: Packet loss must be between 0 and 100.";
        }

        TcpSimulation tcp = new TcpSimulation();
        UdpSimulation udp = new UdpSimulation();

        TransportResult tcpResult = tcp.run(message, lossPercent);
        TransportResult udpResult = udp.run(message, lossPercent);

        StringBuilder output = new StringBuilder();

        output.append("TRANSPORT LAYER SIMULATION\n");
        output.append("==========================\n");
        output.append("Message: ").append(message).append("\n");
        output.append("Packet Loss: ").append(lossPercent).append("%\n\n");

        output.append("TCP SIMULATION\n");
        output.append("--------------\n");
        output.append(tcpResult.getSummary()).append("\n");

        for (String step : tcpResult.getSteps()) {
            output.append(step).append("\n");
        }

        output.append("\nUDP SIMULATION\n");
        output.append("--------------\n");
        output.append(udpResult.getSummary()).append("\n");

        for (String step : udpResult.getSteps()) {
            output.append(step).append("\n");
        }

        return output.toString();

    } catch (IllegalArgumentException | IllegalStateException e) {
        return "Transport Layer Error: " + e.getMessage();
    }
}

    // UNIT V: APPLICATION LAYER
    private static String runApplication(String input) {

    try {
        if (input == null || input.isBlank()) {
            return "Application Layer Error: Enter valid input.";
        }
        if (input.startsWith("action=stream")) {

    String domain = "";
    int chunks = 3;
    int chunkSizeMb = 5;

    String[] fields = input.split(",");

    for (String field : fields) {
        String[] parts = field.split("=", 2);

        if (parts.length != 2) {
            return "Streaming Error: Invalid input format.";
        }

        String key = parts[0].trim();
        String value = parts[1].trim();

        switch (key) {
            case "action" -> {
                if (!"stream".equals(value)) {
                    return "Streaming Error: Invalid action.";
                }
            }
            case "domain" -> domain = value;
            case "chunks" -> {
    try {
        chunks = Integer.parseInt(value);
    } catch (NumberFormatException e) {
        return "Streaming Error: chunks must be a whole number.";
    }
}
case "chunkSizeMb" -> {
    try {
        chunkSizeMb = Integer.parseInt(value);
    } catch (NumberFormatException e) {
        return "Streaming Error: chunkSizeMb must be a whole number.";
    }
}
            default -> {
                return "Streaming Error: Unknown field: " + key;
            }
        }
    }

    if (chunks <= 0) {
    return "Streaming Error: chunks must be greater than zero.";
}

if (chunkSizeMb <= 0) {
    return "Streaming Error: chunkSizeMb must be greater than zero.";
}

    StreamingSimulation streaming = new StreamingSimulation();

    StreamingSimulation.StreamingResult result =
            streaming.simulate(domain, chunks, chunkSizeMb);

    return result.toDisplayString();
}

        if (input.startsWith("action=http")) {

    String domain = "";
    String path = "/";

    String[] fields = input.split(",");

    for (String field : fields) {
        String[] parts = field.split("=", 2);

        if (parts.length != 2) {
            return "HTTP Error: Invalid input format.";
        }

        String key = parts[0].trim();
        String value = parts[1].trim();

        switch (key) {
            case "action" -> {
                if (!"http".equals(value)) {
                    return "HTTP Error: Invalid action.";
                }
            }
            case "domain" -> domain = value;
            case "path" -> path = value;
            default -> {
                return "HTTP Error: Unknown field: " + key;
            }
        }
    }

    if (domain.isBlank()) {
        return "HTTP Error: Domain is required.";
    }

    HttpSimulation.HttpSimulationResult result =
            HTTP_SIMULATION.simulateGet(domain, path);

    return result.toDisplayString();
}
        if (input.startsWith("action=email")) {

            String from = "";
            String to = "";
            String subject = "";
            String body = "";

            String[] fields = input.split(",");

            for (String field : fields) {
                String[] parts = field.split("=", 2);

                if (parts.length != 2) {
                    return "Email Error: Invalid input format.";
                }

                String key = parts[0].trim();
                String value = parts[1].trim();

                switch (key) {
                    case "action" -> {
                        if (!"email".equals(value)) {
                            return "Email Error: Invalid action.";
                        }
                    }
                    case "from" -> from = value;
                    case "to" -> to = value;
                    case "subject" -> subject = value;
                    case "body" -> body = value;
                    default -> {
                        return "Email Error: Unknown field: " + key;
                    }
                }
            }

            if (from.isBlank() || to.isBlank()
                    || subject.isBlank() || body.isBlank()) {
                return "Email Error: All fields are required.";
            }

            EmailSimulation email = new EmailSimulation();

            EmailSimulation.EmailMessage message =
                    new EmailSimulation.EmailMessage(from, to, subject, body);

            EmailSimulation.EmailResult result =
                    email.sendAndReceive(message);

            return result.toDisplayString();
        }

        DnsSimulation.DnsLookupResult result =
                DNS_SIMULATION.resolve(input);

        return result.toDisplayString();

    } catch (IllegalArgumentException | IllegalStateException e) {
        return "Application Layer Error: " + e.getMessage();
    }
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
