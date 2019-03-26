package stuart.diary.pers;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import stuart.diary.ents.User;

/**
 *
 * @author stuart
 */
@Stateless
public class UserFacade extends AbstractFacade<User> {

    @PersistenceContext(unitName = "persistanceDiary")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    /**
     * Look up a user based off their login username
     * @param username
     */
    public User lookupUserByUsername(String username){
        try {
            TypedQuery<User> query = em.createQuery(
                "SELECT u FROM User u WHERE u.username = :username", User.class);
            return query.setParameter("username", username).getSingleResult();
        } catch (NoResultException e){
            return null;
        }
    }
    
    /**
     * Look up a user based off their login email
     * @param username
     */
    public User lookupUserByEmail(String email){
        try {
            TypedQuery<User> query = em.createQuery(
                "SELECT u FROM User u WHERE u.emailAddress = :email", User.class);
            return query.setParameter("email", email).getSingleResult();
        } catch (NoResultException e){
            return null;
        }
    }
   
    public UserFacade() {
        super(User.class);
    }
    
}
