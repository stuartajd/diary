/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stuart.diary.ctrl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import stuart.diary.bus.AppointmentService;
import stuart.diary.ents.Appointment;
import stuart.diary.ents.User;

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
    private ArrayList<String> newAppointmentAttendees = new ArrayList<>();
    
    public Appointment getNewAppointment() {
        return newAppointment;
    }

    public void setNewAppointment(Appointment newAppointment) {
        this.newAppointment = newAppointment;
    }

    public ArrayList<String> getNewAppointmentAttendees() {
        return newAppointmentAttendees;
    }

    public void setNewAppointmentAttendees(ArrayList<String> newAppointmentAttendees) {
        this.newAppointmentAttendees = newAppointmentAttendees;
    }
    
    /**
     * Display appointment variables
     */
    private Appointment viewAppointment;

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
        newAppointment.setOwner((User) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user"));
        
        viewAppointment = as.createAppointment(newAppointment, this.getNewAppointmentAttendees());
                
        this.setViewAppointment(viewAppointment);
        if(viewAppointment != null){
            return "view_appointment";
        }
        
        return "";
    }
    
    /**
     * Open up an appointment on a certain date, pull back the details
     * @return 
     */
    public String doViewAppointment(Appointment appointment){
        this.setViewAppointment(appointment);
        return "view_appointment";
    } 
    
    /**
     * Open up an appointment on a certain date, pull back the details
     * @return 
     */
    public String doDeleteAppointment(Appointment appointment){
        as.deleteAppointment(appointment);
        return "index?faces-redirect=true";
    } 
   
     /**
     * Selects all appointments from the database
     * 
     * @param justForCurrent Returns just the current signed in users appointments.
     * @return List of User of all members of the database.
     */
    public List<Appointment> getAllAppointments(boolean justForCurrent) {    
        if(!justForCurrent){
            return as.searchAppointments();
        } else {
            return as.searchAppointmentsForUser();
        }
    }
}
