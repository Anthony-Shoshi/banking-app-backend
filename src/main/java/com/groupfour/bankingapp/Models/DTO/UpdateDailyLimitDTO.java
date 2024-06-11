package com.groupfour.bankingapp.Models.DTO;

public class UpdateDailyLimitDTO {
    private Long accountId;
    private Double dailyLimit;

    // Getters and setters
    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Double getDailyLimit() {
        return dailyLimit;
    }

    public void setDailyLimit(Double dailyLimit) {
        this.dailyLimit = dailyLimit;
    }
}
