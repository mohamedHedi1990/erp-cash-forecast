package org.apac.erp.cach.forecast.persistence.repositories;

import org.apac.erp.cach.forecast.persistence.entities.Decaissement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DecaissementRepository extends JpaRepository<Decaissement, Long> {

	/*
	 * List<Encaissement>
	 * findByEncaissementDecaissementDeadlineDateGreaterThanEqualAndEncaissementDecaissementDeadlineDateLessThanEqual(
	 * Date startDate, Date endDate);
	 */

}
