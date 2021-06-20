package receiver;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BatteryParameter {

    public double temperature;

    public double stateOfCharge;

    public BatteryParameter() {
        super();
    }

    public BatteryParameter(double temperature, double stateOfCharge) {
        super();
        this.temperature = temperature;
        this.stateOfCharge = stateOfCharge;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getStateofCharge() {
        return stateOfCharge;
    }

    public void setStateofCharge(double stateOfCharge) {
        this.stateOfCharge = stateOfCharge;
    }

}
