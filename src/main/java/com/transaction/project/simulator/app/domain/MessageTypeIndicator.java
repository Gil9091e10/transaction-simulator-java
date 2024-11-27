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
 * A MessageTypeIndicator.
 */
@Entity
@Table(name = "message_type_indicator")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MessageTypeIndicator implements Serializable {

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
    @Size(max = 4)
    @Column(name = "code", length = 4, nullable = false)
    private String code;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "messageTypeIndicator")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "acquirer", "messageTypeIndicator" }, allowSetters = true)
    private Set<MessageIsoConfig> messageIsoConfigs = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public MessageTypeIndicator id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public MessageTypeIndicator name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return this.code;
    }

    public MessageTypeIndicator code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Set<MessageIsoConfig> getMessageIsoConfigs() {
        return this.messageIsoConfigs;
    }

    public void setMessageIsoConfigs(Set<MessageIsoConfig> messageIsoConfigs) {
        if (this.messageIsoConfigs != null) {
            this.messageIsoConfigs.forEach(i -> i.setMessageTypeIndicator(null));
        }
        if (messageIsoConfigs != null) {
            messageIsoConfigs.forEach(i -> i.setMessageTypeIndicator(this));
        }
        this.messageIsoConfigs = messageIsoConfigs;
    }

    public MessageTypeIndicator messageIsoConfigs(Set<MessageIsoConfig> messageIsoConfigs) {
        this.setMessageIsoConfigs(messageIsoConfigs);
        return this;
    }

    public MessageTypeIndicator addMessageIsoConfig(MessageIsoConfig messageIsoConfig) {
        this.messageIsoConfigs.add(messageIsoConfig);
        messageIsoConfig.setMessageTypeIndicator(this);
        return this;
    }

    public MessageTypeIndicator removeMessageIsoConfig(MessageIsoConfig messageIsoConfig) {
        this.messageIsoConfigs.remove(messageIsoConfig);
        messageIsoConfig.setMessageTypeIndicator(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MessageTypeIndicator)) {
            return false;
        }
        return getId() != null && getId().equals(((MessageTypeIndicator) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MessageTypeIndicator{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", code='" + getCode() + "'" +
            "}";
    }
}
