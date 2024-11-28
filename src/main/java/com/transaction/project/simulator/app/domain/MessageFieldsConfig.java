package com.transaction.project.simulator.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A MessageFieldsConfig.
 */
@Entity
@Table(name = "message_fields_config")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MessageFieldsConfig implements Serializable {

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
    @Min(value = 1)
    @Column(name = "field_length", nullable = false)
    private Integer fieldLength;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "acquirer", "messageTypeIndicator", "messageFieldsConfigs" }, allowSetters = true)
    private MessageIsoConfig messageIsoConfig;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "messageFieldsConfigs", "fieldType" }, allowSetters = true)
    private MessageFieldType messageFieldType;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public MessageFieldsConfig id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public MessageFieldsConfig name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getFieldLength() {
        return this.fieldLength;
    }

    public MessageFieldsConfig fieldLength(Integer fieldLength) {
        this.setFieldLength(fieldLength);
        return this;
    }

    public void setFieldLength(Integer fieldLength) {
        this.fieldLength = fieldLength;
    }

    public MessageIsoConfig getMessageIsoConfig() {
        return this.messageIsoConfig;
    }

    public void setMessageIsoConfig(MessageIsoConfig messageIsoConfig) {
        this.messageIsoConfig = messageIsoConfig;
    }

    public MessageFieldsConfig messageIsoConfig(MessageIsoConfig messageIsoConfig) {
        this.setMessageIsoConfig(messageIsoConfig);
        return this;
    }

    public MessageFieldType getMessageFieldType() {
        return this.messageFieldType;
    }

    public void setMessageFieldType(MessageFieldType messageFieldType) {
        this.messageFieldType = messageFieldType;
    }

    public MessageFieldsConfig messageFieldType(MessageFieldType messageFieldType) {
        this.setMessageFieldType(messageFieldType);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MessageFieldsConfig)) {
            return false;
        }
        return getId() != null && getId().equals(((MessageFieldsConfig) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MessageFieldsConfig{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", fieldLength=" + getFieldLength() +
            "}";
    }
}
