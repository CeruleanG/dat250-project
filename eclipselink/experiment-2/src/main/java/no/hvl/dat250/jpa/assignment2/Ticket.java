package no.hvl.dat250.jpa.assignment2;

import javax.persistence.*;

import com.google.gson.Gson;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@NoArgsConstructor
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int voteNb;

    private UserProfile voter;
    
    @ManyToOne
    private VotePoll fromPoll;

    public String toJson()
    {
        Gson gson = new Gson();
        String jsonIntString = gson.toJson(this);
        return jsonIntString;
    }
}
