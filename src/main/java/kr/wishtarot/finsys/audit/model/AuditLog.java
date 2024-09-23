package kr.wishtarot.finsys.audit.model;

import lombok.Data;
import java.sql.Timestamp;

@Data
public class AuditLog {
    private Long logId;
    private String userId;
    private String action;
    private String description;
    private String ipAddress;
    private Timestamp createdAt;
}
