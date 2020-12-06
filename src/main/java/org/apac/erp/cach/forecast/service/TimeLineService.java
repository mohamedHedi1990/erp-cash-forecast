package org.apac.erp.cach.forecast.service;

import java.util.Date;
import java.util.List;

import org.apac.erp.cach.forecast.persistence.entities.BankAccount;
import org.apac.erp.cach.forecast.persistence.entities.TimeLine;
import org.apac.erp.cach.forecast.persistence.entities.TimeLineEntry;
import org.apac.erp.cach.forecast.persistence.repositories.TimeLineEntryRepository;
import org.apac.erp.cach.forecast.persistence.repositories.TimeLineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TimeLineService {

	@Autowired
	private TimeLineRepository timeLineRepo;
	
	@Autowired
	private TimeLineEntryRepository timeLineEntryRepo;

	public TimeLine saveTimeLine(TimeLine timeLine) {
		return this.timeLineRepo.save(timeLine);
	}
	
	public List<TimeLine> getAllTimeLines() {
		return this.timeLineRepo.findAll();
	}
	
	public TimeLine findTimeLineById(Long timeLineId) {
		return this.timeLineRepo.findOne(timeLineId);
	}

	public void deleteTimeLine(Long timeLineId) {
		 this.timeLineRepo.delete(timeLineId);
		
	}
	public List<TimeLineEntry> findByLineDateBetween(Date startDate, Date endDate) {
		return timeLineEntryRepo.findByLineDateBetween(startDate, endDate);
	}
	
	public List<TimeLine> findByTimeLineAccount(BankAccount timeLineAccount) {
		return timeLineRepo.findByTimeLineAccount(timeLineAccount);
	}


}
