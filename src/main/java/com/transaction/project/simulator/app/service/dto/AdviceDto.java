package com.transaction.project.simulator.app.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.transaction.project.simulator.app.domain.Advice} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AdviceDto implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 10)
    private String code;

    @NotNull
    @Size(max = 50)
    private String name;

    private MerchantDto merchant;

    private AcquirerDto acquirer;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MerchantDto getMerchant() {
        return merchant;
    }

    public void setMerchant(MerchantDto merchant) {
        this.merchant = merchant;
    }

    public AcquirerDto getAcquirer() {
        return acquirer;
    }

    public void setAcquirer(AcquirerDto acquirer) {
        this.acquirer = acquirer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AdviceDto)) {
            return false;
        }

        AdviceDto adviceDto = (AdviceDto) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, adviceDto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AdviceDto{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", name='" + getName() + "'" +
            ", merchant=" + getMerchant() +
            ", acquirer=" + getAcquirer() +
            "}";
    }
}
