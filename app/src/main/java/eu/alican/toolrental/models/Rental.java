package eu.alican.toolrental.models;

import java.util.Date;

import eu.alican.toolrental.db.MyDbHandler;
import eu.alican.toolrental.utls.ApplicationContextProvider;

/**
 * Created by alican on 20.04.2015.
 */
public class Rental {
    private int _id;
    private int productId;
    private int locationId;
    private Date startDate;
    private Date endDate;
    private Product product;


    public Rental(int id, int productId, int locationId, Date startDate, Date endDate) {
        this._id = id;
        this.productId = productId;
        this.locationId = locationId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.product = getProduct();
    }

    @Override
    public String toString() {
   //     return product.getName();
        return currentPrice() + "";
    }

    public Product getProduct(){
        MyDbHandler handler = new MyDbHandler(ApplicationContextProvider.getContext(), null, null, 1);
        return handler.getProduct(this.productId);
    }
    public boolean isInRent(){
        if (endDate == null){
            return true;
        }
        return false;
    }

    public float currentPrice(){
        int price = product.getPrice();
        return price * getDayCount();
    }

    public float getDayCount(){

        Date now;

        if (endDate == null){
            now = new Date();
        }else{
            now = endDate;
        }

        int timeDiff = (int)((now.getTime() - startDate.getTime()) / (1000*60*60*24l));
        if (timeDiff == 0){
            return 1.0f;
        }
        return timeDiff;
    }

    public void bringBack(){
        MyDbHandler handler = new MyDbHandler(ApplicationContextProvider.getContext(), null, null, 1);
        handler.bringBackRental(this._id);
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
