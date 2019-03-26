/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stuart.diary.ctrl;

import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import stuart.diary.bus.AppointmentService;
import stuart.diary.ents.Appointment;

/**
 *
 * @author stuart
 */
@Named(value = "appointmentController")
@RequestScoped
public class AppointmentController {

    /**
     * Creates a new instance of AppointmentController
     */
    public AppointmentController() {
    }
    
    @EJB
    private AppointmentService as;

    /**
     * Create appointment variables
     */
    private Appointment newAppointment = new Appointment();

    public Appointment getNewAppointment() {
        return newAppointment;
    }

    public void setNewAppointment(Appointment newAppointment) {
        this.newAppointment = newAppointment;
    }
    
    /**
     * Validate the users and login to the platform
     */
    public String doCreateAppointment() {        
        if(as.createAppointment(newAppointment)){
            return "index?faces-redirect=true";
        }
        
        return "";
    }
    
}
