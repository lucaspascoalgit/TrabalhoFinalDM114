package br.com.siecola.helloworldturbo.webservice;

import br.com.siecola.helloworldturbo.R;
import br.com.siecola.helloworldturbo.models.AccessToken;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.net.HttpURLConnection;
import com.google.gson.Gson;
import br.com.siecola.helloworldturbo.util.WSConstants;
import br.com.siecola.helloworldturbo.util.WSUtil;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Calendar;

public class WebServiceClient {

    private static final String GET_TOKEN = "/oauth/token";

    private static final String TAG = "WebServiceClient";
    //Componente do Android para fazer a conexão HTTP
    private static HttpURLConnection conn;
    //Classe criada dentro de Models para armazenar os dados do token recebidos do servidor no
    // formato JSON
    private static AccessToken accessToken;

    private WebServiceClient(){}


    public static WebServiceResponse get(Context context, String host) {
        WebServiceResponse webServiceResponse;
        InputStream is = null;

        try {
            //host="http://sales-provider.appspot.com:80/api/orders/byemail?email=felipe@siecola.com.br";
            //host="http://sales-provider.appspot.com:80/api/orders/byemail?email=doralice@siecola.com.br";
            //host="https://sales-provider.appspot.com/api/users/byemail?email=felipe@siecola.com.br";
            //Abre a conexão autenticada com o servidor
            webServiceResponse = init(context, host, WSConstants.METHOD_GET);
            if (webServiceResponse.getResponseCode() != 200) {
                return webServiceResponse;
            }

            conn.setDoInput(true);
            conn.connect();

            if (conn.getResponseCode() == 401) {
                invalidateToken(context);
                webServiceResponse = init(context, host,
                        WSConstants.METHOD_GET);
                if (webServiceResponse.getResponseCode() != 200) {
                    return webServiceResponse;
                } else {
                    conn.setDoInput(true);
                    conn.connect();
                }
            }
            is = conn.getInputStream();

            webServiceResponse = new WebServiceResponse();
            webServiceResponse.setResultMessage(readIt(is));
            webServiceResponse.setResponseCode(conn.getResponseCode());
            webServiceResponse.setResponseMessage(conn.getResponseMessage());
            is.close();
        } catch (IOException e) {
            webServiceResponse = new WebServiceResponse();
            webServiceResponse.setResponseCode(0);
            webServiceResponse.setResponseMessage(e.getMessage());
        } finally {
            conn.disconnect();
        }

        return webServiceResponse;
    }

    public static WebServiceResponse post (Context context,
                                           String host, String json) {
        WebServiceResponse webServiceResponse = new WebServiceResponse();
        try {
            webServiceResponse = init(context, host,
                    WSConstants.METHOD_POST);
            if (webServiceResponse.getResponseCode() != 200) {
                return webServiceResponse;
            }

            conn.setDoOutput(true);
            conn.setFixedLengthStreamingMode(json.length());
            conn.getOutputStream().write(json.getBytes("UTF-8"));
            conn.getOutputStream().flush();
            conn.getOutputStream().close();
            conn.connect();

            if (conn.getResponseCode() == 401) {
                invalidateToken(context);
                webServiceResponse = init(context, host,
                        WSConstants.METHOD_POST);
                if (webServiceResponse.getResponseCode() != 200) {
                    return webServiceResponse;
                } else {
                    conn.setDoOutput(true);
                    conn.setFixedLengthStreamingMode(json.length());
                    conn.getOutputStream().write(json.getBytes("UTF-8"));
                    conn.getOutputStream().flush();
                    conn.getOutputStream().close();
                    conn.connect();
                }
            }

            InputStream is = new BufferedInputStream(conn.getInputStream());

            webServiceResponse = new WebServiceResponse();
            webServiceResponse.setResultMessage(readIt(is));
            webServiceResponse.setResponseCode(conn.getResponseCode());
            webServiceResponse.setResponseMessage(conn.getResponseMessage());
            is.close();
        } catch (IOException e) {
            webServiceResponse.setResponseCode(0);
            webServiceResponse.setResponseMessage(e.getMessage());
        } finally {
            conn.disconnect();
        }

        return webServiceResponse;
    }

    //Verifica se o Token está valido e abre a conexão para o método POST e GET
    private static WebServiceResponse init(Context context, String host,
                                           String method) throws IOException {
        WebServiceResponse webServiceResponse;
        //Se Token é inválido autentica
        if (!isTokenValid(context)) {
            webServiceResponse = authenticate(context);
            if (webServiceResponse.getResponseCode() != 200){
                return webServiceResponse;
            }
        } else {
            webServiceResponse = new WebServiceResponse();
            webServiceResponse.setResponseCode(200);
        }
        URL url = new URL(host);
        conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(WSConstants.READ_TIMEOUT);
        conn.setConnectTimeout(WSConstants.CONNECTION_TIMEOUT);
        conn.setRequestMethod(method);
        conn.setRequestProperty("Accept", WSConstants.CONTENT_TYPE_JSON);
        conn.setRequestProperty("Content-Type", WSConstants.CONTENT_TYPE_JSON);
        if (accessToken != null) {
            conn.setRequestProperty("Authorization", "Bearer " +
                    accessToken.getAccess_token());
        }

        return webServiceResponse;
    }

    //Solicita e autentica o Token
    private static WebServiceResponse authenticate (Context context) {
        WebServiceResponse webServiceResponse = new WebServiceResponse();
        //Acessa WSUtil que retorna "http://sales-provider.appspot.com"
        String baseAddress = WSUtil.getHostAddress(context);
        accessToken = null;

        try {
            //Carrega e monta o endereço do servidor de vendas
            //Abre conexão HTTP com método POST
            //URL: http://sales-provider.appspot.com/oauth/token
            //Cabeçalhos:
            //Content-Type: application/x-www-form-urlencoded
            //Authorization: Basic c2llY29sYTptYXRpbGRl
            URL url = new URL(baseAddress + GET_TOKEN);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(WSConstants.READ_TIMEOUT);
            conn.setConnectTimeout(WSConstants.CONNECTION_TIMEOUT);

            conn.setRequestMethod(WSConstants.METHOD_POST);
            conn.setRequestProperty("Accept", WSConstants.CONTENT_TYPE_JSON);
            conn.setRequestProperty("Content-Type",
                    WSConstants.CONTENT_TYPE_URL_ENCODED);
            //ClientId e secret (matilde:siecola em Base64)
            conn.setRequestProperty("Authorization",
                    "Basic c2llY29sYTptYXRpbGRl");

            SharedPreferences sharedSettings =
                    PreferenceManager.getDefaultSharedPreferences(context);

            //Recupera valor do SharedPreferences usando a Key:pref_user_login e caso o valor
            //esteja vazio, retorna o email default
            String wsUsername = sharedSettings.getString(
                    context.getString(R.string.pref_user_login),
                    context.getString(R.string.pref_ws_default_username));
            String wsPassword = sharedSettings.getString(
                    context.getString(R.string.pref_user_password),
                    context.getString(R.string.pref_ws_default_password));

            //passa o email e a senha para o genarateGrantType que retorna a URL montada para fazer
            //a solicitação do Token
            String grantType = generateGrantType(wsUsername, wsPassword);

            //Envia o restante da msg que solicita o Token
            //Corpo da mensagem:
            //grantType="grant_type=password&username=matilde@siecola.com.br&password=matilde";
            conn.setDoOutput(true);
            conn.setFixedLengthStreamingMode(grantType.length());
            conn.getOutputStream().write(grantType.getBytes("UTF-8"));
            conn.getOutputStream().flush();
            conn.getOutputStream().close();
            conn.connect();

            InputStream is = new BufferedInputStream(conn.getInputStream());

            //Se o token foi enviado o código da resposta é 200
            if (conn.getResponseCode() == 200) {
                //"is" esta recebendo os dados em byte do servidor
                //ReadIt() converte para string
                // Declarado na linha 112 InputStream is = new BufferedInputStream(conn.getInputStream());
                String tokenResponse = readIt(is);

                Gson tokenGson = new Gson();
                accessToken = tokenGson.fromJson(tokenResponse,
                        AccessToken.class);

                //Guarda o Token no SharedPreferences e calcula o tempo para expirar
                SharedPreferences.Editor editor = sharedSettings.edit();
                editor.putString(context.getString(
                        R.string.pref_ws_access_token),
                        accessToken.getAccess_token());
                editor.putLong(context.getString(
                        R.string.pref_ws_access_token_expiration),
                        Calendar.getInstance().getTime().getTime() +
                                accessToken.getExpires_in() * 1000);
                editor.commit();

                //Registra o código de sucesso de recebimento de resposta
                webServiceResponse.setResponseCode(200);
                //Registra msg recebida do servidor
                webServiceResponse.setResponseMessage(tokenResponse);
            } else {
                webServiceResponse.setResultMessage(readIt(is));
                webServiceResponse.setResponseCode(conn.getResponseCode());
                webServiceResponse.setResponseMessage(
                        conn.getResponseMessage());
            }
            is.close();
        } catch (IOException e) {
            webServiceResponse.setResponseCode(0);
            webServiceResponse.setResponseMessage(e.getMessage());
        }

        return webServiceResponse;
    }

    //Montar a URL que solicita o Token com a senha e o email
    private static String generateGrantType(String username,
                                            String password) {
        String grantType = "grant_type=password&username=" +
                username + "&password="
                + password;

        return grantType;
    }

    private static void invalidateToken(Context context) {
        SharedPreferences sharedSettings =
                PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedSettings.edit();
        editor.remove(context.getString(R.string.pref_ws_access_token));
        editor.remove(context.getString(R.string.pref_ws_access_token_expiration));
        editor.commit();
    }

    private static boolean isTokenValid(Context context) {
        SharedPreferences sharedSettings =
                PreferenceManager.getDefaultSharedPreferences(context);
        long expirationTime;
        if (sharedSettings.contains(
                context.getString(R.string.pref_ws_access_token)) &&
                sharedSettings.contains(context.getString(
                        R.string.pref_ws_access_token_expiration))) {

            expirationTime = sharedSettings.getLong(context.getString(
                    R.string.pref_ws_access_token_expiration), 0);
            if (Calendar.getInstance().getTime().getTime() < expirationTime) {
                accessToken = new AccessToken();
                accessToken.setAccess_token(sharedSettings.getString(
                        context.getString(R.string.pref_ws_access_token), ""));
                accessToken.setExpires_in(expirationTime);
                return true;
            }
        }
        accessToken = null;
        return false;
    }

    //readIt empacota os dados recebidos do servidor via HTTP
    //InputStream é uma classe que recebe dados por bytes
    //Neste caso está sendo utilizada para receber os dados da rede enviados pelo servidor.
    private static String readIt(InputStream stream) throws IOException {

        try {
            //BufferedReader é uma classe que axilia a leitura de caracteres
            BufferedReader bufferedReader = new BufferedReader(new
                    InputStreamReader(stream));
            StringBuilder stringBuilder = new StringBuilder();
            char[] buffer = new char[1];
            int charsRead;

            while ((charsRead = bufferedReader.read(buffer)) != -1) {
                stringBuilder.append(buffer, 0, charsRead);
            }

            return stringBuilder.toString();
        } finally {
            stream.close();
        }
    }
}
