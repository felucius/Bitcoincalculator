package maximedelange.bitcoincalculator.Domain;

import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

/**
 * Created by M on 9/2/2017.
 */

public class Bitcoin {
    // Fields
    private String lastUpdated = null;
    private String currency = null;
    private String value = null;
    private String description = null;
    private TreeMap<Integer, Bitcoin> bitcoinCurrencies = null;
    private String previousCurrencyDate = null;

    // Constructor
    public Bitcoin(String lastUpdated, String currency, String value, String description){
        this.lastUpdated = lastUpdated;
        this.currency = currency;
        this.value = value;
        this.description = description;
    }

    // Methods
    public String getLastUpdated(){
        return this.lastUpdated;
    }

    public String getCurrency(){
        return this.currency;
    }

    public String getValue(){
        return this.value;
    }

    public String getDescription(){
        return this.description;
    }

    public void setBitcoinCurrencies(TreeMap<Integer, Bitcoin> bitcoinCurrencies){
        this.bitcoinCurrencies = bitcoinCurrencies;
    }

    public TreeMap<Integer, Bitcoin> getBitcoinCurrencies(){
        return this.bitcoinCurrencies;
    }

    public void setPreviousCurrencyDate(String previousCurrencyDate){
        this.previousCurrencyDate = previousCurrencyDate;
    }

    public String getPreviousCurrencyDate(){
        return this.previousCurrencyDate;
    }
}
