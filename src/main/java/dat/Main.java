package dat;/* @auther: Frederik Dupont */

import dat.config.ApplicationConfig;
import dat.config.HibernateConfig;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

public class Main {
    public static void main(String[] args) {
        ApplicationConfig.startServer(7070);
    }

}
