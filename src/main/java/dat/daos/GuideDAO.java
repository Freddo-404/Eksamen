package dat.daos;/* @auther: Frederik Dupont */

import dat.dtos.GuideDTO;
import dat.entities.Guide;
import dat.exceptions.ApiException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class GuideDAO implements IDAO<GuideDTO> {
    private static GuideDAO instance;

    private static EntityManagerFactory emf;
    public GuideDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }
    public static GuideDAO getInstance(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new GuideDAO(emf);
        }
        return instance;
    }
    @Override
    public GuideDTO read(int id)throws ApiException {
        try
        (EntityManager em = emf.createEntityManager()){
            Guide guide = em.find(Guide.class, id);
            return new GuideDTO(guide);
        }
    }

    @Override
    public List<GuideDTO> readAll()throws ApiException {
        try(EntityManager em = emf.createEntityManager()){
            List<GuideDTO> guides = em.createQuery("SELECT g FROM Guide g", GuideDTO.class).getResultList();
            return guides;
        }
    }

    @Override
    public GuideDTO create(GuideDTO guideDTO)throws ApiException {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            // Convert GuideDTO to Guide entity
            Guide guideEntity = new Guide();
            guideEntity.setFirstName(guideDTO.getFirstName());
            guideEntity.setLastName(guideDTO.getLastName());
            guideEntity.setEmail(guideDTO.getEmail());
            guideEntity.setPhonenumber(guideDTO.getPhonenumber());
            guideEntity.setYearsOfExperience(guideDTO.getYearsOfExperience());

            // Persist the Guide entity
            em.persist(guideEntity);

            em.getTransaction().commit();

            // Return the newly created Guide as a GuideDTO
            return new GuideDTO(guideEntity);
        }
    }

    @Override
    public GuideDTO update(int id, GuideDTO guideDTO) throws ApiException {
        try(EntityManager em = emf.createEntityManager()){
            em.getTransaction().begin();
            Guide guideToUpdate = em.find(Guide.class, id);
            if (guideToUpdate == null) {
                throw new ApiException(404, "Guide with that id is not found");
            }
            guideToUpdate.setFirstName(guideDTO.getFirstName());
            guideToUpdate.setLastName(guideDTO.getLastName());
            guideToUpdate.setEmail(guideDTO.getEmail());
            guideToUpdate.setPhonenumber(guideDTO.getPhonenumber());
            guideToUpdate.setYearsOfExperience(guideDTO.getYearsOfExperience());
            em.getTransaction().commit();

            return new GuideDTO(guideToUpdate);

            }
        }



    @Override
    public void delete(int id)throws ApiException {
        try(EntityManager em = emf.createEntityManager()){
            em.getTransaction().begin();
            Guide guideToDelete = em.find(Guide.class, id);
            em.remove(guideToDelete);
            em.getTransaction().commit();
        } catch (Exception e) {
            throw new ApiException(404, "Guide with that id is not found");
        }

    }
    public Boolean validatePrimaryKey(Integer integer) {
        try(EntityManager em = emf.createEntityManager()){
            Guide guide = em.find(Guide.class, integer);
            return guide != null;
        }
    }

}
