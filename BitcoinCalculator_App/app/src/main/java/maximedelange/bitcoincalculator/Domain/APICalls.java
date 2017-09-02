package maximedelange.bitcoincalculator.Domain;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by M on 9/2/2017.
 */

public class APICalls {
    // Fields
    private HttpURLConnection urlConnection = null;
    private URL url = null;
    private BufferedReader reader = null;
    private StringBuilder stringBuilder = null;
    private Bitcoin bitcoin = null;
    private List<String> bitcoinCurrencies = null;

    // Constructor
    public APICalls(){

    }

    // Methods
    public String getJSONObject(String url){
        try {
            this.url = new URL(url);
            urlConnection = (HttpURLConnection) this.url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoOutput(true);
            urlConnection.connect();

            reader = new BufferedReader(new InputStreamReader(this.url.openStream()));
            stringBuilder = new StringBuilder();

            String line = null;
            while((line = reader.readLine()) != null){
                stringBuilder.append(line + "\n");
            }

            reader.close();

            String JSONString = stringBuilder.toString();
            System.out.println(JSONString);

            //bitcoin = parseJSONObject(JSONString);
            return JSONString;
        }catch (MalformedURLException malformed){
            malformed.printStackTrace();
            return null;
        }catch (ProtocolException protocol){
            protocol.printStackTrace();
            return null;
        }catch (IOException ioe){
            ioe.printStackTrace();
            return null;
        }
    }

    public Bitcoin parseJSONObject(String json){
        try{
            JSONObject jsonObject = new JSONObject(json);

            JSONObject lastUpdatedObject = jsonObject.getJSONObject("time");
            String lastUpdated = lastUpdatedObject.getString("updated");

            JSONObject bitcoinObject = jsonObject.getJSONObject("bpi");
            JSONObject bitcoinCurrency = bitcoinObject.getJSONObject("USD");
            String currency = bitcoinCurrency.getString("code");
            String value = bitcoinCurrency.getString("rate");
            String description = bitcoinCurrency.getString("description");


            this.bitcoin = new Bitcoin(lastUpdated, currency, value, description);

            return bitcoin;
        }catch (JSONException jsonEx){
            jsonEx.printStackTrace();
            return null;
        }
    }

    public List<String> getBitcoinCurrencies(String json){
        bitcoinCurrencies = new ArrayList<>();
        List<String> holder = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(json);

            String previousCurrencies = jsonObject.getString("bpi");

            holder = Arrays.asList(previousCurrencies.split(","));

            for(String previousCurrency : holder){
                if(this.bitcoin != null){
                    bitcoinCurrencies.add(previousCurrency);
                    bitcoin.setBitcoinCurrencies(bitcoinCurrencies);
                }
            }

            return bitcoinCurrencies;
        }catch (JSONException jsonEx){
            jsonEx.printStackTrace();
            return null;
        }
    }
}
