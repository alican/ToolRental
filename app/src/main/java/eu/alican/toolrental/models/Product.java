package eu.alican.toolrental.models;

/**
 * Created by alican on 20.04.2015.
 */
public class Product {

    private int _id;
    private String name;
    private String description;
    private int productId;
    private String manufactorId;
    private int price;



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

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getManufactorId() {
        return manufactorId;
    }

    public void setManufactorId(String manufactorId) {
        this.manufactorId = manufactorId;
    }
}
