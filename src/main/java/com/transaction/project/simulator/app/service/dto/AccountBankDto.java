package com.transaction.project.simulator.app.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the {@link com.transaction.project.simulator.app.domain.AccountBank} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AccountBankDto implements Serializable {

    private Long id;

    @NotNull
    private Long number;

    @NotNull
    @Size(max = 20)
    private String numberIBAN;

    @NotNull
    private BigDecimal balance;

    private CurrencyDto currency;

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

    public String getNumberIBAN() {
        return numberIBAN;
    }

    public void setNumberIBAN(String numberIBAN) {
        this.numberIBAN = numberIBAN;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public CurrencyDto getCurrency() {
        return currency;
    }

    public void setCurrency(CurrencyDto currency) {
        this.currency = currency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AccountBankDto)) {
            return false;
        }

        AccountBankDto accountBankDto = (AccountBankDto) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, accountBankDto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AccountBankDto{" +
            "id=" + getId() +
            ", number=" + getNumber() +
            ", numberIBAN='" + getNumberIBAN() + "'" +
            ", balance=" + getBalance() +
            ", currency=" + getCurrency() +
            "}";
    }
}
