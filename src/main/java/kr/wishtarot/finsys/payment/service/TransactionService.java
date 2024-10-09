package kr.wishtarot.finsys.payment.service;

import kr.wishtarot.finsys.billing.mapper.CookieTransactionsMapper;
import kr.wishtarot.finsys.billing.mapper.UserCookiesMapper;
import kr.wishtarot.finsys.billing.model.CookieTransaction;
import kr.wishtarot.finsys.billing.model.UserCookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TransactionService {

    @Autowired
    private UserCookiesMapper userCookiesMapper;

    @Autowired
    private CookieTransactionsMapper cookieTransactionsMapper;

    /**
     * 현재 쿠키 갯수 가져오기
     */
    public BigDecimal getCurrentCookieCount(String userId) {
        UserCookie userCookie = userCookiesMapper.selectUserCookieByUserId(userId);
        return userCookie != null ? userCookie.getBalance() : BigDecimal.ZERO;
    }

    public List<CookieTransaction> getPurchaseHistory(String userId) {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("transactionType", "charge");
        return cookieTransactionsMapper.selectTransactionsByUserIdAndType(params);
    }

    public List<CookieTransaction> getUsageHistory(String userId) {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("transactionType", "use");
        return cookieTransactionsMapper.selectTransactionsByUserIdAndType(params);
    }
}
