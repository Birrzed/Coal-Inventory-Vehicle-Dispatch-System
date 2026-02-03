package service;

import model.Payment;
import java.util.List;

public interface PaymentService {
    void calculateAndProcessPayment(int dispatchId);

    List<Payment> getAllPayments();

    List<Payment> getPaymentsForTransporter(int transporterId);

    void confirmPaymentReceipt(int paymentId);
}
