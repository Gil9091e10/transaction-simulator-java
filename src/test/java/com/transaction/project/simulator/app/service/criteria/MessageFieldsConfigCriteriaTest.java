package com.transaction.project.simulator.app.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class MessageFieldsConfigCriteriaTest {

    @Test
    void newMessageFieldsConfigCriteriaHasAllFiltersNullTest() {
        var messageFieldsConfigCriteria = new MessageFieldsConfigCriteria();
        assertThat(messageFieldsConfigCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void messageFieldsConfigCriteriaFluentMethodsCreatesFiltersTest() {
        var messageFieldsConfigCriteria = new MessageFieldsConfigCriteria();

        setAllFilters(messageFieldsConfigCriteria);

        assertThat(messageFieldsConfigCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void messageFieldsConfigCriteriaCopyCreatesNullFilterTest() {
        var messageFieldsConfigCriteria = new MessageFieldsConfigCriteria();
        var copy = messageFieldsConfigCriteria.copy();

        assertThat(messageFieldsConfigCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(messageFieldsConfigCriteria)
        );
    }

    @Test
    void messageFieldsConfigCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var messageFieldsConfigCriteria = new MessageFieldsConfigCriteria();
        setAllFilters(messageFieldsConfigCriteria);

        var copy = messageFieldsConfigCriteria.copy();

        assertThat(messageFieldsConfigCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(messageFieldsConfigCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var messageFieldsConfigCriteria = new MessageFieldsConfigCriteria();

        assertThat(messageFieldsConfigCriteria).hasToString("MessageFieldsConfigCriteria{}");
    }

    private static void setAllFilters(MessageFieldsConfigCriteria messageFieldsConfigCriteria) {
        messageFieldsConfigCriteria.id();
        messageFieldsConfigCriteria.name();
        messageFieldsConfigCriteria.fieldLength();
        messageFieldsConfigCriteria.messageIsoConfigId();
        messageFieldsConfigCriteria.messageFieldTypeId();
        messageFieldsConfigCriteria.distinct();
    }

    private static Condition<MessageFieldsConfigCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getFieldLength()) &&
                condition.apply(criteria.getMessageIsoConfigId()) &&
                condition.apply(criteria.getMessageFieldTypeId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<MessageFieldsConfigCriteria> copyFiltersAre(
        MessageFieldsConfigCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getFieldLength(), copy.getFieldLength()) &&
                condition.apply(criteria.getMessageIsoConfigId(), copy.getMessageIsoConfigId()) &&
                condition.apply(criteria.getMessageFieldTypeId(), copy.getMessageFieldTypeId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
