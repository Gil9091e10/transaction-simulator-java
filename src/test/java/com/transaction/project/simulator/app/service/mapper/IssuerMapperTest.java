package com.transaction.project.simulator.app.service.mapper;

import static com.transaction.project.simulator.app.domain.IssuerAsserts.*;
import static com.transaction.project.simulator.app.domain.IssuerTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class IssuerMapperTest {

    private IssuerMapper issuerMapper;

    @BeforeEach
    void setUp() {
        issuerMapper = new IssuerMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getIssuerSample1();
        var actual = issuerMapper.toEntity(issuerMapper.toDto(expected));
        assertIssuerAllPropertiesEquals(expected, actual);
    }
}
