package no.hvl.dat250.jpa.assignment2;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

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
    @JsonManagedReference
    //@JsonBackReference
    private Set<Poll> PollsOwned = new HashSet<>();
    
    @ManyToMany(mappedBy = "participants")
    @JsonManagedReference
    //@JsonBackReference
    private Set<Poll> PollsParticipated = new HashSet<>();



    public UserProfile(Long id){
        this.id = id;
    }
    public UserProfile(){}

    public String toJson()
    {
        Gson gson = new Gson();
        String jsonIntString = gson.toJson(this);
        return jsonIntString;
    }
}
