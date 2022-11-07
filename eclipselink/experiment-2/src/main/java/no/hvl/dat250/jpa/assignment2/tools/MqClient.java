package no.hvl.dat250.jpa.assignment2.tools;

import com.hivemq.client.mqtt.MqttClient;
import com.hivemq.client.mqtt.MqttGlobalPublishFilter;
import com.hivemq.client.mqtt.MqttWebSocketConfig;
import com.hivemq.client.mqtt.datatypes.MqttQos;
import com.hivemq.client.mqtt.mqtt5.Mqtt5BlockingClient;
import com.hivemq.client.mqtt.mqtt5.Mqtt5Client;

import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class MqClient {
  protected final MqttManager mqttManager = new MqttManager();
  private Mqtt5Client client = MqttClient.builder()
          .useMqttVersion5()
          .serverHost(mqttManager.getHost()) //.serverHost("broker.hivemq.com")
          .serverPort(Integer.parseInt(mqttManager.getPort()))
          .sslWithDefaultConfig()
          .webSocketConfig(MqttWebSocketConfig.builder().subprotocol("mqtt").serverPath("/mqtt").build())
          .identifier(UUID.randomUUID().toString())
          .build();

  public void publishBlocking(){
    client.toBlocking().connectWith()
            .simpleAuth()
            .username(mqttManager.getUsername())
            .password(StandardCharsets.UTF_8.encode(mqttManager.getPassword()))
            .applySimpleAuth()
            .send();

    client.toBlocking().publishWith().topic("test/topic").qos(MqttQos.AT_LEAST_ONCE).payload("1".getBytes()).send();
    client.toBlocking().disconnect();
  }
  public void subscribeBlocking(){
    client.toBlocking().connectWith()
            .simpleAuth()
            .username(mqttManager.getUsername())
            .password(StandardCharsets.UTF_8.encode(mqttManager.getPassword()))
            .applySimpleAuth()
            .send();

    try (final Mqtt5BlockingClient.Mqtt5Publishes publishes = client.toBlocking().publishes(MqttGlobalPublishFilter.ALL)) {

      client.toBlocking().subscribeWith().topicFilter("test/topic").qos(MqttQos.AT_LEAST_ONCE).send();

      publishes.receive(1, TimeUnit.SECONDS).ifPresent(System.out::println);
      publishes.receive(100, TimeUnit.MILLISECONDS).ifPresent(System.out::println);

    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    } finally {
      client.toBlocking().disconnect();
    }
  }

}
