package maximedelange.bitcoincalculator.Domain;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import maximedelange.bitcoincalculator.Screen.StartScreen;

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
        final Double bitcoin = Double.valueOf(bitcoinValue);

        // Running timer to show a message to the user repetetive.
        Timer timer = new Timer();
        timer.scheduleAtFixedRate( new TimerTask() {
            private Handler updateUI = new Handler(){
                @Override
                public void dispatchMessage(Message msg) {
                    super.dispatchMessage(msg);
                    // Checking if bitcoin value is higher than a certain amount.
                    if(bitcoin > 2000){
                        System.out.println("Value higher than 2000");
                        Toast.makeText(context, bitcoinValue, Toast.LENGTH_SHORT).show();
                    }else{
                        System.out.println("Value lower than 2000");
                        Toast.makeText(context, "Bitcoin value is lower than 2000", Toast.LENGTH_SHORT).show();
                    }
                }
            };
            public void run() {
                try {
                    updateUI.sendEmptyMessage(0);
                } catch (Exception e) {e.printStackTrace(); }
            }
        }, 0, 5000); // Start inmmediatly and refresh the message every 5 seconds.

        // Returning sticky is making the service run constantly without stopping, unless told explicitly
        return START_STICKY;
    }
}
