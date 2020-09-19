package org.apac.erp.cach.forecast.service;

import java.util.List;

import org.apac.erp.cach.forecast.persistence.entities.Customer;
import org.apac.erp.cach.forecast.persistence.entities.TimeLine;
import org.apac.erp.cach.forecast.persistence.repositories.TimeLineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TimeLineService {

	@Autowired
	private TimeLineRepository timeLineRepo;

	public TimeLine saveTimeLine(TimeLine timeLine) {
		return this.timeLineRepo.save(timeLine);
	}
	
	public List<TimeLine> getAllTimeLines() {
		return this.timeLineRepo.findAll();
	}
	
	public TimeLine fetTimeLineById(Long timeLineId) {
		return this.timeLineRepo.findOne(timeLineId);
	}

	public void deleteTimeLine(Long timeLineId) {
		 this.timeLineRepo.delete(timeLineId);
		
	}

}
