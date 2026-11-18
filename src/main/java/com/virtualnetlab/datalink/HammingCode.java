
package com.virtualnetlab.datalink;

public class HammingCode {

    // Encode 4 data bits into 7-bit Hamming code
    public static String encode(String data) {

        validate(data);

        if (data.length() != 4) {
            throw new IllegalArgumentException(
                "Hamming (7,4) requires exactly 4 data bits."
            );
        }

        char[] bits = new char[7];

        // Place data bits in positions 3, 5, 6 and 7
        bits[2] = data.charAt(0);
        bits[4] = data.charAt(1);
        bits[5] = data.charAt(2);
        bits[6] = data.charAt(3);

        // Calculate even parity bits
        bits[0] = (char) ('0' + (
            (bits[2] - '0') ^
            (bits[4] - '0') ^
            (bits[6] - '0')
        ));

        bits[1] = (char) ('0' + (
            (bits[2] - '0') ^
            (bits[5] - '0') ^
            (bits[6] - '0')
        ));

        bits[3] = (char) ('0' + (
            (bits[4] - '0') ^
            (bits[5] - '0') ^
            (bits[6] - '0')
        ));

        return new String(bits);
    }

    // Find the error position using syndrome bits
    public static int findErrorPosition(String received) {

        validate(received);

        if (received.length() != 7) {
            throw new IllegalArgumentException(
                "Received codeword must contain exactly 7 bits."
            );
        }

        int s1 = parity(received, 0, 2, 4, 6);
        int s2 = parity(received, 1, 2, 5, 6);
        int s4 = parity(received, 3, 4, 5, 6);

        // Convert syndrome bits into decimal position
        return s1 + (s2 * 2) + (s4 * 4);
    }

    // Correct a single-bit error
    public static String correctError(String received) {

        int errorPosition = findErrorPosition(received);

        char[] bits = received.toCharArray();

        if (errorPosition != 0) {

            int index = errorPosition - 1;

            bits[index] = bits[index] == '0' ? '1' : '0';
        }

        return new String(bits);
    }

    // Calculate even parity for selected positions
    private static int parity(String data, int... positions) {

        int result = 0;

        for (int position : positions) {

            result ^= data.charAt(position) - '0';
        }

        return result;
    }

    // Validate binary input
    private static void validate(String data) {

        if (data == null || data.isEmpty()) {
            throw new IllegalArgumentException(
                "Input cannot be empty."
            );
        }

        for (int i = 0; i < data.length(); i++) {

            if (data.charAt(i) != '0' && data.charAt(i) != '1') {

                throw new IllegalArgumentException(
                    "Only binary digits are allowed."
                );
            }
        }
    }

    // Main method
    public static void main(String[] args) {

        String data = "1011";

        String encoded = encode(data);

        System.out.println("Original Data: " + data);
        System.out.println("Encoded Data: " + encoded);

        // Simulate an error at position 6
        char[] receivedBits = encoded.toCharArray();

        receivedBits[5] =
            receivedBits[5] == '0' ? '1' : '0';

        String received = new String(receivedBits);

        System.out.println("Received Data: " + received);

        int errorPosition = findErrorPosition(received);

        System.out.println("Error Position: " + errorPosition);

        String corrected = correctError(received);

        System.out.println("Corrected Data: " + corrected);
    }
}
