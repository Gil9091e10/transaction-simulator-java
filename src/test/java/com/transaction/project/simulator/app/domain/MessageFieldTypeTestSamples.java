package com.transaction.project.simulator.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class MessageFieldTypeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static MessageFieldType getMessageFieldTypeSample1() {
        return new MessageFieldType().id(1L).name("name1");
    }

    public static MessageFieldType getMessageFieldTypeSample2() {
        return new MessageFieldType().id(2L).name("name2");
    }

    public static MessageFieldType getMessageFieldTypeRandomSampleGenerator() {
        return new MessageFieldType().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString());
    }
}
