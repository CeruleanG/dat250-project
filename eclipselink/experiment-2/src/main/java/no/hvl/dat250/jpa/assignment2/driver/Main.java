package no.hvl.dat250.jpa.assignment2.driver;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import no.hvl.dat250.jpa.assignment2.UserProfile;
import no.hvl.dat250.jpa.assignment2.Poll;
import no.hvl.dat250.jpa.assignment2.VotePoll;
import no.hvl.dat250.jpa.assignment2.Ticket;

public class Main {
    public static final String PERSISTENCE_UNIT_NAME = "miniproject";

    public static void main(String[] args) {

        EntityManagerFactory factory;

        factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        EntityManager em = factory.createEntityManager();
        
        em.getTransaction().begin();
        // Creation of the different variables
        UserProfile user = new UserProfile();
        Poll poll = new Poll();
        VotePoll votePoll = new VotePoll();
        Ticket ticket = new Ticket();
        
        //user.setId((long) 1);
        user.setLogin("login");
        user.setPwd("pwd");
        //poll.setId((long) 2);
        poll.setPublic(true);
        poll.setStatus(1);
        poll.setTopic("What");
        poll.setTimer((long) 1000);
        votePoll.setId((long) 3);
        Set<String> options = new HashSet<>();
        options.add("1");
        votePoll.setOptions(options);
        //ticket.setId((long) 4);
        ticket.setVoteNb(1);
        
        Set<Poll> ownedPolls = new HashSet<>();
        ownedPolls.add(poll);
        Set<Poll> participatedPolls = new HashSet<>();
        participatedPolls.add(poll);
        Set<UserProfile> userProfiles = new HashSet<>();
        userProfiles.add(user);
        Set<Ticket> tickets = new HashSet<>();
        tickets.add(ticket);
        
        user.setPollsOwned(ownedPolls);
        user.setPollsParticipated(participatedPolls);
        poll.setOwner(user);
        poll.setParticipants(userProfiles);
        //poll.setVotePoll(votePoll);
        votePoll.setTickets(tickets);
        //ticket.setFromPoll(votePoll);
        ticket.setVoter(user);
        
        em.persist(user);
        em.persist(poll);
        em.persist(votePoll);
        em.persist(ticket);
        em.getTransaction().commit();

        em.close();
        
    }
}
