/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stuart.diary.bus;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import stuart.diary.ents.Appointment;
import stuart.diary.pers.AppointmentFacade;

/**
 *
 * @author stuart
 */
@Stateless
public class AppointmentService {
    
    @EJB
    private AppointmentFacade af;
    
    /**
     * Create appointment, validate & check it's not in the past.
     * @param appointment
     * @return 
     */
    public Appointment createAppointment(Appointment appointment){
        boolean valid = true;
        
        // Check if all fields have values
        if( appointment.getTitle().length() == 0 || 
            appointment.getDescription().length() == 0 ||
            appointment.getStartTime() == null ||
            appointment.getEndTime()== null ){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Error: Please complete all the required fields."));
            return null;
        }
        
        
        if(valid){    
            af.create(appointment);  
        }
        
        return appointment;
    }
}
