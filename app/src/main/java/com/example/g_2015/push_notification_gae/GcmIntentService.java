package com.example.g_2015.push_notification_gae;

//import com.example.g_2015.push_notification_gae.MainActivity;
//import com.example.g_2015.push_notification_gae.R;
import java.util.Iterator;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
//import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
//import android.widget.Toast;

/**
 * Created by g-2015 on 2015/02/18.
 */
public class GcmIntentService extends IntentService {
    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;

    public GcmIntentService() {
        super("GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {
            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                Log.d("LOG","messageType(error): " + messageType + ",body:" + extras.toString());
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
                Log.d("LOG","messageType(deleted): " + messageType + ",body:" + extras.toString());
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                Log.d("LOG","messageType(message): " + messageType + ",body:" + extras.toString());

                Iterator<String> it = extras.keySet().iterator();
                String key;
                String value;
                while(it.hasNext()) {
                    key = it.next();
                    value = extras.getString(key);

                }

                //通知バーに表示
                sendNotification(extras.getString("title"), extras.getString("message"));
            }
        }
        GcmBroadcastReceiver.completeWakefulIntent(intent);


    }

    private void sendNotification(String title, String msg) {

        if( title == null) {
            title = "GCM Notification";
        }

        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), 0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle(title)
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(msg))
                        .setContentText(msg);

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

}