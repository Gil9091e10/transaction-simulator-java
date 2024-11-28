package com.transaction.project.simulator.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class TransactionTypeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static TransactionType getTransactionTypeSample1() {
        return new TransactionType().id(1L).code("code1").name("name1");
    }

    public static TransactionType getTransactionTypeSample2() {
        return new TransactionType().id(2L).code("code2").name("name2");
    }

    public static TransactionType getTransactionTypeRandomSampleGenerator() {
        return new TransactionType().id(longCount.incrementAndGet()).code(UUID.randomUUID().toString()).name(UUID.randomUUID().toString());
    }
}
