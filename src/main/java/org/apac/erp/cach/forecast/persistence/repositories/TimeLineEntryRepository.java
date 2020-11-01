package org.apac.erp.cach.forecast.persistence.repositories;

import java.util.Date;
import java.util.List;

import org.apac.erp.cach.forecast.persistence.entities.TimeLineEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimeLineEntryRepository extends JpaRepository<TimeLineEntry, Long> {
	public List<TimeLineEntry> findByLineDateBetween(Date startDate, Date endDate);
}
