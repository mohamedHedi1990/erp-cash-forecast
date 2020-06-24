package org.apac.erp.cach.forecast.persistence.repositories;

import org.apac.erp.cach.forecast.persistence.entities.EncaissementDecaissement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EncaissementDecaissementRepository extends JpaRepository<EncaissementDecaissement, Long> {

}
