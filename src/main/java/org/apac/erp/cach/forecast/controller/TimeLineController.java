package org.apac.erp.cach.forecast.controller;

import java.util.List;

import org.apac.erp.cach.forecast.persistence.entities.TarifBancaire;
import org.apac.erp.cach.forecast.persistence.entities.TimeLine;
import org.apac.erp.cach.forecast.service.TimeLineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/timeLine")
public class TimeLineController {

	@Autowired
	private TimeLineService timeLineService;;

	@CrossOrigin
	@GetMapping()
	public List<TimeLine> findAllTimeLines() {
		return this.timeLineService.getAllTimeLines();
	}

	@CrossOrigin
	@PostMapping()
	public TimeLine saveTimeLine(@RequestBody TimeLine timeLine) {
		return timeLineService.saveTimeLine(timeLine);
	}

	@CrossOrigin
	@GetMapping("/{timeLineId}")
	public TimeLine findBankById(@PathVariable("timeLineId") Long timeLineId) {
		return this.timeLineService.fetTimeLineById(timeLineId);
	}
	@CrossOrigin
	@DeleteMapping("/{timeLineId}")
	public void deleteTimeLine(@PathVariable("timeLineId") Long timeLineId) {
		timeLineService.deleteTimeLine(timeLineId);
	}


}
