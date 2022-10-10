/**************************************************************************************************



FUCK THIS, NOT USED, PUE LA MERDE




**************************************************************************************************/
package no.hvl.dat250.jpa.assignment2;

import javax.persistence.*;

import com.google.gson.Gson;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class VotePoll {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Set<String> options = new HashSet<>();
    
    @OneToMany(mappedBy = "fromPoll")
    private Set<Ticket> tickets = new HashSet<>();

    public String toJson()
    {
        Gson gson = new Gson();
        String jsonIntString = gson.toJson(this);
        return jsonIntString;
    }
}
