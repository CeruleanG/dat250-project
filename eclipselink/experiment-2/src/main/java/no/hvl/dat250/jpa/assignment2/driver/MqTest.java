package no.hvl.dat250.jpa.assignment2.driver;

import no.hvl.dat250.jpa.assignment2.tools.MqClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.junit.Test;

import static java.lang.Thread.sleep;


public class MqTest {

  @Test
  public void testConnectToServer() throws MqttException, InterruptedException {
    MqClient mqClient = new MqClient();
    sleep(10000);
    mqClient.disconnectClient();
  }
}
