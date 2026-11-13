
# Unit II – Data Link Layer

## 1. Overview
This module implements important Data Link Layer
algorithms using Java.

## 2. Implemented Algorithms

### A. Parity Generation and Checking
- Generates even parity.
- Generates odd parity.
- Checks received binary data.
- Detects errors involving an odd number of flipped bits.

### B. Cyclic Redundancy Check (CRC)
- Generates CRC using modulo-2 XOR division.
- Appends the remainder to the original data.
- Verifies received codewords.
- Detects transmission errors.

### C. Hamming Code (7,4)
- Encodes 4 data bits into 7 bits.
- Calculates parity bits at positions 1, 2 and 4.
- Calculates the error syndrome.
- Corrects a single-bit error.

### D. Stop-and-Wait ARQ
- Sends one frame at a time.
- Waits for acknowledgement (ACK).
- Simulates frame loss and ACK loss.
- Performs retransmission.

## 3. Java Package

com.virtualnetlab.datalink

## 4. Source Files

1. Parity.java
2. CRC.java
3. HammingCode.java
4. StopAndWait.java

## 5. Sample Test Inputs

| Algorithm | Input | Expected Output |
|---|---|---|
| Even Parity | 1011 | 10111 |
| Odd Parity | 1011 | 10110 |
| CRC | 1101, 1011 | 1101001 |
| Hamming (7,4) | 1011 | 0110011 |
| Hamming correction | 0110001 | 0110011 |
| Stop-and-Wait | 3 frames | ACK and retransmission trace |

## 6. Programming Language
Java

## 7. Limitations
- Basic parity cannot detect every even-numbered bit error.
- Hamming (7,4) is designed to correct single-bit errors.
- Stop-and-Wait is a simulation, not actual socket-based communication.
