package service.impl;

import dao.DispatchDAO;
import dao.PaymentDAO;
import model.Dispatch;
import model.Payment;
import service.PaymentService;

import java.sql.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class PaymentServiceImpl implements PaymentService {
    private PaymentDAO paymentDAO;
    private DispatchDAO dispatchDAO;

    public PaymentServiceImpl() {
        this.paymentDAO = new PaymentDAO();
        this.dispatchDAO = new DispatchDAO();
    }

    @Override
    public void calculateAndProcessPayment(int dispatchId) {
        // 1. Check if payment already exists
        if (paymentDAO.checkPaymentExists(dispatchId)) {
            System.out.println("Payment already processed for dispatch ID: " + dispatchId);
            return;
        }

        // 2. Get Dispatch details
        Dispatch dispatch = dispatchDAO.getDispatchById(dispatchId);
        if (dispatch == null || !dispatch.getStatus().equals("Delivered")) {
            System.out.println("Dispatch not eligible for payment (Must be Delivered).");
            return;
        }

        // 3. Calculate Duration
        long diffInMillies = Math.abs(dispatch.getArrivalDate().getTime() - dispatch.getDispatchDate().getTime());
        long durationDays = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

        // 4. Calculate Rate per Ton
        // Rule: <= 3 days -> 4000, > 3 days -> 3000
        // Note: Counting inclusive days or strict difference?
        // Standard "arrival - dispatch" calculation implies pure difference.
        // e.g. Dispatch Jan 1, Arrive Jan 4 = 3 days. Arrive Jan 5 = 4 days.

        double ratePerTon = (durationDays <= 3) ? 4000.0 : 3000.0;
        double totalAmount = ratePerTon * dispatch.getProductMass();

        // 5. Save Payment
        Date today = new Date(System.currentTimeMillis());
        Payment payment = new Payment(0, dispatchId, totalAmount, today, "Paid");
        paymentDAO.savePayment(payment);

        // 6. Update Dispatch Status to Completed
        dispatchDAO.updateStatus(dispatchId, "Completed");
    }

    @Override
    public List<Payment> getAllPayments() {
        return paymentDAO.getAllPayments();
    }

    @Override
    public void confirmPaymentReceipt(int paymentId) {
        paymentDAO.updatePaymentStatus(paymentId, "Received");
    }

    @Override
    public List<Payment> getTransporterPayments(int transporterId) {
        return paymentDAO.getPaymentsByTransporter(transporterId);
    }
}
