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

    public void invalidateUserSession(){
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("user");
    }
    
    public boolean validateUser(User loginUser) {
        boolean valid = true;
        
        if(loginUser.getUsername().length() == 0){
            FacesContext.getCurrentInstance().addMessage("loginForm:username", new FacesMessage("Error: You must enter a value to the username field."));
            return false;
        }
        
        User resp = uf.lookupUserByUsername(loginUser.getUsername());
        
        if(resp == null){
            FacesContext.getCurrentInstance().addMessage("loginForm:username", new FacesMessage("Error: A user does not exist with that username"));
            valid = false;
        } else {
            if(!loginUser.getPassword().equalsIgnoreCase(resp.getPassword())){
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Error: The login credentials are incorrect."));
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
       
        
        // Email validation
        if (!registerUser.getEmailAddress().matches("^[a-zA-Z0-9_.]+@[a-zA-Z.]+?\\.[a-zA-Z]{2,3}$")) {
            FacesContext.getCurrentInstance().addMessage("registerForm:emailAddress", new FacesMessage("Error: Email format is invalid."));
            valid = false;
        }

        // Phone validation
        if (!registerUser.getPhoneNumber().matches("((\\+[0-9]{2})\\s*(\\([0-9]{2}\\))\\s*([0-9]{8}))?|((\\([0-9]{3}\\))\\s*([0-9]{8}))?")) {
            FacesContext.getCurrentInstance().addMessage("registerForm:phoneNumber", new FacesMessage("Error: Phone Number format is invalid"));
            valid = false;
        }
        
        // Postcode validation
        if (!registerUser.getPostCode().matches("^[A-Z]{1,2}[0-9R][0-9A-Z]? [0-9][ABD-HJLNP-UW-Z]{2}$")) {
            FacesContext.getCurrentInstance().addMessage("registerForm:postcode", new FacesMessage("Error: Postcode format is invalid (EG: SW1A 1AA)"));
            valid = false;
        }
        
        // Check if username exists
        if(uf.lookupUserByUsername(registerUser.getUsername()) != null){
            FacesContext.getCurrentInstance().addMessage("registerForm:username", new FacesMessage("Error: A user account already exists with this username."));
            valid = false;
        }
        
        // Check if email exists
        if(uf.lookupUserByEmail(registerUser.getEmailAddress()) != null){
            FacesContext.getCurrentInstance().addMessage("registerForm:emailAddress", new FacesMessage("Error: A user account already exists with this email address."));
            valid = false;
        }

        if (valid) {
            uf.create(registerUser);
        }

        return valid;
    }
    
    public boolean updateUser(User updateUser) {
        boolean valid = true;

        // Check if the fields are empty, if they are, invalid
        if(updateUser.getUsername().length() == 0 || updateUser.getAddress().length() == 0 
                || updateUser.getEmailAddress().length() == 0 || updateUser.getPhoneNumber().length() == 0
                || updateUser.getPostCode().length() == 0 || updateUser.getPassword().length() == 0 
                || updateUser.getFirstName().length() == 0 || updateUser.getLastName().length() == 0){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Error: Please complete all the fields to update your account."));
            return false;
        }
        
        // Email validation
        if (!updateUser.getEmailAddress().matches("^[a-zA-Z0-9_.]+@[a-zA-Z.]+?\\.[a-zA-Z]{2,3}$")) {
            FacesContext.getCurrentInstance().addMessage("updateForm:emailAddress", new FacesMessage("Error: Email format is invalid."));
            valid = false;
        }

        // Phone validation
        if (!updateUser.getPhoneNumber().matches("((\\+[0-9]{2})\\s*(\\([0-9]{2}\\))\\s*([0-9]{8}))?|((\\([0-9]{3}\\))\\s*([0-9]{8}))?")) {
            FacesContext.getCurrentInstance().addMessage("updateForm:phoneNumber", new FacesMessage("Error: Phone Number format is invalid"));
            valid = false;
        }
        
        // Postcode validation
        if (!updateUser.getPostCode().matches("^[A-Z]{1,2}[0-9R][0-9A-Z]? [0-9][ABD-HJLNP-UW-Z]{2}$")) {
            FacesContext.getCurrentInstance().addMessage("updateForm:postcode", new FacesMessage("Error: Postcode format is invalid (EG: SW1A 1AA)"));
            valid = false;
        }
        
        User currentUser = (User) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user");

        if(!currentUser.getUsername().equals(updateUser.getUsername())){
            // Username was changed, check if it already exists
            if(uf.lookupUserByUsername(updateUser.getUsername()) != null){
                FacesContext.getCurrentInstance().addMessage("updateForm:username", new FacesMessage("Error: A user account already exists with this username."));
                valid = false;
            }
        }
        
        if(!currentUser.getEmailAddress().equals(updateUser.getEmailAddress())){
             // Check if email exists
            if(uf.lookupUserByEmail(updateUser.getEmailAddress()) != null){
                FacesContext.getCurrentInstance().addMessage("updateForm:emailAddress", new FacesMessage("Error: A user account already exists with this email address."));
                valid = false;
            }
        }
       
        if (valid) {
            uf.edit(updateUser);
        }

        return valid;
    }
}
