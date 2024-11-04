package dat.routes;/* @auther: Frederik Dupont */

import dat.controllers.TripController;
import dat.security.enums.Role;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;

public class TripRoutes {
    private final TripController tripController = new TripController();
    protected final EndpointGroup getRoutes() {
        return () -> {
            get("/", tripController::readAll, Role.USER);
            get("/{id}", tripController::read, Role.USER);
            post("/", tripController::create, Role.ADMIN);
            put("/{id}", tripController::update, Role.ADMIN);
            delete("/{id}", tripController::delete, Role.ADMIN);
            put("/{id}/addGuide/{guideId}", tripController::addGuide, Role.ADMIN);
            post("/populate", tripController::populate, Role.ADMIN);
            get("/category/{category}", tripController::getTripsByCategory, Role.USER);
            get("/guides/totalprice/{id}", tripController::getTotalPriceByGuide, Role.USER);

        };
    }

}
