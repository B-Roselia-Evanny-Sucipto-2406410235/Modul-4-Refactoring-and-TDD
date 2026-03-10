package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.enums.PaymentStatus;
import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceImplTest {
    @InjectMocks
    PaymentServiceImpl paymentService;

    @Mock
    PaymentRepository paymentRepository;

    List<Order> orders;
    Map<String, String> paymentDataVoucher;

    @BeforeEach
    void setUp() {
        List<Product> products = new ArrayList<>();
        Product product1 = new Product();
        product1.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product1.setProductName("Sampo Cap Bambang");
        product1.setProductQuantity(2);
        products.add(product1);

        List<Product> products2 = new ArrayList<>();
        Product product2 = new Product();
        product2.setProductId("a2c62328-4a37-4664-83c7-f32db8620155");
        product2.setProductName("Sabun Cap Usep");
        product2.setProductQuantity(1);
        products2.add(product2);

        orders = new ArrayList<>();
        Order order1 = new Order("13652556-012a-4c07-b546-54eb1396d79b",
                products, 1708560000L, "Safira Sudrajat");
        orders.add(order1);
        Order order2 = new Order("7f9e15bb-4b15-42f4-aebc-c3af385fb078",
                products2, 1708570000L, "Safira Sudrajat");
        orders.add(order2);

        paymentDataVoucher = Map.of("voucherCode", "ESHOP1234ABC5678");
    }

    @Test
    void testAddPaymentVoucherSuccess() {
        Payment payment = new Payment("8af9414d-4010-4000-a0e9-1900eb217417", "VOUCHER", paymentDataVoucher);

        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        Payment result = paymentService.addPayment(orders.get(0), "VOUCHER", paymentDataVoucher);

        assertEquals(PaymentStatus.SUCCESS.getValue(), result.getStatus());
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void testAddPaymentVoucherInvalidLength() {
        Map<String, String> invalidVoucher = Map.of("voucherCode", "ESHOP1234ABC567");
        Payment payment = new Payment("c61d39c4-e6f0-4a62-ad8b-eccd28e76dc3", "VOUCHER", invalidVoucher);

        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        Payment result = paymentService.addPayment(orders.get(0), "VOUCHER", invalidVoucher);

        assertEquals(PaymentStatus.REJECTED.getValue(), result.getStatus());
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void testAddPaymentVoucherNoPrefix() {
        Map<String, String> invalidVoucher = Map.of("voucherCode", "1234ABC5678");
        Payment payment = new Payment("c61d39c4-e6f0-4a62-ad8b-eccd28e76dc3", "VOUCHER", invalidVoucher);

        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        Payment result = paymentService.addPayment(orders.get(0), "VOUCHER", invalidVoucher);

        assertEquals(PaymentStatus.REJECTED.getValue(), result.getStatus());
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void testAddPaymentVoucherNo8Digits() {
        Map<String, String> invalidVoucher = Map.of("voucherCode", "ESHOPESHOPESHOPE");
        Payment payment = new Payment("c61d39c4-e6f0-4a62-ad8b-eccd28e76dc3", "VOUCHER", invalidVoucher);

        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        Payment result = paymentService.addPayment(orders.get(0), "VOUCHER", invalidVoucher);

        assertEquals(PaymentStatus.REJECTED.getValue(), result.getStatus());
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void testSetStatusSuccess() {
        Payment payment = new Payment("c61d39c4-e6f0-4a62-ad8b-eccd28e76dc3",
                "VOUCHER", paymentDataVoucher);

        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        Payment result = paymentService.setStatus(payment, PaymentStatus.SUCCESS.getValue(), orders.get(0));

        assertEquals(PaymentStatus.SUCCESS.getValue(), result.getStatus());
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void testSetStatusRejected() {
        Payment payment = new Payment("c61d39c4-e6f0-4a62-ad8b-eccd28e76dc3",
                "VOUCHER", PaymentStatus.PENDING.getValue(), paymentDataVoucher);

        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        Payment result = paymentService.setStatus(payment, PaymentStatus.REJECTED.getValue(), orders.get(0));

        assertEquals(PaymentStatus.REJECTED.getValue(), result.getStatus());
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void testSetStatusInvalid() {
        Payment payment = new Payment("c61d39c4-e6f0-4a62-ad8b-eccd28e76dc3",
                "VOUCHER", PaymentStatus.PENDING.getValue(), paymentDataVoucher);

        assertThrows(IllegalArgumentException.class, () -> paymentService.setStatus(payment, "MEOW", orders.get(0)));
        verify(paymentRepository, times(0)).save(any(Payment.class));
    }

    @Test
    void testGetPayment() {
        Payment payment = new Payment("c61d39c4-e6f0-4a62-ad8b-eccd28e76dc3",
                "VOUCHER", PaymentStatus.PENDING.getValue(), paymentDataVoucher);

        when(paymentRepository.findById(payment.getId())).thenReturn(payment);

        Payment result = paymentService.getPayment(payment.getId());

        assertEquals(payment.getId(), result.getId());
        verify(paymentRepository, times(1)).findById(payment.getId());
    }

    @Test
    void testGetAllPayments() {
        List<Payment> payments = Arrays.asList(
                new Payment("c61d39c4-e6f0-4a62-ad8b-eccd28e76dc3",
                        "VOUCHER", PaymentStatus.PENDING.getValue(), paymentDataVoucher),
                new Payment("9446c372-122c-4a15-9b7e-b7fcf6833831",
                        "VOUCHER", PaymentStatus.PENDING.getValue(), paymentDataVoucher)
        );

        when(paymentRepository.findAll()).thenReturn(payments);

        List<Payment> result = paymentService.getAllPayments();

        assertEquals(2, result.size());
        verify(paymentRepository, times(1)).findAll();
    }
}