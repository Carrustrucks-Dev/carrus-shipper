package com.carrus.carrusshipper.gcm;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.carrus.carrusshipper.R;
import com.carrus.carrusshipper.activity.BookingDetailsActivity;
import com.carrus.carrusshipper.activity.MainActivity;
import com.carrus.carrusshipper.activity.SplashActivity;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.json.JSONObject;

import java.net.URL;


public class GcmMessageHandler extends IntentService {
    //    public static final int MESSAGE_NOTIFICATION_ID = 435345;
    private NotificationManager mNotificationManager;

    //Device Token:  APA91bG8ySyWfcLn1txW873blkcAWJMbLsBcN4Xy7t03NiPEkkAPVYWgYEOu4O-tEmi_7YWj3Njc-soMYuaY3nNjleBunExvow8BHhYDtd457Zumy-XrU2mylYXJPTevhcHSDVNtQp6k
    //API Key:  AIzaSyAlmqhW0x5MOLv6mOOS3QMrrijDebmIIPE

    public GcmMessageHandler() {
        super("GcmMessageHandler");
    }

    public static void ClearNotification(Context c) {
        Log.v("ClearNotification", "ClearNotification");

        NotificationManager notifManager = (NotificationManager) c.getSystemService(Context.NOTIFICATION_SERVICE);
        notifManager.cancelAll();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // Retrieve data extras from push notification
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // Keys in the data are shown as extras
        Log.i("Complete payload", intent.getExtras().toString());
        try {
            if (extras.getString("message") != null) {
                String msg = extras.getString("message");
//            sendNotification(extras.getString("message").toString(), extras.getString("gcm.notification.title").toString());
                JSONObject myObject = new JSONObject(extras.getString("flag"));
                if (myObject.has("bookingStatus")) {
                    sendRatingNotification(extras.getString("message"), extras.getString("brand_name"), myObject.getString("bookingId"));
                } else
                    sendNotification(extras.getString("message"), extras.getString("brand_name"), myObject.getString("bookingId"));

                Bundle bundle = new Bundle();
                // Storing data into bundle
                bundle.putString("id", myObject.getString("bookingId"));
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction(BookingDetailsActivity.mBroadcastAction);
                broadcastIntent.putExtras(bundle);
                sendBroadcast(broadcastIntent);
            } else {
//                sendNotification(extras.getString("gcm.notification.title").toString(), extras.getString("gcm.notification.body").toString(), extras.getString("gcm.notification.imageUrl").toString());
                Runnable myRunnable = createRunnable(extras);
                new Thread(myRunnable).start();

            }
        } catch (Exception e) {
            sendNotification("", "Carrus Shipper", "");
        }

        // Notify receiver the intent is completed
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    Runnable createRunnable(final Bundle extras) {

        Runnable r = new Runnable() {
            @Override
            public void run() {
                new BigImageNotifAsync(extras.getString("gcm.notification.title").toString(), extras.getString("gcm.notification.body").toString(), extras.getString("gcm.notification.imageUrl").toString(), "", 1).execute();
            }
        };
        return r;
    }

    private void sendNotification(String msg, String title, String id) {
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        final Intent notificationIntent = new Intent(this, BookingDetailsActivity.class);
        notificationIntent.setAction(Intent.ACTION_MAIN);
        notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        notificationIntent.putExtra("id", id);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(getNotificationIcon()).setLargeIcon(BitmapFactory.decodeResource(getResources(),
                        R.mipmap.ic_launcher))
                        .setContentTitle(title)
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(msg)).setAutoCancel(true)
                        .setContentText(msg);

        mBuilder.setContentIntent(contentIntent);
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        mBuilder.setSound(uri);
        mBuilder.setVibrate(new long[]{1000, 1000});
        mBuilder.setLights(Color.BLUE, 300, 300);
        int MESSAGE_NOTIFICATION_ID = (int) System.currentTimeMillis();
        mNotificationManager.notify(MESSAGE_NOTIFICATION_ID, mBuilder.build());
    }

    private void sendRatingNotification(String msg, String title, String id) {
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        final Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.setAction(Intent.ACTION_MAIN);
        notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        notificationIntent.putExtra("fromNotification", true);
        notificationIntent.putExtra("id", id);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(getNotificationIcon()).setLargeIcon(BitmapFactory.decodeResource(getResources(),
                        R.mipmap.ic_launcher))
                        .setContentTitle(title)
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(msg)).setAutoCancel(true)
                        .setContentText(msg);

        mBuilder.setContentIntent(contentIntent);
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        mBuilder.setSound(uri);
        mBuilder.setVibrate(new long[]{1000, 1000});
        mBuilder.setLights(Color.BLUE, 300, 300);
        int MESSAGE_NOTIFICATION_ID = (int) System.currentTimeMillis();
        mNotificationManager.notify(MESSAGE_NOTIFICATION_ID, mBuilder.build());
    }

    private int getNotificationIcon() {
        boolean whiteIcon = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP);
        return whiteIcon ? R.mipmap.notification_icon : R.mipmap.ic_launcher;
    }

    private class BigImageNotifAsync extends AsyncTask<String, String, Bitmap> {

        private Bitmap bitmap = null;
        private String title, message, picture, url;
        private int playSound;

        public BigImageNotifAsync(String title, String message, String picture, String url, int playSound) {
            this.picture = picture;
            this.title = title;
            this.message = message;
            this.url = url;
            this.playSound = playSound;
        }

        @Override
        protected void onPreExecute() {
            // Things to be done before execution of long running operation. For
            // example showing ProgessDialog
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            try {
                URL url = new URL(picture);
                bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch (Exception e) {
                e.printStackTrace();
            }

            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            // execution of result of Long time consuming operation
            try {
                int MESSAGE_NOTIFICATION_ID = (int) System.currentTimeMillis();
                notificationManagerCustomIDWithBitmap(GcmMessageHandler.this, title, message, MESSAGE_NOTIFICATION_ID,
                        result, url, playSound);
//                if(EventsHolder.displayPushHandler != null){ EventsHolder.displayPushHandler.onDisplayMessagePushReceived(); }
            } catch (Exception e) {
                e.printStackTrace();
//                notificationManagerCustomID(GCMIntentService.this, title, message, PROMOTION_NOTIFICATION_ID, deepindex,
//                        url, playSound);
            }
        }

    }

    private void notificationManagerCustomIDWithBitmap(Context context, String title, String message, int notificationId,
                                                       Bitmap bitmap, String url, int playSound) {
        try {
            long when = System.currentTimeMillis();

            mNotificationManager = (NotificationManager)
                    this.getSystemService(Context.NOTIFICATION_SERVICE);

            final Intent notificationIntent = new Intent(this, SplashActivity.class);
            notificationIntent.setAction(Intent.ACTION_MAIN);
            notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                    notificationIntent, 0);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
            builder.setAutoCancel(true);
            builder.setContentTitle(title);
            builder.setStyle(new NotificationCompat.BigTextStyle().bigText(message));
//            setPlaySound(builder, playSound);
            builder.setWhen(when);
            builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), getNotificationIcon()));
            builder.setSmallIcon(getNotificationIcon());
            builder.setContentIntent(contentIntent);
            builder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bitmap).setBigContentTitle(title).setSummaryText(message));
            builder.setContentText(message);
            builder.setTicker(message);
            Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            builder.setSound(uri);
            builder.setVibrate(new long[]{1000, 1000});
            builder.setLights(Color.BLUE, 300, 300);
            if (Build.VERSION.SDK_INT >= 16) {
                builder.setPriority(Notification.PRIORITY_HIGH);
            }

            Notification notification = builder.build();
            mNotificationManager.notify(notificationId, notification);

            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
            wl.acquire(15000);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}