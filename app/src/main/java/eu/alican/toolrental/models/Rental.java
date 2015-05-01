package eu.alican.toolrental.models;

import java.util.Date;

/**
 * Created by alican on 20.04.2015.
 */
public class Rental {
    private int _id;
    private int productId;
    private int locationId;
    private Date startDate;
    private Date endDate;

    public Rental(int id, int productId, int locationId, Date startDate, Date endDate) {
        this._id = id;
        this.productId = productId;
        this.locationId = locationId;
        this.startDate = startDate;
        this.endDate = endDate;
    }


    public boolean isInRent(){
        return true;
    }

    public float currentPrice(){
        return 0.2F;
    }

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        this._id = id;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
}
