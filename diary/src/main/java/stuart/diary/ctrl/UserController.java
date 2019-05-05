/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stuart.diary.ctrl;

import java.util.List;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import stuart.diary.bus.UserService;
import stuart.diary.ents.User;

/**
 *
 * @author stuart
 */
@Named(value = "userController")
@RequestScoped
public class UserController {

    /**
     * Creates a new instance of UserController
     */
    public UserController() {
    }

    @EJB
    private UserService us;

    /**
     * User Registration Variables
     */
    private User registerUser = new User();

    /**
     * Get the register user variables
     * @return User
     */
    public User getRegisterUser() {
        return registerUser;
    }

    /**
     * Set the registered user variables
     * @param registerUser User to be registered
     */
    public void setRegisterUser(User registerUser) {
        this.registerUser = registerUser;
    }

    /**
     * UserService Access Variables
     */
    private User loginUser = new User();

    /**
     * Get the current logged in user details
     * @return User
     */
    public User getLoginUser() {
        return loginUser;
    }

    /**
     * Set the current login user
     * @param loginUser User to be authenticated
     */
    public void setLoginUser(User loginUser) {
        this.loginUser = loginUser;
    }
    
    /**
     * Update User Variables
     */
    private User updateUser = this.getCurrentUser();
    
    /**
     * Get the current update user account
     * @return User
     */
    public User getUpdateUser() {
        return updateUser;
    }

    /**
     * Set the current update user account
     * @param updateUser Set user to update
     */
    public void setUpdateUser(User updateUser) {
        this.updateUser = updateUser;
    }
    
    /**
     * Get the current signed in user
     * @return User
     */
    public User getCurrentUser(){
        return (User)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user");
    }

    /**
     * Validate the users credentials
     * 
     * @return String for redirection of JSF
     */
    public String doLoginUser() {
        if(us.validateUser(loginUser)) {
            return "index?faces-redirect=true";
        }

        return "";
    }

    /**
     * Validate the users registration credentials
     * 
     * @return String for redirection of JSF
     */
    public String doRegisterUser() {
        if(us.registerUser(registerUser)){
            return "login?faces-redirect=true";
        }
        
        return "";
    }
    
    /**
     * Validate the users credentials and update their account
     * 
     * @return String for redirection of JSF
     */
    public String doUpdateUser() {
        if(us.updateUser(updateUser)){
            return "user";
        }
        
        return "";
    }
    
    /**
     * Clears the users sessions
     * 
     * @return String for redirection of JSF
     */
    public String doLogoutUser(){
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "login?faces-redirect=true";
    }
    
    /**
     * Selects all users from the database.
     * 
     * @param excludeCurrent Excludes the current signed in user from the results
     * @return List of User of all members of the database.
     */
    public List<User> getAllUsers(boolean excludeCurrent) {    
        List<User> searchResults = us.searchUsers();
        if(excludeCurrent){
            searchResults.remove((User)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user"));
        }
        return searchResults;
    }

}
