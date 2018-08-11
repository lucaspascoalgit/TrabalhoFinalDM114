package br.com.siecola.helloworldturbo.fragments;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import br.com.siecola.helloworldturbo.R;
import br.com.siecola.helloworldturbo.models.Order;
import br.com.siecola.helloworldturbo.models.Users;
import br.com.siecola.helloworldturbo.webservice.WebServiceClient;
import br.com.siecola.helloworldturbo.webservice.WebServiceResponse;

public class Fragment_login extends Fragment {

    //Declaração dos objetos da tela
    private TextView textViewStatus;
    private TextView textDadosCliente;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonLogar;

    String email;
    String password;
    String host;
    Users compareUsers = new Users();

    //método para inflar o fragment
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_login,
                container, false);

        getActivity().setTitle("Tela Login");

        //instanciando os componentes de tela
        textViewStatus = (TextView) rootView.findViewById(R.id.textViewStatus);
        editTextPassword = (EditText) rootView.findViewById(R.id.editTextPassword);
        editTextEmail = (EditText) rootView.findViewById(R.id.editTextEmail);
        textDadosCliente = (TextView) rootView.findViewById((R.id.textDadosCliente));
        buttonLogar = (Button) rootView.findViewById(R.id.buttonLogar);


        //Dados Salvos no SharedPreference
        if (savedInstanceState != null) {
            editTextEmail.setText(savedInstanceState.getString("dadosUsuario002"));
            editTextPassword.setText(savedInstanceState.getString("dadosUsuario003"));
            String strOrders0 = savedInstanceState.getString("dadosUsuario001");
            Gson gson = new Gson();
            this.compareUsers = gson.fromJson(
                    strOrders0, Users.class);
        }

        //Botão para solicitar o login na aplicação
        buttonLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textViewStatus.setText("Verificando Dados");

                email = editTextEmail.getText().toString();
                password = editTextPassword.getText().toString();
                String comparaEmail;
                String comparaPassword;

                //metodo para solicitar dados do servidor
                verificaLogin(email,password);

                //compara dados do servidor com dados digitados
                comparaPassword = compareUsers.getPassword().toString();
                comparaEmail = compareUsers.getEmail().toString();
                //Se a senha for menor que dois é pq o servidor ainda não retornou o valor
                if(comparaPassword.length()>2){

                    if(comparaPassword.equals(password)&& comparaEmail.equals(email)){

                        textViewStatus.setText("Logado");
                        textDadosCliente.setText("Id:"+(String.valueOf(compareUsers.getId()))+
                                "\nEmail:"+compareUsers.getEmail()+
                                "\nLastLogin:"+compareUsers.getLastLogin()
                        );

                    }else{

                        textViewStatus.setText("Falha no Login");
                        textDadosCliente.setText("Id:"+
                                "\nEmail:"+
                                "\nLastLogin:"
                        );

                    }
                }
            }
        });

        return rootView;
    }

    //Solicita Dados do Cliente ao servidaor para comparar a senha
    @SuppressLint("StaticFieldLeak")
    public void verificaLogin(String email, String password){

        host="https://sales-provider.appspot.com/api/users/byemail?email="+email;

        //***************************************************************************************
        //Metodo de solicitação e tratamento das informações do servidor
        new AsyncTask<Void, Void, WebServiceResponse>() {
            //Solicita dados ao Servidor
            @Override
            protected WebServiceResponse doInBackground(Void... params) {
                return WebServiceClient.get(getActivity(),
                        host);
            }

            //Converte string de dados em pacote JSON
            @Override
            protected void onPostExecute(
                    WebServiceResponse webServiceResponse) {
                if (webServiceResponse.getResponseCode() == 200) {
                    Gson gson = new Gson();
                    try {
                         compareUsers = gson.fromJson(
                                webServiceResponse.getResultMessage(),
                                Users.class);

                        //orderEvents.getOrdersFinished(orders);
                    } catch (Exception e) {
                        //orderEvents.getOrdersFailed(webServiceResponse);
                    }
                } else {
                    //orderEvents.getOrdersFailed(webServiceResponse);
                }
            }
        }.execute(null, null, null);
        //***************************************************************************************

        //Toast.makeText(getActivity(),resposta.getResponseCode(),Toast.LENGTH_LONG).show();

        }

      @Override
      public void onStart(){
      super.onStart();
          //Acessa o ShardedPreferences para resgatar o valor salvo quando a aplicação foi fechada.
          SharedPreferences sharedSettings = getActivity().
                  getSharedPreferences(getActivity().getClass().
                          getSimpleName(), Context.MODE_PRIVATE);
          String strOrders = sharedSettings.getString("dadosUsuario01", "");
          if (strOrders.length()>2){
              //dados do Cliente
              Gson gson = new Gson();
              this.compareUsers = gson.fromJson(
                      strOrders, Users.class);
              textDadosCliente.setText("Id:"+(String.valueOf(compareUsers.getId()))+
                      "\nEmail:"+compareUsers.getEmail()+
                      "\nLastLogin:"+compareUsers.getLastLogin()
              );
              //Dados de login
              editTextEmail.setText(sharedSettings.getString("dadosUsuario02",""));
              editTextPassword.setText(sharedSettings.getString("dadosUsuario03",""));

          }

      }

    //Este método é chamado pelo Android antes de destruir a aplicação.
    //Ele salva o valor do componente especificado dentro da activity
    //nete caso uma caixa de checkbox
    @Override
    public void onStop() {
        super.onStop();
        Gson gson = new Gson();

        SharedPreferences sharedSettings = getActivity().
                getSharedPreferences(getActivity().getClass().
                        getSimpleName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedSettings.edit();
        editor.putString("dadosUsuario01", gson.toJson(this.compareUsers));
        editor.putString("dadosUsuario02", email);
        editor.putString("dadosUsuario03", password);
        //editor.putString("dadosUsuario04", String.valueOf(this.compareUsers.getId()));

        editor.commit();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Gson gson = new Gson();
        outState.putString("dadosUsuario001", gson.toJson(this.compareUsers));
        super.onSaveInstanceState(outState);
        outState.putString("dadosUsuario002", email);
        super.onSaveInstanceState(outState);
        outState.putString("dadosUsuario03", password);
        super.onSaveInstanceState(outState);
    }
}
