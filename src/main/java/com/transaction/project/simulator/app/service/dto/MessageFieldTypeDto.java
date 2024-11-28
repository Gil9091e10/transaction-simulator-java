package com.transaction.project.simulator.app.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.transaction.project.simulator.app.domain.MessageFieldType} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MessageFieldTypeDto implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private FieldTypeDto fieldType;

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

    public FieldTypeDto getFieldType() {
        return fieldType;
    }

    public void setFieldType(FieldTypeDto fieldType) {
        this.fieldType = fieldType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MessageFieldTypeDto)) {
            return false;
        }

        MessageFieldTypeDto messageFieldTypeDto = (MessageFieldTypeDto) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, messageFieldTypeDto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MessageFieldTypeDto{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", fieldType=" + getFieldType() +
            "}";
    }
}
