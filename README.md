# Docker and Azure IoT device client

This repository is an example of how to build and run a Docker image that contains a Java based Azure IoT device client.

# Table of Content
* [Features](#Features)
* [Run Using Docker](#docker)
* [Run as Standalone Java application](#java)


## Features

You can execute the Java based IoT device client using a Docker image or in standalone Java application mode.

The IoT device client sample in this repository is based on the [Azure IoT SDK and sample apps ]((https://github.com/Azure/azure-iot-sdks/tree/master/java/device)).
Please visit the link above to become familiar with the capabilities of the Azure IoT device Java SDK.

This implementation extends the Azure samples with the following features:
    * Send messages using a configurable interval.
    * Simulate multiple devices each of which can send messages at configured inteval.
    * Multi-device simulation for AMQPS is currently not supported.
    * Sends a message containing temperature, humidity and device timestamp in JSON format.
    * Self-registration of IoT Devices using the [RegistryManager API](https://azure.github.io/azure-iot-sdks/java/service/api_reference/com/microsoft/azure/iot/service/sdk/RegistryManager.html#addDevice-com.microsoft.azure.iot.service.sdk.Device-), no pre-registration required.
    * Clean-up and delete auto-registered IoT devices after each run using 

## <a name="docker"></a>Run using Docker   
   
### Minimal [Docker](https://www.docker.com/) image to run [Java](https://www.java.com/) applications.
   This is based on [Alpine Linux](http://alpinelinux.org/) to keep the size minimal (about 25% of an ubuntu-based image).
   
#### Tags
   
   * `latest` : Run a device simulation
   * `cleanup`: Cleanup devices from Registry
   
#### Usage
   
Example of running a simulation for a single IoT device client:

        docker run -e "PROTOCOL=amqps" -e "HUB_CONN_STRING=HostName=company1.azure-devices.net;SharedAccessKeyName=iothubowner;SharedAccessKey=Abcdefghijklmnopqrstuvwxyz=” stans/azureiotclient
   
Example of cleanup and deletion of IoT device client created during the simulation:

        docker run -e "HUB_CONN_STRING=HostName=company1.azure-devices.net;SharedAccessKeyName=iothubowner;SharedAccessKey=Abcdefghijklmnopqrstuvwxyz=” stans/azureiotclient:cleanup
   
#### Scaling IoT device clients
      
1. Run using Azure Container Service
https://azure.microsoft.com/en-us/documentation/articles/container-service-intro/

2. Run using Docker Cloud and Microsoft Azure integration 
https://docs.docker.com/docker-cloud/getting-started/link-azure/

3. Roll your own orchestration using Azure VMs and/or Docker Swarm:
https://docs.docker.com/swarm/overview/

4. Run on any other Docker compliant container service such as AWS Container Service
      

## <a name="java"></a>Run as standalone Java application:
   * create a jar - `./mvn clean package`
   * go to `target/` to find the azureiot-1.0-SNAPSHOT-with-deps.jar
   * execute `java -jar` 
   
        java -jar -Dazureiot_num_devices=<num of devices to simulate>  -Dazureiot_device_data_interval=<interval between sending events in seconds>  -Dazureiot_conn_string=<iot hub connectionstring>  -Dazureiot_protocol="https|mqtt_amqps"  azureiot-1.0-SNAPSHOT-with-deps.jar 
  


         java -jar -Dazureiot_num_devices=3 -Dazureiot_device_data_interval=15  -Dazureiot_conn_string=HostName=company1.azure-devices.net;SharedAccessKeyName=iothubowner;SharedAccessKey=Abcdefghijklmnopqrstuvwxyz=" -Dazureiot_protocol="mqtt"  azureiot-1.0-SNAPSHOT-with-deps.jar 
  
   
### To delete self-registered devices, execute the jar with the CleanupRegistry main class:
   
      java -jar -Dazureiot_conn_string=HostName=company1.azure-devices.net;SharedAccessKeyName=iothubowner;SharedAccessKey=Abcdefghijklmnopqrstuvwxyz=" azureiot-1.0-SNAPSHOT-with-deps.jar io.github.stansa.azureiotsample.CleanUpRegistry
   
  
        
