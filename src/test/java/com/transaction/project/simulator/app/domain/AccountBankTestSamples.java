package com.transaction.project.simulator.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AccountBankTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static AccountBank getAccountBankSample1() {
        return new AccountBank().id(1L).number(1L).numberIBAN("numberIBAN1");
    }

    public static AccountBank getAccountBankSample2() {
        return new AccountBank().id(2L).number(2L).numberIBAN("numberIBAN2");
    }

    public static AccountBank getAccountBankRandomSampleGenerator() {
        return new AccountBank()
            .id(longCount.incrementAndGet())
            .number(longCount.incrementAndGet())
            .numberIBAN(UUID.randomUUID().toString());
    }
}
