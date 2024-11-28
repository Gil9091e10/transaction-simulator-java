package com.transaction.project.simulator.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class MessageIsoConfigTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static MessageIsoConfig getMessageIsoConfigSample1() {
        return new MessageIsoConfig().id(1L).name("name1").filename("filename1");
    }

    public static MessageIsoConfig getMessageIsoConfigSample2() {
        return new MessageIsoConfig().id(2L).name("name2").filename("filename2");
    }

    public static MessageIsoConfig getMessageIsoConfigRandomSampleGenerator() {
        return new MessageIsoConfig()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .filename(UUID.randomUUID().toString());
    }
}
