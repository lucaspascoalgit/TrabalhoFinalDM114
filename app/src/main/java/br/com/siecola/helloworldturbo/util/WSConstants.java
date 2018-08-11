package br.com.siecola.helloworldturbo.util;

/**
 * Created by paulosiecola on 14/04/18.
 */
//Classe utilizada por WebServiceClient
public class WSConstants {

    private WSConstants(){}

    public static final String CONTENT_TYPE_JSON = "application/json";
    public static final String CONTENT_TYPE_URL_ENCODED =
            "application/x-www-form-urlencoded";

    public static final String METHOD_GET = "GET";
    public static final String METHOD_POST = "POST";
    public static final String METHOD_PUT = "PUT";
    public static final String METHOD_DELETE = "DELETE";

    public static final int READ_TIMEOUT = 10000;
    public static final int CONNECTION_TIMEOUT = 15000;

    public static final String PREF_WS_ACCESS_TOKEN = "pref_ws_access_token";
    public static final String PREF_WS_ACCESS_TOKEN_EXPIRATION =
            "pref_ws_access_token_expiration";

}
