package no.hvl.dat250.jpa.assignment2;

import javax.persistence.*;

import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

@Entity
@Getter
@Setter
public class UserProfile implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String login;
    private String pwd;
    
    @OneToMany(mappedBy = "owner")
    private Set<Poll> PollsOwned = new HashSet<>();
    
    @ManyToMany(mappedBy = "participants")
    private Set<Poll> PollsParticipated = new HashSet<>();

    public UserProfile(Long id){
        this.id = id;
    }

    public UserProfile(){
    }

    public String toJson()
    {
        Gson gson = new Gson();
        String jsonIntString = gson.toJson(this);
        return jsonIntString;
    }
}
