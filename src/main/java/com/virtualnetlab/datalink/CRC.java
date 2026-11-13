
package com.virtualnetlab.datalink;

public class CRC {

    // Generate CRC codeword
    public static String generateCRC(String data, String generator) {

        validateBinary(data);
        validateGenerator(generator);

        int zeros = generator.length() - 1;

        String dividend = data + "0".repeat(zeros);

        String remainder = divide(dividend, generator);

        return data + remainder;
    }

    // Verify received CRC codeword
    public static boolean verifyCRC(String codeword, String generator) {

        validateBinary(codeword);
        validateGenerator(generator);

        if (codeword.length() < generator.length()) {
            throw new IllegalArgumentException(
                "Codeword is too short."
            );
        }

        String remainder = divide(codeword, generator);

        return remainder.chars().allMatch(ch -> ch == '0');
    }

    // Perform modulo-2 binary division
    private static String divide(String dividend, String generator) {

        char[] bits = dividend.toCharArray();

        for (int i = 0; i <= bits.length - generator.length(); i++) {

            if (bits[i] == '1') {

                for (int j = 0; j < generator.length(); j++) {

                    bits[i + j] =
                        bits[i + j] == generator.charAt(j) ? '0' : '1';
                }
            }
        }

        return new String(bits, bits.length - generator.length() + 1,
                          generator.length() - 1);
    }

    // Validate binary input
    private static void validateBinary(String value) {

        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("Input cannot be empty.");
        }

        for (int i = 0; i < value.length(); i++) {
            if (value.charAt(i) != '0' && value.charAt(i) != '1') {
                throw new IllegalArgumentException(
                    "Only binary digits are allowed."
                );
            }
        }
    }

    // Validate generator
    private static void validateGenerator(String generator) {

        validateBinary(generator);

        if (generator.length() < 2 || generator.charAt(0) != '1') {
            throw new IllegalArgumentException(
                "Generator must start with 1 and have at least 2 bits."
            );
        }
    }

    // Main method
    public static void main(String[] args) {

        String data = "1101";
        String generator = "1011";

        String codeword = generateCRC(data, generator);

        System.out.println("Original Data: " + data);
        System.out.println("Generator: " + generator);
        System.out.println("CRC Codeword: " + codeword);

        System.out.println("Verification: " +
            verifyCRC(codeword, generator));

        System.out.println("Error Detection: " +
            verifyCRC("1100001", generator));
    }
}
