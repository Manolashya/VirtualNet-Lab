
package com.virtualnetlab.datalink;

import java.util.ArrayList;
import java.util.List;

public class StopAndWait {

    // Simulate Stop-and-Wait ARQ
    public static List<String> simulate(
            int totalFrames,
            int lostFrameNumber,
            int lostAckFrameNumber) {

        if (totalFrames < 1) {
            throw new IllegalArgumentException(
                "Total frames must be at least 1."
            );
        }

        if (lostFrameNumber < 0 || lostFrameNumber > totalFrames ||
            lostAckFrameNumber < 0 || lostAckFrameNumber > totalFrames) {
            throw new IllegalArgumentException(
                "Lost frame and ACK numbers must be between 0 and total frames."
            );
        }

        List<String> log = new ArrayList<>();

        for (int frame = 1; frame <= totalFrames; frame++) {

            log.add("Sender: Sending Frame " + frame);

            // Simulate frame loss
            if (frame == lostFrameNumber) {
                log.add("Receiver: Retransmitted Frame " + frame + " received.");

                log.add("Receiver: Frame " + frame + " lost.");

                log.add("Sender: Timeout occurred.");

                log.add("Sender: Retransmitting Frame " + frame);

            } else {

                log.add("Receiver: Frame " + frame + " received.");
            }

            // Receiver sends acknowledgement
            log.add("Receiver: Sending ACK " + frame);

            // Simulate ACK loss
            if (frame == lostAckFrameNumber) {

                log.add("Network: ACK " + frame + " lost.");

                log.add("Sender: Timeout occurred.");

                log.add("Sender: Retransmitting Frame " + frame);

                log.add("Receiver: Duplicate Frame " + frame +
                        " received. Discarding duplicate.");

                log.add("Receiver: Resending ACK " + frame);
            }

            log.add("Sender: ACK " + frame +
                    " received. Moving to next frame.");

            log.add("--------------------------------");
        }

        return log;
    }

    // Main method
    public static void main(String[] args) {

        int totalFrames = 3;

        // Frame 2 is lost once. No ACK is lost.
        int lostFrameNumber = 2;

        // 0 means no ACK loss.
        int lostAckFrameNumber = 0;

        List<String> result = simulate(
            totalFrames,
            lostFrameNumber,
            lostAckFrameNumber
        );

        System.out.println("STOP-AND-WAIT ARQ SIMULATION");
        System.out.println();

        for (String message : result) {
            System.out.println(message);
        }
    }
}
