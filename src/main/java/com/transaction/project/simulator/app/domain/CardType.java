package com.transaction.project.simulator.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A CardType.
 */
@Entity
@Table(name = "card_type")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CardType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 45)
    @Column(name = "name", length = 45, nullable = false)
    private String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "cardType")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "accountBank", "cardType", "issuer" }, allowSetters = true)
    private Set<Card> cards = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CardType id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public CardType name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Card> getCards() {
        return this.cards;
    }

    public void setCards(Set<Card> cards) {
        if (this.cards != null) {
            this.cards.forEach(i -> i.setCardType(null));
        }
        if (cards != null) {
            cards.forEach(i -> i.setCardType(this));
        }
        this.cards = cards;
    }

    public CardType cards(Set<Card> cards) {
        this.setCards(cards);
        return this;
    }

    public CardType addCard(Card card) {
        this.cards.add(card);
        card.setCardType(this);
        return this;
    }

    public CardType removeCard(Card card) {
        this.cards.remove(card);
        card.setCardType(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CardType)) {
            return false;
        }
        return getId() != null && getId().equals(((CardType) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CardType{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
