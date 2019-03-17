/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stuart.diary.ctrl;

import javax.inject.Named;
import javax.enterprise.context.RequestScoped;

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
    
    /**
     * UserService Access Variables
     */
    private String accessUsername;
    private String accessPassword;

}
