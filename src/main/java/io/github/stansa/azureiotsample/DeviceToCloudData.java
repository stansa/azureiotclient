package io.github.stansa.azureiotsample;

import com.google.gson.Gson;

/**
 *
 */
public class DeviceToCloudData {

    //Device Id
    public String deviceId;

    //Timestamp ISO 8601 format on Device when temperature and humidity were recorded
    public String timestamp;

    //Temperature in Celsius
    public double temperature;

    //Relative humidity (percentage)
    public double humidity;


    public String serialize() {
        Gson gson = new Gson();

        return gson.toJson(this);

    }

    @Override
    public String toString() {
        return "DeviceToCloudData{" +
                "deviceId='" + deviceId + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", temperature=" + temperature +
                ", humidity=" + humidity +
                '}';




    }
}
