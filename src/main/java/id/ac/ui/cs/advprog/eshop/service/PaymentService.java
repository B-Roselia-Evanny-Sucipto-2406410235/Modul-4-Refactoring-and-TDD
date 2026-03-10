package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Order;
import java.util.List;
import java.util.Map;

public interface PaymentService {
    Payment addPayment(Order order, String method, Map<String, String> paymentData);
    Payment setStatus(Payment payment, String status, Order order);
    Payment getPayment(String paymentId);
    List<Payment> getAllPayments();
}