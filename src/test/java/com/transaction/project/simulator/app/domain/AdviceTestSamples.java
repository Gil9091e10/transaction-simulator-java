package com.transaction.project.simulator.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AdviceTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Advice getAdviceSample1() {
        return new Advice().id(1L).code("code1").name("name1");
    }

    public static Advice getAdviceSample2() {
        return new Advice().id(2L).code("code2").name("name2");
    }

    public static Advice getAdviceRandomSampleGenerator() {
        return new Advice().id(longCount.incrementAndGet()).code(UUID.randomUUID().toString()).name(UUID.randomUUID().toString());
    }
}
