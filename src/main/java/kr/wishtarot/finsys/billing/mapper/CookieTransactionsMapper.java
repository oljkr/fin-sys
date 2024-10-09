package kr.wishtarot.finsys.billing.mapper;

import kr.wishtarot.finsys.billing.model.CookieTransaction;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface CookieTransactionsMapper {
    void insertCookieTransaction(CookieTransaction cookieTransaction);
    List<CookieTransaction> selectTransactionsByUserIdAndType(Map<String, Object> params);

    // 필요한 경우 다른 메서드 추가 (예: 조회 메서드)
}
