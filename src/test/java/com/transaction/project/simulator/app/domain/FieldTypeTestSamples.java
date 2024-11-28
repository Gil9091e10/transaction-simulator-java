package com.transaction.project.simulator.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class FieldTypeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static FieldType getFieldTypeSample1() {
        return new FieldType().id(1L).name("name1");
    }

    public static FieldType getFieldTypeSample2() {
        return new FieldType().id(2L).name("name2");
    }

    public static FieldType getFieldTypeRandomSampleGenerator() {
        return new FieldType().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString());
    }
}
