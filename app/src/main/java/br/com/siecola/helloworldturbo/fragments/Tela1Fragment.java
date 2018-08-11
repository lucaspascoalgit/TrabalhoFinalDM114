package br.com.siecola.helloworldturbo.fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import br.com.siecola.helloworldturbo.R;

/**
 * Created by paulosiecola on 25/03/18.
 */

public class Tela1Fragment extends Fragment{

    private EditText editText1;
    private Button button1;
    private TextView textView1;
    private CheckBox chkConfig1;

    private static String PREF_CONFIG_1 = "pref_config_1";
    private static String STATE_USER_TEXT = "user_text";

    public Tela1Fragment() {}

    //Bundle está sendo acessado para resgatar os dados salvos da aplicação quando ela foi para
    //segundo plano. Os dados foram salvos usando a chave STATE_USER_TEXT
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_tela1,
                container, false);

        getActivity().setTitle("Tela 1");

        editText1 = (EditText) rootView.findViewById(R.id.editText1);
        button1 = (Button) rootView.findViewById(R.id.button1);
        textView1 = (TextView) rootView.findViewById(R.id.textView1);
        chkConfig1 = (CheckBox) rootView.findViewById(R.id.chkConfig1);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editText1.getText().toString().isEmpty())
                    textView1.setText(editText1.getText().toString());
            }
        });

        //Caso algum dado foi salvo na mudança de estado, o componente correspondente é carregado
        //aqui utilizando a chave correta
        if (savedInstanceState != null) {
            textView1.setText(savedInstanceState.getString(STATE_USER_TEXT));
        } else {
            textView1.setText("Olá de novo!!!");
        }

        //Acessa o ShardedPreferences para resgatar o valor salvo quando a aplicação foi fechada.
        SharedPreferences sharedSettings = getActivity().
                getSharedPreferences(getActivity().getClass().
                        getSimpleName(), Context.MODE_PRIVATE);
        Boolean config1 = sharedSettings.getBoolean(PREF_CONFIG_1, false);
        chkConfig1.setChecked(config1);

        return rootView;
    }

    //Este método é chamado pelo android quando a aplicação vai para segundo plano
    //É um método usado para salvar os dados da aplicação quando há mudança de estado
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(STATE_USER_TEXT, textView1.getText().toString());
        super.onSaveInstanceState(outState);
    }

    //Este método é chamado pelo Android antes de destruir a aplicação.
    //Ele salva o valor do componente especificado dentro da activity
    //nete caso uma caixa de checkbox
    @Override
    public void onStop() {
        super.onStop();

        SharedPreferences sharedSettings = getActivity().
                getSharedPreferences(getActivity().getClass().
                        getSimpleName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedSettings.edit();
        editor.putBoolean(PREF_CONFIG_1, chkConfig1.isChecked());

        editor.commit();
    }
}