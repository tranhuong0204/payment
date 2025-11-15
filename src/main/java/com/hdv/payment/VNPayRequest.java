package com.hdv.payment;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
//import org.hibernate.annotations.Table;

@Entity
//@Table(name = "vnpay_requests")
public class VNPayRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String orderId; // Mã đơn từ Main Project gửi sang
    private Double amount;
    private String status;         // PENDING, SUCCESS, FAILED
    private String orderInfo;
    private String returnUrl;
    private String ipnUrl;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(String orderInfo) {
        this.orderInfo = orderInfo;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }

    public String getIpnUrl() {
        return ipnUrl;
    }

    public void setIpnUrl(String ipnUrl) {
        this.ipnUrl = ipnUrl;
    }
}
