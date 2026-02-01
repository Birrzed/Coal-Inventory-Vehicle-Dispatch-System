package service;

import model.Dispatch;
import model.Payment;
import java.util.List;

public interface PaymentService {
    void calculateAndProcessPayment(int dispatchId);

    List<Payment> getAllPayments();

    void confirmPaymentReceipt(int paymentId);

    List<Payment> getTransporterPayments(int transporterId);
}
