package com.transaction.project.simulator.app.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.transaction.project.simulator.app.domain.Card} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CardDto implements Serializable {

    private Long id;

    @NotNull
    private Long number;

    private LocalDate expirationDate;

    private Integer verificationValue;

    private AccountBankDto accountBank;

    private CardTypeDto cardType;

    private IssuerDto issuer;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Integer getVerificationValue() {
        return verificationValue;
    }

    public void setVerificationValue(Integer verificationValue) {
        this.verificationValue = verificationValue;
    }

    public AccountBankDto getAccountBank() {
        return accountBank;
    }

    public void setAccountBank(AccountBankDto accountBank) {
        this.accountBank = accountBank;
    }

    public CardTypeDto getCardType() {
        return cardType;
    }

    public void setCardType(CardTypeDto cardType) {
        this.cardType = cardType;
    }

    public IssuerDto getIssuer() {
        return issuer;
    }

    public void setIssuer(IssuerDto issuer) {
        this.issuer = issuer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CardDto)) {
            return false;
        }

        CardDto cardDto = (CardDto) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, cardDto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CardDto{" +
            "id=" + getId() +
            ", number=" + getNumber() +
            ", expirationDate='" + getExpirationDate() + "'" +
            ", verificationValue=" + getVerificationValue() +
            ", accountBank=" + getAccountBank() +
            ", cardType=" + getCardType() +
            ", issuer=" + getIssuer() +
            "}";
    }
}
