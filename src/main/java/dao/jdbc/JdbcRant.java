package dao.jdbc;

import domain.Rant;

public class JdbcRant extends Rant {
  public JdbcRant() {}
  
  private Integer vehicleId;

  public Integer getVehicleId() {
    return vehicleId;
  }

  public void setVehicleId(Integer vehicleId) {
    this.vehicleId = vehicleId;
  }
}
