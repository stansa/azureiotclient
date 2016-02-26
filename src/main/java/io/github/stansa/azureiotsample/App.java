package io.github.stansa.azureiotsample;

import com.microsoft.azure.iot.service.exceptions.IotHubException;
import com.microsoft.azure.iot.service.sdk.Device;
import com.microsoft.azure.iot.service.sdk.DeviceStatus;
import com.microsoft.azure.iot.service.sdk.RegistryManager;
import com.microsoft.azure.iothub.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 *
 *
 */
public class App {

    public static final String CONN_STRING_ENV_NAME = "azureiot_conn_string";
    public static final String PROTOCOL_ENV_NAME = "azureiot_protocol";
    public static final String DEVICE_INTERVAL_ENV_NAME = "azureiot_device_data_interval";
    public static final String NUM_DEVICES= "azureiot_num_devices";
    public static final String DEVICE_ID_PREFIX =  "io.github.stansa.azureiotsample:";


    private static final Logger LOG = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) throws Exception {

        ArgsEnvParser argsEnvParser = new ArgsEnvParser(args);

        if (!argsEnvParser.parsedSuccessfully) {
            printUsage();
            return;
        }

        LOG.warn("Using protocol {}", argsEnvParser.protocol.name());
        LOG.warn("Using hostname {}", argsEnvParser.iotHostname);
        if (argsEnvParser.protocol == IotHubClientProtocol.AMQPS) {
            LOG.warn("AMQPS client can simulate single device only. Using 1 Client.");
        }

        RegistryManager registryManager = RegistryManager.createFromConnectionString(argsEnvParser.connectionString);

        int arrSize = argsEnvParser.numDevices;

        Device[] device = new Device[arrSize];
        DeviceClient[] client = new DeviceClient[arrSize];
        DeviceEventCallback[] callback = new DeviceEventCallback[arrSize];
        SendDeviceEventRunnable[] sendDeviceEventRunnable = new SendDeviceEventRunnable[arrSize];
        ScheduledExecutorService[] executor = new ScheduledExecutorService[arrSize];

        String deviceConnStr[] = new String[arrSize];


        for (int i=0; i < arrSize; i++) {

            LOG.info("Registering Device {}");
            String deviceId = DEVICE_ID_PREFIX + UUID.randomUUID().toString();

            device[i] = Device.createFromId(deviceId, DeviceStatus.Enabled, null);
            try {
                device[i] = registryManager.addDevice(device[i]);
            } catch (IotHubException iote) {
                try {
                    device[i] = registryManager.getDevice(deviceId);
                } catch (IotHubException iotf) {
                    iotf.printStackTrace();
                }
            }
            LOG.info("Device id: " + device[i].getDeviceId());
            LOG.info("Device key: " + device[i].getPrimaryKey());

            deviceConnStr[i] =
                    "HostName=" + argsEnvParser.iotHostname + ";DeviceId=" + device[i].getDeviceId() + ";SharedAccessKey=" + device[i].getPrimaryKey();


            client[i] = new DeviceClient(deviceConnStr[i], argsEnvParser.protocol);

            LOG.info("Opening connection to IoT Hub.");

            client[i].open();

            LOG.info("Opened connection to IoT Hub.");

            callback[i] = new DeviceEventCallback();

            sendDeviceEventRunnable[i] = new SendDeviceEventRunnable(device[i], client[i], callback[i],argsEnvParser.protocol );

            executor[i] = Executors.newScheduledThreadPool(1);

            executor[i].scheduleWithFixedDelay(sendDeviceEventRunnable[i], 3, argsEnvParser.deviceInterval, TimeUnit.SECONDS);

        }


    }

    private static void printUsage() {

        LOG.error("Usage: java -D{}=<iot hub connection string>* -D{}=<iot hub protocol [https | amqps | mqtt]> -D{}=<number of devices to simulate> -jar ...", CONN_STRING_ENV_NAME, PROTOCOL_ENV_NAME, NUM_DEVICES);
        LOG.error("*String containing Hostname, SharedAccessKeyName & SharedAccessKey in one of the following formats: HostName=<iothub_host_name>;SharedAccessKeyName=<registry_shared_access_key_name>;SharedAccessKey=<registry_device_key>");
    }
}
