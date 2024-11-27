package com.transaction.project.simulator.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class MessageTypeIndicatorTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static MessageTypeIndicator getMessageTypeIndicatorSample1() {
        return new MessageTypeIndicator().id(1L).name("name1").code("code1");
    }

    public static MessageTypeIndicator getMessageTypeIndicatorSample2() {
        return new MessageTypeIndicator().id(2L).name("name2").code("code2");
    }

    public static MessageTypeIndicator getMessageTypeIndicatorRandomSampleGenerator() {
        return new MessageTypeIndicator()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .code(UUID.randomUUID().toString());
    }
}
