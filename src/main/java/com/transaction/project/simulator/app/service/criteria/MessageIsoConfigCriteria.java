package com.transaction.project.simulator.app.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.transaction.project.simulator.app.domain.MessageIsoConfig} entity. This class is used
 * in {@link com.transaction.project.simulator.app.web.rest.MessageIsoConfigResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /message-iso-configs?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MessageIsoConfigCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter filename;

    private LongFilter acquirerId;

    private LongFilter messageTypeIndicatorId;

    private LongFilter messageFieldsConfigId;

    private Boolean distinct;

    public MessageIsoConfigCriteria() {}

    public MessageIsoConfigCriteria(MessageIsoConfigCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.filename = other.optionalFilename().map(StringFilter::copy).orElse(null);
        this.acquirerId = other.optionalAcquirerId().map(LongFilter::copy).orElse(null);
        this.messageTypeIndicatorId = other.optionalMessageTypeIndicatorId().map(LongFilter::copy).orElse(null);
        this.messageFieldsConfigId = other.optionalMessageFieldsConfigId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public MessageIsoConfigCriteria copy() {
        return new MessageIsoConfigCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public Optional<StringFilter> optionalName() {
        return Optional.ofNullable(name);
    }

    public StringFilter name() {
        if (name == null) {
            setName(new StringFilter());
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getFilename() {
        return filename;
    }

    public Optional<StringFilter> optionalFilename() {
        return Optional.ofNullable(filename);
    }

    public StringFilter filename() {
        if (filename == null) {
            setFilename(new StringFilter());
        }
        return filename;
    }

    public void setFilename(StringFilter filename) {
        this.filename = filename;
    }

    public LongFilter getAcquirerId() {
        return acquirerId;
    }

    public Optional<LongFilter> optionalAcquirerId() {
        return Optional.ofNullable(acquirerId);
    }

    public LongFilter acquirerId() {
        if (acquirerId == null) {
            setAcquirerId(new LongFilter());
        }
        return acquirerId;
    }

    public void setAcquirerId(LongFilter acquirerId) {
        this.acquirerId = acquirerId;
    }

    public LongFilter getMessageTypeIndicatorId() {
        return messageTypeIndicatorId;
    }

    public Optional<LongFilter> optionalMessageTypeIndicatorId() {
        return Optional.ofNullable(messageTypeIndicatorId);
    }

    public LongFilter messageTypeIndicatorId() {
        if (messageTypeIndicatorId == null) {
            setMessageTypeIndicatorId(new LongFilter());
        }
        return messageTypeIndicatorId;
    }

    public void setMessageTypeIndicatorId(LongFilter messageTypeIndicatorId) {
        this.messageTypeIndicatorId = messageTypeIndicatorId;
    }

    public LongFilter getMessageFieldsConfigId() {
        return messageFieldsConfigId;
    }

    public Optional<LongFilter> optionalMessageFieldsConfigId() {
        return Optional.ofNullable(messageFieldsConfigId);
    }

    public LongFilter messageFieldsConfigId() {
        if (messageFieldsConfigId == null) {
            setMessageFieldsConfigId(new LongFilter());
        }
        return messageFieldsConfigId;
    }

    public void setMessageFieldsConfigId(LongFilter messageFieldsConfigId) {
        this.messageFieldsConfigId = messageFieldsConfigId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final MessageIsoConfigCriteria that = (MessageIsoConfigCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(filename, that.filename) &&
            Objects.equals(acquirerId, that.acquirerId) &&
            Objects.equals(messageTypeIndicatorId, that.messageTypeIndicatorId) &&
            Objects.equals(messageFieldsConfigId, that.messageFieldsConfigId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, filename, acquirerId, messageTypeIndicatorId, messageFieldsConfigId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MessageIsoConfigCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalFilename().map(f -> "filename=" + f + ", ").orElse("") +
            optionalAcquirerId().map(f -> "acquirerId=" + f + ", ").orElse("") +
            optionalMessageTypeIndicatorId().map(f -> "messageTypeIndicatorId=" + f + ", ").orElse("") +
            optionalMessageFieldsConfigId().map(f -> "messageFieldsConfigId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
