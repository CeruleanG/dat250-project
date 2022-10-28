package no.hvl.dat250.jpa.assignment2;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.util.HashSet;
import java.util.Set;

public class DataJPA {
    private static final String PERSISTENCE_UNIT_NAME = "miniproject";
    private static EntityManagerFactory factory;
    EntityManager em;

    public DataJPA()
    {
        factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        em= factory.createEntityManager();
    }

    // -1=wrong class   1=success
    public int saveData(Object jpadata)
    {
        if ((jpadata instanceof Poll) || (jpadata instanceof UserProfile))
        {
            em.getTransaction().begin();
            em.persist(jpadata);
            em.getTransaction().commit();
            return 1;
        }
        else return -1;
    }

    public int deleteData(Object jpadata)
    {
        if ((jpadata instanceof Poll) || (jpadata instanceof UserProfile))
        {
            em.getTransaction().begin();
            em.remove(jpadata);
            em.getTransaction().commit();
            return 1;
        }
        else return -1;
    }

    public Set<Poll> getPolls()
    {
        Query q = em.createQuery("select t from Poll t");
        Set<Poll> polls = new HashSet<>(q.getResultList());
        return polls;
    }
}
