package com.transaction.project.simulator.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Card.
 */
@Entity
@Table(name = "card")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Card implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "number", nullable = false)
    private Long number;

    @Column(name = "expiration_date")
    private LocalDate expirationDate;

    @Column(name = "verification_value")
    private Integer verificationValue;

    @JsonIgnoreProperties(value = { "card", "currency" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private AccountBank accountBank;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "cards" }, allowSetters = true)
    private CardType cardType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "cards" }, allowSetters = true)
    private Issuer issuer;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Card id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNumber() {
        return this.number;
    }

    public Card number(Long number) {
        this.setNumber(number);
        return this;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public LocalDate getExpirationDate() {
        return this.expirationDate;
    }

    public Card expirationDate(LocalDate expirationDate) {
        this.setExpirationDate(expirationDate);
        return this;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Integer getVerificationValue() {
        return this.verificationValue;
    }

    public Card verificationValue(Integer verificationValue) {
        this.setVerificationValue(verificationValue);
        return this;
    }

    public void setVerificationValue(Integer verificationValue) {
        this.verificationValue = verificationValue;
    }

    public AccountBank getAccountBank() {
        return this.accountBank;
    }

    public void setAccountBank(AccountBank accountBank) {
        this.accountBank = accountBank;
    }

    public Card accountBank(AccountBank accountBank) {
        this.setAccountBank(accountBank);
        return this;
    }

    public CardType getCardType() {
        return this.cardType;
    }

    public void setCardType(CardType cardType) {
        this.cardType = cardType;
    }

    public Card cardType(CardType cardType) {
        this.setCardType(cardType);
        return this;
    }

    public Issuer getIssuer() {
        return this.issuer;
    }

    public void setIssuer(Issuer issuer) {
        this.issuer = issuer;
    }

    public Card issuer(Issuer issuer) {
        this.setIssuer(issuer);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Card)) {
            return false;
        }
        return getId() != null && getId().equals(((Card) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Card{" +
            "id=" + getId() +
            ", number=" + getNumber() +
            ", expirationDate='" + getExpirationDate() + "'" +
            ", verificationValue=" + getVerificationValue() +
            "}";
    }
}
