package no.hvl.dat250.jpa.assignment2.driver;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import no.hvl.dat250.jpa.assignment2.API;
import no.hvl.dat250.jpa.assignment2.Poll;
import no.hvl.dat250.jpa.assignment2.UserProfile;
import okhttp3.*;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNotNull;

public class APITest {
  private static final String SERVER_PORT = "6000";
  private static final String BASE_URL = "http://localhost:" + SERVER_PORT + "/";
  private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

  private final OkHttpClient client = new OkHttpClient();
  private final Gson gson = new Gson();
  private static final Type POLL_LIST_TYPE = new TypeToken<List<Poll>>(){}.getType();

  @BeforeClass
  public static void startRESTServer(){
    API.main(new String[]{SERVER_PORT});
  }

  @Test
  public void testCreatePoll(){
    Poll poll = new Poll();
    poll.setSubject("My subject");
    poll.setStatus(1);
    poll.setPublic(true);
    poll.setTimer(666L);
    poll.setOptions(new HashSet<>());
    poll.setTickets(new HashSet<>());
    poll.setOwner(new UserProfile());
    poll.setParticipants(new HashSet<>());

    // Execute post request
    final String postResult = doPostRequest(poll);

    // Parse the created poll
    final Poll createdPoll = gson.fromJson(postResult, Poll.class);

    // Make sure our created poll is correct.
    assertThat(createdPoll.getId(), is(poll.getId()));
    assertThat(createdPoll.getSubject(), is(poll.getSubject()));
    assertThat(createdPoll.getStatus(), is(poll.getStatus()));
    assertThat(createdPoll.getTimer(), is(poll.getTimer()));
    assertThat(createdPoll.getOptions(), is(poll.getOptions()));
    assertThat(createdPoll.getTickets(), is(poll.getTickets()));
    assertThat(createdPoll.getOwner(), is(poll.getOwner()));
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
}
