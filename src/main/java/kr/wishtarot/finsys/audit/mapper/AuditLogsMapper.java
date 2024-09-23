package kr.wishtarot.finsys.audit.mapper;

import kr.wishtarot.finsys.audit.model.AuditLog;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AuditLogsMapper {
    void insertAuditLog(AuditLog auditLog);
    // 필요한 경우 다른 메서드 추가 (예: 조회 메서드)
}
