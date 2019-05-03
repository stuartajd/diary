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

    public User getRegisterUser() {
        return registerUser;
    }

    public void setRegisterUser(User registerUser) {
        this.registerUser = registerUser;
    }

    /**
     * UserService Access Variables
     */
    private User loginUser = new User();

    public User getLoginUser() {
        return loginUser;
    }

    public void setLoginUser(User loginUser) {
        this.loginUser = loginUser;
    }
    
    /**
     * Update User Variables
     */
    private User updateUser = new User();

    public User getUpdateUser() {
        User userSession = (User)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user");
        return userSession;
    }

    public void setUpdateUser(User updateUser) {
        this.updateUser = updateUser;
    }
    
    
    /**
     * Search Users Variables
     */
    private User searchUser = new User();

    public User getSearchUser() {
        return searchUser;
    }

    public void setSearchUser(User searchUser) {
        this.searchUser = searchUser;
    }
    
    private List<User> searchResults = null;

    public List<User> getSearchResults() {
        return searchResults;
    }

    public void setSearchResults(List<User> searchResults) {
        this.searchResults = searchResults;
    }
    

    /**
     * Validate the user who logs into the user to the application
     *
     * @return
     */
    public String doLoginUser() {
        if(us.validateUser(loginUser)) {
            return "index?faces-redirect=true";
        }

        return "";
    }

    /**
     * Validate the users and login to the platform
     */
    public String doRegisterUser() {
        if(us.registerUser(registerUser)){
            return "login?faces-redirect=true";
        }
        
        return "";
    }
    
    /**
     * Validate the users and login to the platform
     */
    public String doUpdateUser() {
        if(us.updateUser(updateUser)){
            return "user?faces-redirect=true";
        }
        
        return "";
    }
    
    /**
     * Clear user request session
     */
    public String doLogoutUser(){
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "login?faces-redirect=true";
    }

    /**
     * Validate the users and login to the platform
     */
    public String doSearchUsers() {    
        searchResults = us.searchUsers();
        return "search";
    }

}
