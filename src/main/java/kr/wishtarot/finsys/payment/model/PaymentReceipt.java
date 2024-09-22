package kr.wishtarot.finsys.payment.model;

import lombok.Data;

@Data
public class PaymentReceipt {
    private Long receiptId;
    private Long transactionId;
    private String receiptData;
    private String verificationStatus;
}
