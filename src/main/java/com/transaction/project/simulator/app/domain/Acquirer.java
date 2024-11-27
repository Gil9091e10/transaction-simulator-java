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
 * A Acquirer.
 */
@Entity
@Table(name = "acquirer")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Acquirer implements Serializable {

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
    @Size(max = 255)
    @Column(name = "socket_url", length = 255, nullable = false)
    private String socketUrl;

    @NotNull
    @Size(max = 100)
    @Column(name = "email", length = 100, nullable = false)
    private String email;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "acquirer")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "merchant", "acquirer" }, allowSetters = true)
    private Set<Advice> advice = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "acquirer")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "acquirer", "messageTypeIndicator" }, allowSetters = true)
    private Set<MessageIsoConfig> messageIsoConfigs = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Acquirer id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Acquirer name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSocketUrl() {
        return this.socketUrl;
    }

    public Acquirer socketUrl(String socketUrl) {
        this.setSocketUrl(socketUrl);
        return this;
    }

    public void setSocketUrl(String socketUrl) {
        this.socketUrl = socketUrl;
    }

    public String getEmail() {
        return this.email;
    }

    public Acquirer email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Advice> getAdvice() {
        return this.advice;
    }

    public void setAdvice(Set<Advice> advice) {
        if (this.advice != null) {
            this.advice.forEach(i -> i.setAcquirer(null));
        }
        if (advice != null) {
            advice.forEach(i -> i.setAcquirer(this));
        }
        this.advice = advice;
    }

    public Acquirer advice(Set<Advice> advice) {
        this.setAdvice(advice);
        return this;
    }

    public Acquirer addAdvice(Advice advice) {
        this.advice.add(advice);
        advice.setAcquirer(this);
        return this;
    }

    public Acquirer removeAdvice(Advice advice) {
        this.advice.remove(advice);
        advice.setAcquirer(null);
        return this;
    }

    public Set<MessageIsoConfig> getMessageIsoConfigs() {
        return this.messageIsoConfigs;
    }

    public void setMessageIsoConfigs(Set<MessageIsoConfig> messageIsoConfigs) {
        if (this.messageIsoConfigs != null) {
            this.messageIsoConfigs.forEach(i -> i.setAcquirer(null));
        }
        if (messageIsoConfigs != null) {
            messageIsoConfigs.forEach(i -> i.setAcquirer(this));
        }
        this.messageIsoConfigs = messageIsoConfigs;
    }

    public Acquirer messageIsoConfigs(Set<MessageIsoConfig> messageIsoConfigs) {
        this.setMessageIsoConfigs(messageIsoConfigs);
        return this;
    }

    public Acquirer addMessageIsoConfig(MessageIsoConfig messageIsoConfig) {
        this.messageIsoConfigs.add(messageIsoConfig);
        messageIsoConfig.setAcquirer(this);
        return this;
    }

    public Acquirer removeMessageIsoConfig(MessageIsoConfig messageIsoConfig) {
        this.messageIsoConfigs.remove(messageIsoConfig);
        messageIsoConfig.setAcquirer(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Acquirer)) {
            return false;
        }
        return getId() != null && getId().equals(((Acquirer) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Acquirer{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", socketUrl='" + getSocketUrl() + "'" +
            ", email='" + getEmail() + "'" +
            "}";
    }
}
