package com.transaction.project.simulator.app.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class AdviceCriteriaTest {

    @Test
    void newAdviceCriteriaHasAllFiltersNullTest() {
        var adviceCriteria = new AdviceCriteria();
        assertThat(adviceCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void adviceCriteriaFluentMethodsCreatesFiltersTest() {
        var adviceCriteria = new AdviceCriteria();

        setAllFilters(adviceCriteria);

        assertThat(adviceCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void adviceCriteriaCopyCreatesNullFilterTest() {
        var adviceCriteria = new AdviceCriteria();
        var copy = adviceCriteria.copy();

        assertThat(adviceCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(adviceCriteria)
        );
    }

    @Test
    void adviceCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var adviceCriteria = new AdviceCriteria();
        setAllFilters(adviceCriteria);

        var copy = adviceCriteria.copy();

        assertThat(adviceCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(adviceCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var adviceCriteria = new AdviceCriteria();

        assertThat(adviceCriteria).hasToString("AdviceCriteria{}");
    }

    private static void setAllFilters(AdviceCriteria adviceCriteria) {
        adviceCriteria.id();
        adviceCriteria.code();
        adviceCriteria.name();
        adviceCriteria.merchantId();
        adviceCriteria.acquirerId();
        adviceCriteria.distinct();
    }

    private static Condition<AdviceCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getCode()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getMerchantId()) &&
                condition.apply(criteria.getAcquirerId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<AdviceCriteria> copyFiltersAre(AdviceCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getCode(), copy.getCode()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getMerchantId(), copy.getMerchantId()) &&
                condition.apply(criteria.getAcquirerId(), copy.getAcquirerId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
