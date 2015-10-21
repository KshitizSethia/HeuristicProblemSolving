import java.util.HashMap;

import jsprit.core.problem.Location;
import jsprit.core.problem.cost.VehicleRoutingTransportCosts;
import jsprit.core.problem.driver.Driver;
import jsprit.core.problem.vehicle.Vehicle;
import jsprit.core.util.Locations;
import jsprit.core.util.ManhattanCosts;

public class ManhattanCostsWithLoadUnloadTime
    implements VehicleRoutingTransportCosts {

  /*
   * public static final HashMap<String, Double> distances = new HashMap<String,
   * Double>();; public static final HashMap<String, Double> times = new
   * HashMap<String, Double>();
   */

  // this takes care of load time for loading each patient into ambulance and
  // unload time at hospital as all times are extended
  private static final int     LOAD_UNLOAD_TIME_FOR_EVERY_LOCATION = 1;

  VehicleRoutingTransportCosts standardManhattan;

  ManhattanCostsWithLoadUnloadTime(Locations locations) {
    standardManhattan = new ManhattanCosts(locations);
  }

  @Override
  public double getTransportTime(Location from, Location to,
      double departureTime, Driver driver, Vehicle vehicle) {
    double result = standardManhattan.getTransportTime(from, to, departureTime,
        driver, vehicle) + LOAD_UNLOAD_TIME_FOR_EVERY_LOCATION;
    // verifyManhattans("Time", from, to, result);
    return result;
  }

  /*
   * private static final Logger loger =
   * Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
   *
   *
   * private void verifyManhattans(String type, Location from, Location to,
   * double result) { String key = String.format("(%d,%d) to (%d,%d)", (int)
   * from.getCoordinate().getX(), (int) from.getCoordinate().getY(), (int)
   * to.getCoordinate().getX(), (int) to.getCoordinate().getY()); if
   * (type.equals("Time")) { if (times.containsKey(key)) { assert (result ==
   * times.get(key)); } else { times.put(key, result); } } else { if
   * (distances.containsKey(key)) { assert (result == distances.get(key)); }
   * else { distances.put(key, result); } } }
   */

  @Override
  public double getBackwardTransportTime(Location from, Location to,
      double arrivalTime, Driver driver, Vehicle vehicle) {
    double result = standardManhattan.getBackwardTransportTime(from, to,
        arrivalTime, driver, vehicle) + LOAD_UNLOAD_TIME_FOR_EVERY_LOCATION;
    // verifyManhattans("Time", to, from, result);
    return result;
  }

  @Override
  public double getTransportCost(Location from, Location to,
      double departureTime, Driver driver, Vehicle vehicle) {
    double result = standardManhattan.getTransportCost(from, to, departureTime,
        driver, vehicle) + LOAD_UNLOAD_TIME_FOR_EVERY_LOCATION;
    // verifyManhattans("Distance", from, to, result);
    return result;
  }

  @Override
  public double getBackwardTransportCost(Location from, Location to,
      double arrivalTime, Driver driver, Vehicle vehicle) {
    double result = standardManhattan.getBackwardTransportCost(from, to,
        arrivalTime, driver, vehicle) + LOAD_UNLOAD_TIME_FOR_EVERY_LOCATION;
    // verifyManhattans("Distance", to, from, result);
    return result;
  }

}
