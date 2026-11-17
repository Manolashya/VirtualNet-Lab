package com.virtualnetlab.application;

import java.util.ArrayList;
import java.util.List;

/** Optional educational video buffering/CDN illustration. */
public class StreamingSimulation {
    public record BufferChunk(int sequence, int sizeMb, String source) {}

    public record StreamingResult(
            String domain,
            String cdnNode,
            List<BufferChunk> chunks,
            int totalMb,
            String status) {

        public String toDisplayString() {
            StringBuilder output = new StringBuilder();
            output.append("VIDEO STREAMING / CDN\n")
                    .append("------------------------------\n")
                    .append("Domain      : ").append(domain).append("\n")
                    .append("CDN Node    : ").append(cdnNode).append("\n")
                    .append("Status      : ").append(status).append("\n")
                    .append("Total Data  : ").append(totalMb).append(" MB\n")
                    .append("Buffer Chunks:\n");
            for (BufferChunk chunk : chunks) {
                output.append("  Chunk ").append(chunk.sequence())
                        .append(" -> ").append(chunk.sizeMb())
                        .append(" MB from ").append(chunk.source()).append("\n");
            }
            return output.toString();
        }
    }

    public StreamingResult simulate(String domain, int chunkCount, int chunkSizeMb) {
        List<BufferChunk> chunks = new ArrayList<>();
        for (int i = 1; i <= chunkCount; i++) {
            chunks.add(new BufferChunk(i, chunkSizeMb, "CDN-EDGE-01"));
        }
        return new StreamingResult(
                domain,
                "CDN-EDGE-01",
                List.copyOf(chunks),
                chunkCount * chunkSizeMb,
                "BUFFERING_COMPLETE");
    }
}
