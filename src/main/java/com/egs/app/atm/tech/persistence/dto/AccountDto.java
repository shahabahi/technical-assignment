package com.egs.app.atm.tech.persistence.dto;

import com.egs.app.atm.tech.persistence.model.User;

import java.io.Serializable;

public class AccountDto implements Serializable {
    private Long id;
    private User user;
    private Double balance;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }
}
