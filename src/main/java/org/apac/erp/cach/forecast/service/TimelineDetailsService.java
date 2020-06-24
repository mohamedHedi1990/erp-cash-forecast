package org.apac.erp.cach.forecast.service;

import java.util.List;

import org.apac.erp.cach.forecast.persistence.entities.TimelineDetails;
import org.apac.erp.cach.forecast.persistence.repositories.TimelineDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TimelineDetailsService {

	@Autowired
	private TimelineDetailsRepository timelineDetailsRepo;

	public List<TimelineDetails> findAllTimelineDetails() {
		return timelineDetailsRepo.findAll();
	}

}
