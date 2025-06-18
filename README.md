# Spring Boot Parallel Processing Demo

A comprehensive demonstration of parallel processing performance in Spring Boot, comparing sequential processing vs parallel streams vs custom ForkJoinPool implementations for CPU-intensive calculations.

## 🎯 Project Overview

This project demonstrates how to effectively use parallel processing in Spring Boot applications, particularly for CPU-bound tasks. It includes performance testing and analysis tools to measure the effectiveness of different parallel processing approaches.

## 🚀 Features

- **Sequential Processing**: Baseline single-threaded implementation
- **Parallel Streams**: Java 8+ parallel streams with default ForkJoinPool
- **Custom ForkJoinPool**: Optimized thread pool configuration for specific workloads
- **Performance Testing**: Benchmarks and analysis
- **Diagnostic Tools**: Thread usage analysis, CPU utilization monitoring, and scalability testing

## 📋 Requirements

- **Java**: 17 or higher
- **Maven**: 3.6+ (or use included Maven wrapper)
- **Spring Boot**: 3.2.x
- **Multi-core CPU**: For meaningful parallel processing benefits

## 🛠️ Getting Started

### Clone the Repository

```bash
git clone https://github.com/SK-Service/JavaParallelProcessing.git
cd JavaParallelProcessing
```

### Build the Project

```bash
# Using Maven wrapper (recommended)
./mvnw clean compile

# Or if you have Maven installed
mvn clean compile
```

### Run the Application

```bash
./mvnw spring-boot:run
```

### Run Performance Tests

```bash
# Run all tests
./mvnw test

# Run specific test class
./mvnw test -Dtest=ParallelProcessingPerformanceTest

# Run specific test method
./mvnw test -Dtest=ParallelProcessingPerformanceTest#ttestThreadUsage
```

## 🧪 Test Suite Overview

### 1. Performance Comparison Test
**Method**: `testPerformanceComparison()`

Compares execution times across three approaches:
- Sequential processing
- Parallel streams (default ForkJoinPool)
- Custom ForkJoinPool configuration

**Expected Output**:
```
Testing Sequential Processing...
Run 1: 2847 ms
Run 2: 2834 ms
...

Testing Parallel Processing (Default Pool)...
Run 1: 734 ms
Run 2: 728 ms
...

Performance Improvement:
Parallel vs Sequential: 3.87x faster
Custom Pool vs Sequential: 4.12x faster
```

### 2. Thread Usage Analysis
**Method**: `testThreadUsage()`

Monitors thread creation and usage patterns during parallel processing.

### 4. Scalability Test
**Method**: `testScalability()`

Tests performance across different data sizes to identify scaling characteristics.

## 🏗️ Design

### Core Components

#### `FinancialCalculationService`
The main service class providing three calculation methods:

```java
// Sequential processing
List<BigDecimal> calculateRiskMetricsSequential(List<Portfolio> portfolios);
    
// Parallel streams with default pool
List<BigDecimal> calculateRiskMetricsParallel(List<Portfolio> portfolios);

// Custom ForkJoinPool
List<BigDecimal> calculateRiskMetricsCustomPool(List<Portfolio> portfolios);
```

#### `Portfolio` Model
Represents a financial portfolio with:
- **ID**: Unique identifier
- **Notional**: Portfolio value
- **Expected Return**: Expected annual return percentage
- **Volatility**: Risk measurement

#### `ParallelProcessingConfig`
Spring configuration for optimized thread pool settings:

```java
@Bean
@Primary
public TaskExecutor taskExecutor() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(Runtime.getRuntime().availableProcessors());
    executor.setMaxPoolSize(Runtime.getRuntime().availableProcessors() * 2);
    // ... additional configuration
    return executor;
}
```

## 📊 Performance Analysis

### When Parallel Processing Helps

**✅ Good candidates for parallel processing:**
- CPU-intensive calculations
- Large datasets (1000+ items)
- Independent operations (no shared state)
- Mathematical computations (Monte Carlo simulations)

**❌ Poor candidates:**
- I/O-bound operations
- Small datasets (< 100 items)
- Operations with heavy synchronization
- Simple calculations with minimal CPU usage

### Interpreting Results

#### Speedup Calculation
```
Speedup = Sequential Time / Parallel Time

Example: 2000ms / 500ms = 4.0x speedup
```

## 📚 Additional Resources

### Related Technologies
- **Spring Batch**: For large-scale batch processing
- **Spring WebFlux**: For reactive, non-blocking I/O
- **Project Reactor**: For reactive programming
- **Apache Spark**: For distributed data processing

### Further Reading
- [Java Parallel Streams Guide](https://docs.oracle.com/javase/tutorial/collections/streams/parallelism.html)
- [ForkJoinPool Documentation](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/ForkJoinPool.html)


## 🤝 Contributing

1. **Fork the repository**
2. **Create a feature branch**: `git checkout -b feature/new-optimization`
3. **Make your changes** and **add tests**
4. **Commit your changes**: `git commit -m "Add new optimization technique"`
5. **Push to the branch**: `git push origin feature/new-optimization`
6. **Submit a pull request**

### Areas for Contribution
- Additional parallel processing algorithms
- Performance optimizations
- More comprehensive financial calculations
- Integration with other Spring Boot features
- Documentation improvements

## 👥 Authors

- **Sunil Kr. Singh** - *Initial work* - https://github.com/SK-Service

## 🙏 Acknowledgments

- Spring Boot team for excellent framework design
- Java concurrent programming community
- Open source contributors for inspiration and guidance

---

**Happy parallel processing!** 🚀