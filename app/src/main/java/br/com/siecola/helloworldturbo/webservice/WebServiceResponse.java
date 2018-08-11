package br.com.siecola.helloworldturbo.webservice;

/**
 * Created by paulosiecola on 14/04/18.
 */
//Armazena a resposta enviada pelo servidor sobre a solicitação feita
public class WebServiceResponse {

    private int responseCode;
    private String responseMessage;
    private String resultMessage;

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public String getResultMessage() {
        return resultMessage;
    }

    public void setResultMessage(String resultMessage) {
        this.resultMessage = resultMessage;
    }
}
