package com.avmathman.snowflake;

import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class SnowflakeIdGeneratorPerformanceTest {
    @Test
    public void generate_elapsedTimeWhenCold_displayInNanoSeconds() {
        SnowflakeIdGenerator generator = new SnowflakeIdGenerator(1);

        long start = System.nanoTime();
        generator.generate();
        long end = System.nanoTime();
        long cost = (end - start);

        assertTrue(cost < 100_000, String.format("Elapsed time is greater than %d", cost));
    }

    @Test
    public void generate_oneMillionIds_shouldGenerateMoreThan4000IdsInOneMillisecond() {
        SnowflakeIdGenerator generator = new SnowflakeIdGenerator(1);
        int iterations = 1_000_000;
        long start = System.currentTimeMillis();
        for (int i = 0; i < iterations; i++) {
            generator.generate();
        }
        long end = System.currentTimeMillis();

        long cost = (end - start);
        long countPerMillisecond = iterations/cost;

        assertTrue(
                countPerMillisecond > 4_000,
                String.format("Generated more than 4000 ids per one milliseond %d", countPerMillisecond)
        );
    }

    @Test
    public void nextId_withMultipleThreads() throws InterruptedException {
        SnowflakeIdGenerator generator = new SnowflakeIdGenerator(3);

        int iterations = 1_000_000;
        int numThreads = 50;

        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);
        CountDownLatch latch = new CountDownLatch(numThreads);

        long start = System.currentTimeMillis();
        for(int i = 0; i < iterations; i++) {
            executorService.submit(() -> {
                generator.generate();
                latch.countDown();
            });
        }
        latch.await();
        long end = System.currentTimeMillis();

        long cost = (end - start);
        long countPerMillisecond = iterations/cost;

        assertTrue(
                countPerMillisecond > 4_096,
                String.format("Generated more than 4096 ids per one milliseond: %d", countPerMillisecond)
        );
    }
}
