package no.hvl.dat250.jpa.assignment2.tools;

import no.hvl.dat250.jpa.assignment2.Poll;
import no.hvl.dat250.jpa.assignment2.UserProfile;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import javax.net.ssl.SSLSocketFactory;
import java.util.Arrays;

import static java.nio.charset.StandardCharsets.UTF_8;

public class MqClient {
  protected static final MqttManager mqttManager = new MqttManager();
  protected final MqttClient mqttClient;
  private Boolean connected = false;
  protected String buffer = "";

  public MqClient() throws MqttException {
    this.mqttClient = new MqttClient(
            "ssl://" + mqttManager.getHost() + ":" + mqttManager.getPort(), // serverURI in format:
            // "protocol://name:port"
            MqttClient.generateClientId(), // ClientId,
            new MemoryPersistence()); // Persistence

    this.setCallback();
    this.connectClient();
  }

  final protected void connectClient() throws MqttException {
    MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
    mqttConnectOptions.setCleanSession(true);
    mqttConnectOptions.setUserName(mqttManager.getUsername());
    mqttConnectOptions.setPassword(mqttManager.getPassword().toCharArray());
    // using the default socket factory
    mqttConnectOptions.setSocketFactory(SSLSocketFactory.getDefault());
    mqttConnectOptions.setAutomaticReconnect(true);
    this.mqttClient.connect(mqttConnectOptions);
    this.connected = true;
  }

  final public void disconnectClient() throws MqttException {
    this.mqttClient.disconnect();
  }

  public void setCallback() {
    this.mqttClient.setCallback(new MqttCallback() {

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
  }

  public void subscribe(String topic) throws MqttException {
    if (!isConnected()) this.connectClient();
    this.mqttClient.subscribe(topic, 1); // subscribe to everything with QoS = 1
  }

  public void publish(String topic, String msg) throws MqttException {
    if (!isConnected()) this.connectClient();
    this.mqttClient.publish(
            topic,
            msg.getBytes(UTF_8),
            2,
            false);
  }

  public void saveData(String topic, Object jpaData) throws MqttException {
    if (jpaData instanceof Poll) {
      if (!isConnected()) this.connectClient();
      this.mqttClient.publish(
              topic,
              ((Poll) jpaData).toJson().getBytes(UTF_8),
              2,
              false);
    } else if (jpaData instanceof UserProfile) {
      if (!isConnected()) this.connectClient();
      this.mqttClient.publish(
              topic,
              ((UserProfile) jpaData).toJson().getBytes(UTF_8),
              2,
              false);
    }
  }

  /*
  public Set<Poll> getPolls(String topic) throws MqttException {
    if (!isConnected()) this.connectClient();
    this.mqttClient.subscribe(topic + "/#", (tpc, msg) -> {
      System.out.println(msg.toString());
      buffer = DatatypeConverter.parseBase64Binary(msg.toString()).toString();
    });
    System.out.println(buffer);
    buffer = "";
    Set<Poll> polls = new HashSet<>();
    return polls;
  }
*/

  private Boolean isConnected() {
    return this.connected;
  }
}
