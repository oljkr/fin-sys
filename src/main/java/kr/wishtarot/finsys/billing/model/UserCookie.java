package kr.wishtarot.finsys.billing.model;

import lombok.Data;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
public class UserCookie {
    private String userId;
    private BigDecimal balance;
    private Timestamp lastUpdated;
}
