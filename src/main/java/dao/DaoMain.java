package dao;

import java.util.Date;

import org.springframework.context.ApplicationContext;
// import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import domain.Rant;
import domain.Vehicle;
import service.RantService;

public class DaoMain {
  public static void main(String[] args) {
    /* ApplicationContext ctx = new FileSystemXmlApplicationContext(new String[] {
        "src/main/webapp/WEB-INF/services.xml", "src/main/webapp/WEB-INF/dataaccess.xml"   
    }); */
		ApplicationContext ctx = new ClassPathXmlApplicationContext(new String[] {
			"roadrantz-data.xml", "roadrantz-data-jpa.xml", "roadrantz-transactions.xml", 
			"roadrantz-email.xml", "roadrantz-mbeans.xml", "roadrantz-services.xml"
		});
    
    RantService dao = (RantService) ctx.getBean("rantService");
    
    Rant rant = new Rant();
    rant.setPostedDate(new Date());
    rant.setRantText("TEST RANT 3");
    Vehicle vehicle = new Vehicle();
    vehicle.setPlateNumber("J55DNY");
    vehicle.setState("TX");
    rant.setVehicle(vehicle);
    dao.addRant(rant);

    rant = new Rant();
    rant.setPostedDate(new Date());
    rant.setRantText("TEST RANT 4");
    vehicle = new Vehicle();
    vehicle.setPlateNumber("G44MNX");
    vehicle.setState("TX");
    rant.setVehicle(vehicle);
    dao.addRant(rant);

  }
}
