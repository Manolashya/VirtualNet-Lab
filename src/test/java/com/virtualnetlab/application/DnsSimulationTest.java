package com.virtualnetlab.application;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DnsSimulationTest {
    @Test
    void firstLookupUsesAuthoritativeTableAndCachesResult() {
        DnsSimulation dns = new DnsSimulation();

        DnsSimulation.DnsLookupResult first = dns.resolve("www.example.com");
        DnsSimulation.DnsLookupResult second = dns.resolve("www.example.com");

        assertEquals("93.184.216.34", first.ipAddress());
        assertFalse(first.cacheHit());
        assertTrue(second.cacheHit());
        assertEquals(1, dns.getCacheSize());
    }

    @Test
    void unknownDomainIsReported() {
        DnsSimulation dns = new DnsSimulation();

        DnsSimulation.DnsLookupResult result = dns.resolve("unknown.example");

        assertEquals("DOMAIN_NOT_FOUND", result.status());
        assertNull(result.ipAddress());
    }
}
