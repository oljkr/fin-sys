package kr.wishtarot.finsys.payment.controller;

import kr.wishtarot.finsys.billing.model.CookieTransaction;
import kr.wishtarot.finsys.billing.service.CookieService;
import kr.wishtarot.finsys.payment.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private CookieService cookieService;

    // 현재 쿠키 갯수 가져오기
    @GetMapping("/current-cookies")
    public ResponseEntity<BigDecimal> getCurrentCookieCount(@RequestParam String userId) {
        BigDecimal cookieCount = transactionService.getCurrentCookieCount(userId);
        return ResponseEntity.ok(cookieCount);
    }

    // 쿠키 구매/충전 내역 가져오기
    @GetMapping("/purchase-history")
    public ResponseEntity<List<CookieTransaction>> getPurchaseHistory(@RequestParam String userId) {
        List<CookieTransaction> purchaseHistory = transactionService.getPurchaseHistory(userId);
        return ResponseEntity.ok(purchaseHistory);
    }

    // 쿠키 사용 내역 가져오기
    @GetMapping("/usage-history")
    public ResponseEntity<List<CookieTransaction>> getUsageHistory(@RequestParam String userId) {
        List<CookieTransaction> usageHistory = transactionService.getUsageHistory(userId);
        return ResponseEntity.ok(usageHistory);
    }

    // 쿠키 사용 API
    @PostMapping("/use-cookies")
    public ResponseEntity<String> useCookies(@RequestParam String userId,
                                             @RequestParam BigDecimal amount,
                                             @RequestParam Long serviceId,
                                             HttpServletRequest request) {
        String ipAddress = request.getRemoteAddr();
        try {
            cookieService.useCookies(userId, amount, serviceId, ipAddress);
            return ResponseEntity.ok("Cookies used successfully for service " + serviceId);
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body("Failed to use cookies: " + e.getMessage());
        }
    }
}
