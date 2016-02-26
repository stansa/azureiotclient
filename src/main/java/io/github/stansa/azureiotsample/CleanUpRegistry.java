package io.github.stansa.azureiotsample;

import com.microsoft.azure.iot.service.exceptions.IotHubException;
import com.microsoft.azure.iot.service.sdk.Device;
import com.microsoft.azure.iot.service.sdk.DeviceStatus;
import com.microsoft.azure.iot.service.sdk.RegistryManager;
import com.microsoft.azure.iothub.DeviceClient;
import com.microsoft.azure.iothub.IotHubClientProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 *
 *
 */
public class CleanUpRegistry {

    public static final String CONN_STRING_ENV_NAME = "azureiot_conn_string";

    private static final Logger LOG = LoggerFactory.getLogger(CleanUpRegistry.class);

    public static void main(String[] args) throws Exception {


        String registryConnString = System.getProperty(App.CONN_STRING_ENV_NAME, System.getenv("HUB_CONN_STRING"));
        if ( registryConnString == null || registryConnString.isEmpty()) {
            printUsage();
        }


        RegistryManager registryManager = RegistryManager.createFromConnectionString(registryConnString);

        //Delete only ones that start with stans.github prefix
        //Delete 100 devices at a time
        ArrayList<Device> dvcs;
        LOG.info("Deleting in batches of 100 Devices...");
        while (true) {

            dvcs = registryManager.getDevices(100);
            if (dvcs.isEmpty()) {
                break;
            } else {
                for (Device d: dvcs) {
                    if (d.getDeviceId().startsWith(App.DEVICE_ID_PREFIX))
                    registryManager.removeDevice(d.getDeviceId());
                }
            }
        }
        LOG.info("Done...");

    }

    private static void printUsage() {

        LOG.error("Usage: java -D{}=<iot hub connection string>* -jar ...", CONN_STRING_ENV_NAME);
        LOG.error("*String containing Hostname, SharedAccessKeyName & SharedAccessKey in one of the following formats: HostName=<iothub_host_name>;SharedAccessKeyName=<registry_shared_access_key_name>;SharedAccessKey=<registry_device_key>");
    }
}
