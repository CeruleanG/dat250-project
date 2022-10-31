package no.hvl.dat250.jpa.assignment2.driver;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import no.hvl.dat250.jpa.assignment2.Poll;
import no.hvl.dat250.jpa.assignment2.UserProfile;
import no.hvl.dat250.jpa.assignment2.tools.DataJPA;
import org.eclipse.jetty.server.Authentication;
import org.junit.BeforeClass;
import org.junit.Test;

import java.lang.reflect.Type;
import java.util.List;

public class testInterface {
    BackEndInterface backEnd = new BackEndInterface();
    private static final String SERVER_PORT = "6060";

    @BeforeClass
    public static void startRESTServer() {
        API.main(new String[]{SERVER_PORT});
    }
    @Test
    public void Test() {

        Poll poll = new Poll();
        String printout;
        //System.out.println(printout);
        printout = backEnd.doPostRequest(poll);
        //System.out.println(printout);
        printout = backEnd.doPostRequest(poll);
        //System.out.println(printout);
        printout = backEnd.doGetRequestPoll();
        //System.out.println(printout);
        printout = backEnd.doGetRequestPoll(1L);
        //System.out.println(printout);
        poll.setTopic("Is this a Counterspell?");
        printout = backEnd.doPutRequestPoll(1L,poll);
        //System.out.println(printout);
        printout = backEnd.doGetRequestPoll();
        //System.out.println(printout);
        printout = backEnd.doDeleteRequestPoll(1L);
        //System.out.println(printout);
        printout = backEnd.doGetRequestPoll();
        //System.out.println(printout);

        UserProfile user = new UserProfile();
        printout = backEnd.doPostRequest(user);
        //System.out.println(printout);
        printout = backEnd.doPostRequest(user);
        //System.out.println(printout);
        printout = backEnd.doGetRequestUser();
        //System.out.println(printout);
        printout = backEnd.doGetRequestUser(1L);
        //System.out.println(printout);
        user.setLogin("Is this a Counterspell?");
        printout = backEnd.doPutRequestUser(1L,user);
        //System.out.println(printout);
        printout = backEnd.doGetRequestUser();
        //System.out.println(printout);
        printout = backEnd.doDeleteRequestUser(1L);
        //System.out.println(printout);
        printout = backEnd.doGetRequestUser();
        //System.out.println(printout);


    }

    @Test
    public void Test2() {

        Poll poll = new Poll();
        String printout;
        //System.out.println(printout);
        printout = backEnd.doPostRequest(poll);
        //System.out.println(printout);
        printout = backEnd.doPostRequest(poll);
        //System.out.println(printout);
        printout = backEnd.doGetRequestPoll();
        //System.out.println(printout);
        printout = backEnd.doGetRequestPoll(1L);
        //System.out.println(printout);
        poll.setTopic("Is this a Counterspell?");
        printout = backEnd.doPutRequestPoll(1L,poll);
        //System.out.println(printout);
        printout = backEnd.doGetRequestPoll();
        //System.out.println(printout);


        UserProfile user = new UserProfile();
        printout = backEnd.doPostRequest(user);
        //System.out.println(printout);
        printout = backEnd.doPostRequest(user);
        //System.out.println(printout);
        printout = backEnd.doGetRequestUser();
        //System.out.println(printout);
        printout = backEnd.doGetRequestUser(1L);
        //System.out.println(printout);
        user.setLogin("Is this a Counterspell?");
        printout = backEnd.doPutRequestUser(1L,user);
        //System.out.println(printout);
        printout = backEnd.doGetRequestUser();
        //System.out.println(printout);

        printout = backEnd.doGetRequestUser();
        System.out.println(printout);
        printout = backEnd.doGetRequestPoll();
        System.out.println(printout);

        System.out.println("=================");

        printout = backEnd.doPutRequestOwner(1L,1L);
        printout = backEnd.doGetRequestUser();
        System.out.println(printout);
        printout = backEnd.doGetRequestPoll();
        System.out.println(printout);

        System.out.println("=================");

        printout = backEnd.doPutRequestParticipant(1L,1L);
        printout = backEnd.doGetRequestUser();
        System.out.println(printout);
        printout = backEnd.doGetRequestPoll();
        System.out.println(printout);


    }

}

