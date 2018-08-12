package br.com.siecola.helloworldturbo.tasks;

import java.util.List;

import br.com.siecola.helloworldturbo.models.Product;
import br.com.siecola.helloworldturbo.webservice.WebServiceResponse;

public interface ProductEvents {
    void getProductsFinished(List<Product> orders);
    void getProductsFailed(WebServiceResponse webServiceResponse);

    void getProductByIdFinished(Product product);
    void getProductByIdFailed(WebServiceResponse webServiceResponse);
}

