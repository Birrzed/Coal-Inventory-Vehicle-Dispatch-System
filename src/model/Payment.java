package model;

import java.sql.Date;

public class Payment {
    private int id;
    private int dispatchId;
    private double amount;
    private Date paymentDate;
    private String status;

    public Payment(int id, int dispatchId, double amount, Date paymentDate, String status) {
        this.id = id;
        this.dispatchId = dispatchId;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDispatchId() {
        return dispatchId;
    }

    public void setDispatchId(int dispatchId) {
        this.dispatchId = dispatchId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
