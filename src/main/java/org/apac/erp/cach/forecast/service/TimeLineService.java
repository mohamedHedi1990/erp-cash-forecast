package org.apac.erp.cach.forecast.service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apac.erp.cach.forecast.dtos.TimeLineInitialInfo;
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

	public List<TimeLineEntry> generateTimeLineEntries(TimeLineInitialInfo timeLineInfo) {

		List<TimeLineEntry> entries = new ArrayList<TimeLineEntry>();

		Double finalAmount = timeLineInfo.getTimeLineInitialAmount()
				+ timeLineInfo.getTimeLineInitialAmount() * timeLineInfo.getTimeLineInterestRate();

		Integer numberofMonth = 1;
		switch (timeLineInfo.getTimeLineAnnuity()) {
		case Mensuelle:
			numberofMonth = 1;
		case Trimesterielle:
			numberofMonth = 3;
		case Semesterielle:
			numberofMonth = 6;
		case Annuelle:
			numberofMonth = 12;
		}

		Double entryTotalAmount = (double) finalAmount / timeLineInfo.getTimeLineAnnityNumber();

		for (int i = 0; i < timeLineInfo.getTimeLineAnnityNumber() - 1; i++) {
			TimeLineEntry entry = new TimeLineEntry();
			if (i == 0) {
				entry.setLineDate(timeLineInfo.getTimeLineStartDate());
			} else {
				// convertir la date precenet dvers local date
				LocalDate localDate = entries.get(i - 1).getLineDate().toInstant().atZone(ZoneId.systemDefault())
						.toLocalDate();
				// Ajouter l'anuité
				LocalDate localdateAfterAnuity = localDate.plusMonths(numberofMonth);
				//convertir localdate to date
					Date lineDate = Date.from(localdateAfterAnuity.atStartOfDay(ZoneId.systemDefault()).toInstant());
				 entry.setLineDate(lineDate);
			}
			entry.setTotal(entryTotalAmount);
			entries.add(entry);
		}
		return entries;
	}

}
