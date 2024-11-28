package com.transaction.project.simulator.app.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.transaction.project.simulator.app.domain.MessageIsoConfig} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MessageIsoConfigDto implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 50)
    private String name;

    @NotNull
    @Size(max = 255)
    private String filename;

    private AcquirerDto acquirer;

    private MessageTypeIndicatorDto messageTypeIndicator;

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

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public AcquirerDto getAcquirer() {
        return acquirer;
    }

    public void setAcquirer(AcquirerDto acquirer) {
        this.acquirer = acquirer;
    }

    public MessageTypeIndicatorDto getMessageTypeIndicator() {
        return messageTypeIndicator;
    }

    public void setMessageTypeIndicator(MessageTypeIndicatorDto messageTypeIndicator) {
        this.messageTypeIndicator = messageTypeIndicator;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MessageIsoConfigDto)) {
            return false;
        }

        MessageIsoConfigDto messageIsoConfigDto = (MessageIsoConfigDto) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, messageIsoConfigDto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MessageIsoConfigDto{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", filename='" + getFilename() + "'" +
            ", acquirer=" + getAcquirer() +
            ", messageTypeIndicator=" + getMessageTypeIndicator() +
            "}";
    }
}
