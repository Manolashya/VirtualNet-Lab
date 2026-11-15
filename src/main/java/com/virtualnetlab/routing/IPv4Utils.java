package com.virtualnetlab.routing;

import java.util.regex.Pattern;

/**
 * IPv4 validation and basic CIDR subnet calculations.
 * This class contains no JavaFX code and can be called by the UI layer.
 */
public final class IPv4Utils {
    private static final Pattern OCTET = Pattern.compile("\\d{1,3}");

    private IPv4Utils() {
    }

    /** Returns true only for four decimal octets in the range 0..255. */
    public static boolean isValidIPv4(String ip) {
        if (ip == null) return false;

        String[] parts = ip.split("\\.", -1);
        if (parts.length != 4) return false;

        for (String part : parts) {
            if (!OCTET.matcher(part).matches()) return false;
            try {
                int value = Integer.parseInt(part);
                if (value < 0 || value > 255) return false;
            } catch (NumberFormatException ex) {
                return false;
            }
        }
        return true;
    }

    /**
     * Calculates subnet information for an IPv4 address and CIDR prefix.
     * Prefix must be between 0 and 32 inclusive.
     */
    public static SubnetInfo calculateSubnet(String ip, int prefix) {
        if (!isValidIPv4(ip)) {
            throw new IllegalArgumentException("Invalid IPv4 address: " + ip);
        }
        if (prefix < 0 || prefix > 32) {
            throw new IllegalArgumentException("CIDR prefix must be between 0 and 32.");
        }

        long address = toLong(ip);
        long mask = prefix == 0 ? 0L : (0xFFFFFFFFL << (32 - prefix)) & 0xFFFFFFFFL;
        long network = address & mask;
        long broadcast = network | (~mask & 0xFFFFFFFFL);

        long totalAddresses = 1L << (32 - prefix);
        long usableHosts;
        long firstHost;
        long lastHost;

        if (prefix == 32) {
            usableHosts = 1;
            firstHost = network;
            lastHost = network;
        } else if (prefix == 31) {
            // RFC 3021 point-to-point subnet: both addresses are usable.
            usableHosts = 2;
            firstHost = network;
            lastHost = broadcast;
        } else {
            usableHosts = Math.max(0, totalAddresses - 2);
            firstHost = network + 1;
            lastHost = broadcast - 1;
        }

        return new SubnetInfo(
                ip, prefix, toDottedDecimal(mask), toDottedDecimal(network),
                toDottedDecimal(broadcast), toDottedDecimal(firstHost),
                toDottedDecimal(lastHost), totalAddresses, usableHosts
        );
    }

    private static long toLong(String ip) {
        String[] parts = ip.split("\\.");
        long result = 0;
        for (String part : parts) {
            result = (result << 8) | Integer.parseInt(part);
        }
        return result;
    }

    private static String toDottedDecimal(long value) {
        return ((value >>> 24) & 255) + "." +
               ((value >>> 16) & 255) + "." +
               ((value >>> 8) & 255) + "." +
               (value & 255);
    }

    /** Immutable subnet calculation output. */
    public static final class SubnetInfo {
        private final String ipAddress;
        private final int prefix;
        private final String subnetMask;
        private final String networkAddress;
        private final String broadcastAddress;
        private final String firstHost;
        private final String lastHost;
        private final long totalAddresses;
        private final long usableHosts;

        public SubnetInfo(String ipAddress, int prefix, String subnetMask,
                          String networkAddress, String broadcastAddress,
                          String firstHost, String lastHost,
                          long totalAddresses, long usableHosts) {
            this.ipAddress = ipAddress;
            this.prefix = prefix;
            this.subnetMask = subnetMask;
            this.networkAddress = networkAddress;
            this.broadcastAddress = broadcastAddress;
            this.firstHost = firstHost;
            this.lastHost = lastHost;
            this.totalAddresses = totalAddresses;
            this.usableHosts = usableHosts;
        }

        public String getIpAddress() { return ipAddress; }
        public int getPrefix() { return prefix; }
        public String getCidr() { return networkAddress + "/" + prefix; }
        public String getSubnetMask() { return subnetMask; }
        public String getNetworkAddress() { return networkAddress; }
        public String getBroadcastAddress() { return broadcastAddress; }
        public String getFirstHost() { return firstHost; }
        public String getLastHost() { return lastHost; }
        public long getTotalAddresses() { return totalAddresses; }
        public long getUsableHosts() { return usableHosts; }

        @Override
        public String toString() {
            return "Subnet: " + getCidr() +
                    "\nSubnet mask: " + subnetMask +
                    "\nNetwork address: " + networkAddress +
                    "\nBroadcast address: " + broadcastAddress +
                    "\nHost range: " + firstHost + " - " + lastHost +
                    "\nTotal addresses: " + totalAddresses +
                    "\nUsable hosts: " + usableHosts;
        }
    }
}
