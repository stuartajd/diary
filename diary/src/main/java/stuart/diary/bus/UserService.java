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
        
        if(loginUser.getUsername().length() == 0){
            FacesContext.getCurrentInstance().addMessage("loginForm:username", new FacesMessage("You must enter a value to the username field."));
            return false;
        }
        
        User resp = uf.lookupUserByUsername(loginUser.getUsername());
        
        if(resp == null){
            FacesContext.getCurrentInstance().addMessage("loginForm:username", new FacesMessage("A user does not exist with that username"));
            valid = false;
        } else {
            if(!loginUser.getPassword().equalsIgnoreCase(resp.getPassword())){
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("The login credentials are incorrect."));
                valid = false;
            }
        }

        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("user", resp);
        return valid;
    }

    public User lookupUserByUsername(String username){
       return uf.lookupUserByUsername(username);
    }
    
    public List<User> searchUsers(){
        return uf.findAll();
    }

    public boolean registerUser(User registerUser) {
        boolean valid = true;

        // Check if the fields are empty, if they are, invalid
        if(registerUser.getUsername().length() == 0 || registerUser.getAddress().length() == 0 
                || registerUser.getEmailAddress().length() == 0 || registerUser.getPhoneNumber().length() == 0
                || registerUser.getPostCode().length() == 0 || registerUser.getPassword().length() == 0 
                || registerUser.getFirstName().length() == 0 || registerUser.getLastName().length() == 0){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Please complete all the fields to register your account."));
            return false;
        }
        
        // Email validation
        if (!registerUser.getEmailAddress().matches("^[a-zA-Z0-9_.]+@[a-zA-Z.]+?\\.[a-zA-Z]{2,3}$")) {
            FacesContext.getCurrentInstance().addMessage("registerForm:emailAddress", new FacesMessage("Email format is invalid."));
            valid = false;
        }

        // Phone validation
        if (!registerUser.getPhoneNumber().matches("[0-9]{10}|[0-9]{11}|\\([0-9]{3}\\)\\s[0-9]{8}|\\+([0-9]{2})\\s\\(([0-9]{2})\\)\\s([0-9]{8})")) {
            FacesContext.getCurrentInstance().addMessage("registerForm:phoneNumber", new FacesMessage("Phone Number format is invalid (EG: +44 (00) 12345678)"));
            valid = false;
        }
        
        // Postcode validation
        if (!registerUser.getPostCode().toUpperCase().matches("^[A-Z]{1,2}[0-9R][0-9A-Z]? [0-9][ABD-HJLNP-UW-Z]{2}$")) {
            FacesContext.getCurrentInstance().addMessage("registerForm:postcode", new FacesMessage("Postcode format is invalid (EG: SW1A 1AA)"));
            valid = false;
        }
        
        // Check if username is correct length
        if(registerUser.getUsername().length() < 6){
            FacesContext.getCurrentInstance().addMessage("registerForm:username", new FacesMessage("The username must be atleast 6 characters in length."));
            valid = false;
        }
        
        // Check if username exists
        if(uf.lookupUserByUsername(registerUser.getUsername()) != null){
            FacesContext.getCurrentInstance().addMessage("registerForm:username", new FacesMessage("A user account already exists with this username."));
            valid = false;
        }
        
        // Check if email exists
        if(uf.lookupUserByEmail(registerUser.getEmailAddress()) != null){
            FacesContext.getCurrentInstance().addMessage("registerForm:emailAddress", new FacesMessage("A user account already exists with this email address."));
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
            
            String response = "Please complete all the fields to update your account.";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, response, response));
            return false;
        }
        
        // Email validation
        if (!updateUser.getEmailAddress().matches("^[a-zA-Z0-9_.]+@[a-zA-Z.]+?\\.[a-zA-Z]{2,3}$")) {
            FacesContext.getCurrentInstance().addMessage("updateForm:emailAddress", new FacesMessage("Email format is invalid."));
            valid = false;
        }

        // Phone validation
        if (!updateUser.getPhoneNumber().matches("[0-9]{10}|[0-9]{11}|\\([0-9]{3}\\)\\s[0-9]{8}|\\+([0-9]{2})\\s\\(([0-9]{2})\\)\\s([0-9]{8})")) {
            FacesContext.getCurrentInstance().addMessage("updateForm:phoneNumber", new FacesMessage("Phone Number format is invalid (EG: +44 (00) 12345678)"));
            valid = false;
        }
        
        // Postcode validation
        if (!updateUser.getPostCode().toUpperCase().matches("^[A-Z]{1,2}[0-9R][0-9A-Z]? [0-9][ABD-HJLNP-UW-Z]{2}$")) {
            FacesContext.getCurrentInstance().addMessage("updateForm:postcode", new FacesMessage("Postcode format is invalid (EG: SW1A 1AA)"));
            valid = false;
        }
        
        // Check if username is correct length
        if(updateUser.getUsername().length() < 6){
            FacesContext.getCurrentInstance().addMessage("updateForm:username", new FacesMessage("The username must be atleast 6 characters in length."));
            valid = false;
        }
        
        User currentUser = (User) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user");

        if(!currentUser.getUsername().equals(updateUser.getUsername())){
            // Username was changed, check if it already exists
            if(uf.lookupUserByUsername(updateUser.getUsername()) != null){
                String response = "A user account already exists with this username.";
                FacesContext.getCurrentInstance().addMessage("updateForm:username", new FacesMessage(FacesMessage.SEVERITY_ERROR, response, response));
                valid = false;
            }
        }
        
        if(!currentUser.getEmailAddress().equals(updateUser.getEmailAddress())){
             // Check if email exists when email changed
            if(uf.lookupUserByEmail(updateUser.getEmailAddress()) != null){
                String response = "A user account already exists with this email address.";
                FacesContext.getCurrentInstance().addMessage("updateForm:emailAddress", new FacesMessage(FacesMessage.SEVERITY_ERROR, response, response));
                valid = false;
            }
        }
       
        if (valid) {
            uf.edit(updateUser);
            
            String response = "Your account has been updated successfully.";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, response, response));
        }

        return valid;
    }
}
