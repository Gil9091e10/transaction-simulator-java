package com.transaction.project.simulator.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class MerchantTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Merchant getMerchantSample1() {
        return new Merchant().id(1L).name("name1").mcc("mcc1").postalCode(1).website("website1");
    }

    public static Merchant getMerchantSample2() {
        return new Merchant().id(2L).name("name2").mcc("mcc2").postalCode(2).website("website2");
    }

    public static Merchant getMerchantRandomSampleGenerator() {
        return new Merchant()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .mcc(UUID.randomUUID().toString())
            .postalCode(intCount.incrementAndGet())
            .website(UUID.randomUUID().toString());
    }
}
