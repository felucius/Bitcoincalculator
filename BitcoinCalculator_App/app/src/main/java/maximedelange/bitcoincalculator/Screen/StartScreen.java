package maximedelange.bitcoincalculator.Screen;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Handler;

import maximedelange.bitcoincalculator.Domain.APICalls;
import maximedelange.bitcoincalculator.Domain.Bitcoin;
import maximedelange.bitcoincalculator.R;

public class StartScreen extends AppCompatActivity {

    // Fields
    // Controlls
    private TextView description = null;
    private TextView value = null;
    private TextView lastUpdated = null;
    private Button btnGoTo = null;
    private Button showCurrencies = null;
    private ToggleButton toggleCurrency = null;

    // Domain
    private APICalls apiCalls = null;
    private Bitcoin bitcoin = null;
    private List<Bitcoin> bitcoinList = null;
    private List<String> bitcoinCurrencies = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Additions
        removeActionBar();
        goToCalculator();
        apiCalls = new APICalls();
        updateMethodCall();
        //getAllCurrencyRecords();
        retrieveBitcoinCurrencies();
        toggleCurrentCurrency();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_start_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void refreshCurrencyStatistics(){
        // Remove internet strictmode
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        bitcoinList = new ArrayList<>();
        String jsonObject = apiCalls.getJSONObject("https://api.coindesk.com/v1/bpi/currentprice.json");
        bitcoinList = apiCalls.parseJSONObject(jsonObject);
        //updateLabels(0);
        if(toggleCurrency.getText().equals("Currency USD")){
            updateLabels(0);
        }else{
            updateLabels(1);
        }
    }

    private void getAllCurrencyRecords(){
        // Remove internet strictmode
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        String jsonObject = apiCalls.getJSONObject("https://api.coindesk.com/v1/bpi/historical/close.json");
        List<String> currencies = apiCalls.getBitcoinCurrencies(jsonObject);

        StringBuilder sb = new StringBuilder();
        for(String currency : currencies){
            sb.append(currency + "\n");
        }

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage(sb.toString());
        dialog.setCancelable(true);
        dialog.show();
    }

    private void updateLabels(final int number){
        final int numberHolder = number;

        description = (TextView) findViewById(R.id.txtDescription);
        value = (TextView) findViewById(R.id.txtValue);
        lastUpdated = (TextView) findViewById(R.id.txtLastUpdated);

        if(bitcoinList.size() > 0){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(number == 0){
                        description.setText(bitcoinList.get(numberHolder).getDescription());
                        value.setText(bitcoinList.get(numberHolder).getValue());
                        lastUpdated.setText("Last updated\n" + bitcoinList.get(numberHolder).getLastUpdated());
                    }else if(number == 1){
                        description.setText(bitcoinList.get(numberHolder).getDescription());
                        value.setText(bitcoinList.get(numberHolder).getValue());
                        lastUpdated.setText("Last updated\n" + bitcoinList.get(numberHolder).getLastUpdated());
                    }else{
                        System.out.println("No number matches the description");
                    }
                }
            });
        }else{
            System.out.println("Bitcoin list is empty");
        }

    }

    private void updateMethodCall(){
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                refreshCurrencyStatistics();
            }
        }, 0, 5000); // initiate timer after 0 seconds and refreshes every 5 seconds.
    }

    private void removeActionBar(){
        ActionBar actionBar = this.getSupportActionBar();
        actionBar.hide();
    }

    private void retrieveBitcoinCurrencies(){
        showCurrencies = (Button) findViewById(R.id.btnShowCurrencies);
        showCurrencies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getAllCurrencyRecords();
            }
        });
    }

    private void goToCalculator(){
        btnGoTo = (Button) findViewById(R.id.btnGoTo);
        btnGoTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), CalculatorScreen.class);
                startActivity(intent);
            }
        });
    }

    private void toggleCurrentCurrency(){
        toggleCurrency = (ToggleButton) findViewById(R.id.btnToggleCurrency);
        toggleCurrency.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    updateLabels(0);
                } else {
                    // The toggle is disabled
                    updateLabels(1);
                }
            }
        });
    }
}
