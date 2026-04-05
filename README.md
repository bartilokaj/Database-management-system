# SIMD-DBMS

**A high-performance columnar storage engine leveraging hardware-level parallelism via the Java Vector API.**

## Overview
This project is a specialized database management system focused on the intersection of **storage efficiency** and **computational throughput**. Designed to handle large-scale integer and string datasets, it implements a custom encoding pipeline optimized for modern CPU architectures.

### Key Engineering Challenges:
* **Balancing Compression vs. Speed:** Minimizing on-disk footprint while maintaining high-speed sequential access.
* **Vectorization:** Overcoming JVM limitations to utilize **SIMD (Single Instruction, Multiple Data)** for bulk data transformation.

---

## Technical Architecture

### Integer Encoding Pipeline
To maximize the effectiveness of Variable Length Quantity (VLQ) encoding, I developed a three-stage sequential transformation pipeline:

1.  **Delta Encoding:** Reduces integer magnitude by storing differences between successive values.
2.  **ZigZag Encoding:** Maps signed integers to unsigned space. This ensures that negative deltas (common in non-sorted data) can be efficiently processed by VLQ.
3.  **VLQ (Variable Length Quantity):** Compresses the resulting values into a minimal byte representation.

> **Design Note:** While initial iterations utilized sorting, the current **Delta -> ZigZag -> VLQ** chain was chosen to allow high compression ratios across all columns simultaneously, rather than being restricted to a single primary key.

### Hardware Acceleration (SIMD)
Critical paths in the encoding and decoding layers are implemented using the **Java Vector API (Incubator)**. By processing data in 256-bit or 512-bit wide registers, the system achieves hardware-level parallelism, significantly outperforming standard iterative Java loops.

### String Storage
Utilizes **Zstandard (Zstd)** for string compression, selected for its superior balance of compression ratio and near real-time decoding speeds.

---

## Getting Started

### Prerequisites
* **Java 21+** (Required for Vector API incubator features)
* **Maven**
* **Docker**

### Deployment with Docker
The included `Makefile` automates the environment setup and ensures data persistence:

```bash
# Build the Docker image
make docker

# Start the container on port 7000 with persistent volume mounting
make docker-run
```
### API Access
The system exposes core functionalities via a Javalin-based REST API.
Note: The API layer is currently under active development to expand support for full CRUD operations.

🧪 Testing & Development
The project includes a suite of unit tests focusing on encoding correctness and SIMD register alignment.

```bash
# Note: Ensure your environment supports --enable-preview for Vector API
mvn clean compile test
```
Development Note: If you encounter issues with preview features, verify that the maven-compiler-plugin target in the pom.xml matches your local SDK version.