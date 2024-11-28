package com.transaction.project.simulator.app.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class CreditCardTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static CreditCard getCreditCardSample1() {
        return new CreditCard().id(1L);
    }

    public static CreditCard getCreditCardSample2() {
        return new CreditCard().id(2L);
    }

    public static CreditCard getCreditCardRandomSampleGenerator() {
        return new CreditCard().id(longCount.incrementAndGet());
    }
}
