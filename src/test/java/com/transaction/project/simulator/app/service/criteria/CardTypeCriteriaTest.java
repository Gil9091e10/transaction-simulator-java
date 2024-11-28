package com.transaction.project.simulator.app.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class CardTypeCriteriaTest {

    @Test
    void newCardTypeCriteriaHasAllFiltersNullTest() {
        var cardTypeCriteria = new CardTypeCriteria();
        assertThat(cardTypeCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void cardTypeCriteriaFluentMethodsCreatesFiltersTest() {
        var cardTypeCriteria = new CardTypeCriteria();

        setAllFilters(cardTypeCriteria);

        assertThat(cardTypeCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void cardTypeCriteriaCopyCreatesNullFilterTest() {
        var cardTypeCriteria = new CardTypeCriteria();
        var copy = cardTypeCriteria.copy();

        assertThat(cardTypeCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(cardTypeCriteria)
        );
    }

    @Test
    void cardTypeCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var cardTypeCriteria = new CardTypeCriteria();
        setAllFilters(cardTypeCriteria);

        var copy = cardTypeCriteria.copy();

        assertThat(cardTypeCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(cardTypeCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var cardTypeCriteria = new CardTypeCriteria();

        assertThat(cardTypeCriteria).hasToString("CardTypeCriteria{}");
    }

    private static void setAllFilters(CardTypeCriteria cardTypeCriteria) {
        cardTypeCriteria.id();
        cardTypeCriteria.name();
        cardTypeCriteria.cardId();
        cardTypeCriteria.distinct();
    }

    private static Condition<CardTypeCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getCardId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<CardTypeCriteria> copyFiltersAre(CardTypeCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getCardId(), copy.getCardId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
