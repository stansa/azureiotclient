package io.github.stansa.azureiotsample;

import com.google.gson.Gson;

/**
 *
 */
public class MachineToCloudData {


    public int machine;

    public int head;

    public int orderNo;

    public int worker;

    public String startTime;

    public String stopTime;

    public long runtime;

    public int state;

    public String mark;

    //Timestamp ISO 8601 format on Device when temperature and humidity were recorded
    public String timestamp;



    public String serialize() {
        Gson gson = new Gson();

        return gson.toJson(this);

    }


}
