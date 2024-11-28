package com.transaction.project.simulator.app.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.transaction.project.simulator.app.domain.DebitCard} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DebitCardDto implements Serializable {

    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DebitCardDto)) {
            return false;
        }

        DebitCardDto debitCardDto = (DebitCardDto) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, debitCardDto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DebitCardDto{" +
            "id=" + getId() +
            "}";
    }
}
