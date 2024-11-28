package com.transaction.project.simulator.app.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.transaction.project.simulator.app.domain.MessageFieldsConfig} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MessageFieldsConfigDto implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 50)
    private String name;

    @NotNull
    @Min(value = 1)
    private Integer fieldLength;

    private MessageIsoConfigDto messageIsoConfig;

    private MessageFieldTypeDto messageFieldType;

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

    public Integer getFieldLength() {
        return fieldLength;
    }

    public void setFieldLength(Integer fieldLength) {
        this.fieldLength = fieldLength;
    }

    public MessageIsoConfigDto getMessageIsoConfig() {
        return messageIsoConfig;
    }

    public void setMessageIsoConfig(MessageIsoConfigDto messageIsoConfig) {
        this.messageIsoConfig = messageIsoConfig;
    }

    public MessageFieldTypeDto getMessageFieldType() {
        return messageFieldType;
    }

    public void setMessageFieldType(MessageFieldTypeDto messageFieldType) {
        this.messageFieldType = messageFieldType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MessageFieldsConfigDto)) {
            return false;
        }

        MessageFieldsConfigDto messageFieldsConfigDto = (MessageFieldsConfigDto) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, messageFieldsConfigDto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MessageFieldsConfigDto{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", fieldLength=" + getFieldLength() +
            ", messageIsoConfig=" + getMessageIsoConfig() +
            ", messageFieldType=" + getMessageFieldType() +
            "}";
    }
}
