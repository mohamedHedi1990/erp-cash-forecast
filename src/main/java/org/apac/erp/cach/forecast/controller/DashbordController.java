package org.apac.erp.cach.forecast.controller;

import org.apac.erp.cach.forecast.dtos.DashbordDto;
import org.apac.erp.cach.forecast.service.DashbordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashbord")
public class DashbordController {

    @Autowired
    private DashbordService dashbordService;

    @CrossOrigin
    @GetMapping()
    private DashbordDto getDashbord(){
        return dashbordService.getDashbordInformation();
    }
}
