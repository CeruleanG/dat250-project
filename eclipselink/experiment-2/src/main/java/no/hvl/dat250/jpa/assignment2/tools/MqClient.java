package no.hvl.dat250.jpa.assignment2.tools;

import com.hivemq.client.mqtt.mqtt5.Mqtt5Client;

import java.nio.charset.StandardCharsets;

public class MqClient {
  private final MqttManager mqttManager = new MqttManager();
  private final Mqtt5Client client;

  public MqClient(String id){
    this.client = Mqtt5Client.builder()
            .identifier(id) // use a unique identifier
            .serverHost(mqttManager.getHost())
            .automaticReconnectWithDefaultConfig() // the client automatically reconnects
            .serverPort(Integer.parseInt(mqttManager.getPort())) // this is the port of your cluster, for mqtt it is the default port 8883
            .sslWithDefaultConfig() // establish a secured connection to HiveMQ Cloud using TLS
            .build();
    /*
    this.client.toBlocking().connectWith()
            .simpleAuth() // using authentication, which is required for a secure connection
            .username(mqttManager.getUsername()) // use the username and password you just created
            .password(mqttManager.getPassword().getBytes(StandardCharsets.UTF_8))
            .applySimpleAuth()
            .willPublish() // the last message, before the client disconnects
            .topic("home/will")
            .payload("sensor gone".getBytes())
            .applyWillPublish()
            .send();
     */
    this.client.toBlocking().connectWith()
            .simpleAuth() // using authentication, which is required for a secure connection
            .username(mqttManager.getUsername()) // use the username and password you just created
            .password(mqttManager.getPassword().getBytes(StandardCharsets.UTF_8))
            .applySimpleAuth();
  }



}
