package kr.wishtarot.finsys.accounting.model;

import lombok.Data;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
public class AccountingEntry {
    private Long entryId;
    private Long transactionId;
    private String accountCode;
    private String accountName;
    private BigDecimal debit;
    private BigDecimal credit;
    private Timestamp entryDate;
    private String description;
}
