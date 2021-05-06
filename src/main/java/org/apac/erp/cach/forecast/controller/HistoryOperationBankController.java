package org.apac.erp.cach.forecast.controller;

import org.apac.erp.cach.forecast.dtos.HistoryBankAccountOperationDto;
import org.apac.erp.cach.forecast.persistence.entities.HistoryOperationBank;
import org.apac.erp.cach.forecast.service.HistoryOperationBankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/history-operation-bank")
public class HistoryOperationBankController {
    @Autowired
    HistoryOperationBankService historyOperationBankService;


    @CrossOrigin
    @PostMapping()
    public HistoryOperationBank saveHistoryOperationBank(@RequestBody HistoryOperationBank historyOperationBank)
    {
        return  this.historyOperationBankService.saveHistoryOperationBank(historyOperationBank);
    }
    @CrossOrigin
    @GetMapping()
    public List<HistoryOperationBank> getAllHistoryOperationBank()
    {
        return  this.historyOperationBankService.getAllHistoryOperationBank();
    }
    @CrossOrigin
    @GetMapping("/between-date/{accountId}/{startDate}")
    public List<HistoryOperationBank> getAllHistoryOperationBankBetweenTwoDate(@PathVariable("accountId") Long accountId,  @PathVariable("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate)
    {
        return  this.historyOperationBankService.getAllHistoryOperationBankBetweenTwoDate(accountId,startDate);
    }
    @CrossOrigin
    @GetMapping("/{history_operation_bank}")
    public HistoryOperationBank getHistoryOperationBankById(@PathVariable("history_operation_bank") Long historyOperationBank)
    {
        return  this.historyOperationBankService.getHistoryOperationBankById(historyOperationBank);
    }
    @CrossOrigin
    @DeleteMapping("/{history_operation_bank}")
    public void deleteProductById(@PathVariable("history_operation_bank") Long historyOperationBank)
    {
        this.historyOperationBankService.deleteHistoryOperationBankById(historyOperationBank);
    }
}
