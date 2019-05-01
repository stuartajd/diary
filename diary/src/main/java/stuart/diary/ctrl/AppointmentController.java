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
     * Display appointment variables
     */
    private Appointment viewAppointment = new Appointment();

    public Appointment getViewAppointment() {
        return viewAppointment;
    }

    public void setViewAppointment(Appointment viewAppointment) {
        this.viewAppointment = viewAppointment;
    }
    
    
    /**
     * Validate the users and login to the platform
     */
    public String doCreateAppointment() {        
        viewAppointment = as.createAppointment(newAppointment);
                
        if(viewAppointment != null){
            return "view_appointment?faces-redirect=true";
        }
        
        return "";
    }
    
    /**
     * Open up an appointment on a certain date, pull back the details
     * @return 
     */
    public String openAppointmentDate(){
        return "view_appointment?faces-redirect=true";
    } 
    
}
