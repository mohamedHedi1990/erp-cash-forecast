package org.apac.erp.cach.forecast.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apac.erp.cach.forecast.persistence.entities.BankAccount;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HistoryBankAccountOperationDto {
    BankAccount account;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone = "Africa/Tunis")
    private Date startDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone = "Africa/Tunis")
    private Date endDate;
}
