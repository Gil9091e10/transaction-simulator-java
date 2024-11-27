package com.transaction.project.simulator.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class IssuerTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Issuer getIssuerSample1() {
        return new Issuer().id(1L).code("code1").name("name1");
    }

    public static Issuer getIssuerSample2() {
        return new Issuer().id(2L).code("code2").name("name2");
    }

    public static Issuer getIssuerRandomSampleGenerator() {
        return new Issuer().id(longCount.incrementAndGet()).code(UUID.randomUUID().toString()).name(UUID.randomUUID().toString());
    }
}
