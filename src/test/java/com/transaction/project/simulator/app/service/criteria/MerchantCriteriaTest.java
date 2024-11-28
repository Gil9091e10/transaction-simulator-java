package com.transaction.project.simulator.app.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class MerchantCriteriaTest {

    @Test
    void newMerchantCriteriaHasAllFiltersNullTest() {
        var merchantCriteria = new MerchantCriteria();
        assertThat(merchantCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void merchantCriteriaFluentMethodsCreatesFiltersTest() {
        var merchantCriteria = new MerchantCriteria();

        setAllFilters(merchantCriteria);

        assertThat(merchantCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void merchantCriteriaCopyCreatesNullFilterTest() {
        var merchantCriteria = new MerchantCriteria();
        var copy = merchantCriteria.copy();

        assertThat(merchantCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(merchantCriteria)
        );
    }

    @Test
    void merchantCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var merchantCriteria = new MerchantCriteria();
        setAllFilters(merchantCriteria);

        var copy = merchantCriteria.copy();

        assertThat(merchantCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(merchantCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var merchantCriteria = new MerchantCriteria();

        assertThat(merchantCriteria).hasToString("MerchantCriteria{}");
    }

    private static void setAllFilters(MerchantCriteria merchantCriteria) {
        merchantCriteria.id();
        merchantCriteria.name();
        merchantCriteria.mcc();
        merchantCriteria.postalCode();
        merchantCriteria.website();
        merchantCriteria.adviceId();
        merchantCriteria.distinct();
    }

    private static Condition<MerchantCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getMcc()) &&
                condition.apply(criteria.getPostalCode()) &&
                condition.apply(criteria.getWebsite()) &&
                condition.apply(criteria.getAdviceId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<MerchantCriteria> copyFiltersAre(MerchantCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getMcc(), copy.getMcc()) &&
                condition.apply(criteria.getPostalCode(), copy.getPostalCode()) &&
                condition.apply(criteria.getWebsite(), copy.getWebsite()) &&
                condition.apply(criteria.getAdviceId(), copy.getAdviceId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
