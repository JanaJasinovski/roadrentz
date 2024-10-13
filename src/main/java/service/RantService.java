package service;

import java.util.Date;
import java.util.List;

import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;

import domain.Rant;
import domain.Motorist;
import domain.Vehicle;

@ManagedResource(objectName="rantz:name=RantService") // Annotation added in Listing 12.5
public interface RantService {
  public void addRant(Rant rant);
  public List<Rant> getRecentRants();
  public void addMotorist(Motorist motorist) throws MotoristAlreadyExistsException;
  public List<Rant> getRantsForVehicle(Vehicle vehicle);
  public List<Rant> getRantsForDay(Date date);
  public void sendEmailForVehicle(Vehicle vehicle);

  @ManagedOperation(description="Send the daily rant e-mail.") // Annotation added in Listing 12.5
  public void sendDailyRantEmails();
}