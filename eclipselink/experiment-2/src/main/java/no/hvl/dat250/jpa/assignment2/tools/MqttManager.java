package no.hvl.dat250.jpa.assignment2.tools;

/*

The following class will manage the mqtt services. It will allow us to send our data to a server and
a database. If we got enough time to develop this part it will also allow us to manage the
authentifications to the application.

The website used as sources are the following :
  _ https://www.hivemq.com/blog/how-to-get-started-with-mqtt/
  _ https://github.com/hivemq/hivemq-mqtt-client/
  _ (Getting Started ) https://console.hivemq.cloud/clients/websocket-java?uuid=679f8737b6f343edbe51d41d9f6dbc53
  _ (Connect to HiveMq cluster) https://www.hivemq.com/blog/connecting-eclipse-paho-mqtt-java-client-hivemq-cloud-broker/
  _ https://github.com/hivemq/mqtt-cli

The documents used as sources are the following :
  _ hivemq-ebook-mqtt-essentials from https://mqtt.org/

We will use : HiveMq as broker and as Browser Client (https://www.hivemq.com/public-mqtt-broker/)
  user : DAT250_projectChLeRu
  psw : projetDeMerde!666
*/



class MqttManager {

  private final String host = "679f8737b6f343edbe51d41d9f6dbc53.s1.eu.hivemq.cloud";  // https://console.hivemq.cloud/clusters
                                                                                      // Connection Settings -> Cluster URL
  private final String username = "DAT250_projectChLeRu"; // your credentials
  private final String password = "projetDeMerde!666";
  private final String port = "8883"; // https://console.hivemq.cloud/clusters
                                      // Connection Settings -> Port

  protected final String getHost(){return this.host;}
  protected final String getPort(){return this.port;}
  protected final String getUsername(){return this.username;}
  protected final String getPassword(){return this.password;}
}
