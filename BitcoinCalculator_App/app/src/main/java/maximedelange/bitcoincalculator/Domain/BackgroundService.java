package maximedelange.bitcoincalculator.Domain;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import maximedelange.bitcoincalculator.R;

/**
 * Created by M on 9/8/2017.
 */

public class BackgroundService extends Service {

    // Fields
    private Bitcoin bitcoin = null;
    private Context context = this;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startID){
        final String bitcoinValue = intent.getStringExtra("bitcoinValue");
        final Integer maximumValue = Integer.valueOf(intent.getStringExtra("maximumValue"));
        final Integer minimumValue = Integer.valueOf(intent.getStringExtra("minimumValue"));
        final Integer bitcoin = Integer.valueOf(bitcoinValue);

        // Running timer to show a message to the user repetetive.
        Timer timer = new Timer();
        timer.scheduleAtFixedRate( new TimerTask() {
            private Handler updateUI = new Handler(){
                @Override
                public void dispatchMessage(Message msg) {
                    super.dispatchMessage(msg);
                    // Checking if bitcoin value is higher than a certain amount.
                    if(bitcoin > maximumValue){
                        createNotification(bitcoinValue);
                    }else if(bitcoin < minimumValue){
                        createNotification(bitcoinValue);
                    }
                    createToastMessage("Service started and running in background...");
                }
            };
            public void run() {
                try {
                    updateUI.sendEmptyMessage(0);
                } catch (Exception e) {e.printStackTrace(); }
            }
        }, 0, 10000); // Start inmmediatly and refresh the message every 10 seconds.

        // Returning sticky is making the service run constantly without stopping, unless told explicitly
        return START_STICKY;
    }

    private void createNotification(String bitcoinValue){
        // Creating notification
        NotificationCompat.Builder notification = new NotificationCompat.Builder(this);
        notification.setSmallIcon(R.mipmap.bitcoin_icon);
        notification.setContentTitle("Bitcoin price alert");
        notification.setContentText(bitcoinValue);
        notification.build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification.build());
    }

    private void createToastMessage(String message){
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}
