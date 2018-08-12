package br.com.siecola.helloworldturbo.tasks;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import br.com.siecola.helloworldturbo.models.Product;
import br.com.siecola.helloworldturbo.util.WSUtil;
import br.com.siecola.helloworldturbo.webservice.WebServiceClient;
import br.com.siecola.helloworldturbo.webservice.WebServiceResponse;

public class ProductTasks {

    private static final String GET_productS = "/api/products";
    private static final String GET_product_BY_ID = "/api/products";

    private ProductEvents productEvents;

    private Context context;

    private String baseAddress;


    public ProductTasks(Context context, ProductEvents productEvents) {
        String host;
        int port;

        this.context = context;
        this.productEvents = productEvents;

        baseAddress = WSUtil.getHostAddress(context);
    }


    @SuppressLint("StaticFieldLeak")
    public void getProducts() {
        baseAddress="https://sales-provider.appspot.com/api/products";
        new AsyncTask<Void, Void, WebServiceResponse>() {

            @Override
            protected WebServiceResponse doInBackground(Void... params) {
                return WebServiceClient.get(context,
                        baseAddress);
            }

            @Override
            protected void onPostExecute(
                    WebServiceResponse webServiceResponse) {
                if (webServiceResponse.getResponseCode() == 200) {
                    Gson gson = new Gson();
                    try {
                        List<Product> products = gson.fromJson(
                                webServiceResponse.getResultMessage(),
                                new TypeToken<List<Product>>() {
                                }.getType());
                        productEvents.getProductsFinished(products);
                    } catch (Exception e) {
                        productEvents.getProductsFailed(webServiceResponse);
                    }
                } else {
                    productEvents.getProductsFailed(webServiceResponse);
                }
            }
        }.execute(null, null, null);
    }


    @SuppressLint("StaticFieldLeak")
    public void getproductByCode(String Code) {
        baseAddress = "https://sales-provider.appspot.com/api/products/"+Code;
        new AsyncTask<Long, Void, WebServiceResponse>() {
            @Override
            protected WebServiceResponse doInBackground(Long... id) {
                return WebServiceClient.get(context,
                        baseAddress +
                                Long.toString(id[0]));
            }

            @Override
            protected void onPostExecute(
                    WebServiceResponse webServiceResponse) {
                if (webServiceResponse.getResponseCode() == 200) {
                    Gson gson = new Gson();
                    try {
                        Product product = gson.fromJson(
                                webServiceResponse.getResultMessage(),
                                Product.class);

                        productEvents.getProductByIdFinished(product);
                    } catch (Exception e) {
                        productEvents.getProductByIdFailed(webServiceResponse);
                    }
                } else {
                    productEvents.getProductByIdFailed(webServiceResponse);
                }
            }
        }.execute(null, null, null);
    }



}
