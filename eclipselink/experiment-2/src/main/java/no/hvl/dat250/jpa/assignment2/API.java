package no.hvl.dat250.jpa.assignment2;
//testAntho

//meow meow test

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
  public static void main(String[] args){
    if(args.length > 0){
      port(Integer.parseInt(args[0]));
    } else {
      port(6000);
    }
    after((req, res) -> res.type("application/json"));

    factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
    EntityManager em = factory.createEntityManager();
    Query q = em.createQuery("select t from Poll t");
    Set<Poll> polls = new HashSet<>(q.getResultList());
    //Set<Poll> polls = new HashSet<>();
    q = em.createQuery("select t from Ticket t");
    Set<Ticket> tickets = new HashSet<>(q.getResultList());
    //Set<Ticket> tickets = new HashSet<>();

    /*em.getTransaction().begin();
    em.persist(polls);
    em.getTransaction().commit();
    em.close();*/


    //***** POLL *****\\
    // Get Polls
    get(
            "/polls",
            (req,res)->
            {
              Gson gson = new Gson();
              return gson.toJson(polls);
            }
    );

    // Get a Poll with a given Id
    get(
            "polls/:id",
            (req,res)->
            {
              if(!req.params(":id").matches("-?\\d+(\\.\\d+)?")){
                return String.format("The id \"%s\" is not a number!", req.params(":id"));}
              for (Poll poll : polls){
                if ( req.params(":id").equals(poll.getId().toString())) {
                  return poll.toJson();
                }}
              return String.format("Poll with the id \"%s\" not found!", req.params(":id"));
            }
    );

    // Create a Poll
    post(
            "/polls",
            (req,res)->
            {
              Poll poll;
              Gson gson = new Gson();
              poll = gson.fromJson(req.body(),Poll.class);

              polls.add(poll);
              /*
              em.getTransaction().begin();
              em.persist(poll);
              em.getTransaction().commit();
              //em.flush();
              em.close();
              */
              return poll.toJson();
            }
    );

    // Delete a Poll
    delete(
            "/polls/:id",
            (req,res)->
            {
              if(!req.params(":id").matches("-?\\d+(\\.\\d+)?")) {
                return String.format("The id \"%s\" is not a number!", req.params(":id"));
              }
              for(Poll poll : polls){
                if(req.params(":id").equals(poll.getId().toString())){
                  polls.remove(poll);
                  /*
                  em.getTransaction().begin();
                  em.persist(poll);
                  em.getTransaction().commit();
                  //em.close();
                  */
                  return poll.toJson();
                }
              }
              return String.format("The id \"%s\" not found!", req.params(":id"));
            }
    );

    //********************
    // TO DELETE
    /*
    int findId = req.params(":id");
    em.createQuery("delete from Poll p where p.id=:findId").executeUpdate();
    */
    //********************

    //***** POLL *****\\
    //***** TICKET *****\\
    // Create a Ticket
    put(
            "/polls/:id/ticket",
            (req,res)->
            {
              if(!req.params(":id").matches("-?\\d+(\\.\\d+)?")) {
                return String.format("The id \"%s\" is not a number!", req.params(":id"));
              }
              for(Poll poll : polls){
                if(req.params(":id").equals(poll.getId().toString())){
                  Ticket ticket;
                  Gson gson = new Gson();
                  ticket = gson.fromJson(req.body(),Ticket.class);

                  ticket.setFromPoll(poll);
                  poll.addTicket(ticket);
                  tickets.add(ticket);
                  /*
                  em.getTransaction().begin();
                  em.persist(ticket);
                  em.getTransaction().commit();
                  //em.close();

                   */
                  return ticket.toJson();
                }
              }
              return String.format("The id \"%s\" not found!", req.params(":id"));
            }
    );

    // Delete a Ticket
    delete(
            "/tickets/:id",
            (req,res)->
            {
              if(!req.params(":id").matches("-?\\d+(\\.\\d+)?")) {
                return String.format("The id \"%s\" is not a number!", req.params(":id"));
              }
              for(Ticket ticket : tickets){
                if(req.params(":id").equals(ticket.getId().toString())){
                  tickets.remove(ticket);
                  /*
                  em.getTransaction().begin();
                  em.persist(ticket);
                  em.getTransaction().commit();
                  //em.close();

                   */
                  return ticket.toJson();
                }
              }
              return String.format("The id \"%s\" not found!", req.params(":id"));
            }
    );
    //***** TICKET *****\\
  }
}
