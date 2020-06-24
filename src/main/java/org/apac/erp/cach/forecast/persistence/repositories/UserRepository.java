package org.apac.erp.cach.forecast.persistence.repositories;

import org.apac.erp.cach.forecast.persistence.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}
