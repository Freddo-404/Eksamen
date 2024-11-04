package dat.controllers; /* @author: Frederik Dupont */

import dat.config.HibernateConfig;
import dat.config.Populate; // Import the Populate class
import dat.daos.TripDAO;
import dat.dtos.TripDTO;
import dat.exceptions.ApiException;
import io.javalin.http.Context;
import jakarta.persistence.EntityManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class TripController implements IController<TripDTO> {
    private static final Logger log = LoggerFactory.getLogger(TripController.class); // Corrected the logger reference
    private final TripDAO dao;
    private static EntityManagerFactory emf;

    public TripController() {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        this.dao = TripDAO.getInstance(emf);
    }

    @Override
    public void read(Context ctx) throws ApiException {
        try {
            int id = ctx.pathParamAsClass("id", Integer.class).check(this::validatePrimaryKey, "Not a valid id").get();
            TripDTO trip = dao.read(id);
            ctx.res().setStatus(200);
            ctx.json(trip, TripDTO.class);
        } catch (ApiException e) {
            log.error("API error reading trip: {}", e.getMessage());
            ctx.status(e.getStatusCode()).json(e.getMessage());
        } catch (Exception e) {
            log.error("Error reading trip: {}", e.getMessage());
            ctx.status(500).json("An unexpected error occurred while reading the trip.");
        }
    }

    @Override
    public void readAll(Context ctx) throws ApiException {
        try {
            ctx.res().setStatus(200);
            ctx.json(dao.readAll(), TripDTO.class);
        } catch (Exception e) {
            log.error("Error reading all trips: {}", e.getMessage());
            ctx.status(500).json("An unexpected error occurred while retrieving all trips.");
        }
    }

    @Override
    public void create(Context ctx) throws ApiException {
        try {
            TripDTO tripDTO = ctx.bodyAsClass(TripDTO.class);
            TripDTO trip = dao.create(tripDTO);
            ctx.res().setStatus(201);
            ctx.json(trip, TripDTO.class);
        } catch (ApiException e) {
            log.error("API error creating trip: {}", e.getMessage());
            ctx.status(e.getStatusCode()).json(e.getMessage());
        } catch (Exception e) {
            log.error("Error creating trip: {}", e.getMessage());
            ctx.status(400).json("An unexpected error occurred while creating the trip.");
        }
    }

    @Override
    public void update(Context ctx) throws ApiException {
        try {
            int id = ctx.pathParamAsClass("id", Integer.class).check(this::validatePrimaryKey, "Not a valid id").get();
            TripDTO tripDTO = ctx.bodyAsClass(TripDTO.class);
            TripDTO trip = dao.update(id, tripDTO);
            ctx.res().setStatus(200);
            ctx.json(trip, TripDTO.class);
        } catch (ApiException e) {
            log.error("API error updating trip: {}", e.getMessage());
            ctx.status(e.getStatusCode()).json(e.getMessage());
        } catch (Exception e) {
            log.error("Error updating trip: {}", e.getMessage());
            ctx.status(400).json("An unexpected error occurred while updating the trip.");
        }
    }

    @Override
    public void delete(Context ctx) throws ApiException {
        try {
            int id = ctx.pathParamAsClass("id", Integer.class).check(this::validatePrimaryKey, "Not a valid id").get();
            dao.delete(id);
            ctx.res().setStatus(204);
        } catch (ApiException e) {
            log.error("API error deleting trip: {}", e.getMessage());
            ctx.status(e.getStatusCode()).json(e.getMessage());
        } catch (Exception e) {
            log.error("Error deleting trip: {}", e.getMessage());
            ctx.status(404).json("An unexpected error occurred while deleting the trip.");
        }
    }

    public void addGuide(Context ctx) {
        int tripId = Integer.parseInt(ctx.pathParam("id"));
        int guideId = Integer.parseInt(ctx.pathParam("guideId"));

        try {
            dao.addGuideToTrip(tripId, guideId);
            ctx.status(204); // No Content
        } catch (ApiException e) {
            log.error("API error adding guide to trip: {}", e.getMessage());
            ctx.status(e.getStatusCode()).json(e.getMessage());
        } catch (Exception e) {
            log.error("Error adding guide to trip: {}", e.getMessage());
            ctx.status(500).json("An unexpected error occurred while adding the guide to the trip.");
        }
    }

    public void populate(Context ctx) {
        try {
            // Create an instance of Populate and call populateDatabase
            Populate populate = new Populate(HibernateConfig.getEntityManagerFactory());
            populate.populateDatabase(); // Assuming this method exists
            ctx.status(200).result("Database populated successfully.");
        } catch (Exception e) {
            log.error("Error populating database: {}", e.getMessage());
            ctx.status(500).result("Error populating database: " + e.getMessage());
        }
    }

    public boolean validatePrimaryKey(int id) {
        return dao.validatePrimaryKey(id);
    }
    public void getTripsByCategory(Context ctx) {
        String category = ctx.pathParam("category");
        try {
            List<TripDTO> trips = dao.getTripsByCategory(category); // Ensure this is implemented in the DAO
            ctx.status(200).json(trips);
        } catch (ApiException e) {
            ctx.status(e.getStatusCode()).json(e.getMessage());
        } catch (Exception e) {
            ctx.status(500).json("An error occurred while retrieving trips by category.");
        }
    }


    public void getTotalPriceByGuide(Context ctx) {
        int guideId = Integer.parseInt(ctx.pathParam("id"));
        try {
            double totalPrice = dao.getTotalPriceByGuide(guideId); // Ensure this is implemented in the DAO
            ctx.status(200).json(Map.of("guideId", guideId, "totalPrice", totalPrice));
        } catch (ApiException e) {
            ctx.status(e.getStatusCode()).json(e.getMessage());
        } catch (Exception e) {
            ctx.status(500).json("An error occurred while retrieving total price by guide.");
        }
    }



}
