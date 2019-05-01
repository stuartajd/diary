package stuart.diary.bus;

import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
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

    public boolean validateUser(User loginUser) {
        boolean valid = true;
        
        User resp = uf.lookupUserByUsername(loginUser.getUsername());
        
        if(resp == null){
            FacesContext.getCurrentInstance().addMessage("username", new FacesMessage("Error: A user does not exist with that username"));
            valid = false;
        } else {
            if(!loginUser.getPassword().equalsIgnoreCase(resp.getPassword())){
                // Passwords don't match
                FacesContext.getCurrentInstance().addMessage("password", new FacesMessage("Error: The login credentials are incorrect."));
                valid = false;
            }
        }

        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("user", loginUser);
        return valid;
    }

    public List<User> searchUser(){
        return uf.findAll();
    }

    public boolean registerUser(User registerUser) {
        boolean valid = true;

        // Check if the fields are empty, if they are, invalid
        if(registerUser.getUsername().length() == 0 || registerUser.getAddress().length() == 0 
                || registerUser.getEmailAddress().length() == 0 || registerUser.getPhoneNumber().length() == 0
                || registerUser.getPostCode().length() == 0 || registerUser.getPassword().length() == 0 
                || registerUser.getFirstName().length() == 0 || registerUser.getLastName().length() == 0){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Error: Please complete all the fields to register your account."));
            return false;
        }
       
        
        // Email validation - TODO: Email regex
//        if (!registerUser.getEmailAddress().matches("")) {
//            FacesContext.getCurrentInstance().addMessage("emailAddress", new FacesMessage("Error: Email format is invalid."));
//            valid = false;
//        }

        // Phone validation - TODO: Phone regex
//        if (!registerUser.getPhoneNumber().matches("")) {
//            FacesContext.getCurrentInstance().addMessage("phoneNumber", new FacesMessage("Error: Phone Number format is invalid"));
//            valid = false;
//        }
        
        // Check if username exists
        if(uf.lookupUserByUsername(registerUser.getUsername()) != null){
            FacesContext.getCurrentInstance().addMessage("username", new FacesMessage("Error: A user account already exists with this username."));
            valid = false;
        }
        
        // Check if email exists
        if(uf.lookupUserByEmail(registerUser.getEmailAddress()) != null){
            FacesContext.getCurrentInstance().addMessage("emailAddress", new FacesMessage("Error: A user account already exists with this email address."));
            valid = false;
        }

        if (valid) {
            uf.create(registerUser);
        }

        return valid;
    }
}
