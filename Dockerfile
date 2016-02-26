#built using:  docker build -f Dockerfile -t stans/azureiotclient:latest .
#run using: docker run -e "HUB_CONN_STRING=<iot_hub_conn_string> -e "PROTOCOL=https" stans/azureiotclient

#if you want to build image based on Ubuntu, large image 600MB+
#FROM java:8
# Use the minimalistic Alpine Linux container
FROM jeanblanchard/java:8

VOLUME /tmp

ADD target/azureiot-1.0-SNAPSHOT-with-deps.jar app.jar

#For Alpine install bash
RUN apk add --update bash && rm -rf /var/cache/apk/
RUN bash -c 'touch /app.jar'

ENTRYPOINT ["java","-jar","/app.jar"]
