package com.egs.app.atm.tech.persistence.model;

import lombok.Builder;

import javax.persistence.*;
@Builder
@Entity
@Table(name = "tbl_role")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String role;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Role(Long id, String role) {
        this.id = id;
        this.role = role;
    }

    public Role() {
    }
}
