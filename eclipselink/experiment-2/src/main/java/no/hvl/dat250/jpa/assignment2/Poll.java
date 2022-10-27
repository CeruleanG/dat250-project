package no.hvl.dat250.jpa.assignment2;

import javax.persistence.*;

import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
public class Poll implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String topic;
    private int status;
    private boolean isPublic;
    private Long timer;

    private Set<String> options = new HashSet<>();

    @OneToMany(mappedBy = "fromPoll")
    private Set<Ticket> tickets = new HashSet<>();
    
    @ManyToOne(cascade = CascadeType.PERSIST)
    private UserProfile owner;
    
    @ManyToMany(cascade = CascadeType.PERSIST)
    private Set<UserProfile> participants = new HashSet<>();

    public Poll(Long id){
        this.id = id;
    }

    public Poll(){}

    public String toJson()
    {
        Gson gson = new Gson();
        String jsonIntString = gson.toJson(this);
        return jsonIntString;
    }

    public void addTicket(Ticket ticket){
        this.tickets.add(ticket);
    }
}
