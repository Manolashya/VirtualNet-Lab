package com.virtualnetlab.application;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HttpSimulationTest {
    @Test
    void getRequestPerformsDnsThenReturnsOk() {
        DnsSimulation dns = new DnsSimulation();
        HttpSimulation http = new HttpSimulation(dns);

        HttpSimulation.HttpSimulationResult result = http.simulateGet("www.example.com", "/index.html");

        assertEquals("RESOLVED_AND_CACHED", result.dnsResult().status());
        assertEquals("GET", result.request().method());
        assertEquals("/index.html", result.request().path());
        assertEquals(200, result.response().statusCode());
        assertEquals("OK", result.response().reason());
    }

    @Test
    void unknownDomainProducesNotFoundResponse() {
        HttpSimulation http = new HttpSimulation();

        HttpSimulation.HttpSimulationResult result = http.simulateGet("missing.example");

        assertEquals(404, result.response().statusCode());
        assertEquals("Not Found", result.response().reason());
    }
}
