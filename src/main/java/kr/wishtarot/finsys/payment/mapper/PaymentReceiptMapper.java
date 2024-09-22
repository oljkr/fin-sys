package kr.wishtarot.finsys.payment.mapper;

import kr.wishtarot.finsys.payment.model.PaymentReceipt;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PaymentReceiptMapper {
    void insertPaymentReceipt(PaymentReceipt receipt);
    PaymentReceipt selectPaymentReceiptByReceiptData(String receiptData);
    void updateVerificationStatus(@Param("receiptId") Long receiptId, @Param("verificationStatus") String verificationStatus);
}
