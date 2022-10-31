package no.hvl.dat250.jpa.assignment2.driver;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import no.hvl.dat250.jpa.assignment2.Poll;
import no.hvl.dat250.jpa.assignment2.UserProfile;
import okhttp3.*;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;

public class BackEndInterface {
    private static final String SERVER_PORT = "6060";
    private static final String BASE_URL = "http://localhost:" + SERVER_PORT + "/";
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private final OkHttpClient client = new OkHttpClient();
    private final Gson gson = new Gson();
    private static final Type POLL_LIST_TYPE = new TypeToken<List<Poll>>() {}.getType();
    private static final Type USER_LIST_TYPE = new TypeToken<List<UserProfile>>() {}.getType();

    public BackEndInterface(){}
    public void startRESTServer() {
        API.main(new String[]{/*SERVER_PORT*/});
    }




    // JSON => Java
    private List<Poll> parsePolls(String result) {
        return gson.fromJson(result, POLL_LIST_TYPE);
    }
    private List<Poll> parseUsers(String result) {
        return gson.fromJson(result, USER_LIST_TYPE);
    }

    //*********************** LEVEL 0 ***********************//
    //****************** POLL ******************//
    //POST a poll
    public String doPostRequest(Poll poll) {
        // Prepare request and add the body
        RequestBody body = RequestBody.create(gson.toJson(poll), JSON);

        Request request = new Request.Builder()
                .url(BASE_URL + "polls")
                .post(body)
                .build();

        return doRequest(request);
    }
    //GET all polls
    public String doGetRequestPoll()
    {
        return this.doGetRequest(BASE_URL + "polls");
    }
    //GET a poll with specific ID
    public String doGetRequestPoll(Long pollId) {
        return this.doGetRequest(BASE_URL + "polls/" + pollId);
    }

    //PUT a poll and updates it with specific ID
    public String doPutRequestPoll(Long pollId,Poll poll) {
        // Prepare request and add the body
        RequestBody body = RequestBody.create(gson.toJson(poll), JSON);

        Request request = new Request.Builder()
                .url(BASE_URL + "polls/" + pollId)
                .put(body)
                .build();

        return doRequest(request);
    }
    //DELETE a poll with specific ID
    public String doDeleteRequestPoll(Long pollId) {
        Request request = new Request.Builder()
                .url(BASE_URL + "polls/" + pollId)
                .delete()
                .build();
        return doRequest(request);
    }
    //****************** POLL ******************//

    //****************** USER ******************//
    //POST a user
    public String doPostRequest(UserProfile user) {
        // Prepare request and add the body
        RequestBody body = RequestBody.create(gson.toJson(user), JSON);

        Request request = new Request.Builder()
                .url(BASE_URL + "users")
                .post(body)
                .build();

        return doRequest(request);
    }
    //GET all users
    public String doGetRequestUser()
    {
        return this.doGetRequest(BASE_URL + "users");
    }
    //GET a user with specific ID
    public String doGetRequestUser(Long userId) {
        return this.doGetRequest(BASE_URL + "users/" + userId);
    }
    //PUT a user and updates it with specific ID
    public String doPutRequestUser(Long userId,UserProfile user) {
        // Prepare request and add the body
        RequestBody body = RequestBody.create(gson.toJson(user), JSON);

        Request request = new Request.Builder()
                .url(BASE_URL + "users/" + userId)
                .put(body)
                .build();

        return doRequest(request);
    }
    //DELETE a user with specific ID
    public String doDeleteRequestUser(Long userId) {
        Request request = new Request.Builder()
                .url(BASE_URL + "users/" + userId)
                .delete()
                .build();

        return doRequest(request);
    }
    //****************** USER ******************//

    //************** RELATIONSHIP **************//
    //PUT a User with ID1 the owner of a Poll with ID2 and vice versa
    public String doPutRequestOwner(UserProfile user,Poll poll){
        return doPutRequestOwner(user.getId(),poll.getId());
    }
    public String doPutRequestOwner(Long userId,Long pollId){
        RequestBody body = RequestBody.create(gson.toJson(null), JSON);
        Request request = new Request.Builder()
                .url(BASE_URL + "users/" + userId + "/owns/" + pollId)
                .put(body)
                .build();

        return doRequest(request);
    }
    //PUT a User with ID1 a participant of a Poll with ID2 and vice versa
    public String doPutRequestParticipant(UserProfile user,Poll poll){
        return doPutRequestParticipant(user.getId(),poll.getId());
    }
    public String doPutRequestParticipant(Long userId,Long pollId){
        RequestBody body = RequestBody.create(gson.toJson(null), JSON);
        Request request = new Request.Builder()
                .url(BASE_URL + "users/" + userId + "/participates/" + pollId)
                .put(body)
                .build();

        return doRequest(request);
    }
    //************** RELATIONSHIP **************//
    //*********************** LEVEL 0 ***********************//

    //*********************** LEVEL -1 **********************//

    //sends any GET request
    private String doGetRequest(String url) {
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        return doRequest(request);
    }

    //*********************** LEVEL -1 **********************//

    //*********************** LEVEL -2 **********************//

    //sends any request
    private String doRequest(Request request) {
        try (Response response = client.newCall(request).execute()) {
            return Objects.requireNonNull(response.body()).string();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    //*********************** LEVEL -2 **********************//


}
