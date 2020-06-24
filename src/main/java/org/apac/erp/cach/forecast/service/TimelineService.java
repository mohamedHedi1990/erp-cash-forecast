package org.apac.erp.cach.forecast.service;

import java.util.List;

import org.apac.erp.cach.forecast.persistence.entities.Timeline;
import org.apac.erp.cach.forecast.persistence.repositories.TimelineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TimelineService {

	@Autowired
	private TimelineRepository timelineRepo;

	public List<Timeline> findAllTimelines() {
		return timelineRepo.findAll();
	}

}
