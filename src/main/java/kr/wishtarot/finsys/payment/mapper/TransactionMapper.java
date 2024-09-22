package kr.wishtarot.finsys.payment.mapper;

import kr.wishtarot.finsys.payment.model.Transaction;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface TransactionMapper {
    void insertTransaction(Transaction transaction);
    Transaction selectTransactionById(Long transactionId);
    Transaction selectTransactionByReceiptData(String receiptData);
    void updateTransactionStatus(@Param("transactionId") Long transactionId, @Param("status") String status);
}
