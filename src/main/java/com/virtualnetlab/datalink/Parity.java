
package com.virtualnetlab.datalink;

public class Parity {

    // Generate even parity
    public static String generateEvenParity(String data) {

        validateBinary(data);

        int count = 0;

        for (int i = 0; i < data.length(); i++) {
            if (data.charAt(i) == '1') {
                count++;
            }
        }

        if (count % 2 == 0) {
            return data + "0";
        } else {
            return data + "1";
        }
    }

    // Generate odd parity
    public static String generateOddParity(String data) {

        validateBinary(data);

        int count = 0;

        for (int i = 0; i < data.length(); i++) {
            if (data.charAt(i) == '1') {
                count++;
            }
        }

        if (count % 2 == 0) {
            return data + "1";
        } else {
            return data + "0";
        }
    }

    // Check even parity
    public static boolean checkEvenParity(String received) {

        validateBinary(received);

        int count = 0;

        for (int i = 0; i < received.length(); i++) {
            if (received.charAt(i) == '1') {
                count++;
            }
        }

        return count % 2 == 0;
    }

    // Check odd parity
    public static boolean checkOddParity(String received) {

        validateBinary(received);

        int count = 0;

        for (int i = 0; i < received.length(); i++) {
            if (received.charAt(i) == '1') {
                count++;
            }
        }

        return count % 2 != 0;
    }

    // Validate binary input
    private static void validateBinary(String data) {

        if (data == null || data.isEmpty()) {
            throw new IllegalArgumentException(
                "Input cannot be empty."
            );
        }

        for (int i = 0; i < data.length(); i++) {
            if (data.charAt(i) != '0' && data.charAt(i) != '1') {
                throw new IllegalArgumentException(
                    "Only binary digits 0 and 1 are allowed."
                );
            }
        }
    }

    // Main method for testing
    public static void main(String[] args) {

        String data = "1011";

        String even = generateEvenParity(data);
        String odd = generateOddParity(data);

        System.out.println("Original Data: " + data);

        System.out.println("Even Parity: " + even);
        System.out.println("Odd Parity: " + odd);

        System.out.println("Even Check: " +
            checkEvenParity(even));

        System.out.println("Odd Check: " +
            checkOddParity(odd));
            System.out.println("Error Check: " +
    checkEvenParity("10011"));
    }
}
