package kr.wishtarot.finsys.payment.service;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.androidpublisher.AndroidPublisher;
import com.google.api.services.androidpublisher.model.ProductPurchase;
import com.google.auth.oauth2.GoogleCredentials;
import kr.wishtarot.finsys.accounting.mapper.AccountingEntriesMapper;
import kr.wishtarot.finsys.accounting.model.AccountingEntry;
import kr.wishtarot.finsys.audit.mapper.AuditLogsMapper;
import kr.wishtarot.finsys.audit.model.AuditLog;
import kr.wishtarot.finsys.billing.mapper.CookieTransactionsMapper;
import kr.wishtarot.finsys.billing.mapper.UserCookiesMapper;
import kr.wishtarot.finsys.billing.model.CookieTransaction;
import kr.wishtarot.finsys.billing.model.UserCookie;
import kr.wishtarot.finsys.payment.model.PaymentReceipt;
import kr.wishtarot.finsys.payment.model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import kr.wishtarot.finsys.payment.mapper.*;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.FileInputStream;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Collections;

@Service
public class ReceiptVerificationService {
    private static final Logger logger = LoggerFactory.getLogger(ReceiptVerificationService.class);

    // application.properties에서 경로를 가져옴
    @Value("${keyFilePath}")
    private String keyFilePath;

    @Autowired
    private TransactionMapper transactionMapper;

    @Autowired
    private PaymentReceiptMapper paymentReceiptMapper;

    @Autowired
    private UserCookiesMapper userCookiesMapper;

    @Autowired
    private CookieTransactionsMapper cookieTransactionsMapper;

    @Autowired
    private AccountingEntriesMapper accountingEntriesMapper;

    @Autowired
    private AuditLogsMapper auditLogsMapper;

    @Transactional
    public boolean verifyAndProcessPurchase(String receiptData, String productId, String userId, String ipAddress) {
        logger.info("start verifyAndProcessPurchase");
        // 이미 처리된 영수증인지 확인
        PaymentReceipt existingReceipt = paymentReceiptMapper.selectPaymentReceiptByReceiptData(receiptData);
        if (existingReceipt != null) {
            logger.info("already processed receipt");
            return false; // 이미 처리된 영수증
        }

        // 새로운 transaction 생성 (상태는 PENDING)
        Transaction transaction = new Transaction();
        transaction.setUserId(userId);
        transaction.setPaymentMethod("Google Play In-App Billing");
        BigDecimal amount = getAmountByProductId(productId);
        transaction.setAmount(amount);
        transaction.setStatus("PENDING");
        transaction.setTransactionDate(new Timestamp(System.currentTimeMillis()));
        transactionMapper.insertTransaction(transaction);

        // 영수증 데이터 저장
        PaymentReceipt receipt = new PaymentReceipt();
        receipt.setTransactionId(transaction.getTransactionId());
        receipt.setReceiptData(receiptData);
        receipt.setVerificationStatus("PENDING");
        paymentReceiptMapper.insertPaymentReceipt(receipt);

        // 구글 서버로 영수증 검증 요청
        boolean isValid = verifyReceiptWithGoogle(receiptData, productId);
        logger.info("isValid: " + isValid);

        if (isValid) {
            // 거래 상태 업데이트
            logger.info("transactionId: " + transaction.getTransactionId());
            transactionMapper.updateTransactionStatus(transaction.getTransactionId(), "COMPLETED");
            logger.info("transaction updated");
            logger.info("receiptId: " + receipt.getReceiptId());
            paymentReceiptMapper.updateVerificationStatus(receipt.getReceiptId(), "VERIFIED");
            logger.info("receipt updated");

            // 사용자 쿠키 잔액 업데이트
            updateUserCookieBalance(userId, amount);

            // 쿠키 충전 내역 기록
            recordCookieTransaction(userId, amount, transaction.getTransactionId());

            // 회계 처리
            recordAccountingEntries(transaction.getTransactionId(), amount);

            // 감사 로그 기록
            recordAuditLog(userId, "쿠키 충전", amount + "원 결제하여 쿠키 " + amount + "개 충전", ipAddress);

            return true;
        } else {
            // 거래 실패 처리
            transactionMapper.updateTransactionStatus(transaction.getTransactionId(), "FAILED");
            paymentReceiptMapper.updateVerificationStatus(receipt.getReceiptId(), "FAILED");
            return false;
        }
    }

    private boolean verifyReceiptWithGoogle(String purchaseToken, String productId) {
        logger.info("start verifyReceiptWithGoogle");
        try {
            // 서비스 계정 키 파일 경로 (resources 폴더에 저장된 경우)
//            String keyFilePath = getClass().getClassLoader().getResource("service-account-key.json").getPath();
            logger.info("keyFilePath: " + keyFilePath);
            // 인증 설정
            GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream(keyFilePath))
                    .createScoped(Collections.singleton("https://www.googleapis.com/auth/androidpublisher"));

            logger.info("credentials: " + credentials);
            // HTTP 전송 설정
            HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();

            AndroidPublisher publisher = new AndroidPublisher.Builder(httpTransport, JacksonFactory.getDefaultInstance(), new HttpCredentialsAdapter(credentials))
                    .setApplicationName("kr.wishtarot.tarotapp")
                    .build();

            // 영수증 검증 요청
            AndroidPublisher.Purchases.Products.Get request = publisher.purchases().products().get(
                    "com.whisperofstar.tarotapp", // 앱의 패키지 이름
                    productId,
                    purchaseToken
            );

            ProductPurchase purchase = request.execute();
            logger.info("purchase : " + purchase.toPrettyString());

            // 구매 상태 확인 (0: 구매 완료)
            logger.info("purchase state: " + purchase.getPurchaseState());
            return purchase.getPurchaseState() == 0;
        } catch (GoogleJsonResponseException e) {
            logger.error("Google API returned error: " + e.getDetails().getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }


    private BigDecimal getAmountByProductId(String productId) {
        // 상품 ID에 따라 금액 반환
        // 실제 상품 가격에 맞게 설정
        if (productId.equals("samplecookie200")) {
            return BigDecimal.valueOf(200);
        } else if (productId.equals("samplecookie500")) {
            return BigDecimal.valueOf(500);
        } else if (productId.equals("samplecookie1000")) {
            return BigDecimal.valueOf(1000);
        }
        return BigDecimal.ZERO;
    }

    private void updateUserCookieBalance(String userId, BigDecimal amount) {
        UserCookie userCookie = userCookiesMapper.selectUserCookieByUserId(userId);
        if (userCookie == null) {
            userCookie = new UserCookie();
            userCookie.setUserId(userId);
            userCookie.setBalance(amount);
            userCookie.setLastUpdated(new Timestamp(System.currentTimeMillis()));
            userCookiesMapper.insertUserCookie(userCookie);
        } else {
            userCookie.setBalance(userCookie.getBalance().add(amount));
            userCookie.setLastUpdated(new Timestamp(System.currentTimeMillis()));
            userCookiesMapper.updateUserCookie(userCookie);
        }
    }

    private void recordCookieTransaction(String userId, BigDecimal amount, Long transactionId) {
        CookieTransaction cookieTransaction = new CookieTransaction();
        cookieTransaction.setUserId(userId);
        cookieTransaction.setAmount(amount);
        cookieTransaction.setTransactionType("charge");
        cookieTransaction.setTransactionDate(new Timestamp(System.currentTimeMillis()));
        cookieTransaction.setServiceId(null); // 쿠키 충전이므로 서비스 ID는 없음
        cookieTransactionsMapper.insertCookieTransaction(cookieTransaction);
    }

    private void recordAccountingEntries(Long transactionId, BigDecimal amount) {
        // 차변: 현금 (Cash)
        AccountingEntry debitEntry = new AccountingEntry();
        debitEntry.setTransactionId(transactionId);
        debitEntry.setAccountCode("101");
        debitEntry.setAccountName("현금");
        debitEntry.setDebit(amount);
        debitEntry.setCredit(BigDecimal.ZERO);
        debitEntry.setEntryDate(new Timestamp(System.currentTimeMillis()));
        debitEntry.setDescription("쿠키 충전 - 현금 수령");
        accountingEntriesMapper.insertAccountingEntry(debitEntry);

        // 대변: 이연수익 (Deferred Revenue)
        AccountingEntry creditEntry = new AccountingEntry();
        creditEntry.setTransactionId(transactionId);
        creditEntry.setAccountCode("202");
        creditEntry.setAccountName("이연수익");
        creditEntry.setDebit(BigDecimal.ZERO);
        creditEntry.setCredit(amount);
        creditEntry.setEntryDate(new Timestamp(System.currentTimeMillis()));
        creditEntry.setDescription("쿠키 충전 - 이연수익 발생");
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
