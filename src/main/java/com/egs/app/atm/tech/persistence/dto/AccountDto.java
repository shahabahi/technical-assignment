package com.egs.app.atm.tech.persistence.dto;

import java.io.Serializable;

public class AccountDto implements Serializable {
    private Long userId;
    private Double remain;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Double getRemain() {
        return remain;
    }

    public void setRemain(Double remain) {
        this.remain = remain;
    }
}
