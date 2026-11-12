
package com.virtualnetlab.datalink;

public class DataLinkLayerTest {

    public static void main(String[] args) {

        String even = Parity.generateEvenParity("1011");
        System.out.println("Even Parity: "
                + (even.equals("10111") ? "PASS" : "FAIL"));

        String odd = Parity.generateOddParity("1011");
        System.out.println("Odd Parity: "
                + (odd.equals("10110") ? "PASS" : "FAIL"));

        String crc = CRC.generateCRC("1101", "1011");
        System.out.println("CRC Generation: "
                + (crc.equals("1101001") ? "PASS" : "FAIL"));

        System.out.println("CRC Verification: "
                + (CRC.verifyCRC(crc, "1011") ? "PASS" : "FAIL"));

        String hamming = HammingCode.encode("1011");
        System.out.println("Hamming Encoding: "
                + (hamming.equals("0110011") ? "PASS" : "FAIL"));

        String corrected = HammingCode.correctError("0110001");
        System.out.println("Hamming Correction: "
                + (corrected.equals("0110011") ? "PASS" : "FAIL"));

        boolean stopWait = StopAndWait.simulate(3, 2, 0)
                .stream()
                .anyMatch(line -> line.contains("ACK 3 received"));

        System.out.println("Stop-and-Wait: "
                + (stopWait ? "PASS" : "FAIL"));
    }
}

