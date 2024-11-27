package com.transaction.project.simulator.app.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.transaction.project.simulator.app.domain.Merchant} entity. This class is used
 * in {@link com.transaction.project.simulator.app.web.rest.MerchantResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /merchants?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MerchantCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter mcc;

    private IntegerFilter postalCode;

    private StringFilter website;

    private LongFilter adviceId;

    private Boolean distinct;

    public MerchantCriteria() {}

    public MerchantCriteria(MerchantCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.mcc = other.optionalMcc().map(StringFilter::copy).orElse(null);
        this.postalCode = other.optionalPostalCode().map(IntegerFilter::copy).orElse(null);
        this.website = other.optionalWebsite().map(StringFilter::copy).orElse(null);
        this.adviceId = other.optionalAdviceId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public MerchantCriteria copy() {
        return new MerchantCriteria(this);
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

    public StringFilter getMcc() {
        return mcc;
    }

    public Optional<StringFilter> optionalMcc() {
        return Optional.ofNullable(mcc);
    }

    public StringFilter mcc() {
        if (mcc == null) {
            setMcc(new StringFilter());
        }
        return mcc;
    }

    public void setMcc(StringFilter mcc) {
        this.mcc = mcc;
    }

    public IntegerFilter getPostalCode() {
        return postalCode;
    }

    public Optional<IntegerFilter> optionalPostalCode() {
        return Optional.ofNullable(postalCode);
    }

    public IntegerFilter postalCode() {
        if (postalCode == null) {
            setPostalCode(new IntegerFilter());
        }
        return postalCode;
    }

    public void setPostalCode(IntegerFilter postalCode) {
        this.postalCode = postalCode;
    }

    public StringFilter getWebsite() {
        return website;
    }

    public Optional<StringFilter> optionalWebsite() {
        return Optional.ofNullable(website);
    }

    public StringFilter website() {
        if (website == null) {
            setWebsite(new StringFilter());
        }
        return website;
    }

    public void setWebsite(StringFilter website) {
        this.website = website;
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
        final MerchantCriteria that = (MerchantCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(mcc, that.mcc) &&
            Objects.equals(postalCode, that.postalCode) &&
            Objects.equals(website, that.website) &&
            Objects.equals(adviceId, that.adviceId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, mcc, postalCode, website, adviceId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MerchantCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalMcc().map(f -> "mcc=" + f + ", ").orElse("") +
            optionalPostalCode().map(f -> "postalCode=" + f + ", ").orElse("") +
            optionalWebsite().map(f -> "website=" + f + ", ").orElse("") +
            optionalAdviceId().map(f -> "adviceId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
