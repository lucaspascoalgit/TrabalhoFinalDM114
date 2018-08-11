package br.com.siecola.helloworldturbo.models;

import java.io.Serializable;

/**
 * Created by paulosiecola on 14/04/18.
 */

public class OrderItem implements Serializable {

    private long id;
    private long productId;
    private long orderId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }
}
