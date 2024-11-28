package com.transaction.project.simulator.app.service.mapper;

import static com.transaction.project.simulator.app.domain.MerchantAsserts.*;
import static com.transaction.project.simulator.app.domain.MerchantTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MerchantMapperTest {

    private MerchantMapper merchantMapper;

    @BeforeEach
    void setUp() {
        merchantMapper = new MerchantMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getMerchantSample1();
        var actual = merchantMapper.toEntity(merchantMapper.toDto(expected));
        assertMerchantAllPropertiesEquals(expected, actual);
    }
}
