package com.transaction.project.simulator.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A MessageIsoConfig.
 */
@Entity
@Table(name = "message_iso_config")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MessageIsoConfig implements Serializable {

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
    @Column(name = "filename", length = 255, nullable = false)
    private String filename;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "advice", "messageIsoConfigs" }, allowSetters = true)
    private Acquirer acquirer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "messageIsoConfigs" }, allowSetters = true)
    private MessageTypeIndicator messageTypeIndicator;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public MessageIsoConfig id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public MessageIsoConfig name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFilename() {
        return this.filename;
    }

    public MessageIsoConfig filename(String filename) {
        this.setFilename(filename);
        return this;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Acquirer getAcquirer() {
        return this.acquirer;
    }

    public void setAcquirer(Acquirer acquirer) {
        this.acquirer = acquirer;
    }

    public MessageIsoConfig acquirer(Acquirer acquirer) {
        this.setAcquirer(acquirer);
        return this;
    }

    public MessageTypeIndicator getMessageTypeIndicator() {
        return this.messageTypeIndicator;
    }

    public void setMessageTypeIndicator(MessageTypeIndicator messageTypeIndicator) {
        this.messageTypeIndicator = messageTypeIndicator;
    }

    public MessageIsoConfig messageTypeIndicator(MessageTypeIndicator messageTypeIndicator) {
        this.setMessageTypeIndicator(messageTypeIndicator);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MessageIsoConfig)) {
            return false;
        }
        return getId() != null && getId().equals(((MessageIsoConfig) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MessageIsoConfig{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", filename='" + getFilename() + "'" +
            "}";
    }
}
