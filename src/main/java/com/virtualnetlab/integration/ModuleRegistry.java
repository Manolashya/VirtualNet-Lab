package com.virtualnetlab.integration;

/**
 * Integration point for Members 2–6.
 * Replace each placeholder call with the corresponding tested module service.
 */
public final class ModuleRegistry {
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

    private static String runNetworkFundamentals(String input) {
        return placeholder("Network Fundamentals", input,
                "Member 2: connect NetworkMetrics / topology simulation here.");
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

    private static String placeholder(String module, String input, String integrationNote) {
        return module + "\n"
                + "------------------------------\n"
                + "Received input: " + input + "\n\n"
                + "DEMO PLACEHOLDER — algorithm not connected yet.\n"
                + integrationNote;
    }
}
