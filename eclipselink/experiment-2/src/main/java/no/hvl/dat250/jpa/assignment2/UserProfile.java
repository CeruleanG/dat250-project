package no.hvl.dat250.jpa.assignment2;

import com.fasterxml.jackson.annotation.*;
import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Getter
@Setter
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class UserProfile implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String login;
    private String pwd;
    
    @OneToMany(mappedBy = "owner")
    @JsonIncludeProperties({"id"})
    private List<Poll> PollsOwned ;
    
    @ManyToMany(mappedBy = "participants")
    @JsonIncludeProperties({"id"})
    private List<Poll> PollsParticipated ;



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
