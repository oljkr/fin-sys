package kr.wishtarot.finsys.billing.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CookieService {

//    @Autowired
//    private UserCookiesMapper userCookiesMapper;
//
//    @Autowired
//    private CookieTransactionsMapper cookieTransactionsMapper;
//
//    @Autowired
//    private SalesMapper salesMapper;
//
//    @Autowired
//    private AccountingEntriesMapper accountingEntriesMapper;
//
//    @Autowired
//    private AuditLogsMapper auditLogsMapper;
//
//    @Transactional
//    public void useCookies(Long userId, BigDecimal amount, Long serviceId) {
//        // 사용자 쿠키 잔액 확인
//        UserCookie userCookie = userCookiesMapper.selectUserCookieByUserId(userId);
//        if (userCookie == null || userCookie.getBalance().compareTo(amount) < 0) {
//            throw new RuntimeException("Insufficient cookie balance");
//        }
//
//        // 쿠키 잔액 차감
//        userCookie.setBalance(userCookie.getBalance().subtract(amount));
//        userCookie.setLastUpdated(new Timestamp(System.currentTimeMillis()));
//        userCookiesMapper.updateUserCookie(userCookie);
//
//        // 쿠키 사용 내역 기록
//        CookieTransaction cookieTransaction = new CookieTransaction();
//        cookieTransaction.setUserId(userId);
//        cookieTransaction.setAmount(amount);
//        cookieTransaction.setTransactionType("쿠키 사용");
//        cookieTransaction.setTransactionDate(new Timestamp(System.currentTimeMillis()));
//        cookieTransaction.setServiceId(serviceId);
//        cookieTransactionsMapper.insertCookieTransaction(cookieTransaction);
//
//        // 매출 기록
//        Sale sale = new Sale();
//        sale.setUserId(userId);
//        sale.setAmount(amount);
//        sale.setSaleDate(new Timestamp(System.currentTimeMillis()));
//        sale.setServiceId(serviceId);
//        salesMapper.insertSale(sale);
//
//        // 회계 처리
//        recordAccountingEntries(cookieTransaction.getTransactionId(), amount);
//
//        // 감사 로그 기록
//        recordAuditLog(userId, "쿠키 사용", "서비스 ID " + serviceId + "를 위해 쿠키 " + amount + "개 사용");
//    }
//
//    private void recordAccountingEntries(Long transactionId, BigDecimal amount) {
//        // 차변: 이연수익 감소
//        AccountingEntry debitEntry = new AccountingEntry();
//        debitEntry.setTransactionId(transactionId);
//        debitEntry.setAccountCode("202");
//        debitEntry.setAccountName("이연수익");
//        debitEntry.setDebit(amount);
//        debitEntry.setCredit(BigDecimal.ZERO);
//        debitEntry.setEntryDate(new Timestamp(System.currentTimeMillis()));
//        debitEntry.setDescription("쿠키 사용 - 이연수익 감소");
//        accountingEntriesMapper.insertAccountingEntry(debitEntry);
//
//        // 대변: 매출 증가
//        AccountingEntry creditEntry = new AccountingEntry();
//        creditEntry.setTransactionId(transactionId);
//        creditEntry.setAccountCode("401");
//        creditEntry.setAccountName("매출");
//        creditEntry.setDebit(BigDecimal.ZERO);
//        creditEntry.setCredit(amount);
//        creditEntry.setEntryDate(new Timestamp(System.currentTimeMillis()));
//        creditEntry.setDescription("쿠키 사용 - 매출 인식");
//        accountingEntriesMapper.insertAccountingEntry(creditEntry);
//    }
//
//    private void recordAuditLog(Long userId, String action, String description) {
//        AuditLog auditLog = new AuditLog();
//        auditLog.setUserId(userId);
//        auditLog.setAction(action);
//        auditLog.setDescription(description);
//        auditLog.setIpAddress("사용자 IP"); // 실제 IP 주소로 대체
//        auditLog.setCreatedAt(new Timestamp(System.currentTimeMillis()));
//        auditLogsMapper.insertAuditLog(auditLog);
//    }
}
