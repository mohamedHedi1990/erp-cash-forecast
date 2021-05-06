package org.apac.erp.cach.forecast.service;

import org.apac.erp.cach.forecast.dtos.HistoryBankAccountOperationDto;
import org.apac.erp.cach.forecast.persistence.entities.BankAccount;
import org.apac.erp.cach.forecast.persistence.entities.HistoryOperationBank;
import org.apac.erp.cach.forecast.persistence.repositories.HistoryOperationBankRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
public class HistoryOperationBankService {
    @Autowired
    HistoryOperationBankRepository historyOperationBankRepository;

    @Autowired
    private BankAccountService accountService;

    public HistoryOperationBank saveHistoryOperationBank(HistoryOperationBank historyOperationBank)
    {
        return this.historyOperationBankRepository.save(historyOperationBank);
    }
    public List<HistoryOperationBank> getAllHistoryOperationBank()
    {
        return  this.historyOperationBankRepository.findAll();
    }
    public List<HistoryOperationBank> getAllHistoryOperationBankBetweenTwoDate( Long accountId,  Date startDate)
    {
        BankAccount bankAccount = this.accountService.getAccountById(accountId);
        List<HistoryOperationBank> historyOperationBanks=new ArrayList<HistoryOperationBank>();
        historyOperationBanks =this.historyOperationBankRepository.findByBankAccountAndHistoryOperationBankDateGreaterThanEqualOrderByHistoryOperationBankDate(bankAccount,startDate);
        if(historyOperationBanks.size()==0) {
            HistoryOperationBank historyOperationBank = this.historyOperationBankRepository.findFirstByBankAccountAndHistoryOperationBankDateBeforeOrderByHistoryOperationBankDateDesc(bankAccount, startDate);
            if(historyOperationBank!=null) {
                historyOperationBanks.add(historyOperationBank);
            }
        }
        return historyOperationBanks;
    }
    public HistoryOperationBank getHistoryOperationBankById(Long id)
    {
        return this.historyOperationBankRepository.findOne(id);
    }
    public void deleteAllHistoryOperationBank()
    {
        this.historyOperationBankRepository.deleteAll();
    }
    public void deleteHistoryOperationBankById(Long id)
    {
        this.historyOperationBankRepository.delete(id);
    }
}
