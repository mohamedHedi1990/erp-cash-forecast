package org.apac.erp.cach.forecast.persistence.repositories;

import java.util.Date;
import java.util.List;

import org.apac.erp.cach.forecast.persistence.entities.Decaissement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DecaissementRepository extends JpaRepository<Decaissement, Long> {

	List<Decaissement> findByTransactionDeadlineDateGreaterThanEqualAndTransactionDeadlineDateLessThanEqual(
			Date startDate, Date endDate);

}
