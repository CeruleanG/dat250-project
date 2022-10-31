# README
## Classes
This repository consists of 6 classes that are useful as shown in the picture below.

![image](https://user-images.githubusercontent.com/84096851/199046917-290d94b9-a958-4e39-83fa-961261ec5498.png)
### Poll & UserProfile
These two classes defines our database: polls and users.

These classes should not be modified.
### tools/DataJPA
This class serves as an interface between the program and the database. It retrieves/saves/modifies the content of the database and feed them to the running program, noticeably `API.java` class.

### drive/API
This class handles HTTPS requests. See further explainations in the next class.

### drive/BackEndInterface
This class acts as an interface between the back end and the front end. Every operation in the front end should be handled solely by an instance of this class.

Here you can see a list of functions that should be useful in the front end developement. Unless specified, every function returns a JSON string that represent a Java object:

`String doPostRequest(Poll poll)` : it saves the object `poll` into the database.

`String doGetRequestPoll()` it returns all polls in the database.

`String doGetRequestPoll(Long pollId)` it searches and returns a poll with its ID equals to `pollId`.

`String doPutRequestPoll(Long pollId,Poll poll)` it searches a poll with its ID equals to `pollId` and updates its proprety from `poll`. The function returns the modified poll.

`String doDeleteRequestPoll(Long pollId)` it searches and deletes a poll with its ID equals to `pollID`. The function returns the deleted poll.

**There are equivalent functions for UserProfile Class. Replace every instance of "Poll" and "poll" by "User" and "user"**

`String doPutRequestOwner(UserProfile user,Poll poll)` & `String doPutRequestOwner(Long userId,Long pollId)` These two functions map the ownership of the `poll` and the "user", so the `user` is the owner of `poll` and the `poll` is owned by `user`. The function returns the updated user.

`String doPutRequestParticipant(UserProfile user,Poll poll)` & `public String doPutRequestParticipant(Long userId,Long pollId)` do similar tasks as two functions above expect they are for the participation.

### driver/testInterface
This class is a test case that demonstrate how to use an instance of `BackEndInterface` class to get data.

Notice that `API.main(new String[]{SERVER_PORT});` this line should always be used before starting any operation. During the developement, you avoid using debug mode as it may cause port issue from the server end thus disconnect yourself from said server.
## Postman Export
Use this link to test the server.
https://www.postman.com/flight-operator-27899014/workspace/user-poll-test
