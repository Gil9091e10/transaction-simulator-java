package com.transaction.project.simulator.app.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class FieldTypeCriteriaTest {

    @Test
    void newFieldTypeCriteriaHasAllFiltersNullTest() {
        var fieldTypeCriteria = new FieldTypeCriteria();
        assertThat(fieldTypeCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void fieldTypeCriteriaFluentMethodsCreatesFiltersTest() {
        var fieldTypeCriteria = new FieldTypeCriteria();

        setAllFilters(fieldTypeCriteria);

        assertThat(fieldTypeCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void fieldTypeCriteriaCopyCreatesNullFilterTest() {
        var fieldTypeCriteria = new FieldTypeCriteria();
        var copy = fieldTypeCriteria.copy();

        assertThat(fieldTypeCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(fieldTypeCriteria)
        );
    }

    @Test
    void fieldTypeCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var fieldTypeCriteria = new FieldTypeCriteria();
        setAllFilters(fieldTypeCriteria);

        var copy = fieldTypeCriteria.copy();

        assertThat(fieldTypeCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(fieldTypeCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var fieldTypeCriteria = new FieldTypeCriteria();

        assertThat(fieldTypeCriteria).hasToString("FieldTypeCriteria{}");
    }

    private static void setAllFilters(FieldTypeCriteria fieldTypeCriteria) {
        fieldTypeCriteria.id();
        fieldTypeCriteria.name();
        fieldTypeCriteria.messageFieldTypeId();
        fieldTypeCriteria.distinct();
    }

    private static Condition<FieldTypeCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getMessageFieldTypeId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<FieldTypeCriteria> copyFiltersAre(FieldTypeCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getMessageFieldTypeId(), copy.getMessageFieldTypeId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
