package no.hvl.dat250.jpa.assignment2;

import javax.persistence.*;

import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

@Entity
@Getter
@Setter
public class UserProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private static final AtomicLong atomicLong = new AtomicLong(0);
    private final Long id;
    private String login;
    private String pwd;
    
    @OneToMany(mappedBy = "owner")
    private Set<Poll> PollsOwned;
    
    @ManyToMany(mappedBy = "participants")
    private Set<Poll> PollsParticipated;

    public UserProfile(Long id){
        this.id = id;
    }

    public UserProfile(){
        this(atomicLong.incrementAndGet());
    }

    public String toJson()
    {
        Gson gson = new Gson();
        String jsonIntString = gson.toJson(this);
        return jsonIntString;
    }
}
