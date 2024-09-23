package kr.wishtarot.finsys.billing.model;

import lombok.Data;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
public class CookieTransaction {
    private Long transactionId;
    private String userId;
    private BigDecimal amount;
    private String transactionType; // '쿠키 충전' 또는 '쿠키 사용'
    private Timestamp transactionDate;
    private Long serviceId;  // 쿠키 사용 시 서비스 ID, 충전 시 null
}
