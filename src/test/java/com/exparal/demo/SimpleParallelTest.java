package com.exparal.demo;

import com.exparal.demo.model.Portfolio;
import com.exparal.demo.service.FinancialCalculationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SpringBootTest
public class SimpleParallelTest {

    @Test
    @DisplayName("Simple test to validate working of parallel streams on machine")
    void simpleCpuTest() {
        List<Integer> numbers = IntStream.range(0, 10000).boxed().collect(Collectors.toList());

        // Sequential
        long start = System.nanoTime();
        long seqResult = numbers.stream()
                .mapToLong(this::expensiveCalculation)
                .sum();
        long seqTime = (System.nanoTime() - start) / 1_000_000;

        // Parallel
        start = System.nanoTime();
        long parResult = numbers.parallelStream()
                .mapToLong(this::expensiveCalculation)
                .sum();
        long parTime = (System.nanoTime() - start) / 1_000_000;

        System.out.printf("Sequential: %d ms, Parallel: %d ms, Speedup: %.2fx%n",
                seqTime, parTime, (double) seqTime / parTime);
    }

    private long expensiveCalculation(int n) {
        long result = 0;
        for (int i = 0; i < 100000; i++) {
            result += Math.sqrt(n * i) + Math.sin(n * i);
        }
        return result;
    }
}