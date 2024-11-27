package com.transaction.project.simulator.app.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.transaction.project.simulator.app.domain.CardType} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CardTypeDto implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 45)
    private String name;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CardTypeDto)) {
            return false;
        }

        CardTypeDto cardTypeDto = (CardTypeDto) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, cardTypeDto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CardTypeDto{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
