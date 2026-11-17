
package com.virtualnetlab.datalink;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DataLinkLayerTest {

    @Test
    void testEvenParity() {
        assertEquals("10111", Parity.generateEvenParity("1011"));
    }

    @Test
    void testOddParity() {
        assertEquals("10110", Parity.generateOddParity("1011"));
    }

    @Test
    void testCRCGeneration() {
        String crc = CRC.generateCRC("1101", "1011");
        assertEquals("1101001", crc);
    }

    @Test
    void testCRCVerification() {
        String crc = CRC.generateCRC("1101", "1011");
        assertTrue(CRC.verifyCRC(crc, "1011"));
    }

    @Test
    void testHammingEncoding() {
        assertEquals("0110011", HammingCode.encode("1011"));
    }

    @Test
    void testHammingCorrection() {
        assertEquals("0110011", HammingCode.correctError("0110001"));
    }

    @Test
    void testStopAndWait() {
        boolean result = StopAndWait.simulate(3, 2, 0)
                .stream()
                .anyMatch(line -> line.contains("ACK 3 received"));

        assertTrue(result);
    }
}
