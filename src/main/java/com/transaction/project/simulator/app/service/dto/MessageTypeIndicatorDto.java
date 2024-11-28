package com.transaction.project.simulator.app.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.transaction.project.simulator.app.domain.MessageTypeIndicator} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MessageTypeIndicatorDto implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 50)
    private String name;

    @NotNull
    @Size(max = 4)
    private String code;

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MessageTypeIndicatorDto)) {
            return false;
        }

        MessageTypeIndicatorDto messageTypeIndicatorDto = (MessageTypeIndicatorDto) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, messageTypeIndicatorDto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MessageTypeIndicatorDto{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", code='" + getCode() + "'" +
            "}";
    }
}
