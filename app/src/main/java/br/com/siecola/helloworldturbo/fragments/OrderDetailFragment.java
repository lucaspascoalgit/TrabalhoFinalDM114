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
import br.com.siecola.helloworldturbo.models.Order;
import br.com.siecola.helloworldturbo.models.OrderItem;
import br.com.siecola.helloworldturbo.tasks.OrderEvents;
import br.com.siecola.helloworldturbo.tasks.OrderTasks;
import br.com.siecola.helloworldturbo.webservice.WebServiceResponse;

/**
 * Created by paulosiecola on 14/04/18.
 */

public class OrderDetailFragment extends Fragment implements OrderEvents {

    private TextView txtId;
    private TextView txtEmail;
    private TextView txtFrete;
    private TextView txtQtd;
    private TextView txtProdutos;

    private Order order;

    public OrderDetailFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_oder_detail01,
                container, false);

        getActivity().setTitle("Order Datails");

        txtId = rootView.findViewById(R.id.txtId);
        txtEmail = rootView.findViewById(R.id.txtName);
        txtFrete = rootView.findViewById(R.id.txtDescrip);
        txtQtd = rootView.findViewById(R.id.txtCode);
        txtProdutos = rootView.findViewById(R.id.txtProdutos);

        if ((savedInstanceState != null) && (savedInstanceState.containsKey("pedido"))){
                this.order = (Order) savedInstanceState.getSerializable("pedido");
                showOrder(this.order);
        }else{
            long orderId;
            Bundle bundle = this.getArguments();
            if((bundle != null) && (bundle.containsKey("order_id"))){
                orderId = bundle.getLong("order_id");

                Toast.makeText(getActivity(), "Id: " + orderId, Toast.LENGTH_SHORT).show();

                OrderTasks orderTasks = new OrderTasks(getActivity(), this);
                orderTasks.getOrderById(orderId);
            }
        }

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("pedido", this.order);
        super.onSaveInstanceState(outState);
    }

    private void showOrder(Order order){
        List<OrderItem> listaProduto;
        String exibeProdutos = "";
        txtId.setText(String.valueOf(order.getId()));
        txtEmail.setText(order.getEmail());
        txtFrete.setText(String.valueOf(order.getFreightPrice()));
        txtQtd.setText(String.valueOf(order.getOrderItems().size()));
        //Mostrar pedidos*********************************************************
        listaProduto = order.getOrderItems();
        for (int i=0;i<order.getOrderItems().size();i++){
            exibeProdutos +="Item"+String.valueOf(i+1)+":"+
                     String.valueOf(listaProduto.get(i).getId()+"\n");
        }
        txtProdutos.setText(exibeProdutos);
        //***********************************************************************
    }

    @Override
    public void getOrderByIdFinished(Order order) {
        this.order = order;
        showOrder(order);
    }


    @Override
    public void getOrdersFinished(List<Order> orders) {

    }

    @Override
    public void getOrdersFailed(WebServiceResponse webServiceResponse) {

    }

    @Override
    public void getOrderByIdFailed(WebServiceResponse webServiceResponse) {
        Toast.makeText(getActivity(), "Falha ao buscar pedidos" +
                webServiceResponse.getResultMessage() + " - Código do erro: " +
                webServiceResponse.getResponseCode(), Toast.LENGTH_SHORT).show();
    }


}
