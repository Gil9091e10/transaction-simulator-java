package com.transaction.project.simulator.app.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the {@link com.transaction.project.simulator.app.domain.CreditCard} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CreditCardDto implements Serializable {

    private Long id;

    @NotNull
    private BigDecimal maxLimit;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getMaxLimit() {
        return maxLimit;
    }

    public void setMaxLimit(BigDecimal maxLimit) {
        this.maxLimit = maxLimit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CreditCardDto)) {
            return false;
        }

        CreditCardDto creditCardDto = (CreditCardDto) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, creditCardDto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CreditCardDto{" +
            "id=" + getId() +
            ", maxLimit=" + getMaxLimit() +
            "}";
    }
}
