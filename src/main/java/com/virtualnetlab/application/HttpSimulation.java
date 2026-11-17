package com.virtualnetlab.application;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Basic HTTP GET request/response simulation built on top of the DNS simulation.
 * It does not make a real network connection.
 */
public class HttpSimulation {
    public record HttpRequest(
            String method,
            String host,
            String path,
            Map<String, String> headers) {}

    public record HttpResponse(
            int statusCode,
            String reason,
            Map<String, String> headers,
            String body) {}

    public record HttpSimulationResult(
            DnsSimulation.DnsLookupResult dnsResult,
            HttpRequest request,
            HttpResponse response) {

        public String toDisplayString() {
            StringBuilder result = new StringBuilder();
            result.append(dnsResult.toDisplayString())
                    .append("\n\nHTTP GET REQUEST\n")
                    .append("------------------------------\n")
                    .append("Method      : ").append(request.method()).append("\n")
                    .append("Host        : ").append(request.host()).append("\n")
                    .append("Path        : ").append(request.path()).append("\n")
                    .append("Headers     : ").append(request.headers()).append("\n\n")
                    .append("HTTP RESPONSE\n")
                    .append("------------------------------\n")
                    .append("Status      : ").append(response.statusCode())
                    .append(" ").append(response.reason()).append("\n")
                    .append("Headers     : ").append(response.headers()).append("\n")
                    .append("Body        : ").append(response.body());
            return result.toString();
        }
    }

    private final DnsSimulation dnsSimulation;

    public HttpSimulation() {
        this(new DnsSimulation());
    }

    public HttpSimulation(DnsSimulation dnsSimulation) {
        this.dnsSimulation = dnsSimulation;
    }

    public HttpSimulationResult simulateGet(String domain) {
        return simulateGet(domain, "/");
    }

    public HttpSimulationResult simulateGet(String domain, String path) {
        String normalizedPath = normalizePath(path);
        DnsSimulation.DnsLookupResult dnsResult = dnsSimulation.resolve(domain);

        Map<String, String> requestHeaders = new LinkedHashMap<>();
        requestHeaders.put("Host", dnsResult.domain());
        requestHeaders.put("Connection", "close");

        HttpRequest request = new HttpRequest("GET", dnsResult.domain(), normalizedPath, requestHeaders);

        HttpResponse response;
        if (!"RESOLVED".equals(dnsResult.status())
                && !"RESOLVED_AND_CACHED".equals(dnsResult.status())) {
            Map<String, String> errorHeaders = new LinkedHashMap<>();
            errorHeaders.put("Content-Type", "text/plain");
            response = new HttpResponse(
                    404,
                    "Not Found",
                    errorHeaders,
                    "DNS lookup failed for " + dnsResult.domain());
        } else {
            Map<String, String> responseHeaders = new LinkedHashMap<>();
            responseHeaders.put("Content-Type", "text/html");
            responseHeaders.put("Content-Length", "simulated");
            response = new HttpResponse(
                    200,
                    "OK",
                    responseHeaders,
                    "Hello from VirtualNet Lab.\nGET " + normalizedPath + " served for " + dnsResult.domain());
        }

        return new HttpSimulationResult(dnsResult, request, response);
    }

    public DnsSimulation getDnsSimulation() {
        return dnsSimulation;
    }

    private String normalizePath(String path) {
        if (path == null || path.isBlank()) {
            return "/";
        }
        return path.startsWith("/") ? path : "/" + path;
    }
}
