package dao.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Connection;

import java.util.Date;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.core.PreparedStatementCreator;

import dao.RantDao;
import domain.Motorist;
import domain.Rant;
import domain.Vehicle;

public class JdbcRantDao extends JdbcDaoSupport implements RantDao {

   private static final String RANT_INSERT = "insert into rant (id, rantText, vehicle_id, postedDate) "
                     + "values (null,?,?,?)";

   private static final String RANT_SELECT = "select id, rantText, vehicle_id, postedDate from rant";

   private static final String RANT_FOR_VEHICLE_SELECT = RANT_SELECT
                     + " where vehicle_id = ?";

   private static final String RANT_FOR_DAY_SELECT = RANT_SELECT
                     + " where postedDate=?";

   private static final String VEHICLE_SELECT = "select id, plateNumber, state from vehicle";

   private static final String VEHICLE_BY_ID_SELECT = VEHICLE_SELECT
                     + " where id=?";

   private static final String VEHICLE_BY_PLATE_SELECT = VEHICLE_SELECT
                     + " where state=? and plateNumber=?";

   private static final String VEHICLE_INSERT = "insert into vehicle (id, plateNumber, state, motorist_id) "
                     + "values (null,?,?,?)";

   private static final String MOTORIST_SELECT = "select id, email, password, firstName, lastName from motorist";

   private static final String MOTORIST_BY_EMAIL_SELECT = MOTORIST_SELECT
                     + " where email=?";

   private static final String MOTORIST_BY_ID_SELECT = MOTORIST_SELECT
                     + " where id=?";

   private static final String MOTORIST_INSERT = "insert into motorist (id, email, password, firstName, lastName) "
                     + "values (null, ?,?,?,?)";

   public JdbcRantDao() {}

   /**
    * Saves a Motorist using Spring's JDBC template.
    * 
    * From Listing 5.4
    * 
    * @param motorist
    *           The Motorist to save
    */
   public void saveMotorist(Motorist motorist) {
      getJdbcTemplate().update(
                        MOTORIST_INSERT,
                        new Object[] { motorist.getEmail(),
                              motorist.getPassword(), motorist.getFirstName(),
                              motorist.getLastName() });

      motorist.setId(queryForIdentity());
   }

   private int queryForIdentity() {
      return new Integer(getJdbcTemplate().queryForInt("call identity()"));
   }

   public void saveRant(Rant rant) {
      /* getJdbcTemplate()
                        .update(
                                          RANT_INSERT,
                                          new Object[] { rant.getRantText(),
                                                rant.getVehicle().getId(),
                                                rant.getPostedDate() });

			
      rant.setId(queryForIdentity()); */

			final Rant r = rant;

			KeyHolder keyHolder = new GeneratedKeyHolder();

      getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
					PreparedStatement ps = connection.prepareStatement(RANT_INSERT,
									new String[]{"id"});

					ps.setString(1, r.getRantText());
					ps.setInt(2, r.getVehicle().getId());
					ps.setDate(3, new java.sql.Date(r.getPostedDate().getTime()));

					return ps;
				}
			}, keyHolder);
			
			rant.setId(keyHolder.getKey().intValue());

   }

   public void saveVehicle(Vehicle vehicle) {
			final Vehicle v = vehicle;
      final Integer motoristId = vehicle.getMotorist() != null ? vehicle
                        .getMotorist().getId() : null;

			KeyHolder keyHolder = new GeneratedKeyHolder();

      getJdbcTemplate().update(new PreparedStatementCreator() {
						public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
								PreparedStatement ps = connection.prepareStatement(
											VEHICLE_INSERT, new String[]{"id"});

								ps.setString(1, v.getPlateNumber());
								ps.setString(2, v.getState());

								if(null != motoristId) {
									ps.setInt(3, motoristId);
								} else {
									ps.setNull(3, java.sql.Types.INTEGER);
								}
								
								return ps;
						}
				}, keyHolder);

			vehicle.setId(keyHolder.getKey().intValue());

      // vehicle.setId(queryForIdentity());
   }

   @SuppressWarnings("unchecked")
   public List<Rant> getAllRants() {
      return getJdbcTemplate().query(RANT_SELECT, new RowMapper() {
         public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            Rant rant = new Rant();
            rant.setId(rs.getInt(1));
            rant.setRantText(rs.getString(2));
            rant.setPostedDate(rs.getDate(4));
            Vehicle vehicle = findVehicleById(rs.getInt(3));
            rant.setVehicle(vehicle);
            return rant;
         }
      });
   }

   @SuppressWarnings("unchecked")
   public List<Rant> getRantsForDay(Date day) {
      return getJdbcTemplate().query(RANT_FOR_DAY_SELECT, new Object[] { day },
                        new RowMapper() {
                           public Object mapRow(ResultSet rs, int rowNum)
                                             throws SQLException {
                              Rant rant = new Rant();
                              rant.setId(rs.getInt(1));
                              rant.setRantText(rs.getString(2));
                              rant.setPostedDate(rs.getDate(4));
                              Vehicle vehicle = findVehicleById(rs.getInt(3));
                              rant.setVehicle(vehicle);
                              return rant;
                           }
                        });
   }

   public Vehicle findVehicleById(Integer id) {

      List matches = getJdbcTemplate().query(VEHICLE_BY_ID_SELECT,
                        new Object[] { id }, new RowMapper() {
                           public Object mapRow(ResultSet rs, int rowNum)
                                             throws SQLException,
                                             DataAccessException {
                              Vehicle vehicle = new Vehicle();
                              vehicle.setId(rs.getInt(1));
                              vehicle.setPlateNumber(rs.getString(2));
                              vehicle.setState(rs.getString(3));
                              return vehicle;
                           }
                        });

      return matches.size() > 0 ? (Vehicle) matches.get(0) : null;
   }

   /**
    * Retrieves a Motorist by its ID
    * 
    * From Listing 5.5
    * 
    * @param id
    *           The ID of the Motorist to retrieve
    * @return The Motorist
    */
   public Motorist getMotoristById(long id) {
      List matches = getJdbcTemplate().query(MOTORIST_BY_ID_SELECT,
                        new Object[] { Long.valueOf(id) }, new RowMapper() {
                           public Object mapRow(ResultSet rs, int rowNum)
                                             throws SQLException,
                                             DataAccessException {
                              Motorist motorist = new Motorist();

                              motorist.setId(rs.getInt(1));
                              motorist.setEmail(rs.getString(2));
                              motorist.setPassword(rs.getString(3));
                              motorist.setFirstName(rs.getString(4));
                              motorist.setLastName(rs.getString(5));
                              return motorist;
                           }
                        });

      return matches.size() > 0 ? (Motorist) matches.get(0) : null;
   }

   public Vehicle findVehicleByPlate(String state, String plateNumber) {

      List matches = getJdbcTemplate().query(VEHICLE_BY_PLATE_SELECT,
                        new Object[] { state, plateNumber }, new RowMapper() {
                           public Object mapRow(ResultSet rs, int rowNum)
                                             throws SQLException,
                                             DataAccessException {
                              Vehicle vehicle = new Vehicle();
                              vehicle.setId(rs.getInt(1));
                              vehicle.setPlateNumber(rs.getString(2));
                              vehicle.setState(rs.getString(3));
                              return vehicle;
                           }
                        });

      if (matches.size() == 0) {
         return null;
      }

      final Vehicle vehicle = (Vehicle) matches.get(0);

      List rants = getJdbcTemplate().query(RANT_FOR_VEHICLE_SELECT,
                        new Object[] { vehicle.getId() }, new RowMapper() {
                           public Object mapRow(ResultSet rs, int rowNum)
                                             throws SQLException {
                              Rant rant = new Rant();
                              rant.setId(rs.getInt(1));
                              rant.setRantText(rs.getString(2));
                              rant.setPostedDate(rs.getDate(4));
                              rant.setVehicle(vehicle);
                              return rant;
                           }
                        });

      vehicle.setRants(rants);

      return vehicle;
   }

   public Motorist getMotoristByEmail(String email) {
      List matches = getJdbcTemplate().query(MOTORIST_BY_EMAIL_SELECT,
                        new Object[] { email }, new RowMapper() {
                           public Object mapRow(ResultSet rs, int rowNum)
                                             throws SQLException,
                                             DataAccessException {
                              Motorist motorist = new Motorist();

                              motorist.setId(rs.getInt(1));
                              motorist.setEmail(rs.getString(2));
                              motorist.setPassword(rs.getString(3));
                              motorist.setFirstName(rs.getString(4));
                              motorist.setLastName(rs.getString(5));
                              return motorist;
                           }
                        });

      return matches.size() > 0 ? (Motorist) matches.get(0) : null;
   }

   public int getMotoristCount() {
      // TODO Auto-generated method stub
      return 0;
   }

}
