package model;

import java.sql.Date;

public class Payment {
    private int id;
    private int dispatchId;
    private double amount;
    private Date paymentDate;
    private String status;
    private String transporterName;

    public Payment(int id, int dispatchId, double amount, Date paymentDate, String status) {
        this(id, dispatchId, amount, paymentDate, status, null);
    }

    public Payment(int id, int dispatchId, double amount, Date paymentDate, String status, String transporterName) {
        this.id = id;
        this.dispatchId = dispatchId;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.status = status;
        this.transporterName = transporterName;
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

    public String getTransporterName() {
        return transporterName;
    }

    public void setTransporterName(String transporterName) {
        this.transporterName = transporterName;
    }
}
