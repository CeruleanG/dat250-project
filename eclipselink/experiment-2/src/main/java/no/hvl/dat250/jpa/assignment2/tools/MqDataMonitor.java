package no.hvl.dat250.jpa.assignment2.tools;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import javax.xml.bind.DatatypeConverter;
import java.io.File;
import java.io.IOException;

/*
File managing : https://www.w3schools.com/java/java_files_create.asp
*/

public class MqDataMonitor extends MqClient {
  private final static String BASE_PATH = "D:\\Program Files\\Java\\database\\mini_project\\";
  // C:\\Users\\rusli\\Desktop\\database\\mini_project\\
  private File dataFile = new File(BASE_PATH + "dataFile.txt");

  public MqDataMonitor() throws MqttException {
    super();
    super.subscribe("#");
    createFile();
  }

  @Override
  public void setCallback() {
    this.mqttClient.setCallback(new MqttCallback() {

      @Override
      // Called when the client lost the connection to the broker
      public void connectionLost(Throwable cause) {
        System.out.println("client lost connection " + cause);
      }

      @Override
      public void messageArrived(String topic, MqttMessage message) {
        //System.out.println(topic + ": " + Arrays.toString(message.getPayload()));
        System.out.println("New message. Updating the data file...");
        // The data comes encoded to reduce the size they take on the cloud
        // We will keep the data decoded on the computer
        System.out.println("Caught message : " + message);
        buffer = DatatypeConverter.parseBase64Binary(message.toString()).toString();
        System.out.println("Decoded message : " + buffer);
        buffer = "";
      }

      @Override
      // Called when an outgoing publish is complete
      public void deliveryComplete(IMqttDeliveryToken token) {
        System.out.println("delivery complete " + token);
      }
    });
  }

  private void createFile() {
    try {
      if (this.dataFile.createNewFile()) {
        System.out.println("File created: " + this.dataFile.getName());
      } else {
        System.out.println("File already exists.\nSuppression of the existing file");
        this.deleteFile();
        System.out.println("Creation of a new file...");
        this.createFile();
      }
    } catch (IOException e) {
      System.out.println("An error occurred.");
      e.printStackTrace();
    }
  }

  private void deleteFile() {
    try {
      if (this.dataFile.delete()) {
        System.out.println(this.dataFile.getName() + " deleted");
      } else {
        System.out.println("Suppression failed");
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
