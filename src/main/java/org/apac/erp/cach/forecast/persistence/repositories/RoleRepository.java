package org.apac.erp.cach.forecast.persistence.repositories;

import org.apac.erp.cach.forecast.enumeration.ERole;
import org.apac.erp.cach.forecast.persistence.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {
    Optional<Role> findByName(ERole name);
}
