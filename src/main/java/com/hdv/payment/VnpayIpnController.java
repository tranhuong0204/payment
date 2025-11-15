package com.hdv.payment;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@RestController
@RequestMapping("/payment/vnpay")
public class VnpayIpnController {

    private final PaymentTransactionRepository repo;
    private static final String VNP_HASHSECRET = "5P56F0FIWJVCRBST3WQH4EW6T2TKXT0M";

    public VnpayIpnController(PaymentTransactionRepository repo) {
        this.repo = repo;
    }

    @PostMapping("/ipn")
    public ResponseEntity<String> ipn(@RequestParam Map<String, String> params) {
        String secureHash = params.get("vnp_SecureHash");
        params.remove("vnp_SecureHash");
        params.remove("vnp_SecureHashType");

        String hashData = VnpUtils.buildQuery(params);
        String myHash = VnpUtils.hmacSHA512(VNP_HASHSECRET, hashData);

        if (!myHash.equalsIgnoreCase(secureHash)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid checksum");
        }

        String orderId = params.get("vnp_TxnRef");
        String responseCode = params.get("vnp_ResponseCode");
        String transactionNo = params.get("vnp_TransactionNo");

        PaymentTransaction tx = repo.findByOrderId(orderId).orElseGet(PaymentTransaction::new);
        tx.setOrderId(orderId);
        tx.setVnpResponseCode(responseCode);
        tx.setVnpTransactionNo(transactionNo);
        tx.setAmount(Long.parseLong(params.getOrDefault("vnp_Amount", "0")) / 100);

        if ("00".equals(responseCode)) {
            tx.setStatus("SUCCESS");
            // TODO: call-back sang Main Project để cập nhật hóa đơn/đơn hàng
        } else {
            tx.setStatus("FAILED");
        }

        repo.save(tx);
        return ResponseEntity.ok("OK");
    }
}

