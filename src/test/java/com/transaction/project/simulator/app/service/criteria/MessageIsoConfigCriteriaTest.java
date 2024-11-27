package com.transaction.project.simulator.app.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class MessageIsoConfigCriteriaTest {

    @Test
    void newMessageIsoConfigCriteriaHasAllFiltersNullTest() {
        var messageIsoConfigCriteria = new MessageIsoConfigCriteria();
        assertThat(messageIsoConfigCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void messageIsoConfigCriteriaFluentMethodsCreatesFiltersTest() {
        var messageIsoConfigCriteria = new MessageIsoConfigCriteria();

        setAllFilters(messageIsoConfigCriteria);

        assertThat(messageIsoConfigCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void messageIsoConfigCriteriaCopyCreatesNullFilterTest() {
        var messageIsoConfigCriteria = new MessageIsoConfigCriteria();
        var copy = messageIsoConfigCriteria.copy();

        assertThat(messageIsoConfigCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(messageIsoConfigCriteria)
        );
    }

    @Test
    void messageIsoConfigCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var messageIsoConfigCriteria = new MessageIsoConfigCriteria();
        setAllFilters(messageIsoConfigCriteria);

        var copy = messageIsoConfigCriteria.copy();

        assertThat(messageIsoConfigCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(messageIsoConfigCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var messageIsoConfigCriteria = new MessageIsoConfigCriteria();

        assertThat(messageIsoConfigCriteria).hasToString("MessageIsoConfigCriteria{}");
    }

    private static void setAllFilters(MessageIsoConfigCriteria messageIsoConfigCriteria) {
        messageIsoConfigCriteria.id();
        messageIsoConfigCriteria.name();
        messageIsoConfigCriteria.filename();
        messageIsoConfigCriteria.acquirerId();
        messageIsoConfigCriteria.messageTypeIndicatorId();
        messageIsoConfigCriteria.distinct();
    }

    private static Condition<MessageIsoConfigCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getFilename()) &&
                condition.apply(criteria.getAcquirerId()) &&
                condition.apply(criteria.getMessageTypeIndicatorId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<MessageIsoConfigCriteria> copyFiltersAre(
        MessageIsoConfigCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getFilename(), copy.getFilename()) &&
                condition.apply(criteria.getAcquirerId(), copy.getAcquirerId()) &&
                condition.apply(criteria.getMessageTypeIndicatorId(), copy.getMessageTypeIndicatorId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
