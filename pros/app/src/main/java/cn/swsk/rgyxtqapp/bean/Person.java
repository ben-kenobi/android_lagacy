package cn.swsk.rgyxtqapp.bean;

/**
 * Created by tom on 2015/10/17.
 */
public class Person {
    private int id;
    private String name;
    private String address;

    public Person() {
        super();
    }
    public Person(int id, String name, String addrss) {
        super();
        this.id = id;
        this.name = name;
        this.address = addrss;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
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
    @Override
    public String toString() {
        return "Person [addrss=" + address + ", id=" + id + ", name=" + name
                + "]";
    }

}
