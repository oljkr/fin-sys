package kr.wishtarot.finsys.accounting.mapper;

import kr.wishtarot.finsys.accounting.model.AccountingEntry;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AccountingEntriesMapper {
    void insertAccountingEntry(AccountingEntry accountingEntry);
    // 필요한 경우 다른 메서드 추가 (예: 조회 메서드)
}
