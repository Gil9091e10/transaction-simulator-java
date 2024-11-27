package com.transaction.project.simulator.app.domain;

import static com.transaction.project.simulator.app.domain.AssertUtils.bigDecimalCompareTo;
import static org.assertj.core.api.Assertions.assertThat;

public class AccountBankAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAccountBankAllPropertiesEquals(AccountBank expected, AccountBank actual) {
        assertAccountBankAutoGeneratedPropertiesEquals(expected, actual);
        assertAccountBankAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAccountBankAllUpdatablePropertiesEquals(AccountBank expected, AccountBank actual) {
        assertAccountBankUpdatableFieldsEquals(expected, actual);
        assertAccountBankUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAccountBankAutoGeneratedPropertiesEquals(AccountBank expected, AccountBank actual) {
        assertThat(expected)
            .as("Verify AccountBank auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAccountBankUpdatableFieldsEquals(AccountBank expected, AccountBank actual) {
        assertThat(expected)
            .as("Verify AccountBank relevant properties")
            .satisfies(e -> assertThat(e.getNumber()).as("check number").isEqualTo(actual.getNumber()))
            .satisfies(e -> assertThat(e.getNumberIBAN()).as("check numberIBAN").isEqualTo(actual.getNumberIBAN()))
            .satisfies(e ->
                assertThat(e.getBalance()).as("check balance").usingComparator(bigDecimalCompareTo).isEqualTo(actual.getBalance())
            );
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAccountBankUpdatableRelationshipsEquals(AccountBank expected, AccountBank actual) {
        assertThat(expected)
            .as("Verify AccountBank relationships")
            .satisfies(e -> assertThat(e.getCurrency()).as("check currency").isEqualTo(actual.getCurrency()));
    }
}
