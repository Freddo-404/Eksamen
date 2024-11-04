package dat.config; /* @author: Frederik Dupont */

import dat.daos.GuideDAO;
import dat.daos.TripDAO;
import dat.dtos.GuideDTO;
import dat.dtos.TripDTO;
import dat.entities.Category;
import jakarta.persistence.EntityManagerFactory;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Populate {
    private final TripDAO tripDAO;
    private final GuideDAO guideDAO;

    public Populate(EntityManagerFactory emf) {
        this.tripDAO = TripDAO.getInstance(emf);
        this.guideDAO = GuideDAO.getInstance(emf);
    }

    public void populateDatabase() {
        try {
            populateGuides();
            populateTrips();
        } catch (Exception e) {
            System.err.println("Error during population: " + e.getMessage());
        }
    }

    private void populateGuides() {
        try {
            // Create sample guides
            List<GuideDTO> guides = new ArrayList<>();
            guides.add(new GuideDTO(0, "John", "Doe", "john@example.com", "1234567890", 5, null));
            guides.add(new GuideDTO(0, "Jane", "Smith", "jane@example.com", "0987654321", 3, null));
            guides.add(new GuideDTO(0, "Alice", "Johnson", "alice@example.com", "1112223333", 8, null));

            // Persist guides
            for (GuideDTO guideDTO : guides) {
                guideDAO.create(guideDTO);
            }

            System.out.println("Sample guides added to the database.");
        } catch (Exception e) {
            System.err.println("Error populating guides: " + e.getMessage());
        }
    }

    private void populateTrips() {
        try {
            // Retrieve the guides to associate trips with
            GuideDTO guide1 = guideDAO.read(1); // Assuming the first guide is John Doe
            GuideDTO guide2 = guideDAO.read(2); // Assuming the second guide is Jane Smith

            // Create sample trips
            TripDTO trip1 = new TripDTO();
            trip1.setName("City Tour");
            trip1.setStartTime(LocalTime.of(9, 0));
            trip1.setEndTime(LocalTime.of(12, 0));
            trip1.setStartPosition("Main Square");
            trip1.setPrice(29.99);
            trip1.setCategory(Category.city);
            trip1.setGuideId(guide1.getId()); // Associate the trip with John Doe

            TripDTO trip2 = new TripDTO();
            trip2.setName("Mountain Adventure");
            trip2.setStartTime(LocalTime.of(8, 0));
            trip2.setEndTime(LocalTime.of(16, 0));
            trip2.setStartPosition("Base Camp");
            trip2.setPrice(49.99);
            trip2.setCategory(Category.forest);
            trip2.setGuideId(guide2.getId()); // Associate the trip with Jane Smith

            // Persist trips
            tripDAO.create(trip1);
            tripDAO.create(trip2);

            System.out.println("Sample trips added to the database.");
        } catch (Exception e) {
            System.err.println("Error populating trips: " + e.getMessage());
        }
    }
}
