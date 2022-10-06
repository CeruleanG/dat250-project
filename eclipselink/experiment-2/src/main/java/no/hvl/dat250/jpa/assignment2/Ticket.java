package no.hvl.dat250.jpa.assignment2;

import javax.persistence.*;

import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicLong;


@Entity
@Getter
@Setter
public class Ticket implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private final Long id;
    private static final AtomicLong atomicLong = new AtomicLong(0);
    private int voteNb;

    private UserProfile voter;
    
    @ManyToOne
    private Poll fromPoll;

    public Ticket(Long id){
        this.id = id;
    }

    public Ticket(){
        this(atomicLong.incrementAndGet());
    }

    public String toJson()
    {
        Gson gson = new Gson();
        String jsonIntString = gson.toJson(this);
        return jsonIntString;
    }
}
