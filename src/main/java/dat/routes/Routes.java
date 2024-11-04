package dat.routes;/* @auther: Frederik Dupont */
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.path;

public class Routes {
private final TripRoutes tripRoutes = new TripRoutes();   //private final RoomRoute roomRoute = new RoomRoute();

    public EndpointGroup getRoutes() {
        return () -> {
               path("/trips", tripRoutes.getRoutes());
        };
    }

}
