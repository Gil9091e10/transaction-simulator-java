package com.transaction.project.simulator.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CardTypeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static CardType getCardTypeSample1() {
        return new CardType().id(1L).name("name1");
    }

    public static CardType getCardTypeSample2() {
        return new CardType().id(2L).name("name2");
    }

    public static CardType getCardTypeRandomSampleGenerator() {
        return new CardType().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString());
    }
}
