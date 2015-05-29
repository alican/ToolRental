package eu.alican.toolrental.models;

import android.graphics.drawable.Drawable;

import eu.alican.toolrental.db.MyDbHandler;

/**
 * Created by alican on 20.04.2015.
 */
public class Product {

    private int _id;
    private String name;
    private String description;
    private String productId;
    private String manufactorId;
    private int price;
    private int category;


    public Product() {}

    public Product(int _id, String name, String description, int price, String productId, int category) {
        super();
        this._id = _id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.productId = productId;
        this.category = category;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getManufactorId() {
        return manufactorId;
    }

    public void setManufactorId(String manufactorId) {
        this.manufactorId = manufactorId;
    }

    public String getImage(){
        return "image" + (_id-1) + ".jpg";
    }


    public int rentMe(int placeId){
        //MyDbHandler handler = new MyDbHandler();

        return 0;
    }
}
