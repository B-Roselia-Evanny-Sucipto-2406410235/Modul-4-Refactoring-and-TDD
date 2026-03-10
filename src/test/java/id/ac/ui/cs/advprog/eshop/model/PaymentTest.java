package id.ac.ui.cs.advprog.eshop.model;

import id.ac.ui.cs.advprog.eshop.enums.PaymentStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class PaymentTest {
    private String id;
    private String method;
    private Map<String, String> paymentData;

    @BeforeEach
    void setUp() {
        this.id = "32039f0f-32ae-40a7-844d-c32af2958bca";
        this.method = "VOUCHER";
        this.paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP1234ABC5678");
    }

    @Test
    void testCreatePaymentDefaultStatus() {
        Payment payment = new Payment(this.id, this.method, this.paymentData);

        assertEquals(this.id, payment.getId());
        assertEquals(this.method, payment.getMethod());
        assertEquals(PaymentStatus.PENDING.getValue(), payment.getStatus());
        assertEquals("ESHOP1234ABC5678", payment.getPaymentData().get("voucherCode"));
    }

    @Test
    void testCreatePaymentSuccessStatus() {
        Payment payment = new Payment(this.id, this.method, PaymentStatus.SUCCESS.getValue(), this.paymentData);
        assertEquals(PaymentStatus.SUCCESS.getValue(), payment.getStatus());
    }

    @Test
    void testCreatePaymentRejectedStatus() {
        Payment payment = new Payment(this.id, this.method, PaymentStatus.REJECTED.getValue(), this.paymentData);
        assertEquals(PaymentStatus.REJECTED.getValue(), payment.getStatus());
    }

    @Test
    void testCreatePaymentInvalidStatus() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Payment(this.id, this.method, "MEOW", this.paymentData);
        });
    }

    @Test
    void testCreatePaymentWithEmptyData() {
        Map<String, String> emptyData = new HashMap<>();
        assertThrows(IllegalArgumentException.class, () -> {
            new Payment(id, method, emptyData);
        });
    }

    @Test
    void testSetStatusToSuccess() {
        Payment payment = new Payment(this.id, this.method, this.paymentData);
        payment.setStatus(PaymentStatus.SUCCESS.getValue());
        assertEquals(PaymentStatus.SUCCESS.getValue(), payment.getStatus());
    }

    @Test
    void testSetStatusToRejected() {
        Payment payment = new Payment(this.id, this.method, this.paymentData);
        payment.setStatus(PaymentStatus.REJECTED.getValue());
        assertEquals(PaymentStatus.REJECTED.getValue(), payment.getStatus());
    }
}
