package no.hvl.dat250.jpa.assignment2;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.*;
import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Entity
@Getter
@Setter
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Poll implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String topic;
    private int yesNb;
    private int noNb;
    private int status; // -1 = closed; 0 = not open; 1 = open
    private boolean isPublic;
    
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JsonIncludeProperties({"id"})
    private UserProfile owner;
    
    @ManyToMany(cascade = CascadeType.PERSIST)
    @JsonIncludeProperties({"id"})
    private List<UserProfile> participants;

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

}
