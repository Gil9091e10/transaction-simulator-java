package com.transaction.project.simulator.app.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class MessageTypeIndicatorCriteriaTest {

    @Test
    void newMessageTypeIndicatorCriteriaHasAllFiltersNullTest() {
        var messageTypeIndicatorCriteria = new MessageTypeIndicatorCriteria();
        assertThat(messageTypeIndicatorCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void messageTypeIndicatorCriteriaFluentMethodsCreatesFiltersTest() {
        var messageTypeIndicatorCriteria = new MessageTypeIndicatorCriteria();

        setAllFilters(messageTypeIndicatorCriteria);

        assertThat(messageTypeIndicatorCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void messageTypeIndicatorCriteriaCopyCreatesNullFilterTest() {
        var messageTypeIndicatorCriteria = new MessageTypeIndicatorCriteria();
        var copy = messageTypeIndicatorCriteria.copy();

        assertThat(messageTypeIndicatorCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(messageTypeIndicatorCriteria)
        );
    }

    @Test
    void messageTypeIndicatorCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var messageTypeIndicatorCriteria = new MessageTypeIndicatorCriteria();
        setAllFilters(messageTypeIndicatorCriteria);

        var copy = messageTypeIndicatorCriteria.copy();

        assertThat(messageTypeIndicatorCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(messageTypeIndicatorCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var messageTypeIndicatorCriteria = new MessageTypeIndicatorCriteria();

        assertThat(messageTypeIndicatorCriteria).hasToString("MessageTypeIndicatorCriteria{}");
    }

    private static void setAllFilters(MessageTypeIndicatorCriteria messageTypeIndicatorCriteria) {
        messageTypeIndicatorCriteria.id();
        messageTypeIndicatorCriteria.name();
        messageTypeIndicatorCriteria.code();
        messageTypeIndicatorCriteria.messageIsoConfigId();
        messageTypeIndicatorCriteria.distinct();
    }

    private static Condition<MessageTypeIndicatorCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getCode()) &&
                condition.apply(criteria.getMessageIsoConfigId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<MessageTypeIndicatorCriteria> copyFiltersAre(
        MessageTypeIndicatorCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getCode(), copy.getCode()) &&
                condition.apply(criteria.getMessageIsoConfigId(), copy.getMessageIsoConfigId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
