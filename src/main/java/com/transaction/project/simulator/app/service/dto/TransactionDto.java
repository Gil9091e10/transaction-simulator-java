package com.transaction.project.simulator.app.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.transaction.project.simulator.app.domain.Transaction} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TransactionDto implements Serializable {

    private Long id;

    @NotNull
    private Instant date;

    @NotNull
    private String message;

    private TransactionTypeDto transactionType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public TransactionTypeDto getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionTypeDto transactionType) {
        this.transactionType = transactionType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TransactionDto)) {
            return false;
        }

        TransactionDto transactionDto = (TransactionDto) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, transactionDto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TransactionDto{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", message='" + getMessage() + "'" +
            ", transactionType=" + getTransactionType() +
            "}";
    }
}
