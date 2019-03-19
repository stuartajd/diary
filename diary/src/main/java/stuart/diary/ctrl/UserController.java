/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stuart.diary.ctrl;

import javax.ejb.EJB;
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
     * Validate the user who logs into the user to the application
     * @return 
     */        
    public String doLoginUser(){                 
        if(us.validateUser(loginUser)){
            return "index";
        }
        
        return "";
    } 
    
    
    /**
     * Validate the users and login to the platform 
     */ 
    public String doRegisterUser(){
        if(us.registerUser(registerUser)){
            return "index";
        } 
        
        return "";
    }

}
