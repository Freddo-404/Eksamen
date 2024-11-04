package dat.controllers;

import dat.config.ApplicationConfig;
import dat.config.HibernateConfig;
import dat.daos.GuideDAO;
import dat.daos.TripDAO;
import dat.dtos.GuideDTO;
import dat.dtos.TripDTO;
import dat.entities.Category;
import dat.exceptions.ApiException;
import dat.security.controllers.SecurityController;
import dat.security.daos.SecurityDAO;
import dat.security.exceptions.ValidationException;
import dk.bugelhartmann.UserDTO;
import io.javalin.Javalin;
import io.restassured.common.mapper.TypeRef;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;

import java.time.LocalTime;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TripControllerTest {
    private static Javalin app;
    private static final EntityManagerFactory emf = HibernateConfig.getEntityManagerFactoryForTest();
    private final static SecurityController securityController = SecurityController.getInstance();
    private final SecurityDAO securityDAO = new SecurityDAO(emf);
    private static TripDAO tripDAO;
    private static GuideDAO guideDAO;
    private static GuideDTO g1, g2;
    private static TripDTO t1, t2;
    private static UserDTO userDTO, adminDTO;
    private static String userToken, adminToken;
    private static final String BASE_URL = "http://localhost:7070/api";

    @BeforeAll
    void setUpAll() {
        HibernateConfig.setTest(true);
        app = ApplicationConfig.startServer(7070);
        guideDAO = GuideDAO.getInstance(emf);
        tripDAO = TripDAO.getInstance(emf);
    }

    @BeforeEach
    void setup() throws ApiException {
        System.out.println("Populating database");
        // Use the Populator to populate guides and trips
        Populator.populateGuidesAndTrips(emf); // Fixed call

        // Retrieve guides created during population
        g1 = guideDAO.read(1); // Assuming guides were added with IDs 1 and 2
        g2 = guideDAO.read(2);

        // Retrieve trips created during population
        t1 = tripDAO.read(1); // Assuming trips were added with IDs 1 and 2
        t2 = tripDAO.read(2);

        // User setup
        UserDTO[] userDTOs = Populator.populateUsers(emf);
        userDTO = userDTOs[0];  // Assuming userDTOs has at least two users
        adminDTO = userDTOs[1];

        try {
            UserDTO verifiedUser = securityDAO.getVerifiedUser(userDTO.getUsername(), userDTO.getPassword());
            UserDTO verifiedAdmin = securityDAO.getVerifiedUser(adminDTO.getUsername(), adminDTO.getPassword());
            userToken = "Bearer " + securityController.createToken(verifiedUser);
            adminToken = "Bearer " + securityController.createToken(verifiedAdmin);
        } catch (ValidationException e) {
            throw new RuntimeException(e);
        }
    }

    @AfterEach
    void tearDown() {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.createQuery("DELETE FROM Trip").executeUpdate();
            em.createQuery("DELETE FROM Guide").executeUpdate();
            em.createQuery("DELETE FROM User").executeUpdate();
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @AfterAll
    void tearDownAll() {
        ApplicationConfig.stopServer(app);
    }

    @Test
    void readAllTrips() {
        List<TripDTO> trips = given()
                .header("Authorization", userToken)
                .when()
                .get(BASE_URL + "/trips")
                .then()
                .statusCode(200)
                .body("size()", is(2)) // Expecting 2 trips from population
                .log().all()
                .extract()
                .as(new TypeRef<List<TripDTO>>() {});

        assertThat(trips.size(), is(2));
    }

    @Test
    void readTrip() {
        TripDTO trip = given()
                .header("Authorization", userToken)
                .when()
                .get(BASE_URL + "/trips/" + t1.getId())
                .then()
                .statusCode(200)
                .log().all()
                .extract()
                .as(TripDTO.class);

        assertThat(trip.getName(), is(t1.getName()));
    }

    @Test
    void createTrip() {
        TripDTO newTrip = new TripDTO();
        newTrip.setName("Beach Getaway");
        newTrip.setStartTime(LocalTime.of(10, 0));
        newTrip.setEndTime(LocalTime.of(14, 0));
        newTrip.setStartPosition("Beach Entrance");
        newTrip.setPrice(39.99);
        newTrip.setCategory(Category.beach); // Ensure Category.BEACH exists in your enum
        newTrip.setGuideId(g1.getId());

        TripDTO createdTrip = given()
                .header("Authorization", adminToken)
                .contentType("application/json")
                .body(newTrip)
                .when()
                .post(BASE_URL + "/trips")
                .then()
                .statusCode(201)
                .log().all()
                .extract()
                .as(TripDTO.class);

        assertThat(createdTrip.getId(), is(notNullValue()));
        assertThat(createdTrip.getName(), is(newTrip.getName()));
    }

    @Test
    void updateTrip() {
        // Assuming t1 is created and persisted correctly before this test runs
        TripDTO updatedTrip = new TripDTO();
        updatedTrip.setId(t1.getId()); // Make sure t1 is valid and exists in the DB
        updatedTrip.setName("City Tour 2.0");
        updatedTrip.setStartTime(LocalTime.of(9, 30));
        updatedTrip.setEndTime(LocalTime.of(12, 30));
        updatedTrip.setStartPosition("Main Square 2");
        updatedTrip.setPrice(34.99);
        updatedTrip.setCategory(Category.city);
        updatedTrip.setGuideId(g2.getId());

        TripDTO updated = given()
                .header("Authorization", adminToken)
                .contentType("application/json")
                .body(updatedTrip)
                .when()
                .put(BASE_URL + "/trips/" + updatedTrip.getId()) // Ensure this matches the route
                .then()
                .statusCode(200)
                .log().all()
                .extract()
                .as(TripDTO.class);

        assertThat(updated.getName(), is(updatedTrip.getName()));
    }



    @Test
    void deleteTrip() {
        given()
                .header("Authorization", adminToken)
                .when()
                .delete(BASE_URL + "/trips/" + t1.getId())
                .then()
                .statusCode(204);

        // Verify the trip is deleted
        given()
                .header("Authorization", userToken)
                .when()
                .get(BASE_URL + "/trips/" + t1.getId())
                .then()
                .statusCode(404); // Expecting not found after deletion
    }



    @Test
    void getTripsByCategory(){
        List<TripDTO> trips = given()
                .header("Authorization", userToken)
                .when()
                .get(BASE_URL + "/trips/category/" + Category.city)
                .then()
                .statusCode(200)
                .log().all()
                .extract()
                .as(new TypeRef<List<TripDTO>>() {});

        assertThat(trips.size(), is(greaterThan(0))); // Expecting at least one trip
    }
    @Test
    void getTotalPriceByGuide() {

    }

}
