package id.ac.ui.cs.advprog.eshop.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.Map;

@Getter
@Setter
public class Payment {
    String id;
    String method;
    String status;
    Map<String, String> paymentData;

    public Payment(String id, String method, Map<String, String> paymentData) {
        this.id = id;
        this.method = method;
        this.status = "PENDING";

        if (paymentData.isEmpty()) {
            throw new IllegalArgumentException();
        } else {
            this.paymentData = paymentData;
        }
    }

    public Payment(String id, String method, String status, Map<String, String> paymentData) {
        this(id, method, paymentData);
        this.setStatus(status);
    }

    public void setStatus(String status) {
        String[] validStatuses = {"PENDING", "SUCCESS", "REJECTED"};
        if (Arrays.stream(validStatuses).noneMatch(valid -> valid.equals(status))) {
            throw new IllegalArgumentException();
        }
        this.status = status;
    }
}
