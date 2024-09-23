package kr.wishtarot.finsys.billing.model;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class UseCookieRequest {
    private String userId;
    private BigDecimal amount;
    private Long serviceId;
}

