/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stuart.diary.pers;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import stuart.diary.ents.Appointment;
import stuart.diary.ents.User;

/**
 *
 * @author stuart
 */
@Stateless
public class AppointmentFacade extends AbstractFacade<Appointment> {

    @PersistenceContext(unitName = "persistanceDiary")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AppointmentFacade() {
        super(Appointment.class);
    }    
}
