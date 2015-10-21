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
    return result;
  }

  @Override
  public double getBackwardTransportTime(Location from, Location to,
      double arrivalTime, Driver driver, Vehicle vehicle) {
    double result = standardManhattan.getBackwardTransportTime(from, to,
        arrivalTime, driver, vehicle) + LOAD_UNLOAD_TIME_FOR_EVERY_LOCATION;
    return result;
  }

  @Override
  public double getTransportCost(Location from, Location to,
      double departureTime, Driver driver, Vehicle vehicle) {
    double result = standardManhattan.getTransportCost(from, to, departureTime,
        driver, vehicle) + LOAD_UNLOAD_TIME_FOR_EVERY_LOCATION;
    return result;
  }

  @Override
  public double getBackwardTransportCost(Location from, Location to,
      double arrivalTime, Driver driver, Vehicle vehicle) {
    double result = standardManhattan.getBackwardTransportCost(from, to,
        arrivalTime, driver, vehicle) + LOAD_UNLOAD_TIME_FOR_EVERY_LOCATION;
    return result;
  }

}
