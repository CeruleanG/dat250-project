package no.hvl.dat250.jpa.assignment2.driver;

import io.reactivex.Maybe;
import no.hvl.dat250.jpa.assignment2.tools.MqClient;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.junit.jupiter.api.Test;
//import org.junit.Test;

import javax.net.ssl.SSLSocketFactory;

import java.util.Arrays;

import static java.lang.Thread.sleep;
import static java.nio.charset.StandardCharsets.UTF_8;


public class MqTest {

  // When everything will work, remove this test
  @Test
  public void testGettingStarted() throws MqttException {

      MqttClient client = new MqttClient(
              "ssl://679f8737b6f343edbe51d41d9f6dbc53.s1.eu.hivemq.cloud:8883", // serverURI in format: "protocol://name:port"
              MqttClient.generateClientId(), // ClientId
              new MemoryPersistence()); // Persistence

      MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
      mqttConnectOptions.setUserName("DAT250_projectChLeRu");
      mqttConnectOptions.setPassword("projetDeMerde!666".toCharArray());
      mqttConnectOptions.setSocketFactory(SSLSocketFactory.getDefault()); // using the default socket factory
      client.connect(mqttConnectOptions);

      client.setCallback(new MqttCallback() {

        @Override
        // Called when the client lost the connection to the broker
        public void connectionLost(Throwable cause) {
          System.out.println("client lost connection " + cause);
        }

        @Override
        public void messageArrived(String topic, MqttMessage message) {
          System.out.println(topic + ": " + Arrays.toString(message.getPayload()));
        }

        @Override
        // Called when an outgoing publish is complete
        public void deliveryComplete(IMqttDeliveryToken token) {
          System.out.println("delivery complete " + token);
        }
      });

      client.subscribe("#", 1); // subscribe to everything with QoS = 1

      client.publish(
              "topic",
              "payload".getBytes(UTF_8),
              2, // QoS = 2
              false);

      client.disconnect();
  }

  @Test
  public void testConnectToServer() throws MqttException, InterruptedException {
    MqClient mqClient = new MqClient();
    sleep(10000);
    mqClient.disconnectClient();
  }

  @Test
  public void testPublishAndSubscribe() throws MqttException {
    MqClient subscriber = new MqClient();
    MqClient publisher = new MqClient();

    subscriber.subscribe("#");
    publisher.publish("test/topic", "I am the msg");
  }
}
