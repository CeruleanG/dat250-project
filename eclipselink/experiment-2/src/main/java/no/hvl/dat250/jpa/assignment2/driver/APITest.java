package no.hvl.dat250.jpa.assignment2.driver;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import no.hvl.dat250.jpa.assignment2.tools.DataJPA;
import no.hvl.dat250.jpa.assignment2.Poll;
import no.hvl.dat250.jpa.assignment2.UserProfile;
import okhttp3.*;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.*;

public class APITest {
    private static final String SERVER_PORT = "6060";
    private static final String BASE_URL = "http://localhost:" + SERVER_PORT + "/";
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private final OkHttpClient client = new OkHttpClient();
    private final Gson gson = new Gson();
    private static final Type POLL_LIST_TYPE = new TypeToken<List<Poll>>() {
    }.getType();

    @BeforeClass
    public static void startRESTServer() {
        API.main(new String[]{SERVER_PORT});
    }

    @Test
    public void testJPA()
    {
        Poll poll = new Poll();
        poll.setTopic("My subject");
        poll.setStatus(1);
        poll.setPublic(true);
        poll.setOwner(new UserProfile());

        DataJPA jpa = new DataJPA();
        jpa.saveData(poll);
    }
    @Test
    public void testCreatePoll() {
        Poll poll = new Poll();
        poll.setTopic("My subject");
        poll.setStatus(1);
        poll.setPublic(true);
        poll.setOwner(new UserProfile());

        // Execute post request
        final String postResult = doPostRequest(poll);

        // Parse the created poll
        final Poll createdPoll = gson.fromJson(postResult, Poll.class);

        // Make sure our created poll is correct.
        assertThat(createdPoll.getTopic(), is(poll.getTopic()));
        assertThat(createdPoll.getStatus(), is(poll.getStatus()));
        assertThat(createdPoll.getParticipants(), is(poll.getParticipants()));
        assertNotNull(createdPoll.getId());
    }

    private String doPostRequest(Poll poll) {
        // Prepare request and add the body
        RequestBody body = RequestBody.create(gson.toJson(poll), JSON);

        Request request = new Request.Builder()
                .url(BASE_URL + "polls")
                .post(body)
                .build();

        return doRequest(request);
    }

    private String doRequest(Request request) {
        try (Response response = client.newCall(request).execute()) {
            return Objects.requireNonNull(response.body()).string();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Poll> parsePolls(String result) {
        return gson.fromJson(result, POLL_LIST_TYPE);
    }

    private String doGetRequestPoll(Long pollId) {
        return this.doGetRequestPoll(BASE_URL + "polls/" + pollId);
    }

    private String doGetRequestPoll(String url) {
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        return doRequest(request);
    }

    private String doGetRequestPoll() {
        return this.doGetRequestPoll(BASE_URL + "polls");
    }

    @Test
    public void testReadOnePoll() {
        // Save one poll.
        final Poll poll = new Poll();
        poll.setTopic("My subject");
        poll.setStatus(1);
        poll.setPublic(true);
        poll.setOwner(new UserProfile());
        final Poll createdPoll = gson.fromJson(doPostRequest(poll), Poll.class);

        // Execute get request
        final String getResult = doGetRequestPoll(createdPoll.getId());

        // Parse returned poll.
        final Poll returnedPoll = gson.fromJson(getResult, Poll.class);

//    // The returned poll must be the one we created earlier.
//    assertThat(returnedPoll, is(createdPoll));
        // Make sure our created poll is correct.
        assertThat(returnedPoll.getId(), is(createdPoll.getId()));
        assertThat(returnedPoll.getTopic(), is(createdPoll.getTopic()));
        assertThat(returnedPoll.getStatus(), is(createdPoll.getStatus()));
        assertThat(returnedPoll.getOwner().toJson(), is(createdPoll.getOwner().toJson()));
        assertThat(returnedPoll.getParticipants(), is(createdPoll.getParticipants()));
        assertNotNull(returnedPoll.getId());
    }

    @Test
    public void testReadAllPoll() {
        // Save 2 polls.
        final Poll poll1 = new Poll();
        poll1.setTopic("My subject");
        poll1.setStatus(1);
        poll1.setPublic(true);
        poll1.setOwner(new UserProfile());

        final Poll poll2 = new Poll();
        poll2.setTopic("My subject");
        poll2.setStatus(1);
        poll2.setPublic(true);
        poll2.setOwner(new UserProfile());

        final Poll createdPoll1 = gson.fromJson(doPostRequest(poll1), Poll.class);
        final Poll createdPoll2 = gson.fromJson(doPostRequest(poll2), Poll.class);

        // Execute get request
        final String getResult = doGetRequestPoll();

        // Parse returned list of polls.
        final List<Poll> polls = parsePolls(getResult);

        // We have at least the two created polls.
        assertTrue(polls.size() >= 2);

        // The polls are contained in the list.
        assertTrue(polls.contains(createdPoll1));
        assertTrue(polls.contains(createdPoll2));
    }


    @Test
    public void testDeletePoll() {
        // Save an element, which we can delete later.

    }

    private String doDeleteRequest(Long pollId) {
        Request request = new Request.Builder()
                .url(BASE_URL + "polls/" + pollId)
                .delete()
                .build();

        doRequest(request);
        return doRequest(request);
    }

}