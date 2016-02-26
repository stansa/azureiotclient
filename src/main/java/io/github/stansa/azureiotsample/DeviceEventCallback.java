package io.github.stansa.azureiotsample;

import com.microsoft.azure.iothub.IotHubEventCallback;
import com.microsoft.azure.iothub.IotHubStatusCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class DeviceEventCallback implements IotHubEventCallback {

    private static final Logger LOG = LoggerFactory.getLogger(DeviceEventCallback.class);

    public void execute(IotHubStatusCode status, Object context) {



        if (status != IotHubStatusCode.OK && status != IotHubStatusCode.OK_EMPTY) {

            LOG.error("IoT Hub responded to message with status " + status.name());

        } else {

            LOG.info("IoT Hub responded to message with status " + status.name());
        }

    }
}
