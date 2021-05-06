package org.apac.erp.cach.forecast.persistence.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.apac.erp.cach.forecast.constants.Utils;
import org.apac.erp.cach.forecast.enumeration.OperationType;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "erp_History_Operation_Bank")
@Data
public class HistoryOperationBank extends AuditableSql implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long historyOperationBankId;
    private String historyOperationBankLabel;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Africa/Tunis")
    @Temporal(TemporalType.TIMESTAMP)
    private Date historyOperationBankValueDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Africa/Tunis")
    @Temporal(TemporalType.TIMESTAMP)
    private Date historyOperationBankDate;
    private Double historyOperationBankInitialAmount;
    private String historyOperationBankInitialAmountS;
    private Double historyOperationBankFinalAmount;
    private String historyOperationBankFinalAmountS;
    private Double historyOperationBankAmount;
    private String historyOperationBankAmountS;
    @Enumerated(EnumType.STRING)
    private OperationType historyOperationBankType;
    @ManyToOne
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    BankAccount bankAccount;
    @PrePersist
    public void prePersist() {
       this.historyOperationBankInitialAmountS=Utils.convertAmountToStringWithSeperator(this.historyOperationBankInitialAmount);
       this.historyOperationBankFinalAmountS=Utils.convertAmountToStringWithSeperator(this.historyOperationBankFinalAmount);
       this.historyOperationBankAmountS=Utils.convertAmountToStringWithSeperator(this.historyOperationBankAmount);

    }

    @PreUpdate
    public void preUpdate() {
        this.historyOperationBankInitialAmountS=Utils.convertAmountToStringWithSeperator(this.historyOperationBankInitialAmount);
        this.historyOperationBankFinalAmountS=Utils.convertAmountToStringWithSeperator(this.historyOperationBankFinalAmount);
        this.historyOperationBankAmountS=Utils.convertAmountToStringWithSeperator(this.historyOperationBankAmount);
    }
}
