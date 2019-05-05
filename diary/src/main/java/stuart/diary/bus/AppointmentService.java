/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stuart.diary.bus;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import stuart.diary.ents.Appointment;
import stuart.diary.ents.User;
import stuart.diary.pers.AppointmentFacade;
import stuart.diary.pers.UserFacade;

/**
 *
 * @author stuart
 */
@Stateless
public class AppointmentService {
    
    @EJB
    private AppointmentFacade af;
    
    @EJB
    private UserFacade uf;
    
    /**
     * Returns a list of all appointments, except those in the past
     * @return List
     */
    public List<Appointment> searchAppointments(){
        
        List<Appointment> appointments = af.findAll();
        
        // Remove the old appointments
        for (int i = 0; i < appointments.size(); i++) {
            if(appointments.get(i).getStartDate().before(new Date())){
                appointments.remove(appointments.get(i));
            }
        }
        
        return appointments;
    }
    
    /**
     * Returns a list of all appointments, except those in the past
     * @param user User to search for
     * @return List
     */
    public List<Appointment> getAppointmentsByOwner(User user){
        
        List<Appointment> appointments = af.findAll();
        
        // Remove the old appointments
        for (int i = 0; i < appointments.size(); i++) {
            if(!appointments.get(i).getOwner().equals(user)){
                appointments.remove(appointments.get(i));
            }
        }
        
        return appointments;
    }
    
    /**
     * Returns a list of all appointments where the user is attending, except those in the past
     * @param user User to search for
     * @return List
     */
    public List<Appointment> getAppointmentsByAttending(User user){
        
        List<Appointment> appointments = af.findAll();
        
        // Remove the old appointments
        for (int i = 0; i < appointments.size(); i++) {
            if(!appointments.get(i).getAttendees().contains(user)){
                appointments.remove(appointments.get(i));
            }
        }
        
        return appointments;
    }
    
    /**
     * Returns a list of all appointments for the current user, except those in the past
     * @return List
     */
    public List<Appointment> searchAppointmentsForUser(){
        User currentUser = (User) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user");
        return this.searchAppointmentsForSelectedUser(currentUser);
    }
    
    /**
     * Returns a list of all appointments for the specified user, except those in the past
     * @param user User to search for
     * @return List
     */
    public List<Appointment> searchAppointmentsForSelectedUser(User user){
        
        List<Appointment> appointments = this.getAppointmentsByAttending(user);
        
        List<Appointment> ownerAppointments = this.getAppointmentsByOwner(user);
        
        for (int i = 0; i < ownerAppointments.size(); i++) {
            Appointment app = ownerAppointments.get(i);
            if(!appointments.contains(app)){
                appointments.add(app);
            }
        }
                
        // Remove the old appointments
        for (int i = 0; i < appointments.size(); i++) {
            if(appointments.get(i).getStartDate().before(new Date())){
                appointments.remove(appointments.get(i));
            }
        }
        
        return appointments;
    }
    
    /**
     * Return an appointment by ID
     * @param appointmentID Appointment ID
     * @return appointment
     */
    public Appointment getAppointmentById(int appointmentID){
        return af.find(appointmentID);
    }
    
    
    /**
     * Return an appointment by ID
     * @param appointment Appointment ID
     */
    public void deleteAppointment(Appointment appointment){
        af.remove(appointment);
    }
    
    
    /**
     * Create appointment, validate to check it is not in the past.
     * @param appointment The appointment to check
     * @param attendeesList List of names for those who will attend 
     * @return appointment
     */
    public Appointment createAppointment(Appointment appointment, List<String> attendeesList){
        boolean valid = true;
        
        // Get all error messages, remove when the appointment is checked
        FacesContext context = FacesContext.getCurrentInstance();
        Iterator<FacesMessage> it = context.getMessages();
        while ( it.hasNext() ) {
            it.next();
            it.remove();
        }
        
        // Check if all fields have values
        if( appointment.getTitle().length() == 0 || 
            appointment.getDescription().length() == 0 ||
            appointment.getStartDate() == null ||
            appointment.getEndDate()== null ){
            
            String response = "Please complete all the required fields.";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, response, response));
            return null;
        }
        
        // Check if the appointment start date is in the past
        if(appointment.getStartDate().before(new Date())){
            // Not a new date
            String response = "The start date must not be in the past.";
            FacesContext.getCurrentInstance().addMessage("appointmentForm:startDate", new FacesMessage(FacesMessage.SEVERITY_ERROR, response, response));
            valid = false;
        }
        
        // Check if the appointment end date is in the past
        if(appointment.getEndDate().before(new Date())){
            // Not a new date
            String response = "The end date must not be in the past.";
            FacesContext.getCurrentInstance().addMessage("appointmentForm:endDate", new FacesMessage(FacesMessage.SEVERITY_ERROR, response, response));
            valid = false;
        }
        
        // Check if the end date is before the start date
        if(appointment.getEndDate().before(appointment.getStartDate())){
            // Not a new date
            String response = "The end date must not be before the start date.";
            FacesContext.getCurrentInstance().addMessage("appointmentForm:endDate", new FacesMessage(FacesMessage.SEVERITY_ERROR, response, response));
            valid = false;
        }
        
        List<User> attendingUsers = new ArrayList<>();
        
        if(attendeesList.size() > 0){
            for(int i = 0; i < attendeesList.size(); i++){
                User user = (User) uf.lookupUserByUsername(attendeesList.get(i));
                attendingUsers.add(user);

                if(this.checkUserForExistingAppointments(appointment, user)){
                    String response = "There is an appointment clash for " + user.getFirstName() + " " + user.getLastName() +".";
                    FacesContext.getCurrentInstance().addMessage("appointmentForm:attendees", new FacesMessage(FacesMessage.SEVERITY_ERROR, response, response));
                    valid = false;
                }        
                appointment.setAttendees(attendingUsers);
            }   
        }

        
        if(this.checkUserForExistingAppointments(appointment, appointment.getOwner())){
            String response = "There is an appointment clash for the selected time and date.";
            FacesContext.getCurrentInstance().addMessage("appointmentForm:startDate", new FacesMessage(FacesMessage.SEVERITY_ERROR, response, response));
            valid = false;
        }
        
        if(valid){    
            af.create(appointment); 
            return appointment;
        }
        
        return null;
    }
    
    /**
     * Check a user for existing appointments that clash with the newly created times
     * @param appointment Appointment to check for clash
     * @param user User to check if they have a clash event
     * @return Boolean
     */
    public boolean checkUserForExistingAppointments(Appointment appointment, User user){
        
        boolean existingAppointment = false;
        
        // Get all the users existing events
        for(Appointment app : this.searchAppointmentsForSelectedUser(user)){
            Date startDate = app.getStartDate();
            Date endDate = app.getEndDate();

            // Check for matching start date
            if(startDate.equals(appointment.getStartDate())){
                existingAppointment = true;   
            }

            // Check for matching end date
            if(endDate.equals(appointment.getEndDate())){
                existingAppointment = true;   
            }

            if(startDate.after(appointment.getStartDate()) && startDate.before(appointment.getEndDate())){
                // Between the two appointment dates
                existingAppointment = true;
            }

            if(endDate.after(appointment.getStartDate()) && endDate.before(appointment.getEndDate())){
                // Between the two appointment dates
                existingAppointment = true;
            }
        }
        
        return existingAppointment;
    }
}
