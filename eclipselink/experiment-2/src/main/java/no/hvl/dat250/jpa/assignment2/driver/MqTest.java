package no.hvl.dat250.jpa.assignment2.driver;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import no.hvl.dat250.jpa.assignment2.Poll;
import no.hvl.dat250.jpa.assignment2.UserProfile;
import no.hvl.dat250.jpa.assignment2.tools.MqClient;
import no.hvl.dat250.jpa.assignment2.tools.MqDataMonitor;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

import static java.lang.Thread.sleep;
import static java.nio.charset.StandardCharsets.UTF_8;

public class MqTest {
  private static final Gson gson = new Gson();
  private static final Type POLL_LIST_TYPE = new TypeToken<List<Poll>>() {
  }.getType();
  private static MqDataMonitor dataMonitor;

  public static void main(String[] args) throws MqttException, InterruptedException, IOException {
    MqDataMonitor dataMonitor = new MqDataMonitor();

    //testGettingStarted();
    //testConnectToServer();
    //testPublishAndSubscribe();
    testSaveData();
    //testGetPolls();

    dataMonitor.disconnectClient();
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
  public static void testSaveData() throws MqttException {
    /*
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
    */
    MqClient client = new MqClient();

    Poll poll = new Poll();
    poll.setTopic("My subject");
    poll.setStatus(1);
    poll.setPublic(true);
    poll.setOwner(new UserProfile());

    client.saveData("/polls", poll);
    client.disconnectClient();
  }

  //@Test
  public static void testGetPolls() throws MqttException, IOException {
    MqClient client = new MqClient();

    final Poll poll1 = new Poll();
    poll1.setTopic("My subject");
    poll1.setStatus(1);
    poll1.setPublic(true);
    poll1.setOwner(new UserProfile());

    final Poll poll2 = new Poll();
    poll2.setTopic("My subject");
    poll2.setStatus(1);
    poll2.setPublic(true);
    poll2.setOwner(new UserProfile());

    client.saveData(poll1.getTopic(), poll1);
    client.saveData(poll2.getTopic(), poll2);

    final String getResult = dataMonitor.getPolls().toString();
    final List<Poll> polls = parsePolls(getResult);

    System.out.println("Polls : " + polls.toString());

    client.disconnectClient();
  }

  private static List<Poll> parsePolls(String result) {
    return gson.fromJson(result, POLL_LIST_TYPE);
  }
}
