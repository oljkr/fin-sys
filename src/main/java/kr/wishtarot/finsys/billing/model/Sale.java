package kr.wishtarot.finsys.billing.model;

import lombok.Data;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
public class Sale {
    private Long saleId;
    private String userId;
    private BigDecimal amount;
    private Timestamp saleDate;
    private Long serviceId;
}
