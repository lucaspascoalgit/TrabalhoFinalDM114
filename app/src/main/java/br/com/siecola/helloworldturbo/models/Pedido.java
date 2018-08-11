package br.com.siecola.helloworldturbo.models;

/**
 * Created by paulosiecola on 25/03/18.
 */

import java.io.Serializable;

@SuppressWarnings("serial")
public class Pedido implements Serializable{

    private int OrderId;
    private String dataPedido;

    @Override
    public String toString() {
        return "Pedido: " + this.OrderId + " - Data: " +
                this.dataPedido.replace("T", " ");
    }

    public int getOrderId() {
        return OrderId;
    }
    public void setOrderId(int orderId) {
        OrderId = orderId;
    }
    public String getDataPedido() {
        return dataPedido;
    }
    public void setDataPedido(String dataPedido) {
        this.dataPedido = dataPedido;
    }
}