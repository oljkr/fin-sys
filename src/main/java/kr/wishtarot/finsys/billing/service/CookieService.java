package kr.wishtarot.finsys.billing.service;

import kr.wishtarot.finsys.billing.model.Sale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import kr.wishtarot.finsys.accounting.mapper.AccountingEntriesMapper;
import kr.wishtarot.finsys.accounting.model.AccountingEntry;
import kr.wishtarot.finsys.audit.mapper.AuditLogsMapper;
import kr.wishtarot.finsys.audit.model.AuditLog;
import kr.wishtarot.finsys.billing.mapper.CookieTransactionsMapper;
import kr.wishtarot.finsys.billing.mapper.UserCookiesMapper;
import kr.wishtarot.finsys.billing.mapper.SalesMapper;
import kr.wishtarot.finsys.billing.model.CookieTransaction;
import kr.wishtarot.finsys.billing.model.UserCookie;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Service
public class CookieService {
    private static final Logger logger = LoggerFactory.getLogger(CookieService.class);

    @Autowired
    private UserCookiesMapper userCookiesMapper;

    @Autowired
    private CookieTransactionsMapper cookieTransactionsMapper;

    @Autowired
    private SalesMapper salesMapper;

    @Autowired
    private AccountingEntriesMapper accountingEntriesMapper;

    @Autowired
    private AuditLogsMapper auditLogsMapper;

    @Transactional
    public void useCookies(String userId, BigDecimal amount, Long serviceId, String ipAddress) {
        logger.info("Using cookies for user " + userId + " with amount " + amount + " for service ID " + serviceId);
        // 사용자 쿠키 잔액 확인
        UserCookie userCookie = userCookiesMapper.selectUserCookieByUserId(userId);
        if (userCookie == null || userCookie.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient cookie balance");
        }
        logger.info("User " + userId + " has enough cookies to use");

        // 쿠키 잔액 차감
        userCookie.setBalance(userCookie.getBalance().subtract(amount));
        userCookie.setLastUpdated(new Timestamp(System.currentTimeMillis()));
        userCookiesMapper.updateUserCookie(userCookie);
        logger.info("User " + userId + " has used " + amount + " cookies");

        // 쿠키 사용 내역 기록
        CookieTransaction cookieTransaction = new CookieTransaction();
        cookieTransaction.setUserId(userId);
        cookieTransaction.setAmount(amount);
        cookieTransaction.setTransactionType("쿠키 사용");
        cookieTransaction.setTransactionDate(new Timestamp(System.currentTimeMillis()));
        cookieTransaction.setServiceId(serviceId);
        cookieTransactionsMapper.insertCookieTransaction(cookieTransaction);
        logger.info("Cookie transaction record created for user " + userId);

        // 매출 기록
        Sale sale = new Sale();
        sale.setUserId(userId);
        sale.setAmount(amount);
        sale.setSaleDate(new Timestamp(System.currentTimeMillis()));
        sale.setServiceId(serviceId);
        salesMapper.insertSale(sale);
        logger.info("Sale record created for user " + userId);

        // 회계 처리
        recordAccountingEntries(cookieTransaction.getTransactionId(), amount);
        logger.info("Accounting entries recorded for user " + userId);

        // 감사 로그 기록
        recordAuditLog(userId, "쿠키 사용", "서비스 ID " + serviceId + "를 위해 쿠키 " + amount + "개 사용", ipAddress);
        logger.info("Audit log recorded for user " + userId);
    }

    private void recordAccountingEntries(Long transactionId, BigDecimal amount) {
        // 차변: 이연수익 감소
        AccountingEntry debitEntry = new AccountingEntry();
        debitEntry.setTransactionId(transactionId);
        debitEntry.setAccountCode("202");
        debitEntry.setAccountName("이연수익");
        debitEntry.setDebit(amount);
        debitEntry.setCredit(BigDecimal.ZERO);
        debitEntry.setEntryDate(new Timestamp(System.currentTimeMillis()));
        debitEntry.setDescription("쿠키 사용 - 이연수익 감소");
        accountingEntriesMapper.insertAccountingEntry(debitEntry);

        // 대변: 매출 증가
        AccountingEntry creditEntry = new AccountingEntry();
        creditEntry.setTransactionId(transactionId);
        creditEntry.setAccountCode("401");
        creditEntry.setAccountName("매출");
        creditEntry.setDebit(BigDecimal.ZERO);
        creditEntry.setCredit(amount);
        creditEntry.setEntryDate(new Timestamp(System.currentTimeMillis()));
        creditEntry.setDescription("쿠키 사용 - 매출 인식");
        accountingEntriesMapper.insertAccountingEntry(creditEntry);
    }

    private void recordAuditLog(String userId, String action, String description, String ipAddress) {
        AuditLog auditLog = new AuditLog();
        auditLog.setUserId(userId);
        auditLog.setAction(action);
        auditLog.setDescription(description);
        auditLog.setIpAddress(ipAddress); // 실제 IP 주소로 대체
        auditLog.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        auditLogsMapper.insertAuditLog(auditLog);
    }
}
