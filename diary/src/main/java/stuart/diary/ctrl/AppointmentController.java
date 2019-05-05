/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stuart.diary.ctrl;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import stuart.diary.bus.AppointmentService;
import stuart.diary.bus.UserService;
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

    @EJB
    private UserService us;
    
    /**
     * Create appointment variables
     */
    private Appointment newAppointment = new Appointment();
    private ArrayList<String> newAppointmentAttendees = new ArrayList<>();
    
    /**
     * Get the new appointment details 
     * @return Appointment
     */
    public Appointment getNewAppointment() {
        return newAppointment;
    }

    /**
     * Set the new appointment details
     * @param newAppointment Appointment details
     */
    public void setNewAppointment(Appointment newAppointment) {
        this.newAppointment = newAppointment;
    }

    /**
     * Get appointment attendees list
     * @return ArrayList
     */
    public ArrayList<String> getNewAppointmentAttendees() {
        return newAppointmentAttendees;
    }

    /**
     * Set the new appointment list of users
     * @param newAppointmentAttendees Appointment attendees
     */
    public void setNewAppointmentAttendees(ArrayList<String> newAppointmentAttendees) {
        this.newAppointmentAttendees = newAppointmentAttendees;
    }
    
    /**
     * Display appointment variables
     */
    private Appointment viewAppointment;

    /**
     * Get the viewed appointment details
     * @return Appointment
     */
    public Appointment getViewAppointment() {
        return viewAppointment;
    }

    /**
     * Set the appointment to be viewed
     * @param viewAppointment Appointment viewed
     */
    public void setViewAppointment(Appointment viewAppointment) {
        this.viewAppointment = viewAppointment;
    }
    
    /**
     * Validate the users and login to the platform
     * @return String JSF route
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
     * @param appointment Appointment to view
     * @return String JSF route
     */
    public String doViewAppointment(Appointment appointment){
        this.setViewAppointment(appointment);
        return "view_appointment";
    } 
    
    /**
     * Open up an appointment on a certain date, pull back the details
     * @param appointment Appointment to delete
     * @return String JSF route 
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
    
    /**
     * Selected user variable
     */
    private User searchSelectedUser = new User();

    /**
     * Get the current selected user for searching
     * @return User
     */
    public User getSearchSelectedUser() {
        return searchSelectedUser;
    }

    /**
     * Set the selected user for searching
     * @param searchSelectedUser Selected User
     */
    public void setSearchSelectedUser(User searchSelectedUser) {
        this.searchSelectedUser = searchSelectedUser;
    }
    
    /**
     * List of appointments for the searched user
     */
    private List<Appointment> appointmentsSearchForUser;

    /**
     * Get the list of users who were searched 
     * @return List
     */
    public List<Appointment> getAppointmentsSearchForUser() {
        return appointmentsSearchForUser;
    }

    /**
     * Set the search appointment results list
     * @param appointmentsSearchForUser List of appointments for the user
     */
    public void setAppointmentsSearchForUser(List<Appointment> appointmentsSearchForUser) {
        this.appointmentsSearchForUser = appointmentsSearchForUser;
    }
    
    /**
     * Select the user appointments by UserName
     * @param userName Username for the user to search
     * @return JSF Route Link
     */
    public String showUserAppointments(String userName){
        User selectedUser = us.lookupUserByUsername(userName);
        this.setSearchSelectedUser(selectedUser);
        this.setAppointmentsSearchForUser(as.searchAppointmentsForSelectedUser(selectedUser));
        return "all_appointments";
    }
}
