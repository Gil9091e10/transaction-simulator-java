package com.transaction.project.simulator.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AcquirerTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Acquirer getAcquirerSample1() {
        return new Acquirer().id(1L).name("name1").socketUrl("socketUrl1").email("email1");
    }

    public static Acquirer getAcquirerSample2() {
        return new Acquirer().id(2L).name("name2").socketUrl("socketUrl2").email("email2");
    }

    public static Acquirer getAcquirerRandomSampleGenerator() {
        return new Acquirer()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .socketUrl(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString());
    }
}
