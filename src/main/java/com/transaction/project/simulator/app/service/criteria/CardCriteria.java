package com.transaction.project.simulator.app.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.transaction.project.simulator.app.domain.Card} entity. This class is used
 * in {@link com.transaction.project.simulator.app.web.rest.CardResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /cards?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CardCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter number;

    private LocalDateFilter expirationDate;

    private IntegerFilter verificationValue;

    private LongFilter accountBankId;

    private LongFilter cardTypeId;

    private LongFilter issuerId;

    private Boolean distinct;

    public CardCriteria() {}

    public CardCriteria(CardCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.number = other.optionalNumber().map(LongFilter::copy).orElse(null);
        this.expirationDate = other.optionalExpirationDate().map(LocalDateFilter::copy).orElse(null);
        this.verificationValue = other.optionalVerificationValue().map(IntegerFilter::copy).orElse(null);
        this.accountBankId = other.optionalAccountBankId().map(LongFilter::copy).orElse(null);
        this.cardTypeId = other.optionalCardTypeId().map(LongFilter::copy).orElse(null);
        this.issuerId = other.optionalIssuerId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public CardCriteria copy() {
        return new CardCriteria(this);
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

    public LocalDateFilter getExpirationDate() {
        return expirationDate;
    }

    public Optional<LocalDateFilter> optionalExpirationDate() {
        return Optional.ofNullable(expirationDate);
    }

    public LocalDateFilter expirationDate() {
        if (expirationDate == null) {
            setExpirationDate(new LocalDateFilter());
        }
        return expirationDate;
    }

    public void setExpirationDate(LocalDateFilter expirationDate) {
        this.expirationDate = expirationDate;
    }

    public IntegerFilter getVerificationValue() {
        return verificationValue;
    }

    public Optional<IntegerFilter> optionalVerificationValue() {
        return Optional.ofNullable(verificationValue);
    }

    public IntegerFilter verificationValue() {
        if (verificationValue == null) {
            setVerificationValue(new IntegerFilter());
        }
        return verificationValue;
    }

    public void setVerificationValue(IntegerFilter verificationValue) {
        this.verificationValue = verificationValue;
    }

    public LongFilter getAccountBankId() {
        return accountBankId;
    }

    public Optional<LongFilter> optionalAccountBankId() {
        return Optional.ofNullable(accountBankId);
    }

    public LongFilter accountBankId() {
        if (accountBankId == null) {
            setAccountBankId(new LongFilter());
        }
        return accountBankId;
    }

    public void setAccountBankId(LongFilter accountBankId) {
        this.accountBankId = accountBankId;
    }

    public LongFilter getCardTypeId() {
        return cardTypeId;
    }

    public Optional<LongFilter> optionalCardTypeId() {
        return Optional.ofNullable(cardTypeId);
    }

    public LongFilter cardTypeId() {
        if (cardTypeId == null) {
            setCardTypeId(new LongFilter());
        }
        return cardTypeId;
    }

    public void setCardTypeId(LongFilter cardTypeId) {
        this.cardTypeId = cardTypeId;
    }

    public LongFilter getIssuerId() {
        return issuerId;
    }

    public Optional<LongFilter> optionalIssuerId() {
        return Optional.ofNullable(issuerId);
    }

    public LongFilter issuerId() {
        if (issuerId == null) {
            setIssuerId(new LongFilter());
        }
        return issuerId;
    }

    public void setIssuerId(LongFilter issuerId) {
        this.issuerId = issuerId;
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
        final CardCriteria that = (CardCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(number, that.number) &&
            Objects.equals(expirationDate, that.expirationDate) &&
            Objects.equals(verificationValue, that.verificationValue) &&
            Objects.equals(accountBankId, that.accountBankId) &&
            Objects.equals(cardTypeId, that.cardTypeId) &&
            Objects.equals(issuerId, that.issuerId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, number, expirationDate, verificationValue, accountBankId, cardTypeId, issuerId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CardCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalNumber().map(f -> "number=" + f + ", ").orElse("") +
            optionalExpirationDate().map(f -> "expirationDate=" + f + ", ").orElse("") +
            optionalVerificationValue().map(f -> "verificationValue=" + f + ", ").orElse("") +
            optionalAccountBankId().map(f -> "accountBankId=" + f + ", ").orElse("") +
            optionalCardTypeId().map(f -> "cardTypeId=" + f + ", ").orElse("") +
            optionalIssuerId().map(f -> "issuerId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
