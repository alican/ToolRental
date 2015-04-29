package eu.alican.toolrental.models;

/**
 * Baustelle
 */
public class Place {
    private int _id;
    private String name;
    private String address;

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
