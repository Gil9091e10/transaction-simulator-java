package com.transaction.project.simulator.app.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.transaction.project.simulator.app.domain.AccountBank} entity. This class is used
 * in {@link com.transaction.project.simulator.app.web.rest.AccountBankResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /account-banks?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AccountBankCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter number;

    private StringFilter numberIBAN;

    private BigDecimalFilter balance;

    private LongFilter cardId;

    private LongFilter currencyId;

    private Boolean distinct;

    public AccountBankCriteria() {}

    public AccountBankCriteria(AccountBankCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.number = other.optionalNumber().map(LongFilter::copy).orElse(null);
        this.numberIBAN = other.optionalNumberIBAN().map(StringFilter::copy).orElse(null);
        this.balance = other.optionalBalance().map(BigDecimalFilter::copy).orElse(null);
        this.cardId = other.optionalCardId().map(LongFilter::copy).orElse(null);
        this.currencyId = other.optionalCurrencyId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public AccountBankCriteria copy() {
        return new AccountBankCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LongFilter getNumber() {
        return number;
    }

    public Optional<LongFilter> optionalNumber() {
        return Optional.ofNullable(number);
    }

    public LongFilter number() {
        if (number == null) {
            setNumber(new LongFilter());
        }
        return number;
    }

    public void setNumber(LongFilter number) {
        this.number = number;
    }

    public StringFilter getNumberIBAN() {
        return numberIBAN;
    }

    public Optional<StringFilter> optionalNumberIBAN() {
        return Optional.ofNullable(numberIBAN);
    }

    public StringFilter numberIBAN() {
        if (numberIBAN == null) {
            setNumberIBAN(new StringFilter());
        }
        return numberIBAN;
    }

    public void setNumberIBAN(StringFilter numberIBAN) {
        this.numberIBAN = numberIBAN;
    }

    public BigDecimalFilter getBalance() {
        return balance;
    }

    public Optional<BigDecimalFilter> optionalBalance() {
        return Optional.ofNullable(balance);
    }

    public BigDecimalFilter balance() {
        if (balance == null) {
            setBalance(new BigDecimalFilter());
        }
        return balance;
    }

    public void setBalance(BigDecimalFilter balance) {
        this.balance = balance;
    }

    public LongFilter getCardId() {
        return cardId;
    }

    public Optional<LongFilter> optionalCardId() {
        return Optional.ofNullable(cardId);
    }

    public LongFilter cardId() {
        if (cardId == null) {
            setCardId(new LongFilter());
        }
        return cardId;
    }

    public void setCardId(LongFilter cardId) {
        this.cardId = cardId;
    }

    public LongFilter getCurrencyId() {
        return currencyId;
    }

    public Optional<LongFilter> optionalCurrencyId() {
        return Optional.ofNullable(currencyId);
    }

    public LongFilter currencyId() {
        if (currencyId == null) {
            setCurrencyId(new LongFilter());
        }
        return currencyId;
    }

    public void setCurrencyId(LongFilter currencyId) {
        this.currencyId = currencyId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final AccountBankCriteria that = (AccountBankCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(number, that.number) &&
            Objects.equals(numberIBAN, that.numberIBAN) &&
            Objects.equals(balance, that.balance) &&
            Objects.equals(cardId, that.cardId) &&
            Objects.equals(currencyId, that.currencyId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, number, numberIBAN, balance, cardId, currencyId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AccountBankCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalNumber().map(f -> "number=" + f + ", ").orElse("") +
            optionalNumberIBAN().map(f -> "numberIBAN=" + f + ", ").orElse("") +
            optionalBalance().map(f -> "balance=" + f + ", ").orElse("") +
            optionalCardId().map(f -> "cardId=" + f + ", ").orElse("") +
            optionalCurrencyId().map(f -> "currencyId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
