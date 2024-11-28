package com.transaction.project.simulator.app.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class MessageFieldTypeCriteriaTest {

    @Test
    void newMessageFieldTypeCriteriaHasAllFiltersNullTest() {
        var messageFieldTypeCriteria = new MessageFieldTypeCriteria();
        assertThat(messageFieldTypeCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void messageFieldTypeCriteriaFluentMethodsCreatesFiltersTest() {
        var messageFieldTypeCriteria = new MessageFieldTypeCriteria();

        setAllFilters(messageFieldTypeCriteria);

        assertThat(messageFieldTypeCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void messageFieldTypeCriteriaCopyCreatesNullFilterTest() {
        var messageFieldTypeCriteria = new MessageFieldTypeCriteria();
        var copy = messageFieldTypeCriteria.copy();

        assertThat(messageFieldTypeCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(messageFieldTypeCriteria)
        );
    }

    @Test
    void messageFieldTypeCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var messageFieldTypeCriteria = new MessageFieldTypeCriteria();
        setAllFilters(messageFieldTypeCriteria);

        var copy = messageFieldTypeCriteria.copy();

        assertThat(messageFieldTypeCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(messageFieldTypeCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var messageFieldTypeCriteria = new MessageFieldTypeCriteria();

        assertThat(messageFieldTypeCriteria).hasToString("MessageFieldTypeCriteria{}");
    }

    private static void setAllFilters(MessageFieldTypeCriteria messageFieldTypeCriteria) {
        messageFieldTypeCriteria.id();
        messageFieldTypeCriteria.name();
        messageFieldTypeCriteria.messageFieldsConfigId();
        messageFieldTypeCriteria.fieldTypeId();
        messageFieldTypeCriteria.distinct();
    }

    private static Condition<MessageFieldTypeCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getMessageFieldsConfigId()) &&
                condition.apply(criteria.getFieldTypeId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<MessageFieldTypeCriteria> copyFiltersAre(
        MessageFieldTypeCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getMessageFieldsConfigId(), copy.getMessageFieldsConfigId()) &&
                condition.apply(criteria.getFieldTypeId(), copy.getFieldTypeId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
