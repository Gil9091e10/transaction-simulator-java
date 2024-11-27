package com.transaction.project.simulator.app.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class AccountBankCriteriaTest {

    @Test
    void newAccountBankCriteriaHasAllFiltersNullTest() {
        var accountBankCriteria = new AccountBankCriteria();
        assertThat(accountBankCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void accountBankCriteriaFluentMethodsCreatesFiltersTest() {
        var accountBankCriteria = new AccountBankCriteria();

        setAllFilters(accountBankCriteria);

        assertThat(accountBankCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void accountBankCriteriaCopyCreatesNullFilterTest() {
        var accountBankCriteria = new AccountBankCriteria();
        var copy = accountBankCriteria.copy();

        assertThat(accountBankCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(accountBankCriteria)
        );
    }

    @Test
    void accountBankCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var accountBankCriteria = new AccountBankCriteria();
        setAllFilters(accountBankCriteria);

        var copy = accountBankCriteria.copy();

        assertThat(accountBankCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(accountBankCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var accountBankCriteria = new AccountBankCriteria();

        assertThat(accountBankCriteria).hasToString("AccountBankCriteria{}");
    }

    private static void setAllFilters(AccountBankCriteria accountBankCriteria) {
        accountBankCriteria.id();
        accountBankCriteria.number();
        accountBankCriteria.numberIBAN();
        accountBankCriteria.balance();
        accountBankCriteria.cardId();
        accountBankCriteria.currencyId();
        accountBankCriteria.distinct();
    }

    private static Condition<AccountBankCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getNumber()) &&
                condition.apply(criteria.getNumberIBAN()) &&
                condition.apply(criteria.getBalance()) &&
                condition.apply(criteria.getCardId()) &&
                condition.apply(criteria.getCurrencyId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<AccountBankCriteria> copyFiltersAre(AccountBankCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getNumber(), copy.getNumber()) &&
                condition.apply(criteria.getNumberIBAN(), copy.getNumberIBAN()) &&
                condition.apply(criteria.getBalance(), copy.getBalance()) &&
                condition.apply(criteria.getCardId(), copy.getCardId()) &&
                condition.apply(criteria.getCurrencyId(), copy.getCurrencyId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
