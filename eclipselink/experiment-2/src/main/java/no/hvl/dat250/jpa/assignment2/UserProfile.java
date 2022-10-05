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
public class UserProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String login;
    private String pwd;
    
    @OneToMany(mappedBy = "owner")
    private Set<Poll> PollsOwned;
    
    @ManyToMany(mappedBy = "participants")
    private Set<Poll> PollsParticipated;

    public String toJson()
    {
        Gson gson = new Gson();
        String jsonIntString = gson.toJson(this);
        return jsonIntString;
    }
}
