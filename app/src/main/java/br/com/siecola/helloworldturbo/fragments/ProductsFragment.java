package br.com.siecola.helloworldturbo.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import br.com.siecola.helloworldturbo.R;
import br.com.siecola.helloworldturbo.adapters.ProductAdapter;
import br.com.siecola.helloworldturbo.models.Product;
import br.com.siecola.helloworldturbo.tasks.ProductEvents;
import br.com.siecola.helloworldturbo.tasks.ProductTasks;
import br.com.siecola.helloworldturbo.util.CheckNetworkConnection;
import br.com.siecola.helloworldturbo.webservice.WebServiceResponse;

public class ProductsFragment extends Fragment implements ProductEvents {

    private ListView listViewProducts;
    private List<Product> products;
    private String email;
    private Product product;

    public ProductsFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_products_list,
                container, false);

        getActivity().setTitle("Products");
        setHasOptionsMenu(true);

        listViewProducts = (ListView) rootView.
                findViewById(R.id.products_list);

        listViewProducts.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent,
                                            View view, int position, long id) {
                        Product productSelected = (Product)
                                listViewProducts.getItemAtPosition(position);
                        startProductDetail(id);
                    }
                });
//*********************************************************************************
/*//Carrega valor do email para consulta de products
        //Acessa o ShardedPreferences para resgatar o valor salvo quando a aplicação foi fechada.
        SharedPreferences sharedSettings = getActivity().
                getSharedPreferences(getActivity().getClass().
                        getSimpleName(), Context.MODE_PRIVATE);
        email = sharedSettings.getString("dadosProdutos02", "");*/
//*********************************************************************************

        if ((savedInstanceState != null) && (savedInstanceState.containsKey("produtos"))){
            String strProducts = savedInstanceState.getString("produtos");
            Gson gson = new Gson();
            this.products = gson.fromJson(
                    strProducts, new TypeToken<List<Product>>(){}.getType());
            showProducts();

        }else{
            getProducts();
        }

        return rootView;
    }

    private void getProducts() {
        if (CheckNetworkConnection.isNetworkConnected(getActivity())) {
            ProductTasks productTasks = new ProductTasks(getActivity(), this);
            productTasks.getProducts();
        }else{
            Toast.makeText(getActivity(),"Seu dispositivo não esta conectado na Internet!", Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    public void onSaveInstanceState(Bundle outState) {
        Gson gson = new Gson();
        outState.putString("produtos", gson.toJson(this.products));
        super.onSaveInstanceState(outState);
    }

    private void showProducts(){
        ProductAdapter productAdapter = new ProductAdapter(getActivity(), products);
        listViewProducts.setAdapter(productAdapter);
    }


    private void startProductDetail (long productId) {
        Class fragmentClass;
        Fragment fragment = null;

        fragmentClass = ProductDetailFragment.class;

        try {
            fragment = (Fragment) fragmentClass.newInstance();

            if (productId >= 0) {
                Bundle args = new Bundle();
                args.putLong("product_id", productId);
                fragment.setArguments(args);
            }

            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction transaction =
                    fragmentManager.beginTransaction();

            transaction.replace(R.id.container, fragment,
                    ProductDetailFragment.class.getCanonicalName());
            transaction.addToBackStack(
                    ProductsFragment.class.getCanonicalName());

            transaction.commit();
        } catch (Exception e) {
            try {
                Toast.makeText(getActivity(),
                        "Erro ao tentar abrir detalhes do pedido",
                        Toast.LENGTH_SHORT).show();
            } catch (Exception e1) {}
        }
    }

    @Override
    public void getProductsFinished(List<Product> products) {
        this.products = products;
        Toast.makeText(getActivity(), "Produtos Atualizados", Toast.LENGTH_SHORT).show();
        showProducts();
    }

    @Override
    public void getProductsFailed(WebServiceResponse webServiceResponse) {
        Toast.makeText(getActivity(), "Falha na consulta da lista de produtos" +
                webServiceResponse.getResultMessage() + " - Código do erro: " +
                webServiceResponse.getResponseCode(), Toast.LENGTH_SHORT).show();
    }



    @Override
    public void getProductByIdFinished(Product product1) {
        this.product = product1;
        Toast.makeText(getActivity(), "Produtos Atualizados", Toast.LENGTH_SHORT).show();
        showProducts();

    }

    @Override
    public void getProductByIdFailed(WebServiceResponse webServiceResponse) {

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.products_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh_products:
                //*********************************************************************************
//Carrega valor do email para consulta de products
                //Acessa o ShardedPreferences para resgatar o valor salvo quando a aplicação foi fechada.
                SharedPreferences sharedSettings = getActivity().
                        getSharedPreferences(getActivity().getClass().
                                getSimpleName(), Context.MODE_PRIVATE);
                email = sharedSettings.getString("dadosProdutos02", "");
//*********************************************************************************
                //getProductsByEmail(email);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //******************************************************************************
//Pegando email da tela de de login
    @Override
    public void onStart(){
        super.onStart();
        //Acessa o ShardedPreferences para resgatar o valor salvo quando a aplicação foi fechada.
        SharedPreferences sharedSettings = getActivity().
                getSharedPreferences(getActivity().getClass().
                        getSimpleName(), Context.MODE_PRIVATE);
        email = sharedSettings.getString("dadosProdutos02", "");

    }


//******************************************************************************
}
