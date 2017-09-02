package maximedelange.bitcoincalculator.Domain;

/**
 * Created by M on 9/2/2017.
 */

public class Bitcoin {
    // Fields
    private String lastUpdated = null;
    private String currency = null;
    private String value = null;
    private String description = null;

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
}
