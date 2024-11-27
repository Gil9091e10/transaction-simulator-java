package com.transaction.project.simulator.app.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class CardTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Card getCardSample1() {
        return new Card().id(1L).number(1L).verificationValue(1);
    }

    public static Card getCardSample2() {
        return new Card().id(2L).number(2L).verificationValue(2);
    }

    public static Card getCardRandomSampleGenerator() {
        return new Card().id(longCount.incrementAndGet()).number(longCount.incrementAndGet()).verificationValue(intCount.incrementAndGet());
    }
}
