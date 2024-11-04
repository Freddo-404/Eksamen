package dat.daos; /* @author: Frederik Dupont */

import dat.dtos.GuideDTO;
import dat.dtos.TripDTO;
import dat.entities.Category;
import dat.entities.Guide;
import dat.entities.Trip;
import dat.exceptions.ApiException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;

import java.util.*;
import java.util.stream.Collectors;

public class TripDAO implements IDAO<TripDTO>, ITripGuideDAO {

    private static TripDAO instance;
    private static EntityManagerFactory emf;

    public TripDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public static TripDAO getInstance(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new TripDAO(emf);
        }
        return instance;
    }

    @Override
    public TripDTO read(int id) throws ApiException {
        try (EntityManager em = emf.createEntityManager()) {
            Trip trip = em.find(Trip.class, id);
            if (trip == null) {
                throw new ApiException(404, "Trip not found");
            }
            Guide guide = trip.getGuide(); // Assuming the Trip entity has a getGuide() method
            GuideDTO guideDTO = null;
            if (guide != null) {
                guideDTO = new GuideDTO(guide.getId(), guide.getFirstName(), guide.getLastName(), guide.getEmail(), guide.getPhonenumber(), guide.getYearsOfExperience());
            }

            // Create TripDTO with guide details
            TripDTO tripDTO = new TripDTO();
            tripDTO.setId(trip.getId());
            tripDTO.setName(trip.getName());
            tripDTO.setStartTime(trip.getStartTime());
            tripDTO.setEndTime(trip.getEndTime());
            tripDTO.setStartPosition(trip.getStartPosition());
            tripDTO.setPrice(trip.getPrice());
            tripDTO.setCategory(trip.getCategory());
            tripDTO.setGuide(guideDTO); // Set guide details

            return tripDTO;
        }
    }


    @Override
    public List<TripDTO> readAll() throws ApiException {
        try (EntityManager em = emf.createEntityManager()) {
            List<Trip> trips = em.createQuery("SELECT t FROM Trip t", Trip.class).getResultList();
            return trips.stream()
                    .map(trip -> {
                        Guide guide = trip.getGuide(); // Get associated guide
                        GuideDTO guideDTO = null;
                        if (guide != null) {
                            guideDTO = new GuideDTO(guide.getId(), guide.getFirstName(), guide.getLastName(), guide.getEmail(), guide.getPhonenumber(), guide.getYearsOfExperience());
                        }
                        // Create and return TripDTO with guide information
                        TripDTO tripDTO = new TripDTO();
                        tripDTO.setId(trip.getId());
                        tripDTO.setName(trip.getName());
                        tripDTO.setStartTime(trip.getStartTime());
                        tripDTO.setEndTime(trip.getEndTime());
                        tripDTO.setStartPosition(trip.getStartPosition());
                        tripDTO.setPrice(trip.getPrice());
                        tripDTO.setCategory(trip.getCategory());
                        tripDTO.setGuide(guideDTO); // Set guide details
                        return tripDTO;
                    })
                    .collect(Collectors.toList());
        }
    }


    @Override
    public TripDTO create(TripDTO tripDTO) throws ApiException {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            // Create a new Trip entity from TripDTO
            Trip trip = new Trip();
            trip.setName(tripDTO.getName());
            trip.setStartTime(tripDTO.getStartTime());
            trip.setEndTime(tripDTO.getEndTime()); // Map endTime here
            trip.setStartPosition(tripDTO.getStartPosition());
            trip.setPrice(tripDTO.getPrice());
            trip.setCategory(tripDTO.getCategory());

            if (tripDTO.getGuideId() != null) {
                Guide guide = em.find(Guide.class, tripDTO.getGuideId());
                trip.setGuide(guide); // Set the guide for the trip
            }

            em.persist(trip);
            em.getTransaction().commit();
            return new TripDTO(trip); // Return the newly created TripDTO
        }
    }


    @Override
    public TripDTO update(int id, TripDTO tripDTO) throws ApiException {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Trip tripToUpdate = em.find(Trip.class, id);
            if (tripToUpdate != null) {
                tripToUpdate.setName(tripDTO.getName());
                tripToUpdate.setCategory(tripDTO.getCategory());
                tripToUpdate.setStartPosition(tripDTO.getStartPosition());
                tripToUpdate.setStartTime(tripDTO.getStartTime());
                tripToUpdate.setPrice(tripDTO.getPrice());

                // Update guide if it exists
                if (tripDTO.getGuideId() != null) {
                    Guide guide = em.find(Guide.class, tripDTO.getGuideId());
                    tripToUpdate.setGuide(guide); // Set the guide for the trip
                } else {
                    tripToUpdate.setGuide(null); // Remove guide assignment if no guideId provided
                }
            }
            em.getTransaction().commit();
            return new TripDTO(tripToUpdate); // Return updated TripDTO
        }
    }

    @Override
    public void delete(int id) throws ApiException {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Trip tripToDelete = em.find(Trip.class, id);
            if (tripToDelete != null) {
                em.remove(tripToDelete);
            }
            em.getTransaction().commit();
        }
    }

    @Override
    public void addGuideToTrip(int tripId, int guideId) throws ApiException {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Trip trip = em.find(Trip.class, tripId);
            Guide guide = em.find(Guide.class, guideId);
            if (trip != null && guide != null) {
                trip.setGuide(guide); // Set the guide for the trip
            }
            em.getTransaction().commit();
        }
    }

    @Override
    public Set<TripDTO> getTripsByGuide(int guideId) throws ApiException {
        try (EntityManager em = emf.createEntityManager()) {
            Guide guide = em.find(Guide.class, guideId);
            if (guide == null) {
                return Set.of(); // Return an empty set if the guide is not found
            }
            // Convert List<Trip> to Set<TripDTO>
            return guide.getTrips().stream()
                    .map(TripDTO::new) // Convert each Trip to TripDTO
                    .collect(Collectors.toSet()); // Collect into a Set
        }
    }

    public Boolean validatePrimaryKey(Integer integer) {
        try (EntityManager em = emf.createEntityManager()) {
            Trip trip = em.find(Trip.class, integer);
            return trip != null;
        }
    }

    public List<TripDTO> getTripsByCategory(String category) throws ApiException {
        try (EntityManager em = emf.createEntityManager()) {
            // Fetch trips by valid category directly from the database
            List<Trip> trips = em.createQuery("SELECT t FROM Trip t WHERE LOWER(t.category) = LOWER(:category)", Trip.class)
                    .setParameter("category", category)
                    .getResultList();

            if (trips.isEmpty()) {
                throw new ApiException(400, "No trips found for the specified category.");
            }

            return trips.stream().map(TripDTO::new).collect(Collectors.toList());
        } catch (Exception e) {
            throw new ApiException(500, "An error occurred while retrieving trips by category.");
        }
    }




    public double getTotalPriceByGuide(int guideId) throws ApiException {
        try (EntityManager em = emf.createEntityManager()) {
            Double totalPrice = em.createQuery(
                            "SELECT SUM(t.price) FROM Trip t WHERE t.guide.id = :guideId", Double.class)
                    .setParameter("guideId", guideId)
                    .getSingleResult();

            return totalPrice != null ? totalPrice : 0.0; // Return 0 if no trips found
        } catch (NoResultException e) {
            return 0.0; // If no trips, return 0
        }
    }


}
