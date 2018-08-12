package br.com.siecola.helloworldturbo.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import br.com.siecola.helloworldturbo.R;
import br.com.siecola.helloworldturbo.models.Product;
//import br.com.siecola.helloworldturbo.models.ProductItem;
import br.com.siecola.helloworldturbo.tasks.ProductEvents;
import br.com.siecola.helloworldturbo.tasks.ProductTasks;
import br.com.siecola.helloworldturbo.webservice.WebServiceResponse;

public class ProductDetailFragment extends Fragment implements ProductEvents {

    private TextView txtId;
    private TextView txtName;
    private TextView txtDescrip;
    private TextView txtCode;
    private TextView txtPrice;

    private Product product;

    public ProductDetailFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_product_detail,
                container, false);

        getActivity().setTitle("Product Datails");

        txtId = rootView.findViewById(R.id.txtId);
        txtName = rootView.findViewById(R.id.txtName);
        txtDescrip = rootView.findViewById(R.id.txtDescrip);
        txtCode = rootView.findViewById(R.id.txtCode);
        txtPrice = rootView.findViewById(R.id.txtPrice);

        if ((savedInstanceState != null) && (savedInstanceState.containsKey("produto"))){
            this.product = (Product) savedInstanceState.getSerializable("produto");
            showProduct(this.product);
        }else{
            long productId;
            Bundle bundle = this.getArguments();
            if((bundle != null) && (bundle.containsKey("product_id"))){
                productId = bundle.getLong("product_id");

                Toast.makeText(getActivity(), "Id: " + productId, Toast.LENGTH_SHORT).show();

                ProductTasks productTasks = new ProductTasks(getActivity(), this);
                //productTasks.getProductById(productId);
            }
        }

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("produto", this.product);
        super.onSaveInstanceState(outState);
    }

    private void showProduct(Product product){
        List<Product> listaProduto;
        String exibeProdutos = "";
        txtId.setText(String.valueOf(product.getId()));
        txtName.setText(product.getName());
        txtDescrip.setText(String.valueOf(product.getPrice()));
        txtCode.setText(product.getCode());
        txtPrice.setText(String.valueOf(product.getPrice()));

    }



    @Override
    public void getProductByIdFinished(Product product) {
        this.product = product;
        showProduct(product);
    }


    @Override
    public void getProductsFinished(List<Product> products) {

    }

    @Override
    public void getProductsFailed(WebServiceResponse webServiceResponse) {

    }

    @Override
    public void getProductByIdFailed(WebServiceResponse webServiceResponse) {
        Toast.makeText(getActivity(), "Falha ao buscar produtos" +
                webServiceResponse.getResultMessage() + " - Código do erro: " +
                webServiceResponse.getResponseCode(), Toast.LENGTH_SHORT).show();
    }


}

