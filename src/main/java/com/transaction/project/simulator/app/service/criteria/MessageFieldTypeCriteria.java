package com.transaction.project.simulator.app.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.transaction.project.simulator.app.domain.MessageFieldType} entity. This class is used
 * in {@link com.transaction.project.simulator.app.web.rest.MessageFieldTypeResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /message-field-types?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MessageFieldTypeCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private LongFilter messageFieldsConfigId;

    private LongFilter fieldTypeId;

    private Boolean distinct;

    public MessageFieldTypeCriteria() {}

    public MessageFieldTypeCriteria(MessageFieldTypeCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.messageFieldsConfigId = other.optionalMessageFieldsConfigId().map(LongFilter::copy).orElse(null);
        this.fieldTypeId = other.optionalFieldTypeId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public MessageFieldTypeCriteria copy() {
        return new MessageFieldTypeCriteria(this);
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

    public LongFilter getFieldTypeId() {
        return fieldTypeId;
    }

    public Optional<LongFilter> optionalFieldTypeId() {
        return Optional.ofNullable(fieldTypeId);
    }

    public LongFilter fieldTypeId() {
        if (fieldTypeId == null) {
            setFieldTypeId(new LongFilter());
        }
        return fieldTypeId;
    }

    public void setFieldTypeId(LongFilter fieldTypeId) {
        this.fieldTypeId = fieldTypeId;
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
        final MessageFieldTypeCriteria that = (MessageFieldTypeCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(messageFieldsConfigId, that.messageFieldsConfigId) &&
            Objects.equals(fieldTypeId, that.fieldTypeId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, messageFieldsConfigId, fieldTypeId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MessageFieldTypeCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalMessageFieldsConfigId().map(f -> "messageFieldsConfigId=" + f + ", ").orElse("") +
            optionalFieldTypeId().map(f -> "fieldTypeId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
