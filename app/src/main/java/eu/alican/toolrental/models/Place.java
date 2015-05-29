package eu.alican.toolrental.models;

import java.util.ArrayList;
import java.util.Date;

import eu.alican.toolrental.db.MyDbHandler;
import eu.alican.toolrental.utls.ApplicationContextProvider;

/**
 * Baustelle
 */
public class Place {
    private int _id;
    private String name;
    private String address;
    private float pricePerDayCache = -1;
    private float totalPriceCache = -1;

    public Place(int id, String name, String address) {
        super();
        this.name = name;
        this.address = address;
        this._id = id;

    }

    public Place(String name, String address) {
        super();
        this.name = name;
        this.address = address;
    }

    //MyDbHandler.RentalEntry.FILTER_ALL

    public ArrayList<Rental> getRentals(int filter){
        MyDbHandler handler = new MyDbHandler(ApplicationContextProvider.getContext(), null, null, 1);
        return handler.getRentalsByPlace(_id, filter);
    }

    public float getPricePerDay(){
       //fac if (pricePerDayCache != -1) return pricePerDayCache;
        ArrayList<Rental> rentals = getRentals(MyDbHandler.RentalEntry.FILTER_STILL_BORROWED);
        float summ = 0;
        for (int i = 0; i < rentals.size(); i++){
            summ += rentals.get(i).currentPrice();
        }
        pricePerDayCache = summ;
        return summ;
    }
    public float getTotalPrice(){
       // if (totalPriceCache != -1) return totalPriceCache;
        float totalPrice = 0;
        ArrayList<Rental> rentals = getRentals(MyDbHandler.RentalEntry.FILTER_ALL);
        for (int i = 0; i < rentals.size(); i++){
            Rental rental = rentals.get(i);
            totalPrice += rental.currentPrice() * rental.getDayCount();
        }
        totalPriceCache = totalPrice;
        return totalPrice;
    }
    public int getRentCount(int filter){

        return getRentals(filter).size();
    }

    @Override
    public String toString() {
        return name + "\n" + address;
    }

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        this._id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
