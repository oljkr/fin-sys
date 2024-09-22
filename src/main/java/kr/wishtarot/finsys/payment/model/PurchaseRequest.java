package kr.wishtarot.finsys.payment.model;

import lombok.Data;

@Data
public class PurchaseRequest {
    private String purchaseToken;
    private String productId;
    private String userId;
}
