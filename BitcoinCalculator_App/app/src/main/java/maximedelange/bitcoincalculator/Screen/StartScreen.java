package maximedelange.bitcoincalculator.Screen;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import maximedelange.bitcoincalculator.Domain.APICalls;
import maximedelange.bitcoincalculator.Domain.Bitcoin;
import maximedelange.bitcoincalculator.R;

public class StartScreen extends AppCompatActivity {

    // Fields
    // Controlls
    private TextView description = null;
    private TextView value = null;
    private TextView currency = null;
    private TextView lastUpdated = null;

    // Domain
    private APICalls apiCalls = null;
    private Bitcoin bitcoin = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Additions
        refreshCurrencyStatistics();
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

        apiCalls = new APICalls();
        bitcoin = apiCalls.getJSONObject("https://api.coindesk.com/v1/bpi/currentprice.json");

        updateLabels();
    }

    private void updateLabels(){
        description = (TextView) findViewById(R.id.txtDescription);
        value = (TextView) findViewById(R.id.txtValue);
        currency = (TextView) findViewById(R.id.txtCurrency);
        lastUpdated = (TextView) findViewById(R.id.txtLastUpdated);

        description.setText(bitcoin.getDescription());
        value.setText(bitcoin.getValue());
        //currency.setText(bitcoin.getCurrency());
        lastUpdated.setText("Last updated\n" + bitcoin.getLastUpdated());
    }
}
