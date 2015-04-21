package eu.alican.toolrental.models;

/**
 * Baustelle
 */
public class Location {
    private int _id;
    private String name;
    private String address;


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
