package br.com.siecola.helloworldturbo.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import br.com.siecola.helloworldturbo.R;

/**
 * Created by paulosiecola on 14/04/18.
 */
//Nesta classe são configurados os endereços do servidor de vendas
//Esta classe retorna a URL completa para acessar o servidor
public class WSUtil {
    private WSUtil(){}

    public static String getHostAddress (Context context) {
        String host;
        String baseAddress;

        int port;

        SharedPreferences sharedSettings =
                PreferenceManager.getDefaultSharedPreferences(context);
        host = sharedSettings.getString(
                context.getString(R.string.pref_ws_host),
                context.getString(R.string.pref_ws_default_host));
        port = sharedSettings.getInt(
                context.getString(R.string.pref_host_port),
                Integer.parseInt(context.getString(R.string.pref_ws_default_port)));

        if (host.endsWith("/")) {
            host = host.substring(0, host.length() - 1);
        }

        if (!host.startsWith("http://")) {
            host = "http://" + host;
        }

        baseAddress = host + ":" + port;

        return baseAddress;
    }
}
