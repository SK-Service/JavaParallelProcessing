package com.exparal.demo;

import com.exparal.demo.model.Portfolio;
import com.exparal.demo.service.FinancialCalculationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.sun.management.OperatingSystemMXBean; // Platform-specific
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.LongSummaryStatistics;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import static org.assertj.core.api.Assertions.assertThat;

// Performance test class
@SpringBootTest
public class ThreadPoolUtilizationComparisonTest {

    @Autowired
    private FinancialCalculationService calculationService;

    private List<Portfolio> testPortfolios;

    @BeforeEach
    void setUp() {
        // Create test data
        testPortfolios = generateTestPortfolios(10000); // Adjust size based on your need
    }


    @Test
    @DisplayName("Thread Usage Analysis")
    void testThreadUsage() {
        // Monitor thread usage during parallel processing
        ThreadMXBean threadBean = ManagementFactory.getThreadMXBean();

        System.out.println("\nThread Analysis:");
        System.out.println("Available processors: " + Runtime.getRuntime().availableProcessors());
        System.out.println("Default ForkJoinPool parallelism: " + ForkJoinPool.commonPool().getParallelism());

        // Sequential processing
        int threadsBefore = threadBean.getThreadCount();
        calculationService.calculateRiskMetricsSequential(testPortfolios.subList(0, 1000));
        int threadsAfterSequential = threadBean.getThreadCount();

        // Parallel processing
        calculationService.calculateRiskMetricsParallel(testPortfolios.subList(0, 1000));
        int threadsAfterParallel = threadBean.getThreadCount();

        System.out.printf("Threads before: %d%n", threadsBefore);
        System.out.printf("Threads after sequential: %d%n", threadsAfterSequential);
        System.out.printf("Threads after parallel: %d%n", threadsAfterParallel);
    }

    @Test
    @DisplayName("CPU Utilization Test")
    void testCpuUtilization() {
        OperatingSystemMXBean osBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

        // Measure CPU utilization during sequential processing
        double cpuBefore = osBean.getProcessCpuLoad();
        long startTime = System.currentTimeMillis();

        calculationService.calculateRiskMetricsSequential(testPortfolios);

        long sequentialTime = System.currentTimeMillis() - startTime;
        double cpuAfterSequential = osBean.getProcessCpuLoad();

        // Allow CPU to settle
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Measure CPU utilization during parallel processing
        startTime = System.currentTimeMillis();

        calculationService.calculateRiskMetricsParallel(testPortfolios);

        long parallelTime = System.currentTimeMillis() - startTime;
        double cpuAfterParallel = osBean.getProcessCpuLoad();

        System.out.printf("\nCPU Usage Analysis:%n");
        System.out.printf("Sequential time: %d ms%n", sequentialTime);
        System.out.printf("Parallel time: %d ms%n", parallelTime);
        System.out.printf("CPU before: %.2f%%%n", cpuBefore * 100);
        System.out.printf("CPU after sequential: %.2f%%%n", cpuAfterSequential * 100);
        System.out.printf("CPU after parallel: %.2f%%%n", cpuAfterParallel * 100);
    }

    @Test
    @DisplayName("Scalability Test")
    void testScalability() {
        int[] dataSizes = {100, 500, 1000};

        System.out.println("\nScalability Test Results:");
        System.out.println("Data Size | Sequential (ms) | Parallel (ms) | Speedup");
        System.out.println("----------|-----------------|---------------|--------");

        for (int size : dataSizes) {
            List<Portfolio> subset = testPortfolios.subList(0, Math.min(size, testPortfolios.size()));

            // Sequential
            long seqStart = System.nanoTime();
            calculationService.calculateRiskMetricsSequential(subset);
            long seqTime = (System.nanoTime() - seqStart) / 1_000_000;

            // Parallel
            long parStart = System.nanoTime();
            calculationService.calculateRiskMetricsParallel(subset);
            long parTime = (System.nanoTime() - parStart) / 1_000_000;

            double speedup = (double) seqTime / parTime;

            System.out.printf("%8d | %14d | %12d | %6.2fx%n",
                    size, seqTime, parTime, speedup);
        }
    }

    private List<Portfolio> generateTestPortfolios(int count) {
        List<Portfolio> portfolios = new ArrayList<>();
        Random random = new Random(42); // Fixed seed for reproducible tests

        for (int i = 0; i < count; i++) {
            portfolios.add(new Portfolio(
                    "PORTFOLIO_" + i,
                    1_000_000 + random.nextDouble() * 9_000_000, // 1M to 10M notional
                    0.05 + random.nextDouble() * 0.15, // 5% to 20% expected return
                    0.1 + random.nextDouble() * 0.3    // 10% to 40% volatility
            ));
        }

        return portfolios;
    }

    private void displayPerformanceStats(String method, List<Long> times) {
        LongSummaryStatistics stats = times.stream().mapToLong(Long::longValue).summaryStatistics();

        System.out.printf("\n%s Performance Stats:%n", method);
        System.out.printf("  Average: %.2f ms%n", stats.getAverage());
        System.out.printf("  Min: %d ms%n", stats.getMin());
        System.out.printf("  Max: %d ms%n", stats.getMax());
        System.out.printf("  Total: %d ms%n", stats.getSum());
    }
}
