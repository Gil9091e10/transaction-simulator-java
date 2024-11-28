package com.transaction.project.simulator.app.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class DebitCardTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static DebitCard getDebitCardSample1() {
        return new DebitCard().id(1L);
    }

    public static DebitCard getDebitCardSample2() {
        return new DebitCard().id(2L);
    }

    public static DebitCard getDebitCardRandomSampleGenerator() {
        return new DebitCard().id(longCount.incrementAndGet());
    }
}
