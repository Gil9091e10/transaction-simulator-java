package com.transaction.project.simulator.app.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class DebitCardCriteriaTest {

    @Test
    void newDebitCardCriteriaHasAllFiltersNullTest() {
        var debitCardCriteria = new DebitCardCriteria();
        assertThat(debitCardCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void debitCardCriteriaFluentMethodsCreatesFiltersTest() {
        var debitCardCriteria = new DebitCardCriteria();

        setAllFilters(debitCardCriteria);

        assertThat(debitCardCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void debitCardCriteriaCopyCreatesNullFilterTest() {
        var debitCardCriteria = new DebitCardCriteria();
        var copy = debitCardCriteria.copy();

        assertThat(debitCardCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(debitCardCriteria)
        );
    }

    @Test
    void debitCardCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var debitCardCriteria = new DebitCardCriteria();
        setAllFilters(debitCardCriteria);

        var copy = debitCardCriteria.copy();

        assertThat(debitCardCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(debitCardCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var debitCardCriteria = new DebitCardCriteria();

        assertThat(debitCardCriteria).hasToString("DebitCardCriteria{}");
    }

    private static void setAllFilters(DebitCardCriteria debitCardCriteria) {
        debitCardCriteria.id();
        debitCardCriteria.distinct();
    }

    private static Condition<DebitCardCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria -> condition.apply(criteria.getId()) && condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<DebitCardCriteria> copyFiltersAre(DebitCardCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria -> condition.apply(criteria.getId(), copy.getId()) && condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
