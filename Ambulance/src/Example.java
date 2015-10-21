import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.common.base.Joiner;

import jsprit.core.algorithm.VehicleRoutingAlgorithm;
import jsprit.core.algorithm.box.Jsprit;
import jsprit.core.problem.Location;
import jsprit.core.problem.VehicleRoutingProblem;
import jsprit.core.problem.VehicleRoutingProblem.FleetSize;
import jsprit.core.problem.job.Shipment;
import jsprit.core.problem.solution.VehicleRoutingProblemSolution;
import jsprit.core.problem.solution.route.VehicleRoute;
import jsprit.core.problem.solution.route.activity.TourActivity;
import jsprit.core.problem.solution.route.activity.TourActivity.JobActivity;
import jsprit.core.problem.vehicle.VehicleImpl;
import jsprit.core.problem.vehicle.VehicleType;
import jsprit.core.problem.vehicle.VehicleTypeImpl;
import jsprit.core.reporting.SolutionPrinter;
import jsprit.core.reporting.SolutionPrinter.Print;
import jsprit.core.util.Coordinate;
import jsprit.core.util.Solutions;

public class Example {

  private final static int       AMBU_CAPACITY  = 4;
  private final static String    NUM_THREADS    = "4";
  private final static int       MAX_ITERATIONS = 500;
  private static final FleetSize FLEETSIZE      = FleetSize.FINITE;
  static final int               FIRSTCOUNT     = 1;

  public static void main(String[] args) throws FileNotFoundException {

    // read initial state of problem
    InitialState state = InputReader.read(args[0]);
    state.computeClusters();
    //System.out.println(state.toString());

    // create vehicle routing problem
    VehicleRoutingProblem problem = makeVRProblem(state);

    // create vehicle routing algo
    VehicleRoutingAlgorithm algo = Jsprit.Builder.newInstance(problem)
        .setProperty(Jsprit.Parameter.THREADS.toString(), NUM_THREADS)
        .buildAlgorithm();

    algo.setMaxIterations(MAX_ITERATIONS);

    // find best solution
    Collection<VehicleRoutingProblemSolution> solutions =
        algo.searchSolutions();
    VehicleRoutingProblemSolution bestSolution = Solutions.bestOf(solutions);

    // output the solution to console
    /*
     * System.out.println("Distances: \n" +
     * ManhattanCostsWithLoadUnloadTime.distances.toString());
     * System.out.println( "Times: \n" +
     * ManhattanCostsWithLoadUnloadTime.times.toString());
     */
    //SolutionPrinter.print(problem, bestSolution, Print.VERBOSE);

    // output in required format
    printSolution(state, problem, bestSolution);
  }

  private static void printSolution(InitialState state,
      VehicleRoutingProblem problem,
      VehicleRoutingProblemSolution bestSolution) {

    // print hospital details
    System.out.println(state.hospitalsToString());

    // print route details
    for (VehicleRoute route : bestSolution.getRoutes()) {

      List<String> patients = new ArrayList<String>(AMBU_CAPACITY);

      Coordinate lastCoordinates = null;

      for (TourActivity activity : route.getActivities()) {
        if (activity instanceof JobActivity) {

          JobActivity job = (JobActivity) activity;

          int patientID = Integer.parseInt(job.getJob().getId().split("_")[1]);
          Patient savedPatient =
              state.getPatients().get(patientID - FIRSTCOUNT);

          if (job.getName().equals("pickupShipment")) {
            String patientDetails = String.format("%s,%d,%d,%d", patientID,
                savedPatient.x, savedPatient.y, savedPatient.ttl);
            patients.add(patientDetails);
          } else if (job.getName().equals("deliverShipment")
              && patients.size() > 0) {
            Coordinate startCoordinates;
            if (lastCoordinates == null) {
              startCoordinates = route.getStart().getLocation().getCoordinate();
            } else {
              startCoordinates = lastCoordinates;
            }

            printTour(route.getVehicle().getId().split("_")[1],
                startCoordinates, patients,
                savedPatient.getAssignedClusterCentroid().getCoordinate());
            patients.clear();

            lastCoordinates =
                savedPatient.getAssignedClusterCentroid().getCoordinate();
          }
        }
      }

    }
  }

  static void printTour(String vehicleID, Coordinate startCoordinate,
      List<String> patients, Coordinate endCoordinate) {
    System.out.format("Ambulance:%s|%d,%d|%s|%d,%d\n", vehicleID,
        (int) startCoordinate.getX(), (int) startCoordinate.getY(),
        Joiner.on(";").join(patients), (int) endCoordinate.getX(),
        (int) endCoordinate.getY());
  }

  private static VehicleRoutingProblem makeVRProblem(InitialState state) {
    // todo verify: make distances manhattan
    // FIXME make ambulances deposit patients at any hospital
    // todo verify: add time delays for picking up and depositing patients

    // make vehicle routing problem builder
    VehicleRoutingProblem.Builder vrpBuilder =
        VehicleRoutingProblem.Builder.newInstance();

    // make vehicle type
    VehicleTypeImpl.Builder vehicleTypeBuilder =
        VehicleTypeImpl.Builder.newInstance("ambulanceWithFourBeds")
            .addCapacityDimension(0, AMBU_CAPACITY);
    VehicleType vehicleType = vehicleTypeBuilder.build();

    // set vehicles at their start point
    List<Location> locations = state.getVehicleLocations();
    int counter = FIRSTCOUNT;
    for (Location location : locations) {
      VehicleImpl.Builder vehicleBuilder =
          VehicleImpl.Builder.newInstance("ambulance_" + counter++);
      vehicleBuilder.setStartLocation(location);
      vehicleBuilder.setType(vehicleType).setReturnToDepot(false);

      vrpBuilder.addVehicle(vehicleBuilder.build());
    }

    List<Patient> patients = state.getPatients();
    counter = FIRSTCOUNT;
    for (Patient patient : patients) {
      Shipment shipment = Shipment.Builder
          .newInstance("patient_" + counter++ + "_" + patient.ttl)
          .addSizeDimension(0, 1).setDeliveryTimeWindow(patient.getTimeWindow())
          .setPickupLocation(Location.newInstance(patient.x, patient.y))
          .setDeliveryLocation(patient.getAssignedClusterCentroid()).build();

      vrpBuilder.addJob(shipment);
    }

    vrpBuilder.setRoutingCost(
        new ManhattanCostsWithLoadUnloadTime(vrpBuilder.getLocations()));

    vrpBuilder.setFleetSize(FLEETSIZE);
    VehicleRoutingProblem problem = vrpBuilder.build();
    return problem;
  }

}
