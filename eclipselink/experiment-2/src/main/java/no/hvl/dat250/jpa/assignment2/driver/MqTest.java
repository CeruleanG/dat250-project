package no.hvl.dat250.jpa.assignment2.driver;

import no.hvl.dat250.jpa.assignment2.tools.MqClient;
import org.junit.Test;

public class MqTest {

  @Test
  public void testConnectToServer() {
    MqClient publisher = new MqClient();
    MqClient subscriber = new MqClient();

    publisher.publishBlocking();
    subscriber.subscribeBlocking();
  }
}
