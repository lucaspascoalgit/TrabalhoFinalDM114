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

import br.com.siecola.helloworldturbo.R;
import br.com.siecola.helloworldturbo.adapters.OrderAdapter;
import br.com.siecola.helloworldturbo.models.Order;
import br.com.siecola.helloworldturbo.models.Users;
import br.com.siecola.helloworldturbo.tasks.OrderEvents;
import br.com.siecola.helloworldturbo.tasks.OrderTasks;
import br.com.siecola.helloworldturbo.util.CheckNetworkConnection;
import br.com.siecola.helloworldturbo.webservice.WebServiceResponse;

import java.util.ArrayList;
import java.util.List;

public class OrdersFragment extends Fragment implements OrderEvents {

    private ListView listViewOrders;
    private List<Order> orders;
    private String email;

    public OrdersFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_orders_list,
                container, false);

        getActivity().setTitle("Orders");
        setHasOptionsMenu(true);

        listViewOrders = (ListView) rootView.
                findViewById(R.id.orders_list);

        listViewOrders.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent,
                                            View view, int position, long id) {
                        Order orderSelected = (Order)
                                listViewOrders.getItemAtPosition(position);
                        startOrderDetail(id);
                    }
                });
//*********************************************************************************
//Carrega valor do email para consulta de orders
        //Acessa o ShardedPreferences para resgatar o valor salvo quando a aplicação foi fechada.
        SharedPreferences sharedSettings = getActivity().
                getSharedPreferences(getActivity().getClass().
                        getSimpleName(), Context.MODE_PRIVATE);
        email = sharedSettings.getString("dadosUsuario02", "");
//*********************************************************************************

        if ((savedInstanceState != null) && (savedInstanceState.containsKey("pedidos"))){
            String strOrders = savedInstanceState.getString("pedidos");
            Gson gson = new Gson();
            this.orders = gson.fromJson(
              strOrders, new TypeToken<List<Order>>(){}.getType());
            showOrders();

        }else{
            getOrdersByEmail(email);
        }

        return rootView;
    }

    private void getOrders() {
        if (CheckNetworkConnection.isNetworkConnected(getActivity())) {
            OrderTasks orderTasks = new OrderTasks(getActivity(), this);
            orderTasks.getOrders();
        }else{
            Toast.makeText(getActivity(),"Seu dispositivo não esta conectado na Internet!", Toast.LENGTH_SHORT).show();
        }
    }

//**************************************************************************
//Pegando ordem pelo Email
private void getOrdersByEmail(String _email) {
    if (CheckNetworkConnection.isNetworkConnected(getActivity())) {
        OrderTasks orderTasks = new OrderTasks(getActivity(), this);
        orderTasks.getOrdersByEmail(_email);
    }else{
        Toast.makeText(getActivity(),"Seu dispositivo não esta conectado na Internet!", Toast.LENGTH_SHORT).show();
    }
}
//**************************************************************************

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Gson gson = new Gson();
        outState.putString("pedidos", gson.toJson(this.orders));
        super.onSaveInstanceState(outState);
    }

    private void showOrders(){
        OrderAdapter orderAdapter = new OrderAdapter(getActivity(), orders);
        listViewOrders.setAdapter(orderAdapter);
    }


    private void startOrderDetail (long orderId) {
        Class fragmentClass;
        Fragment fragment = null;

        fragmentClass = OrderDetailFragment.class;

        try {
            fragment = (Fragment) fragmentClass.newInstance();

            if (orderId >= 0) {
                Bundle args = new Bundle();
                args.putLong("order_id", orderId);
                fragment.setArguments(args);
            }

            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction transaction =
                    fragmentManager.beginTransaction();

            transaction.replace(R.id.container, fragment,
                    OrderDetailFragment.class.getCanonicalName());
            transaction.addToBackStack(
                    OrdersFragment.class.getCanonicalName());

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
    public void getOrdersFinished(List<Order> orders) {
        this.orders = orders;
        Toast.makeText(getActivity(), "Pedidos Atualizados", Toast.LENGTH_SHORT).show();
        showOrders();
    }

    @Override
    public void getOrdersFailed(WebServiceResponse webServiceResponse) {
        Toast.makeText(getActivity(), "Falha na consulta da lista de pedidos" +
                webServiceResponse.getResultMessage() + " - Código do erro: " +
                webServiceResponse.getResponseCode(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void getOrderByIdFinished(Order order) {

    }

    @Override
    public void getOrderByIdFailed(WebServiceResponse webServiceResponse) {

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.orders_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh_orders:
                //*********************************************************************************
//Carrega valor do email para consulta de orders
                //Acessa o ShardedPreferences para resgatar o valor salvo quando a aplicação foi fechada.
                SharedPreferences sharedSettings = getActivity().
                        getSharedPreferences(getActivity().getClass().
                                getSimpleName(), Context.MODE_PRIVATE);
                email = sharedSettings.getString("dadosUsuario02", "");
//*********************************************************************************
                getOrdersByEmail(email);
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
    email = sharedSettings.getString("dadosUsuario02", "");

    }


//******************************************************************************
}