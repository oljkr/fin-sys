package kr.wishtarot.finsys.payment.model;

import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
public class Transaction {
    private Long transactionId;
    private String userId;
    private String paymentMethod;
    private BigDecimal amount;
    private Timestamp transactionDate;
    private String status;
}

