package dat.entities; /* @author: Frederik Dupont */

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;
import java.util.List;

@Entity
@NoArgsConstructor
@Setter
@Getter
public class Guide {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String phonenumber;
    private int yearsOfExperience;

    @OneToMany(mappedBy = "guide")
    private List<Trip> trips;

    // Constructor that accepts all fields except ID
    public Guide(String firstName, String lastName, String email, String phonenumber, int yearsOfExperience) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phonenumber = phonenumber;
        this.yearsOfExperience = yearsOfExperience;
    }



}
