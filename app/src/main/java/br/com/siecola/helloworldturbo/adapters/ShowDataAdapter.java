package br.com.siecola.helloworldturbo.adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import br.com.siecola.helloworldturbo.R;
import br.com.siecola.helloworldturbo.models.Users;

public class ShowDataAdapter extends BaseAdapter {

    private final Activity activity;
    Users user;
    int resultado;

    public ShowDataAdapter(Activity activity, Users user, int resultado) {
        this.activity = activity;
        this.user = user;
        this.resultado = resultado;
    }

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public Object getItem(int position) {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return 1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = activity.getLayoutInflater().inflate(
                R.layout.dados_login, null);



        TextView textViewStatus = (TextView) view.findViewById(R.id.textViewStatus);
        TextView textViewId = (TextView) view.findViewById(R.id.textViewId);
        TextView textViewEmail = (TextView) view.findViewById(R.id.textViewEmail);
        TextView textViewLastLogin = (TextView) view.findViewById(R.id.textViewLastLogin);

        if(resultado ==0)
        {
            textViewStatus.setText("Coloque seus dados e pressione logar");

        }if (resultado==1){
            textViewStatus.setText("Logando");
        }
        if (resultado==2){
            textViewStatus.setText("Login Sucesso");

            textViewId.setText(String.valueOf(user.getId()));
            textViewEmail.setText(user.getEmail());
            //textViewLastLogin.setText(String.valueOf(user.getLastLogin()));
            textViewLastLogin.setText("Sem valor");

        }if (resultado==3){
            textViewStatus.setText("Erro Login");
        }if (resultado==4){
            textViewStatus.setText("Falha na conexão ");
        }

        return view;
    }
}
