package no.hvl.dat250.jpa.assignment2.driver;

import no.hvl.dat250.jpa.assignment2.Poll;
import no.hvl.dat250.jpa.assignment2.UserProfile;
import no.hvl.dat250.jpa.assignment2.tools.MqClient;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import javax.net.ssl.SSLSocketFactory;
import java.util.Arrays;
import java.util.Set;

import static java.lang.Thread.sleep;
import static java.nio.charset.StandardCharsets.UTF_8;


public class MqTest {

  public static void main(String[] args) throws MqttException, InterruptedException {
    //testGettingStarted();
    //testConnectToServer();
    //testPublishAndSubscribe();
    testPublishAndSubscribeAPIClass();
  }

  // When everything will work, remove this test
  //@Test
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

  //@Test
  public static void testConnectToServer() throws MqttException, InterruptedException {
    MqClient mqClient = new MqClient();
    sleep(10000);
    mqClient.disconnectClient();
  }

  //@Test
  public static void testPublishAndSubscribe() throws MqttException, InterruptedException {
    MqClient subscriber = new MqClient();
    MqClient publisher = new MqClient();

    subscriber.subscribe("test/topic");
    publisher.publish("test/topic", "I am the msg");
    sleep(5000);
    publisher.disconnectClient();
    sleep(5000);
    subscriber.disconnectClient();
  }

  //@Test
  public static void testPublishAndSubscribeAPIClass() throws MqttException {
    MqClient client = new MqClient();

    Poll poll = new Poll();
    poll.setTopic("My subject");
    poll.setStatus(1);
    poll.setPublic(true);
    poll.setOwner(new UserProfile());

    client.saveData("/polls", poll);
    System.out.println("Created Polls :\n" + poll.toJson().toString());

    Set<Poll> retrievedPoll = client.getPolls("/polls");
    System.out.println("Retrieved Polls :\n");
    client.disconnectClient();
  }
}
