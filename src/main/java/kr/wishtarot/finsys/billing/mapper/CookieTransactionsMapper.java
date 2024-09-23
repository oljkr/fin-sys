package kr.wishtarot.finsys.billing.mapper;

import kr.wishtarot.finsys.billing.model.CookieTransaction;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CookieTransactionsMapper {
    void insertCookieTransaction(CookieTransaction cookieTransaction);
    // 필요한 경우 다른 메서드 추가 (예: 조회 메서드)
}
