package com.transaction.project.simulator.app.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.transaction.project.simulator.app.domain.Acquirer} entity. This class is used
 * in {@link com.transaction.project.simulator.app.web.rest.AcquirerResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /acquirers?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AcquirerCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter socketUrl;

    private StringFilter email;

    private LongFilter adviceId;

    private LongFilter messageIsoConfigId;

    private Boolean distinct;

    public AcquirerCriteria() {}

    public AcquirerCriteria(AcquirerCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.socketUrl = other.optionalSocketUrl().map(StringFilter::copy).orElse(null);
        this.email = other.optionalEmail().map(StringFilter::copy).orElse(null);
        this.adviceId = other.optionalAdviceId().map(LongFilter::copy).orElse(null);
        this.messageIsoConfigId = other.optionalMessageIsoConfigId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public AcquirerCriteria copy() {
        return new AcquirerCriteria(this);
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

    public StringFilter getSocketUrl() {
        return socketUrl;
    }

    public Optional<StringFilter> optionalSocketUrl() {
        return Optional.ofNullable(socketUrl);
    }

    public StringFilter socketUrl() {
        if (socketUrl == null) {
            setSocketUrl(new StringFilter());
        }
        return socketUrl;
    }

    public void setSocketUrl(StringFilter socketUrl) {
        this.socketUrl = socketUrl;
    }

    public StringFilter getEmail() {
        return email;
    }

    public Optional<StringFilter> optionalEmail() {
        return Optional.ofNullable(email);
    }

    public StringFilter email() {
        if (email == null) {
            setEmail(new StringFilter());
        }
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
    }

    public LongFilter getAdviceId() {
        return adviceId;
    }

    public Optional<LongFilter> optionalAdviceId() {
        return Optional.ofNullable(adviceId);
    }

    public LongFilter adviceId() {
        if (adviceId == null) {
            setAdviceId(new LongFilter());
        }
        return adviceId;
    }

    public void setAdviceId(LongFilter adviceId) {
        this.adviceId = adviceId;
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
        final AcquirerCriteria that = (AcquirerCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(socketUrl, that.socketUrl) &&
            Objects.equals(email, that.email) &&
            Objects.equals(adviceId, that.adviceId) &&
            Objects.equals(messageIsoConfigId, that.messageIsoConfigId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, socketUrl, email, adviceId, messageIsoConfigId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AcquirerCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalSocketUrl().map(f -> "socketUrl=" + f + ", ").orElse("") +
            optionalEmail().map(f -> "email=" + f + ", ").orElse("") +
            optionalAdviceId().map(f -> "adviceId=" + f + ", ").orElse("") +
            optionalMessageIsoConfigId().map(f -> "messageIsoConfigId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
