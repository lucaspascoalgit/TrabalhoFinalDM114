package br.com.siecola.helloworldturbo.models;

import java.io.Serializable;

public class ProductInfo implements Serializable {

    private long id;
    //private String productID;
    //private String model;
    private int code;
    private float price;
    private String email;



    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }



    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}