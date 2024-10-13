package ejb;

import java.util.Date;
import java.util.List;

import javax.ejb.CreateException;

import org.springframework.ejb.support.AbstractStatelessSessionBean;

import domain.Motorist;
import domain.Rant;
import domain.Vehicle;
import service.MotoristAlreadyExistsException;
import service.RantService;

public class RantServiceEjb 
    extends AbstractStatelessSessionBean 
    implements RantService {  
  public RantServiceEjb() {}

  private RantService rantService;
  protected void onEjbCreate() throws CreateException {
    rantService = (RantService) getBeanFactory().getBean("rantService");
  }
  
  public void addMotorist(Motorist motorist) 
      throws MotoristAlreadyExistsException {
    rantService.addMotorist(motorist);
  }
  
  public void addRant(Rant rant) {
    rantService.addRant(rant);
  }

  public List<Rant> getRantsForDay(Date date) {
    return rantService.getRantsForDay(date);
  }

  public List<Rant> getRantsForVehicle(Vehicle vehicle) {
    return rantService.getRantsForVehicle(vehicle);
  }

  public List<Rant> getRecentRants() {
    return rantService.getRecentRants();
  }

  public void sendDailyRantEmails() {
    rantService.sendDailyRantEmails();
  }

  public void sendEmailForVehicle(Vehicle vehicle) {
    rantService.sendEmailForVehicle(vehicle);
  }
}
