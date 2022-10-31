package no.hvl.dat250.jpa.assignment2;

import com.google.gson.Gson;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.util.HashSet;
import java.util.Set;

import static spark.Spark.*;

public class API {
  private static final String PERSISTENCE_UNIT_NAME = "miniproject";
  private static EntityManagerFactory factory;

  public static void main(String[] args) {
    if (args.length > 0) {
      port(Integer.parseInt(args[0]));
    } else {
      port(6060);
    }
    after((req, res) -> res.type("application/json"));

    DataJPA jpa = new DataJPA();

    //****************** POLL END ******************\\
    // Create a Poll
    post(
            "/polls",
            (req, res) ->
            {
              Gson gson = new Gson();
              Poll poll = gson.fromJson(req.body(), Poll.class);
              jpa.saveData(poll);
              return poll.toJson();
            }
    );

    // Get all Polls
    get(
            "/polls",
            (req, res) ->
            {
              Gson gson = new Gson();
              return gson.toJson(jpa.getPolls());
            }
    );

    // Get a Poll with a given Id
    get(
            "polls/:id",
            (req, res) ->
            {
              if (!req.params(":id").matches("-?\\d+(\\.\\d+)?")) {
                return String.format("The poll id \"%s\" is not a number!", req.params(":id"));
              }
              Set<Poll> polls=jpa.getPolls();
              for (Poll poll : polls) {
                if (req.params(":id").equals(poll.getId().toString())) {
                  return poll.toJson();
                }
              }
              return String.format("Poll with the id \"%s\" not found!", req.params(":id"));
            }
    );

    // Update a Poll with a given Id
    put(
            "polls/:id",
            (req, res) ->
            {
              if (!req.params(":id").matches("-?\\d+(\\.\\d+)?")) {
                return String.format("The poll id \"%s\" is not a number!", req.params(":id"));
              }
              Set<Poll> polls=jpa.getPolls();
              for (Poll poll : polls) {
                if (req.params(":id").equals(poll.getId().toString())) {
                  Gson gson = new Gson();
                  Poll fromPoll = gson.fromJson(req.body(), Poll.class);
                  poll.setTopic(fromPoll.getTopic());
                  poll.setStatus(fromPoll.getStatus());
                  poll.setPublic(fromPoll.isPublic());
                  poll.setOwner(fromPoll.getOwner());
                  poll.setParticipants(fromPoll.getParticipants());
                  jpa.saveData(poll);
                  return poll.toJson();
                }
              }
              return String.format("Poll with the id \"%s\" not found!", req.params(":id"));
            }
    );

    // Delete a Poll with a given Id
    delete(
            "/polls/:id",
            (req, res) ->
            {
              if (!req.params(":id").matches("-?\\d+(\\.\\d+)?")) {
                return String.format("The poll id \"%s\" is not a number!", req.params(":id"));
              }
              Set<Poll> polls = jpa.getPolls();
              for (Poll poll : polls) {
                if (req.params(":id").equals(poll.getId().toString())) {
                  jpa.deleteData(poll);
                  return poll.toJson();
                }
              }
              return String.format("Poll with the id \"%s\" not found!", req.params(":id"));
            }
    );
    //****************** POLL END ******************\\

    //***************** User Start *****************\\
    //create a user
    post(
            "/users",
            (req, res) ->
            {
              Gson gson = new Gson();
              UserProfile user = gson.fromJson(req.body(), UserProfile.class);
              jpa.saveData(user);
              return user.toJson();
            }
    );

    // Get all Users
    get(
            "/users",
            (req, res) ->
            {
              Gson gson = new Gson();
              return gson.toJson(jpa.getUsers());
            }
    );

    // Get a Poll with a given Id
    get(
            "users/:id",
            (req, res) ->
            {
              if (!req.params(":id").matches("-?\\d+(\\.\\d+)?")) {
                return String.format("The user id \"%s\" is not a number!", req.params(":id"));
              }
              Set<UserProfile> users=jpa.getUsers();
              for (UserProfile user : users) {
                if (req.params(":id").equals(user.getId().toString())) {
                  return user.toJson();
                }
              }
              return String.format("User with the id \"%s\" not found!", req.params(":id"));
            }
    );

    // Update a Poll with a given Id
    put(
            "users/:id",
            (req, res) ->
            {
              if (!req.params(":id").matches("-?\\d+(\\.\\d+)?")) {
                return String.format("The user id \"%s\" is not a number!", req.params(":id"));
              }
              Set<UserProfile> users=jpa.getUsers();
              for (UserProfile user : users) {
                if (req.params(":id").equals(user.getId().toString())) {
                  Gson gson = new Gson();
                  UserProfile fromUser = gson.fromJson(req.body(), UserProfile.class);
                  user.setLogin(fromUser.getLogin());
                  user.setPwd(fromUser.getPwd());
                  user.setPollsOwned(fromUser.getPollsOwned());
                  user.setPollsParticipated(fromUser.getPollsParticipated());

                  jpa.saveData(user);
                  return user.toJson();
                }
              }
              return String.format("Poll with the id \"%s\" not found!", req.params(":id"));
            }
    );

    // make the User with "ID" the owner of the Poll "ID2"
    put(
            "users/:id/owns/:id2",
            (req, res) ->
            {
              if (!req.params(":id").matches("-?\\d+(\\.\\d+)?")) {
                return String.format("The user id \"%s\" is not a number!", req.params(":id"));
              }
              if (!req.params(":id2").matches("-?\\d+(\\.\\d+)?")) {
                return String.format("The Poll id \"%s\" is not a number!", req.params(":id"));
              }
              Set<UserProfile> users=jpa.getUsers();
              Set<Poll> polls = jpa.getPolls();
              for (UserProfile user : users) {
                if (req.params(":id").equals(user.getId().toString())) {
                  for (Poll poll : polls) {
                    if (req.params(":id2").equals(poll.getId().toString())) {
                      //poll.setOwner(user);
                      user.getPollsOwned().add(poll);
                    }
                  }
                  jpa.saveData(user);
                  return user.toJson();
                }
              }
              return String.format("Poll with the id \"%s\" not found!", req.params(":id"));
            }
    );
    put(
            "users/:id/owned/:id2",
            (req, res) ->
            {
              if (!req.params(":id").matches("-?\\d+(\\.\\d+)?")) {
                return String.format("The user id \"%s\" is not a number!", req.params(":id"));
              }
              if (!req.params(":id2").matches("-?\\d+(\\.\\d+)?")) {
                return String.format("The Poll id \"%s\" is not a number!", req.params(":id"));
              }
              Set<UserProfile> users=jpa.getUsers();
              Set<Poll> polls = jpa.getPolls();
              for (UserProfile user : users) {
                if (req.params(":id").equals(user.getId().toString())) {
                  for (Poll poll : polls) {
                    if (req.params(":id2").equals(poll.getId().toString())) {
                      poll.setOwner(user);
                      //user.getPollsOwned().add(poll);
                    }
                  }
                  jpa.saveData(user);
                  return user.toJson();
                }
              }
              return String.format("Poll with the id \"%s\" not found!", req.params(":id"));
            }
    );

    // Delete a Poll with a given Id
    delete(
            "/users/:id",
            (req, res) ->
            {
              if (!req.params(":id").matches("-?\\d+(\\.\\d+)?")) {
                return String.format("The user id \"%s\" is not a number!", req.params(":id"));
              }
              Set<UserProfile> users=jpa.getUsers();
              for (UserProfile user : users) {
                if (req.params(":id").equals(user.getId().toString())) {
                  jpa.deleteData(user);
                  return user.toJson();
                }
              }
              return String.format("Poll with the id \"%s\" not found!", req.params(":id"));
            }
    );
    //****************** USER END ******************\\

  }
}
