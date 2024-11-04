package dat.dtos;

import dat.entities.Category;
import dat.entities.Guide;
import dat.entities.Trip;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@NoArgsConstructor
public class TripDTO {
    private int id;
    private String name;
    private LocalTime startTime;
    private LocalTime endTime;
    private String startPosition;
    private double price;
    private Category category;
    private GuideDTO guide; // Holds guide details
    private Integer guideId; // Added to keep track of the guide ID

    // Constructor to create TripDTO from Trip entity
    public TripDTO(Trip trip) {
        this.id = trip.getId();
        this.name = trip.getName();
        this.startTime = trip.getStartTime();
        this.endTime = trip.getEndTime();
        this.startPosition = trip.getStartPosition();
        this.price = trip.getPrice();
        this.category = trip.getCategory();

        Guide guideEntity = trip.getGuide(); // Assuming Trip has a getGuide() method
        if (guideEntity != null) {
            this.guide = new GuideDTO(guideEntity.getId(), guideEntity.getFirstName(), guideEntity.getLastName(),
                    guideEntity.getEmail(), guideEntity.getPhonenumber(),
                    guideEntity.getYearsOfExperience());
            this.guideId = guideEntity.getId(); // Set guideId for easy access
        }
    }

    // All-arguments constructor
    public TripDTO(int id, LocalTime startTime, LocalTime endTime, String startPosition,
                   String name, double price, Category category, Integer guideId) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.startPosition = startPosition;
        this.name = name;
        this.price = price;
        this.category = category;
        this.guideId = guideId;
    }
}
