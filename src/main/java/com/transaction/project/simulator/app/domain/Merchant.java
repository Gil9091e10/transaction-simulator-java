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
 * A Merchant.
 */
@Entity
@Table(name = "merchant")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Merchant implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 50)
    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @NotNull
    @Column(name = "mcc", nullable = false)
    private String mcc;

    @Column(name = "postal_code")
    private Integer postalCode;

    @Column(name = "website")
    private String website;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "merchant")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "merchant", "acquirer" }, allowSetters = true)
    private Set<Advice> advice = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Merchant id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Merchant name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMcc() {
        return this.mcc;
    }

    public Merchant mcc(String mcc) {
        this.setMcc(mcc);
        return this;
    }

    public void setMcc(String mcc) {
        this.mcc = mcc;
    }

    public Integer getPostalCode() {
        return this.postalCode;
    }

    public Merchant postalCode(Integer postalCode) {
        this.setPostalCode(postalCode);
        return this;
    }

    public void setPostalCode(Integer postalCode) {
        this.postalCode = postalCode;
    }

    public String getWebsite() {
        return this.website;
    }

    public Merchant website(String website) {
        this.setWebsite(website);
        return this;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public Set<Advice> getAdvice() {
        return this.advice;
    }

    public void setAdvice(Set<Advice> advice) {
        if (this.advice != null) {
            this.advice.forEach(i -> i.setMerchant(null));
        }
        if (advice != null) {
            advice.forEach(i -> i.setMerchant(this));
        }
        this.advice = advice;
    }

    public Merchant advice(Set<Advice> advice) {
        this.setAdvice(advice);
        return this;
    }

    public Merchant addAdvice(Advice advice) {
        this.advice.add(advice);
        advice.setMerchant(this);
        return this;
    }

    public Merchant removeAdvice(Advice advice) {
        this.advice.remove(advice);
        advice.setMerchant(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Merchant)) {
            return false;
        }
        return getId() != null && getId().equals(((Merchant) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Merchant{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", mcc='" + getMcc() + "'" +
            ", postalCode=" + getPostalCode() +
            ", website='" + getWebsite() + "'" +
            "}";
    }
}
