package br.com.siecola.helloworldturbo.tasks;

import java.util.List;

import br.com.siecola.helloworldturbo.models.Order;
import br.com.siecola.helloworldturbo.webservice.WebServiceResponse;

public interface OrderEvents {
    void getOrdersFinished(List<Order> orders);
    void getOrdersFailed(WebServiceResponse webServiceResponse);

    void getOrderByIdFinished(Order order);
    void getOrderByIdFailed(WebServiceResponse webServiceResponse);
}
