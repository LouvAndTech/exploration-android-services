package com.example.exploration_services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

/**
 * DataService is a foreground service that allows clients to bind to it and retrieve or set data.
 */
public class DataService extends Service {

    private static final String TAG = "DEV_DataService";
    private static final int NOTIFICATION_ID = 1; // Unique ID for the notification
    private static final String CHANNEL_ID = "STORAGE_APP_EXPLO"; // Notification channel ID
    private final IBinder binder = new DataServiceBinder(); // Binder given to clients
    private String data = null; // The actual data to be managed by the service

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "Service created");
        createNotificationChannel(); // Create the notification channel.
    }

    /**
     * onStartCommand is called when the service is started.
     * It creates a notification to run the service in the foreground.
     *
     * @param intent The Intent that started the service.
     * @param flags  Additional data about the start request.
     * @param startId A unique integer representing this specific request to start.
     * @return The return value indicates how the system should handle the service if it is killed.
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "Service started");
        // Create a notification to run the service in the foreground.
        Notification notification = createNotification();
        startForeground(NOTIFICATION_ID, notification); 
        return START_STICKY;
    }

    /**
     *  onBind is called when a client binds to the service.
     *  It returns an IBinder that clients can use to communicate with the service.
     */
    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "Service bound to a new client");
        return binder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "Service destroyed");
    }

    /**
     * Gets the current data stored in the service.
     *
     * @return The current data.
     */
    public String getData() {
        return data;
    }

    /**
     * Sets the data in the service.
     *
     * @param data The data to be set.
     */
    public void setData(String data) {
        Log.i(TAG, "Data set: " + data);
        this.data = data;
    }

    public class DataServiceBinder extends Binder {
        DataService getService() {
            return DataService.this;
        }
    }

    /**
     * Creates a notification channel.
     * This is required for foreground services to show notifications.
     */
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Data Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(serviceChannel);
            }
        }
    }

    /**
     * Creates a notification for the foreground service.
     *
     * @return The notification to be displayed.
     */
    private Notification createNotification() {
        return new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Data Service")
                .setContentText("Running in the foreground")
                .setSmallIcon(R.drawable.ic_notification)
                .build();
    }
}