package com.transaction.project.simulator.app.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.transaction.project.simulator.app.domain.MessageFieldsConfig} entity. This class is used
 * in {@link com.transaction.project.simulator.app.web.rest.MessageFieldsConfigResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /message-fields-configs?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MessageFieldsConfigCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private IntegerFilter fieldLength;

    private LongFilter messageIsoConfigId;

    private LongFilter messageFieldTypeId;

    private Boolean distinct;

    public MessageFieldsConfigCriteria() {}

    public MessageFieldsConfigCriteria(MessageFieldsConfigCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.fieldLength = other.optionalFieldLength().map(IntegerFilter::copy).orElse(null);
        this.messageIsoConfigId = other.optionalMessageIsoConfigId().map(LongFilter::copy).orElse(null);
        this.messageFieldTypeId = other.optionalMessageFieldTypeId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public MessageFieldsConfigCriteria copy() {
        return new MessageFieldsConfigCriteria(this);
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

    public IntegerFilter getFieldLength() {
        return fieldLength;
    }

    public Optional<IntegerFilter> optionalFieldLength() {
        return Optional.ofNullable(fieldLength);
    }

    public IntegerFilter fieldLength() {
        if (fieldLength == null) {
            setFieldLength(new IntegerFilter());
        }
        return fieldLength;
    }

    public void setFieldLength(IntegerFilter fieldLength) {
        this.fieldLength = fieldLength;
    }

    public LongFilter getMessageIsoConfigId() {
        return messageIsoConfigId;
    }

    public Optional<LongFilter> optionalMessageIsoConfigId() {
        return Optional.ofNullable(messageIsoConfigId);
    }

    public LongFilter messageIsoConfigId() {
        if (messageIsoConfigId == null) {
            setMessageIsoConfigId(new LongFilter());
        }
        return messageIsoConfigId;
    }

    public void setMessageIsoConfigId(LongFilter messageIsoConfigId) {
        this.messageIsoConfigId = messageIsoConfigId;
    }

    public LongFilter getMessageFieldTypeId() {
        return messageFieldTypeId;
    }

    public Optional<LongFilter> optionalMessageFieldTypeId() {
        return Optional.ofNullable(messageFieldTypeId);
    }

    public LongFilter messageFieldTypeId() {
        if (messageFieldTypeId == null) {
            setMessageFieldTypeId(new LongFilter());
        }
        return messageFieldTypeId;
    }

    public void setMessageFieldTypeId(LongFilter messageFieldTypeId) {
        this.messageFieldTypeId = messageFieldTypeId;
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
        final MessageFieldsConfigCriteria that = (MessageFieldsConfigCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(fieldLength, that.fieldLength) &&
            Objects.equals(messageIsoConfigId, that.messageIsoConfigId) &&
            Objects.equals(messageFieldTypeId, that.messageFieldTypeId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, fieldLength, messageIsoConfigId, messageFieldTypeId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MessageFieldsConfigCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalFieldLength().map(f -> "fieldLength=" + f + ", ").orElse("") +
            optionalMessageIsoConfigId().map(f -> "messageIsoConfigId=" + f + ", ").orElse("") +
            optionalMessageFieldTypeId().map(f -> "messageFieldTypeId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
