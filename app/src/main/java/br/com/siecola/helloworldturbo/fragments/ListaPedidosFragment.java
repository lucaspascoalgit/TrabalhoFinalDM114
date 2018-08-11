package br.com.siecola.helloworldturbo.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.net.HttpURLConnection;
import java.util.ArrayList;

import br.com.siecola.helloworldturbo.R;
import br.com.siecola.helloworldturbo.adapters.PedidoAdapter;
import br.com.siecola.helloworldturbo.models.Pedido;

/**
 * Created by paulosiecola on 25/03/18.
 */

public class ListaPedidosFragment extends Fragment {

    private ListView listViewPedidos;
    //Array do tipo Pedido implementado dentro de models
    //O objeto Pedidos extende Serializable e contém OrderID e dataPedito
    //Atraves do toString(); retorna uma string trabalhada
    private ArrayList<Pedido> pedidos;

    public ListaPedidosFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_lista_pedidos,
                container, false);

        getActivity().setTitle("Pedidos");
        //Este método habilita a função de menu de opços no canto superior direito da activity
        //o menu é inflado por onCreateOptionsMenu(Menu menu, MenuInflater inflater)
        setHasOptionsMenu(true);

        pedidos = new ArrayList<Pedido>();
        for (int j = 0; j < 5; j++) {
            Pedido pedidoAux = new Pedido();
            pedidoAux.setOrderId(j);
            pedidoAux.setDataPedido("10/04/2016 11:50:00");
            pedidos.add(pedidoAux);
        }

        listViewPedidos = (ListView) rootView.
                findViewById(R.id.lista_pedidos);

        PedidoAdapter pedidoAdapter = new PedidoAdapter(
                getActivity(), pedidos);
        //Retorna a lista detalhada com Numero, ID e Data inflado de PedidoAdapter
        //que acessa a tela R.layout.pedido_list_item.xml
        listViewPedidos.setAdapter(pedidoAdapter);

        return rootView;
    }

    //Método para inflar a opção de menu superior direito na activity
    //o arquivo xml correspondente se encontra na pasta res/menu
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.pedidos_menu, menu);
        //inflater.inflate(R.menu.main, menu);
    }

    //Quando a opção de menu for selecionada este método executa
    //neste caso uma mensagem na base da activity é exibida
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.novo_pedido:
                Toast.makeText(getActivity(),
                        "Novo pedido", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.action_settings:
                Toast.makeText(getActivity(),
                        "Botão de Opções", Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}