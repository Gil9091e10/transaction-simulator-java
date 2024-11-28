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
 * A MessageFieldType.
 */
@Entity
@Table(name = "message_field_type")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MessageFieldType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "messageFieldType")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "messageIsoConfig", "messageFieldType" }, allowSetters = true)
    private Set<MessageFieldsConfig> messageFieldsConfigs = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "messageFieldTypes" }, allowSetters = true)
    private FieldType fieldType;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public MessageFieldType id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public MessageFieldType name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<MessageFieldsConfig> getMessageFieldsConfigs() {
        return this.messageFieldsConfigs;
    }

    public void setMessageFieldsConfigs(Set<MessageFieldsConfig> messageFieldsConfigs) {
        if (this.messageFieldsConfigs != null) {
            this.messageFieldsConfigs.forEach(i -> i.setMessageFieldType(null));
        }
        if (messageFieldsConfigs != null) {
            messageFieldsConfigs.forEach(i -> i.setMessageFieldType(this));
        }
        this.messageFieldsConfigs = messageFieldsConfigs;
    }

    public MessageFieldType messageFieldsConfigs(Set<MessageFieldsConfig> messageFieldsConfigs) {
        this.setMessageFieldsConfigs(messageFieldsConfigs);
        return this;
    }

    public MessageFieldType addMessageFieldsConfig(MessageFieldsConfig messageFieldsConfig) {
        this.messageFieldsConfigs.add(messageFieldsConfig);
        messageFieldsConfig.setMessageFieldType(this);
        return this;
    }

    public MessageFieldType removeMessageFieldsConfig(MessageFieldsConfig messageFieldsConfig) {
        this.messageFieldsConfigs.remove(messageFieldsConfig);
        messageFieldsConfig.setMessageFieldType(null);
        return this;
    }

    public FieldType getFieldType() {
        return this.fieldType;
    }

    public void setFieldType(FieldType fieldType) {
        this.fieldType = fieldType;
    }

    public MessageFieldType fieldType(FieldType fieldType) {
        this.setFieldType(fieldType);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MessageFieldType)) {
            return false;
        }
        return getId() != null && getId().equals(((MessageFieldType) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MessageFieldType{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
