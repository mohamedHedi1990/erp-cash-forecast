package org.apac.erp.cach.forecast.controller;

import org.apac.erp.cach.forecast.persistence.entities.Avoir;
import org.apac.erp.cach.forecast.persistence.entities.AvoirLine;
import org.apac.erp.cach.forecast.service.AvoirLineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/avoirline")
public class AvoirLineConroller {

	@Autowired
	private AvoirLineService avoirLineService;


}
