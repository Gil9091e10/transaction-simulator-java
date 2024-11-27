package com.transaction.project.simulator.app.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.transaction.project.simulator.app.domain.Acquirer} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AcquirerDto implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 50)
    private String name;

    @NotNull
    @Size(max = 255)
    private String socketUrl;

    @NotNull
    @Size(max = 100)
    private String email;

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

    public String getSocketUrl() {
        return socketUrl;
    }

    public void setSocketUrl(String socketUrl) {
        this.socketUrl = socketUrl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AcquirerDto)) {
            return false;
        }

        AcquirerDto acquirerDto = (AcquirerDto) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, acquirerDto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AcquirerDto{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", socketUrl='" + getSocketUrl() + "'" +
            ", email='" + getEmail() + "'" +
            "}";
    }
}
