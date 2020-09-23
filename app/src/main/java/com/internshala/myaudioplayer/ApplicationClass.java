package com.internshala.myaudioplayer;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class ApplicationClass extends Application {
    public static final String Channel_Id_1="Channel_1";
    public static final String Channel_Id_2="Channel_1";
    public static final String Action_Next="Next";
    public static final String Action_previous="Prev";
    public static final String Action_play="Play";

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
    }

    private void createNotificationChannel() {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel notificationChannel1=new NotificationChannel( Channel_Id_1,"Channel(1)" , NotificationManager.IMPORTANCE_HIGH );
            notificationChannel1.setDescription( "Channel 1 Description" );

            NotificationChannel notificationChannel2=new NotificationChannel( Channel_Id_2,"Channel(2)" , NotificationManager.IMPORTANCE_HIGH );
            notificationChannel2.setDescription( "Channel 1 Description" );

            NotificationManager notificationManager=getSystemService( NotificationManager.class );
            notificationManager.createNotificationChannel( notificationChannel1 );
            notificationManager.createNotificationChannel( notificationChannel2 );

        }
    }
}
