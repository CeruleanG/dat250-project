package no.hvl.dat250.jpa.assignment2.tools;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/*
File managing : https://www.w3schools.com/java/java_files_create.asp
*/

public class MqDataMonitor extends MqClient {
  private final static String BASE_PATH = "D:\\Program Files\\Java\\database\\mini_project\\";
  // C:\\Users\\rusli\\Desktop\\database\\mini_project\\
  private final static String POLL_FILE_NAME = "pollDataFile.txt";
  private final static String USER_FILE_NAME = "userDataFile.txt";
  private final static String COMPLETE_POLL_FILE_NAME = BASE_PATH + POLL_FILE_NAME;
  private final static String COMPLETE_USER_FILE_NAME = BASE_PATH + USER_FILE_NAME;
  private File pollDataFile = new File(COMPLETE_POLL_FILE_NAME);
  private File userDataFile = new File(COMPLETE_USER_FILE_NAME);

  public MqDataMonitor() throws MqttException, IOException {
    super();
    super.subscribe("#");
    createFile(Path.of(COMPLETE_POLL_FILE_NAME));
    createFile(Path.of(COMPLETE_USER_FILE_NAME));
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
      public void messageArrived(String topic, MqttMessage message) throws IOException {
        //System.out.println(topic + ": " + Arrays.toString(message.getPayload()));
        System.out.println("New message. Updating the data file...");
        // The data comes encoded to reduce the size they take on the cloud
        // We will keep the data decoded on the computer
        if (topic.matches("(.*)polls(.*)")) {
          writeFile(Path.of(COMPLETE_POLL_FILE_NAME), message.toString());
        } else if (topic.matches("(.*)users(.*)")) {
          writeFile(Path.of(COMPLETE_USER_FILE_NAME), message.toString());
        } else {
          System.out.println("Topic : \"" + topic.toString() + "\" is not referred");
        }
      }

      @Override
      // Called when an outgoing publish is complete
      public void deliveryComplete(IMqttDeliveryToken token) {
        System.out.println("delivery complete " + token);
      }
    });
  }

  private void createFile(Path file_pathName) throws IOException {
    try {
      Files.createFile(file_pathName);
      System.out.println("File created");
    } catch (FileAlreadyExistsException ex) {
      System.err.println("File already exists");
      deleteFile(file_pathName);
      createFile(file_pathName);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private void deleteFile(Path file_pathName) throws IOException {
    if (Files.deleteIfExists(file_pathName)) {
      System.out.println("File deleted");
    } else {
      System.err.println("File doesn't exist. Creation of the file...");
      this.createFile(file_pathName);
    }
  }

  public List<String> readFile(Path file_pathName) throws IOException {
    if (Files.exists(file_pathName)) {
      return Files.readAllLines(file_pathName, StandardCharsets.UTF_8);
    } else {
      System.out.println("The file you want to read doesn't exist\n" +
              "Creation of the file");
      createFile(file_pathName);
      readFile(file_pathName);
    }
    return null;
  }

  public void writeFile(Path file_pathName, String line) throws IOException {
    if (Files.exists(file_pathName)) {
      List<String> lines = new ArrayList<>();
      lines = this.readFile(file_pathName);
      lines.add(line);
      Files.write(file_pathName, lines, StandardCharsets.UTF_8, StandardOpenOption.CREATE);
    } else {
      System.out.println("The file you want to write in doesn't exist\n" +
              "Creation of the file");
      createFile(file_pathName);
      writeFile(file_pathName, line);
    }
  }

  public Set<String> getPolls() throws IOException {
    List<String> polls = new ArrayList<>();
    Path pollsFile = Path.of(COMPLETE_POLL_FILE_NAME);
    if (Files.exists(pollsFile)) {
      polls = (List<String>) this.readFile(pollsFile);
    }
    return new HashSet<>(polls);
  }
}
