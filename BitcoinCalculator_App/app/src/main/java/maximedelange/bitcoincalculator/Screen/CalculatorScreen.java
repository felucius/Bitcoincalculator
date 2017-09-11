package maximedelange.bitcoincalculator.Screen;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import maximedelange.bitcoincalculator.Domain.BackgroundService;
import maximedelange.bitcoincalculator.Domain.Bitcoin;
import maximedelange.bitcoincalculator.R;

public class CalculatorScreen extends AppCompatActivity {

    // Fields
    private Button startService = null;
    private EditText maximumValue = null;
    private EditText minimumValue = null;
    private Bitcoin bitcoin = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Additions
        removeActionBar();
        //getBitcoinValue();
        startPriceWatcherService();
    }

    private void removeActionBar(){
        ActionBar actionBar = this.getSupportActionBar();
        actionBar.hide();
    }

    private String getBitcoinValue(){
        String bitcoinValue = getIntent().getStringExtra("bitcoinValue");
        System.out.println(bitcoinValue);
        return bitcoinValue;
    }

    private void startPriceWatcherService(){
        startService = (Button) findViewById(R.id.btnStartService);
        startService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                maximumValue = (EditText) findViewById(R.id.txtMaximumValue);
                minimumValue = (EditText) findViewById(R.id.txtMinimumValue);

                // Starting service to run app in background
                String bitcoinValue = getBitcoinValue();

                Intent intent = new Intent(view.getContext(), BackgroundService.class);
                intent.putExtra("bitcoinValue", bitcoinValue);
                intent.putExtra("maximumValue", maximumValue.getText().toString());
                intent.putExtra("minimumValue", minimumValue.getText().toString());
                startService(intent);

            }
        });
    }
}
