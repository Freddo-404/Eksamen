package dat.dtos;/* @auther: Frederik Dupont */

import dat.entities.Guide;
import dat.entities.Trip;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class GuideDTO {
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String phonenumber;
    private int yearsOfExperience;
    private List<Trip> trips; // List of trips associated with the guide

    // Constructor for creating a GuideDTO with all fields
    public GuideDTO(int id, String firstName, String lastName, String email, String phonenumber, int yearsOfExperience, List<Trip> trips) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phonenumber = phonenumber;
        this.yearsOfExperience = yearsOfExperience;
        this.trips = trips;
    }

    // Constructor to create a GuideDTO from a Guide entity
    public GuideDTO(Guide guide) {
        this.id = guide.getId();
        this.firstName = guide.getFirstName();
        this.lastName = guide.getLastName();
        this.email = guide.getEmail();
        this.phonenumber = guide.getPhonenumber();
        this.yearsOfExperience = guide.getYearsOfExperience();
        this.trips = guide.getTrips(); // Make sure that trips are fetched correctly from the Guide entity
    }

    public GuideDTO(int id, String firstName, String lastName, String email, String phonenumber, int yearsOfExperience) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phonenumber = phonenumber;
        this.yearsOfExperience = yearsOfExperience;
    }
}
