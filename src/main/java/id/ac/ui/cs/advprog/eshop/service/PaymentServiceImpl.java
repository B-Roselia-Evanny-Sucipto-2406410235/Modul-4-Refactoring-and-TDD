package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.enums.OrderStatus;
import id.ac.ui.cs.advprog.eshop.enums.PaymentStatus;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;

    public PaymentServiceImpl(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    public Payment addPayment(Order order, String method, Map<String, String> paymentData) {
        String paymentId = UUID.randomUUID().toString();
        Payment payment = new Payment(paymentId, method, PaymentStatus.PENDING.getValue(), paymentData);

        if ("VOUCHER".equals(method)) {
            String voucherCode = paymentData.get("voucherCode");
            if (voucherCode != null
                    && voucherCode.length() == 16
                    && voucherCode.startsWith("ESHOP")
                    && voucherCode.replaceAll("\\D", "").length() == 8) {
                setStatus(payment, PaymentStatus.SUCCESS.getValue(), order);
            } else {
                setStatus(payment, PaymentStatus.REJECTED.getValue(), order);
            }
        } else if ("CASH_ON_DELIVERY".equals(method)) {
            if (!paymentData.containsKey("address") || !paymentData.containsKey("deliveryFee")) {
                setStatus(payment, PaymentStatus.REJECTED.getValue(), order);
            } else {
                String address = paymentData.get("address");
                String deliveryFee = paymentData.get("deliveryFee");

                if (address == null || address.isBlank() || deliveryFee == null || deliveryFee.isBlank()) {
                    setStatus(payment, PaymentStatus.REJECTED.getValue(), order);
                } else {
                    setStatus(payment, PaymentStatus.SUCCESS.getValue(), order);
                }
            }
        } else {
            setStatus(payment, PaymentStatus.REJECTED.getValue(), order);
        }

        return payment;
    }

    @Override
    public Payment setStatus(Payment payment, String status, Order order) {
        if (!PaymentStatus.contains(status)) {
            throw new IllegalArgumentException("Invalid payment status");
        }
        payment.setStatus(status);

        if (PaymentStatus.SUCCESS.getValue().equals(status)) {
            order.setStatus(OrderStatus.SUCCESS.getValue());
        }

        return paymentRepository.save(payment);
    }

    @Override
    public Payment getPayment(String paymentId) {
        return paymentRepository.findById(paymentId);
    }

    @Override
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }
}