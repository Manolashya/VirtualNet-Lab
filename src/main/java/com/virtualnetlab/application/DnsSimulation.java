package com.virtualnetlab.application;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Small, deterministic DNS lookup table/cache used by the Application Layer simulation.
 * No real DNS/network traffic is generated.
 */
public class DnsSimulation {
    public record DnsRecord(String domain, String ipAddress, int ttlSeconds) {}

    public record DnsLookupResult(
            String domain,
            String ipAddress,
            boolean cacheHit,
            String source,
            String status,
            int cacheSize) {

        public String toDisplayString() {
            return "DNS LOOKUP\n"
                    + "------------------------------\n"
                    + "Domain      : " + domain + "\n"
                    + "IP Address  : " + (ipAddress == null ? "Not found" : ipAddress) + "\n"
                    + "Source      : " + source + "\n"
                    + "Cache Hit   : " + cacheHit + "\n"
                    + "Status      : " + status + "\n"
                    + "Cache Size  : " + cacheSize;
        }
    }

    private final Map<String, DnsRecord> lookupTable = new LinkedHashMap<>();
    private final Map<String, DnsRecord> cache = new LinkedHashMap<>();

    public DnsSimulation() {
        seedDefaultRecords();
    }

    public void addRecord(String domain, String ipAddress) {
        String normalizedDomain = normalizeDomain(domain);
        lookupTable.put(normalizedDomain, new DnsRecord(normalizedDomain, ipAddress, 300));
    }

    public DnsLookupResult resolve(String domain) {
        String normalizedDomain = normalizeDomain(domain);
        if (normalizedDomain.isBlank()) {
            return new DnsLookupResult("", null, false, "None", "INVALID_DOMAIN", cache.size());
        }

        DnsRecord cached = cache.get(normalizedDomain);
        if (cached != null) {
            return new DnsLookupResult(
                    normalizedDomain,
                    cached.ipAddress(),
                    true,
                    "DNS Cache",
                    "RESOLVED",
                    cache.size());
        }

        DnsRecord record = lookupTable.get(normalizedDomain);
        if (record == null) {
            return new DnsLookupResult(
                    normalizedDomain,
                    null,
                    false,
                    "Authoritative Lookup",
                    "DOMAIN_NOT_FOUND",
                    cache.size());
        }

        cache.put(normalizedDomain, record);
        return new DnsLookupResult(
                normalizedDomain,
                record.ipAddress(),
                false,
                "Authoritative Lookup",
                "RESOLVED_AND_CACHED",
                cache.size());
    }

    public int getCacheSize() {
        return cache.size();
    }

    public void clearCache() {
        cache.clear();
    }

    private void seedDefaultRecords() {
        addRecord("www.example.com", "93.184.216.34");
        addRecord("api.example.com", "93.184.216.34");
        addRecord("virtualnetlab.local", "192.168.1.10");
        addRecord("mail.example.com", "192.0.2.25");
    }

    private String normalizeDomain(String domain) {
        if (domain == null) {
            return "";
        }
        String normalized = domain.trim().toLowerCase(Locale.ROOT);
        while (normalized.endsWith(".")) {
            normalized = normalized.substring(0, normalized.length() - 1);
        }
        return normalized;
    }
}
