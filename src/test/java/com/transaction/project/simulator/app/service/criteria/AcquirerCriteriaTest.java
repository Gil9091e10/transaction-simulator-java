package com.transaction.project.simulator.app.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class AcquirerCriteriaTest {

    @Test
    void newAcquirerCriteriaHasAllFiltersNullTest() {
        var acquirerCriteria = new AcquirerCriteria();
        assertThat(acquirerCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void acquirerCriteriaFluentMethodsCreatesFiltersTest() {
        var acquirerCriteria = new AcquirerCriteria();

        setAllFilters(acquirerCriteria);

        assertThat(acquirerCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void acquirerCriteriaCopyCreatesNullFilterTest() {
        var acquirerCriteria = new AcquirerCriteria();
        var copy = acquirerCriteria.copy();

        assertThat(acquirerCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(acquirerCriteria)
        );
    }

    @Test
    void acquirerCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var acquirerCriteria = new AcquirerCriteria();
        setAllFilters(acquirerCriteria);

        var copy = acquirerCriteria.copy();

        assertThat(acquirerCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(acquirerCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var acquirerCriteria = new AcquirerCriteria();

        assertThat(acquirerCriteria).hasToString("AcquirerCriteria{}");
    }

    private static void setAllFilters(AcquirerCriteria acquirerCriteria) {
        acquirerCriteria.id();
        acquirerCriteria.name();
        acquirerCriteria.socketUrl();
        acquirerCriteria.email();
        acquirerCriteria.adviceId();
        acquirerCriteria.messageIsoConfigId();
        acquirerCriteria.distinct();
    }

    private static Condition<AcquirerCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getSocketUrl()) &&
                condition.apply(criteria.getEmail()) &&
                condition.apply(criteria.getAdviceId()) &&
                condition.apply(criteria.getMessageIsoConfigId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<AcquirerCriteria> copyFiltersAre(AcquirerCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getSocketUrl(), copy.getSocketUrl()) &&
                condition.apply(criteria.getEmail(), copy.getEmail()) &&
                condition.apply(criteria.getAdviceId(), copy.getAdviceId()) &&
                condition.apply(criteria.getMessageIsoConfigId(), copy.getMessageIsoConfigId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
