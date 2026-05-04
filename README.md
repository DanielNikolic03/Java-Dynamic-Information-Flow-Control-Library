# Java Dynamic Information Flow Control Library
## Overview
This project implements a **dynamic information flow control (IFC)** library for Java.  
The library tracks how sensitive data propagates during program execution and prevents illegal information flows at runtime.

Unlike systems such as JSFlow, which implement IFC through a custom interpreter, this project explores a **library-based approach**. The goal is to make IFC easier to experiment with directly in standard Java programs without requiring a new language or runtime.

## Features

- **Security labels**
  - Simple label lattice: `LOW` and `HIGH`
  - Label join operation for combining sensitivities

- **Labeled values**
  - Generic wrapper `Labeled<T>` that attaches a label to runtime data
  - Label propagation through computations

- **Explicit flow tracking**
  - Labels are combined during operations (e.g., arithmetic, comparisons)

- **Program counter (PC) tracking**
  - Tracks control-flow dependencies
  - Detects implicit flows caused by branching on sensitive data

- **Secure mutable state**
  - `SecCell<T>` represents labeled variables / storage
  - Enforces security checks on writes

- **Checked outputs (sinks)**
  - Prevents sensitive data from being written to low-observable outputs

- **Runtime enforcement**
  - Illegal flows result in a runtime exception