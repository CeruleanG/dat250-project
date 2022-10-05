package no.hvl.dat250.jpa.assignment2;

import javax.persistence.*;

import com.google.gson.Gson;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Poll {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String subject;
    private int Status;
    private boolean isPublic;
    private Long timer;

    private VotePoll votePoll;
    
    @ManyToOne
    private UserProfile owner;
    
    @ManyToMany
    private Set<UserProfile> participants;

    public String toJson()
    {
        Gson gson = new Gson();
        String jsonIntString = gson.toJson(this);
        return jsonIntString;
    }
}
