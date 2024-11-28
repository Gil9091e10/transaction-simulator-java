package com.transaction.project.simulator.app.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class CreditCardCriteriaTest {

    @Test
    void newCreditCardCriteriaHasAllFiltersNullTest() {
        var creditCardCriteria = new CreditCardCriteria();
        assertThat(creditCardCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void creditCardCriteriaFluentMethodsCreatesFiltersTest() {
        var creditCardCriteria = new CreditCardCriteria();

        setAllFilters(creditCardCriteria);

        assertThat(creditCardCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void creditCardCriteriaCopyCreatesNullFilterTest() {
        var creditCardCriteria = new CreditCardCriteria();
        var copy = creditCardCriteria.copy();

        assertThat(creditCardCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(creditCardCriteria)
        );
    }

    @Test
    void creditCardCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var creditCardCriteria = new CreditCardCriteria();
        setAllFilters(creditCardCriteria);

        var copy = creditCardCriteria.copy();

        assertThat(creditCardCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(creditCardCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var creditCardCriteria = new CreditCardCriteria();

        assertThat(creditCardCriteria).hasToString("CreditCardCriteria{}");
    }

    private static void setAllFilters(CreditCardCriteria creditCardCriteria) {
        creditCardCriteria.id();
        creditCardCriteria.maxLimit();
        creditCardCriteria.distinct();
    }

    private static Condition<CreditCardCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) && condition.apply(criteria.getMaxLimit()) && condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<CreditCardCriteria> copyFiltersAre(CreditCardCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getMaxLimit(), copy.getMaxLimit()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
