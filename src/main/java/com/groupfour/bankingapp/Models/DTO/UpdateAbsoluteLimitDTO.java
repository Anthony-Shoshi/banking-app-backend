package com.groupfour.bankingapp.Models.DTO;

public class UpdateAbsoluteLimitDTO {
    private Long accountId;
    private Double absoluteLimit;

    // Getters and setters
    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Double getAbsoluteLimit() {
        return absoluteLimit;
    }

    public void setAbsoluteLimit(Double absoluteLimit) {
        this.absoluteLimit = absoluteLimit;
    }
}
