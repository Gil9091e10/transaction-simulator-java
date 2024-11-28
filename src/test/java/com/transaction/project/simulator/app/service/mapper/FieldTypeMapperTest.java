package com.transaction.project.simulator.app.service.mapper;

import static com.transaction.project.simulator.app.domain.FieldTypeAsserts.*;
import static com.transaction.project.simulator.app.domain.FieldTypeTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FieldTypeMapperTest {

    private FieldTypeMapper fieldTypeMapper;

    @BeforeEach
    void setUp() {
        fieldTypeMapper = new FieldTypeMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getFieldTypeSample1();
        var actual = fieldTypeMapper.toEntity(fieldTypeMapper.toDto(expected));
        assertFieldTypeAllPropertiesEquals(expected, actual);
    }
}
