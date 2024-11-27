package com.transaction.project.simulator.app.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class IssuerCriteriaTest {

    @Test
    void newIssuerCriteriaHasAllFiltersNullTest() {
        var issuerCriteria = new IssuerCriteria();
        assertThat(issuerCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void issuerCriteriaFluentMethodsCreatesFiltersTest() {
        var issuerCriteria = new IssuerCriteria();

        setAllFilters(issuerCriteria);

        assertThat(issuerCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void issuerCriteriaCopyCreatesNullFilterTest() {
        var issuerCriteria = new IssuerCriteria();
        var copy = issuerCriteria.copy();

        assertThat(issuerCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(issuerCriteria)
        );
    }

    @Test
    void issuerCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var issuerCriteria = new IssuerCriteria();
        setAllFilters(issuerCriteria);

        var copy = issuerCriteria.copy();

        assertThat(issuerCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(issuerCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var issuerCriteria = new IssuerCriteria();

        assertThat(issuerCriteria).hasToString("IssuerCriteria{}");
    }

    private static void setAllFilters(IssuerCriteria issuerCriteria) {
        issuerCriteria.id();
        issuerCriteria.code();
        issuerCriteria.name();
        issuerCriteria.cardId();
        issuerCriteria.distinct();
    }

    private static Condition<IssuerCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getCode()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getCardId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<IssuerCriteria> copyFiltersAre(IssuerCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getCode(), copy.getCode()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getCardId(), copy.getCardId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
