package com.transaction.project.simulator.app.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.transaction.project.simulator.app.domain.Merchant} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MerchantDto implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 50)
    private String name;

    @NotNull
    private String mcc;

    private Integer postalCode;

    private String website;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMcc() {
        return mcc;
    }

    public void setMcc(String mcc) {
        this.mcc = mcc;
    }

    public Integer getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(Integer postalCode) {
        this.postalCode = postalCode;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MerchantDto)) {
            return false;
        }

        MerchantDto merchantDto = (MerchantDto) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, merchantDto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MerchantDto{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", mcc='" + getMcc() + "'" +
            ", postalCode=" + getPostalCode() +
            ", website='" + getWebsite() + "'" +
            "}";
    }
}
