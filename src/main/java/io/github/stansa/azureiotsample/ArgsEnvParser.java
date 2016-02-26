package io.github.stansa.azureiotsample;

import com.microsoft.azure.iothub.IotHubClientProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class ArgsEnvParser {


    public String[] rawArguments;
    public boolean parsedSuccessfully;
    public String connectionString;
    public String iotHostname;
    public IotHubClientProtocol protocol;
    public int deviceInterval;
    public int numDevices;

    private static final Logger LOG = LoggerFactory.getLogger(ArgsEnvParser.class);

    // Constructors
    public ArgsEnvParser(String[] args) {
        rawArguments = args;
        parsedSuccessfully = false;
        parse();
    }


    private void parse() {

        try {
            String registryConnString = System.getProperty(App.CONN_STRING_ENV_NAME, System.getenv("HUB_CONN_STRING"));
            String proto = System.getProperty(App.PROTOCOL_ENV_NAME, System.getenv("PROTOCOL"));
            String dInterval = System.getProperty(App.DEVICE_INTERVAL_ENV_NAME,  System.getenv("CLIENT_INTERVAL"));
            String numDevices = System.getProperty(App.NUM_DEVICES, System.getenv(App.NUM_DEVICES));

            LOG.debug("Registry Connection String given:" + registryConnString);
            LOG.info("Protocol given:" + protocol);

            if ( registryConnString == null || registryConnString.isEmpty()) {
                this.parsedSuccessfully = false;
                return;
            } else {
                this.connectionString = registryConnString;
                Map<String, String> map = new HashMap<String, String>();
                for (String pair : connectionString.split("; *(?![^\\[\\]]*\\])")) {
                    String[] parts = pair.split("=");
                    map.put(parts[0], parts[1]);
                }

                this.iotHostname = map.get("HostName");

                this.parsedSuccessfully = true;
            }

            if (proto == null || proto.isEmpty()) {
                this.protocol = IotHubClientProtocol.AMQPS;
            } else if (proto.equals("https")) {
                this.protocol = IotHubClientProtocol.HTTPS;
            } else if (proto.equals("amqps")) {
                this.protocol = IotHubClientProtocol.AMQPS;
            } else if (proto.equals("mqtt")) {
                this.protocol = IotHubClientProtocol.MQTT;
            } else {
                throw new Exception("Invalid protocol: " + proto);
            }

            if (numDevices == null || numDevices.isEmpty() || this.protocol == IotHubClientProtocol.AMQPS ) {

                this.numDevices = 1;
            } else {
                this.numDevices = Integer.parseInt(numDevices);
            }

            if (dInterval == null || dInterval.isEmpty()) {
                this.deviceInterval = 5;
            } else {
                this.deviceInterval = Integer.parseInt(dInterval);
                if (this.deviceInterval < 5) {
                    this.deviceInterval = 5;
                }
            }

            this.parsedSuccessfully = true;
        } catch (Exception e) {
            e.printStackTrace();
            this.parsedSuccessfully = false;
        }

    }
}

