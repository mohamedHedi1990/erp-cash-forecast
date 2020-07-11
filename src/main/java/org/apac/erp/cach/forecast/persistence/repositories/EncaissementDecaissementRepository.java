package org.apac.erp.cach.forecast.persistence.repositories;

import java.util.Date;
import java.util.List;

import org.apac.erp.cach.forecast.persistence.entities.EncaissementDecaissement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EncaissementDecaissementRepository extends JpaRepository<EncaissementDecaissement, Long> {

	List<EncaissementDecaissement> findByEncaissementDecaissementDeadlineDateGreaterThanEqualAndEncaissementDecaissementDeadlineDateLessThanEqual(
			Date startDate, Date endDate);

}
