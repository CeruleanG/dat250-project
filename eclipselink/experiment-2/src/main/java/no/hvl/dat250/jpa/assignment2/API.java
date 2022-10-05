package no.hvl.dat250.jpa.assignment2;

import com.google.gson.Gson;
import no.hvl.dat250.jpa.assignment2.UserProfile;
import no.hvl.dat250.jpa.assignment2.Poll;
import no.hvl.dat250.jpa.assignment2.Ticket;
import org.eclipse.jetty.util.DateCache;

import java.util.HashSet;
import java.util.Set;

import static spark.Spark.*;

public class API {
  public static void main(String[] args){
    if(args.length > 0){
      port(Integer.parseInt(args[0]));
    } else {
      port(6000);
    }
    after((req, res) -> res.type("application/json"));

    Set<Poll> polls = new HashSet<>();
    Set<Ticket> tickets = new HashSet<>();

    //***** POLL *****\\
    // Create a Poll
    post(
            "/polls",
            (req,res)->
            {
              Poll poll = new Poll();
              Gson gson = new Gson();
              poll = gson.fromJson(req.body(),Poll.class);

              polls.add(poll);
              return poll.toJson();
            }
    );

    // Delete a Poll
    delete(
            "/poll/:id",
            (req,res)->
            {
              if(!req.params(":id").matches("-?\\d+(\\.\\d+)?")) {
                return String.format("The id \"%s\" is not a number!", req.params(":id"));
              }
              for(Poll poll : polls){
                if(req.params(":id").equals(poll.getId().toString())){
                  polls.remove(poll);
                  return poll.toJson();
                }
              }
              return String.format("The id \"%s\" not found!", req.params(":id"));
            }
    );
    //***** POLL *****\\
    //***** TICKET *****\\
    // Create a Ticket
    post(
            "/poll/:id/ticket/",
            (req,res)->
            {
              if(!req.params(":id").matches("-?\\d+(\\.\\d+)?")) {
                return String.format("The id \"%s\" is not a number!", req.params(":id"));
              }
              for(Poll poll : polls){
                if(req.params(":id").equals(poll.getId().toString())){
                  Ticket ticket = new Ticket();
                  Gson gson = new Gson();
                  ticket = gson.fromJson(req.body(),Ticket.class);

                  ticket.setFromPoll(poll);
                  poll.addTicket(ticket);
                  tickets.add(ticket);
                  return ticket.toJson();
                }
              }
              return String.format("The id \"%s\" not found!", req.params(":id"));
            }
    );

    // Delete a Ticket
    delete(
            "/ticket/:id",
            (req,res)->
            {
              if(!req.params(":id").matches("-?\\d+(\\.\\d+)?")) {
                return String.format("The id \"%s\" is not a number!", req.params(":id"));
              }
              for(Ticket ticket : tickets){
                if(req.params(":id").equals(ticket.getId().toString())){
                  tickets.remove(ticket);
                  return ticket.toJson();
                }
              }
              return String.format("The id \"%s\" not found!", req.params(":id"));
            }
    );
    //***** TICKET *****\\
  }
}
