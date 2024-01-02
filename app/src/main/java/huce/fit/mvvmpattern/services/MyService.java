package huce.fit.mvvmpattern.services;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import huce.fit.mvvmpattern.R;
import huce.fit.mvvmpattern.views.MainActivity;

public class MyService extends Service {
    public MyService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("MyService", "onCreate: ");
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
//        throw new UnsupportedOperationException("Not yet implemented");


            return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        Bundle bundle = intent.getExtras();
//        if (bundle != null) {
//            String songName = bundle.getString("song");
//            Log.d("MyService", "onBind: " + songName);
//        }
        sendNotification();
        return START_NOT_STICKY;
    }
public void sendNotification(){
//   mở activity khi click vào notification
//    Intent intent = new Intent(this, MainActivity.class);
//    PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//    khởi tạo notification
    Notification notification = new NotificationCompat.Builder(this, "CHANNEL_ID")
            .setSmallIcon(R.drawable.icon_heart_red)
            .setContentTitle("MUSS Player")
            .setContentText("something")
//            .setContentIntent(pendingIntent)

            .build();
//    hiển thị notification
    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.TIRAMISU) {
        startForeground(1, notification);
    }
}
    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
}

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}