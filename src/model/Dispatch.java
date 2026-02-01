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

    public Dispatch(int id, double productMass, Date dispatchDate, Date confirmDate, Date arrivalDate,
                    int sellerId, int transporterId, int destinationId, String status) {
        this.id = id;
        this.productMass = productMass;
        this.dispatchDate = dispatchDate;
        this.confirmDate = confirmDate;
        this.arrivalDate = arrivalDate;
        this.sellerId = sellerId;
        this.transporterId = transporterId;
        this.destinationId = destinationId;
        this.status = status;
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
}
