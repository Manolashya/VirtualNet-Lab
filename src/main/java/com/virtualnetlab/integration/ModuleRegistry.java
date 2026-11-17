package com.virtualnetlab.integration;

import com.virtualnetlab.datalink.Parity;
import com.virtualnetlab.datalink.CRC;
import com.virtualnetlab.datalink.HammingCode;
import com.virtualnetlab.datalink.StopAndWait;

public final class ModuleRegistry {

    private ModuleRegistry() {}

    public static String runDemo(int unit, String input) {
        return switch (unit) {
            case 1 -> placeholder("Network Fundamentals", input, "Member 2 integration pending.");
            case 2 -> runDataLink(input);
            case 3 -> placeholder("Network Layer", input, "Member 4 integration pending.");
            case 4 -> placeholder("Transport Layer", input, "Member 5 integration pending.");
            case 5 -> placeholder("Application Layer", input, "Member 6 integration pending.");
            default -> "Unknown module.";
        };
    }

    private static String runDataLink(String input) {
        try {
            String data = input.trim();

            if (data.isEmpty()) {
                return "Please enter binary data.";
            }

            StringBuilder result = new StringBuilder();

            result.append("DATA LINK LAYER SIMULATION\n");
            result.append("==============================\n");
            result.append("Original Data: ").append(data).append("\n\n");

            // 1. PARITY CHECK
            result.append("1. PARITY CHECK\n");

            String even = Parity.generateEvenParity(data);
            String odd = Parity.generateOddParity(data);

            result.append("Even Parity: ").append(even).append("\n");
            result.append("Odd Parity: ").append(odd).append("\n");
            result.append("Even Parity Verification: ")
                  .append(Parity.checkEvenParity(even) ? "No error" : "Error")
                  .append("\n\n");

            // 2. CRC
            result.append("2. CYCLIC REDUNDANCY CHECK (CRC)\n");

            String generator = "1011";
            String codeword = CRC.generateCRC(data, generator);

            result.append("Generator: ").append(generator).append("\n");
            result.append("CRC Codeword: ").append(codeword).append("\n");
            result.append("Verification: ")
                  .append(CRC.verifyCRC(codeword, generator) ? "Valid - No error" : "Error detected")
                  .append("\n\n");

            // 3. HAMMING CODE
            result.append("3. HAMMING CODE (7,4)\n");

            String hammingData = data.substring(0, Math.min(4, data.length()));

            if (hammingData.length() == 4) {
                String encoded = HammingCode.encode(hammingData);

                char[] receivedBits = encoded.toCharArray();
                receivedBits[2] = receivedBits[2] == '0' ? '1' : '0';

                String received = new String(receivedBits);
                int errorPosition = HammingCode.findErrorPosition(received);
                String corrected = HammingCode.correctError(received);

                result.append("Data Bits: ").append(hammingData).append("\n");
                result.append("Encoded Code: ").append(encoded).append("\n");
                result.append("Received with simulated error: ").append(received).append("\n");
                result.append("Error Position: ").append(errorPosition).append("\n");
                result.append("Corrected Code: ").append(corrected).append("\n\n");
            } else {
                result.append("Enter at least 4 bits for Hamming Code.\n\n");
            }

            // 4. STOP-AND-WAIT ARQ
            result.append("4. STOP-AND-WAIT ARQ\n");

            for (String message : StopAndWait.simulate(3, 2, 0)) {
                result.append(message).append("\n");
            }

            result.append("\nAll available Data Link Layer simulations completed.");

            return result.toString();

        } catch (IllegalArgumentException e) {
            return "Invalid Input: " + e.getMessage();
        }
    }

    private static String placeholder(String module, String input, String note) {
        return module + "\nReceived input: " + input + "\n" + note;
    }
}
