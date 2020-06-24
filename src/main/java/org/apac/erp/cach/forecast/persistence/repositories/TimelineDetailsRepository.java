package org.apac.erp.cach.forecast.persistence.repositories;

import org.apac.erp.cach.forecast.persistence.entities.TimelineDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimelineDetailsRepository extends JpaRepository<TimelineDetails, Long> {

}
