package com.virtualnetlab.routing;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class IPv4UtilsTest {
    @Test
    void acceptsValidIpv4() {
        assertTrue(IPv4Utils.isValidIPv4("192.168.1.10"));
        assertTrue(IPv4Utils.isValidIPv4("0.0.0.0"));
        assertTrue(IPv4Utils.isValidIPv4("255.255.255.255"));
    }

    @Test
    void rejectsInvalidIpv4() {
        assertFalse(IPv4Utils.isValidIPv4("256.168.1.10"));
        assertFalse(IPv4Utils.isValidIPv4("192.168.1"));
        assertFalse(IPv4Utils.isValidIPv4("192.168.1.1.5"));
        assertFalse(IPv4Utils.isValidIPv4("192.168.-1.1"));
        assertFalse(IPv4Utils.isValidIPv4(null));
    }

    @Test
    void calculatesSlash24Subnet() {
        IPv4Utils.SubnetInfo result =
                IPv4Utils.calculateSubnet("192.168.1.10", 24);

        assertEquals("255.255.255.0", result.getSubnetMask());
        assertEquals("192.168.1.0", result.getNetworkAddress());
        assertEquals("192.168.1.255", result.getBroadcastAddress());
        assertEquals("192.168.1.1", result.getFirstHost());
        assertEquals("192.168.1.254", result.getLastHost());
        assertEquals(254, result.getUsableHosts());
    }

    @Test
    void rejectsInvalidPrefix() {
        assertThrows(IllegalArgumentException.class,
                () -> IPv4Utils.calculateSubnet("192.168.1.10", 33));
    }

    @Test
    void handlesSlash31PointToPointSubnet() {
        IPv4Utils.SubnetInfo result =
                IPv4Utils.calculateSubnet("10.0.0.1", 31);
        assertEquals(2, result.getUsableHosts());
        assertEquals("10.0.0.0", result.getFirstHost());
        assertEquals("10.0.0.1", result.getLastHost());
    }
}
