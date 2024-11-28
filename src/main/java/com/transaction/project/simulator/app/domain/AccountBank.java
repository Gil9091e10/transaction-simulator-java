package com.transaction.project.simulator.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AccountBank.
 */
@Entity
@Table(name = "account_bank")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AccountBank implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "number", nullable = false)
    private Long number;

    @NotNull
    @Size(max = 20)
    @Column(name = "number_iban", length = 20, nullable = false)
    private String numberIBAN;

    @NotNull
    @Column(name = "balance", precision = 21, scale = 2, nullable = false)
    private BigDecimal balance;

    @JsonIgnoreProperties(value = { "accountBank", "cardType", "issuer" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "accountBank")
    private Card card;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "accountBanks" }, allowSetters = true)
    private Currency currency;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AccountBank id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNumber() {
        return this.number;
    }

    public AccountBank number(Long number) {
        this.setNumber(number);
        return this;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public String getNumberIBAN() {
        return this.numberIBAN;
    }

    public AccountBank numberIBAN(String numberIBAN) {
        this.setNumberIBAN(numberIBAN);
        return this;
    }

    public void setNumberIBAN(String numberIBAN) {
        this.numberIBAN = numberIBAN;
    }

    public BigDecimal getBalance() {
        return this.balance;
    }

    public AccountBank balance(BigDecimal balance) {
        this.setBalance(balance);
        return this;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Card getCard() {
        return this.card;
    }

    public void setCard(Card card) {
        if (this.card != null) {
            this.card.setAccountBank(null);
        }
        if (card != null) {
            card.setAccountBank(this);
        }
        this.card = card;
    }

    public AccountBank card(Card card) {
        this.setCard(card);
        return this;
    }

    public Currency getCurrency() {
        return this.currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public AccountBank currency(Currency currency) {
        this.setCurrency(currency);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AccountBank)) {
            return false;
        }
        return getId() != null && getId().equals(((AccountBank) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AccountBank{" +
            "id=" + getId() +
            ", number=" + getNumber() +
            ", numberIBAN='" + getNumberIBAN() + "'" +
            ", balance=" + getBalance() +
            "}";
    }
}
