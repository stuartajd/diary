/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stuart.diary.bus;

import javax.ejb.EJB;
import javax.ejb.Stateless;
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
    public boolean createAppointment(Appointment appointment){
        boolean valid = true;
        
        // Check if all fields have values
        if(appointment.getTitle().length() == 0 || appointment.getDescription().length() == 0
                || appointment.getStartDate().length() == 0 || appointment.getEndDate().length() == 0){
            
            return false;
        }
        
            
        // Validate appointment
        
//        af.create(appointment);
        return valid;
    }
}
