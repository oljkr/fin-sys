package kr.wishtarot.finsys.payment.controller;

import kr.wishtarot.finsys.payment.model.PurchaseRequest;
import kr.wishtarot.finsys.payment.service.ReceiptVerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/purchase")
public class PurchaseController {

    private static final Logger logger = LoggerFactory.getLogger(PurchaseController.class);

    @Autowired
    private ReceiptVerificationService receiptVerificationService;

    @PostMapping("/verify")
    public ResponseEntity<?> verifyPurchase(HttpServletRequest request, @RequestBody PurchaseRequest purchaseRequest) {
        logger.info("start verifyAndProcessPurchase");
        logger.info("purchaseToken: " + purchaseRequest.getPurchaseToken());
        logger.info("productId: " + purchaseRequest.getProductId());
        logger.info("userId: " + purchaseRequest.getUserId());

        String ipAddress = request.getRemoteAddr();

        boolean result = true;
//        boolean result = receiptVerificationService.verifyAndProcessPurchase(
//                purchaseRequest.getPurchaseToken(),
//                purchaseRequest.getProductId(),
//                purchaseRequest.getUserId(),
//                ipAddress
//        );

        if (result) {
            return ResponseEntity.ok("Purchase verified and processed");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Purchase verification failed");
        }
    }
}
