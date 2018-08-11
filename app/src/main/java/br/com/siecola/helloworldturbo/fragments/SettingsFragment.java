package br.com.siecola.helloworldturbo.fragments;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.widget.Toast;

import br.com.siecola.helloworldturbo.R;

/**
 * Created by paulosiecola on 25/03/18.
 */

public class SettingsFragment extends PreferenceFragment {

    private String title;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        addPreferencesFromResource(R.xml.fragment_preferences);

        getActivity().setTitle("Configurações");

    }


    void onSaveInstaceState(Bundle outState){
        //EditTextPreference pref_user_password;
        //pref_user_password= (EditTextPreference) findPreference("pref_user_password");
        //pref_user_password.setOnPreferenceChangeListener(new View.On);
        SharedPreferences estocado = PreferenceManager.getDefaultSharedPreferences(getActivity());


        String password = estocado.getString("pref_user_passowrd","");

        if(password != ""){
            Toast.makeText(getActivity(),"Senha ADD",Toast.LENGTH_LONG).show();
        }

    }
/*
    @Override
    public void onStart(){
        super.onStart();

        EditTextPreference pref_user_cpf;
        EditTextPreference pref_user_login;
        EditTextPreference pref_user_password;

        pref_user_cpf = (EditTextPreference) findPreference("pref_user_cpf");
        pref_user_login= (EditTextPreference) findPreference("pref_user_login");
        pref_user_password= (EditTextPreference) findPreference("pref_user_password");

        SharedPreferences estocado = PreferenceManager.getDefaultSharedPreferences(getActivity());

        pref_user_cpf.setText(estocado.getString("pref_user_cpf","CPF_00"));
        pref_user_login.setText(estocado.getString("pref_user_login","Login_00"));
        pref_user_password.setText(estocado.getString("pref_user_passowrd","Passowrd_00"));

    }

    @Override
    public void onResume(){
        super.onResume();

        EditTextPreference pref_user_cpf;
        EditTextPreference pref_user_login;
        EditTextPreference pref_user_password;

        pref_user_cpf = (EditTextPreference) findPreference("pref_user_cpf");
        pref_user_login= (EditTextPreference) findPreference("pref_user_login");
        pref_user_password= (EditTextPreference) findPreference("pref_user_password");

        SharedPreferences estocado = PreferenceManager.getDefaultSharedPreferences(getActivity());

        String cpf = estocado.getString("pref_user_cpf","");
        String login = estocado.getString("pref_user_login","");
        String password = estocado.getString("pref_user_password","");

        if(password==""){
            pref_user_cpf.setSummary("cpf_para_logar_no_provedor_de_vendas");
            pref_user_login.setSummary("login_do_provedor_de_vendas");
            pref_user_password.setSummary("password_para_logar_no_provedor_de_vendas");
        }else{
            Toast.makeText(getActivity(),"Senha ADD",Toast.LENGTH_LONG).show();
        }

    }*/





}