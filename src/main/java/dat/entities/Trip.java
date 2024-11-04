package dat.entities; /* @author: Frederik Dupont */

import dat.dtos.TripDTO;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Entity
@NoArgsConstructor
@Data
public class Trip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private LocalTime startTime;
    private LocalTime endTime;
    private String startPosition;
    private String name;
    private double price;

    @Enumerated(EnumType.STRING)
    private Category category;

    @ManyToOne
    @JoinColumn(name = "guide_id")
    private Guide guide;

    // Constructor for creating a Trip from its attributes
    public Trip(String name, LocalTime startTime, LocalTime endTime, String startPosition, double price, Category category) {
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.startPosition = startPosition;
        this.price = price;
        this.category = category;
    }

    // Constructor to create a Trip from TripDTO
    public Trip(TripDTO trip) {
        if (trip != null) {
            this.id = trip.getId();
            this.startTime = trip.getStartTime();
            this.endTime = trip.getEndTime();
            this.startPosition = trip.getStartPosition();
            this.name = trip.getName();
            this.price = trip.getPrice();
            this.category = trip.getCategory();

            // Safely handle the guide
            if (trip.getGuide() != null) {
                this.guide = new Guide(); // Create a new Guide object only if necessary
                this.guide.setId(trip.getGuide().getId()); // Ensure to call getId() on the guide DTO
            }
        }
    }
}
