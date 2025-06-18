package com.exparal.demo.service;

import com.exparal.demo.model.Portfolio;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

// Service class demonstrating parallel processing
@Service
public class FinancialCalculationService {

    private final ForkJoinPool customThreadPool;

    public FinancialCalculationService() {
        // Create custom ForkJoinPool with specific parallelism level
        int parallelism = Runtime.getRuntime().availableProcessors();
        System.out.println("Available processors,%d" + parallelism);
        this.customThreadPool = new ForkJoinPool(parallelism);
    }

    // Sequential processing
    public List<BigDecimal> calculateRiskMetricsSequential(List<Portfolio> portfolios) {
        return portfolios.stream()
                .map(this::calculateComplexRiskMetric)
                .collect(Collectors.toList());
    }

    // Parallel processing with default ForkJoinPool
    public List<BigDecimal> calculateRiskMetricsParallel(List<Portfolio> portfolios) {
        return portfolios.parallelStream()
                .map(this::calculateComplexRiskMetric)
                .collect(Collectors.toList());
    }

    // Parallel processing with custom ForkJoinPool
    public List<BigDecimal> calculateRiskMetricsCustomPool(List<Portfolio> portfolios) {
        try {
            return customThreadPool.submit(() ->
                    portfolios.parallelStream()
                            .map(this::calculateComplexRiskMetric)
                            .collect(Collectors.toList())
            ).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Parallel calculation failed", e);
        }
    }

    // Simulate CPU-intensive calculation (Monte Carlo simulation)
    private BigDecimal calculateComplexRiskMetric(Portfolio portfolio) {
        // Simulate complex financial calculation
        double result = 0.0;
        int iterations = 100000; // CPU-intensive loop

        Random random = ThreadLocalRandom.current();

        for (int i = 0; i < iterations; i++) {
            // Monte Carlo simulation for risk calculation
            double randomValue = random.nextGaussian();
            double portfolioReturn = portfolio.getExpectedReturn() +
                    (portfolio.getVolatility() * randomValue);
            result += Math.max(0, portfolio.getNotional() - portfolioReturn);
        }

        // Additional CPU-intensive operations
        result = Math.sqrt(Math.abs(result / iterations));
        result = Math.pow(result, 1.5);

        return BigDecimal.valueOf(result).setScale(4, RoundingMode.HALF_UP);
    }

    @PreDestroy
    public void shutdown() {
        customThreadPool.shutdown();
        try {
            if (!customThreadPool.awaitTermination(60, TimeUnit.SECONDS)) {
                customThreadPool.shutdownNow();
            }
        } catch (InterruptedException e) {
            customThreadPool.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}