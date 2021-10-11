package com.egs.app.atm.tech.persistence.model.request;


public class LoginRequestModel {
    private String cardNumber;
    private String password;

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
