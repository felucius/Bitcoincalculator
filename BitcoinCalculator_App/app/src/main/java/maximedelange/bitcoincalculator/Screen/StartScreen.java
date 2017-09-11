package maximedelange.bitcoincalculator.Screen;

import android.app.Dialog;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Handler;

import maximedelange.bitcoincalculator.Domain.APICalls;
import maximedelange.bitcoincalculator.Domain.BackgroundService;
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
    private boolean currencyChecker = false;
    private Integer numberHolder = 0;

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

        if(toggleCurrency.getText().equals("Currency USD")){
            // Display value in dollars.
            updateLabels(0);
        }else{
            // Display value in euros.
            updateLabels(1);
        }
    }

    private void getAllCurrencyRecords(){
        // Remove internet strictmode
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // API call to retrieve default value in dollars.
        String jsonDollarObject = apiCalls.getJSONObject("https://api.coindesk.com/v1/bpi/historical/close.json");
        // API call to retrieve value in euros.
        String jsonEuroObject = apiCalls.getJSONObject("https://api.coindesk.com/v1/bpi/historical/close.json?currency=EUR");
        // Adding dollar currencies from API to a sorted map (TreeMap).
        TreeMap<Integer, Bitcoin> dollarCurrencies = apiCalls.getBitcoinCurrencies(jsonDollarObject);
        // Adding euro currencies from API to a sorted map (TreeMap).
        TreeMap<Integer, Bitcoin> euroCurrencies = apiCalls.getBitcoinCurrencies(jsonEuroObject);

        // Adding dollar value to the stringbuilder.
        StringBuilder sbDollars = new StringBuilder();
        for(Map.Entry<Integer, Bitcoin> currency : dollarCurrencies.entrySet()){
            sbDollars.append("Date: " + currency.getValue().getLastUpdated()
                    + "\n" + "Dollar value: " + currency.getValue().getValue() + "\n\n");
        }

        // Adding euro values to the stringbuilder.
        StringBuilder sbEuros = new StringBuilder();
        for(Map.Entry<Integer, Bitcoin> currency : euroCurrencies.entrySet()){
            sbEuros.append("Date: " + currency.getValue().getLastUpdated()
            + "\n" + "Euro value: " + currency.getValue().getValue() + "\n\n");
        }

        // Custom dialog that displays the previous currencies.
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.previous_currencies_dialog);
        TextView txt = (TextView) dialog.findViewById(R.id.textCurrencies);

        // Checking currency. For default currency (which is false) displays all currencies in dollars.
        if(currencyChecker){
            // Display all previous currencies in dollars.
            txt.setText(sbEuros.toString());
        }else{
            // Display all previous currencies in euros.
            txt.setText(sbDollars.toString());
        }
        dialog.show();
    }

    private void updateLabels(final int number){
        numberHolder = number;

        description = (TextView) findViewById(R.id.txtDescription);
        value = (TextView) findViewById(R.id.txtValue);
        lastUpdated = (TextView) findViewById(R.id.txtLastUpdated);

        if(bitcoinList.size() > 0){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(number == 0){
                        description.setText(bitcoinList.get(numberHolder).getDescription());
                        value.setText("$" + bitcoinList.get(numberHolder).getValue());
                        lastUpdated.setText("Last updated\n" + bitcoinList.get(numberHolder).getLastUpdated());
                    }else if(number == 1){
                        description.setText(bitcoinList.get(numberHolder).getDescription());
                        value.setText("€" + bitcoinList.get(numberHolder).getValue());
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
            if(bitcoinList.size() > 0){
                bitcoin = bitcoinList.get(numberHolder);

                if(bitcoin != null){
                    Intent intent = new Intent(view.getContext(), CalculatorScreen.class);
                    intent.putExtra("bitcoinValue", bitcoin.getValue());
                    startActivity(intent);
                }else{
                    System.out.println("Bitcoin object is null.");
                }
            }else{
                System.out.println("List size is empty.");
            }
            }
        });
    }

    private void toggleCurrentCurrency(){
        toggleCurrency = (ToggleButton) findViewById(R.id.btnToggleCurrency);
        toggleCurrency.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    // Value displayed in dollars.
                    updateLabels(0);
                    currencyChecker = true;
                } else {
                    // The toggle is disabled
                    // Value displayed in euros.
                    updateLabels(1);
                    currencyChecker = false;
                }
            }
        });
    }
}
