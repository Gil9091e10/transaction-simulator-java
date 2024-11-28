package com.transaction.project.simulator.app.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.transaction.project.simulator.app.domain.FieldType} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FieldTypeDto implements Serializable {

    private Long id;

    @NotNull
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
        if (!(o instanceof FieldTypeDto)) {
            return false;
        }

        FieldTypeDto fieldTypeDto = (FieldTypeDto) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, fieldTypeDto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FieldTypeDto{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
