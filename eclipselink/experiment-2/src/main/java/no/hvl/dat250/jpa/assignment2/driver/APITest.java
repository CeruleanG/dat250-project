package no.hvl.dat250.jpa.assignment2.driver;

import no.hvl.dat250.jpa.assignment2.API;
import no.hvl.dat250.jpa.assignment2.Poll;
import no.hvl.dat250.jpa.assignment2.Ticket;

import com.google.gson.Gson;
import okhttp3.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;

import com.google.gson.reflect.TypeToken;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;
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
    Poll poll = new Poll("test summary", "test description");

    // Execute post request
    final String postResult = doPostRequest(poll);

    // Parse the created poll
    final Poll createdPoll = gson.fromJson(postResult, Poll.class);

    // Make sure our created todo is correct.
    assertThat(createdPoll.getDescription(), is(poll.getDescription()));
    assertThat(createdPoll.getSummary(), is(poll.getSummary()));
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
