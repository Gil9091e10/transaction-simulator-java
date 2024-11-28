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
 * A FieldType.
 */
@Entity
@Table(name = "field_type")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FieldType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "fieldType")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "messageFieldsConfigs", "fieldType" }, allowSetters = true)
    private Set<MessageFieldType> messageFieldTypes = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public FieldType id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public FieldType name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<MessageFieldType> getMessageFieldTypes() {
        return this.messageFieldTypes;
    }

    public void setMessageFieldTypes(Set<MessageFieldType> messageFieldTypes) {
        if (this.messageFieldTypes != null) {
            this.messageFieldTypes.forEach(i -> i.setFieldType(null));
        }
        if (messageFieldTypes != null) {
            messageFieldTypes.forEach(i -> i.setFieldType(this));
        }
        this.messageFieldTypes = messageFieldTypes;
    }

    public FieldType messageFieldTypes(Set<MessageFieldType> messageFieldTypes) {
        this.setMessageFieldTypes(messageFieldTypes);
        return this;
    }

    public FieldType addMessageFieldType(MessageFieldType messageFieldType) {
        this.messageFieldTypes.add(messageFieldType);
        messageFieldType.setFieldType(this);
        return this;
    }

    public FieldType removeMessageFieldType(MessageFieldType messageFieldType) {
        this.messageFieldTypes.remove(messageFieldType);
        messageFieldType.setFieldType(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FieldType)) {
            return false;
        }
        return getId() != null && getId().equals(((FieldType) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FieldType{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
