package com.virtualnetlab.application;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StreamingSimulationTest {
    @Test
    void bufferingCreatesExpectedChunks() {
        StreamingSimulation streaming = new StreamingSimulation();

        StreamingSimulation.StreamingResult result = streaming.simulate("video.example.com", 4, 5);

        assertEquals(4, result.chunks().size());
        assertEquals(20, result.totalMb());
        assertEquals("BUFFERING_COMPLETE", result.status());
    }
}
