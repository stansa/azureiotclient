package io.github.stansa.azureiotsample;

import com.microsoft.azure.iot.service.sdk.Device;
import com.microsoft.azure.iothub.DeviceClient;
import com.microsoft.azure.iothub.IotHubClientProtocol;
import com.microsoft.azure.iothub.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

import java.sql.Timestamp;

/**
 *
 */
public class SendDeviceEventRunnable implements Runnable {


    private Device device;
    private DeviceClient deviceClient;
    private DeviceEventCallback deviceCallback;
    private IotHubClientProtocol protocol;

    private static final Logger LOG = LoggerFactory.getLogger(SendDeviceEventRunnable.class);

    public SendDeviceEventRunnable(Device device, DeviceClient client, DeviceEventCallback callback, IotHubClientProtocol protocol) {
        this.device = device;
        this.deviceClient = client;
        this.deviceCallback = callback;
        this.protocol = protocol;

    }


    public void run() {

        MachineToCloudData data = new MachineToCloudData();

        data.machine =  getRandomNumberBetween(100, 200);;
        data.orderNo = getRandomNumberBetween(900000, 999999);;
        data.head = getRandomNumberBetween(1, 10);
        data.worker = getRandomNumberBetween(900000, 999999);;
        data.startTime = "2016-06-17 17:13:27";
        data.stopTime = "2016-06-17 17:20:35";
        data.state = getRandomNumberBetween(1, 10);
        data.runtime = 302;

        data.timestamp = ZonedDateTime.now().format(DateTimeFormatter.ISO_INSTANT);

        String msgStr = null;

        Message msg;

//        if (this.protocol == IotHubClientProtocol.HTTPS) {
//            //Work around for bug in IoT SDK, sending JSON with double quotes gives INTERNAL_SERVER_ERROR in EventCallback,
//            // so we use toString() version with singqule quotes
//            //msgStr = data.toString();
//            msgStr = data.serialize();
//
//        } else {
//
//            msgStr = data.serialize();
//        }
        msgStr = data.serialize();
        msg = new Message(msgStr);
        LOG.info("Sending Event Data to IoT Hub..." + msgStr);

        deviceClient.sendEventAsync(msg, deviceCallback, null);


    }

    public static int getRandomNumberBetween(int min, int max) {
        Random foo = new Random();
        int randomNumber = foo.nextInt(max - min) + min;
        if(randomNumber == min) {
            // Since the random number is between the min and max values, simply add 1
            return min + 1;
        }
        else {
            return randomNumber;
        }

    }


}

