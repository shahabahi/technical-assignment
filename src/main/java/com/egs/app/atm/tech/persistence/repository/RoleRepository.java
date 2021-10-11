package com.egs.app.atm.tech.persistence.repository;


import com.egs.app.atm.tech.persistence.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByRole(String role);
}
