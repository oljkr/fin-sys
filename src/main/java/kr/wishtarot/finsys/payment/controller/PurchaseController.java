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

@RestController
@RequestMapping("/api/purchase")
public class PurchaseController {

    private static final Logger logger = LoggerFactory.getLogger(PurchaseController.class);

    @Autowired
    private ReceiptVerificationService receiptVerificationService;

    @PostMapping("/verify")
    public ResponseEntity<?> verifyPurchase(@RequestBody PurchaseRequest request) {
        logger.info("start verifyAndProcessPurchase");
        logger.info("purchaseToken: " + request.getPurchaseToken());
        logger.info("productId: " + request.getProductId());
        logger.info("userId: " + request.getUserId());

        boolean result = true;
//        boolean result = receiptVerificationService.verifyAndProcessPurchase(
//                request.getPurchaseToken(),
//                request.getProductId(),
//                request.getUserId()
//        );

        if (result) {
            return ResponseEntity.ok("Purchase verified and processed");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Purchase verification failed");
        }
    }
}
