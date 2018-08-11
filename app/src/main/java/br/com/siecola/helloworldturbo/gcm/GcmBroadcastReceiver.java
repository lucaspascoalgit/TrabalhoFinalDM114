package br.com.siecola.helloworldturbo.gcm;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.os.Bundle;

import br.com.siecola.helloworldturbo.MainActivity;
import br.com.siecola.helloworldturbo.R;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.gson.Gson;

import br.com.siecola.helloworldturbo.models.ProductInfo;

public class GcmBroadcastReceiver extends BroadcastReceiver {

    private Context context;
    private NotificationManager mNotificationManager;
    public static final int NOTIFICATION_ID = 1;

//Este evento é chamado quando o GCM tiver uma mensagem disponível
//A mensagem é passaga atraves do objeto Intent
    @Override
    public void onReceive(Context context, Intent intent) {

        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(context);
        this.context = context;
        String messageType = gcm.getMessageType(intent);

        if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
            Bundle extras = intent.getExtras();

            Gson gson = new Gson();

            if (extras.containsKey("productOfInterest")) {
                String productStr = extras.getString("productOfInterest");
                if (productStr != null) {
                    ProductInfo productInfo = gson.fromJson(productStr,
                            ProductInfo.class);
                    sendNotification(productInfo);
                }
            }
        }

        setResultCode(Activity.RESULT_OK);
    }

    private void sendNotification(ProductInfo productInfo) {
        mNotificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("productInfo", productInfo);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder mBuilder = new Notification.Builder(
                context)
                .setSmallIcon(R.drawable.ic_action_name)
                .setAutoCancel(true)
                .setContentTitle("Siecola Vendas")
                .setStyle(new Notification.BigTextStyle().bigText("Produto:"
                        + productInfo.getId() + " - "
                        + productInfo.getEmail()))
                .setContentText(
                        "Produto:" + productInfo.getId() + " - "
                                + productInfo.getEmail());

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }
}