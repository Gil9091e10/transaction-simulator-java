package com.transaction.project.simulator.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class MessageFieldsConfigTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static MessageFieldsConfig getMessageFieldsConfigSample1() {
        return new MessageFieldsConfig().id(1L).name("name1").fieldLength(1);
    }

    public static MessageFieldsConfig getMessageFieldsConfigSample2() {
        return new MessageFieldsConfig().id(2L).name("name2").fieldLength(2);
    }

    public static MessageFieldsConfig getMessageFieldsConfigRandomSampleGenerator() {
        return new MessageFieldsConfig()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .fieldLength(intCount.incrementAndGet());
    }
}
