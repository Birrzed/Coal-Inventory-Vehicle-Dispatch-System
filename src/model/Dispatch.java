package model;

import java.sql.Date;

public class Dispatch {
    private int id;
    private double productMass;
    private Date dispatchDate;
    private Date confirmDate;
    private Date arrivalDate;
    private int sellerId;
    private int transporterId;
    private int destinationId;
    private String status;
    private String transporterName;
    private String destinationName;

    public Dispatch(int id, double productMass, Date dispatchDate, Date confirmDate, Date arrivalDate,
                    int sellerId, int transporterId, int destinationId, String status) {
        this(id, productMass, dispatchDate, confirmDate, arrivalDate, sellerId, transporterId, destinationId, status,
                null, null);
    }

    public Dispatch(int id, double productMass, Date dispatchDate, Date confirmDate, Date arrivalDate,
                    int sellerId, int transporterId, int destinationId, String status, String transporterName,
                    String destinationName) {
        this.id = id;
        this.productMass = productMass;
        this.dispatchDate = dispatchDate;
        this.confirmDate = confirmDate;
        this.arrivalDate = arrivalDate;
        this.sellerId = sellerId;
        this.transporterId = transporterId;
        this.destinationId = destinationId;
        this.status = status;
        this.transporterName = transporterName;
        this.destinationName = destinationName;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getProductMass() {
        return productMass;
    }

    public void setProductMass(double productMass) {
        this.productMass = productMass;
    }

    public Date getDispatchDate() {
        return dispatchDate;
    }

    public void setDispatchDate(Date dispatchDate) {
        this.dispatchDate = dispatchDate;
    }

    public Date getConfirmDate() {
        return confirmDate;
    }

    public void setConfirmDate(Date confirmDate) {
        this.confirmDate = confirmDate;
    }

    public Date getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(Date arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public int getSellerId() {
        return sellerId;
    }

    public void setSellerId(int sellerId) {
        this.sellerId = sellerId;
    }

    public int getTransporterId() {
        return transporterId;
    }

    public void setTransporterId(int transporterId) {
        this.transporterId = transporterId;
    }

    public int getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(int destinationId) {
        this.destinationId = destinationId;
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

    public String getDestinationName() {
        return destinationName;
    }

    public void setDestinationName(String destinationName) {
        this.destinationName = destinationName;
    }
}
