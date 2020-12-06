package org.apac.erp.cach.forecast.service;

import org.apac.erp.cach.forecast.persistence.entities.TimeLineEntry;
import org.apac.erp.cach.forecast.persistence.repositories.TimeLineEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TimeLineEntryService {

	@Autowired
	private TimeLineEntryRepository timeLineEntryRepo;

	public TimeLineEntry saveTimeLineEntry(TimeLineEntry entry) {
		return this.timeLineEntryRepo.save(entry);
	}

	public TimeLineEntry findTimeLineEntryById(Long entryId) {
		return this.timeLineEntryRepo.findOne(entryId);
	}

}
