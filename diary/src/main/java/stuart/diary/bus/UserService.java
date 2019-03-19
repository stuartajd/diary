package stuart.diary.bus;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import stuart.diary.ents.User;
import stuart.diary.pers.UserFacade;

/**
 *
 * @author stuart
 */
@Stateless
public class UserService {
    @EJB
    private UserFacade uf;
    
    
    public boolean validateUser(User loginUser){
        
        return true;
    } 
    
    
    public boolean registerUser(User registerUser){
        uf.create(registerUser);
        return true;
    }
}
