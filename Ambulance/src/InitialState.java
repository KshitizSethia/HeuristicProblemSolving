import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import com.google.code.ekmeans.EKmeans;

import jsprit.core.problem.Location;

public class InitialState {

  private static final int    NUM_DIMENSIONS = 2;

  private List<Patient>       patients;
  private final List<Integer> initialAmbulanceCount;
  private int[][]             clusterCenters;

  public InitialState(List<Patient> initialPatients,
      List<Integer> initialAmbulanceCount) {
    this.patients = initialPatients;
    this.initialAmbulanceCount = initialAmbulanceCount;
  }

  public void computeClusters() {
    // todo cache?
    // compute min and max values of locations to compute random cluster heads
    int minX = Integer.MAX_VALUE, maxX = Integer.MIN_VALUE,
        minY = Integer.MAX_VALUE, maxY = Integer.MIN_VALUE;
    for (Patient patient : patients) {
      if (minX > patient.x) {
        minX = patient.x;
      }
      if (minY > patient.y) {
        minY = patient.y;
      }

      if (maxX < patient.x) {
        maxX = patient.x;
      }
      if (maxY < patient.y) {
        maxY = patient.y;
      }
    }

    double[][] points = new double[patients.size()][NUM_DIMENSIONS];
    for (int index = 0; index < patients.size(); index++) {
      Patient patient = patients.get(index);
      points[index][0] = patient.x;
      points[index][1] = patient.y;
    }

    double[][] initialCentroids =
        makeInitialCentroidLocations(minX, maxX, minY, maxY);

    EKmeans meanCalc = new EKmeans(initialCentroids, points);
    meanCalc.setIteration(1000);
    meanCalc.setEqual(true);
    meanCalc.setDistanceFunction(EKmeans.MANHATTAN_DISTANCE_FUNCTION);
    meanCalc.run();

    clusterCenters = getInts(meanCalc.getCentroids());

    int[] clusterAssignments = meanCalc.getAssignments();
    for (int index = 0; index < patients.size(); index++) {
      int centroidIndex = clusterAssignments[index];
      int x = clusterCenters[centroidIndex][0];
      int y = clusterCenters[centroidIndex][1];

      patients.get(index).setClusterCentroid(x, y);
    }

  }

  private int[][] getInts(double[][] centroids) {
    int[][] results = new int[centroids.length][centroids[0].length];
    for (int row = 0; row < centroids.length; row++) {
      for (int col = 0; col < centroids[0].length; col++) {
        results[row][col] = (int) centroids[row][col];
      }
    }
    return results;
  }

  double[][] makeInitialCentroidLocations(int minX, int maxX, int minY,
      int maxY) {
    Random random = new Random();
    double[][] initialCentroids =
        new double[initialAmbulanceCount.size()][NUM_DIMENSIONS];
    for (int centroidIndex = 0; centroidIndex < initialAmbulanceCount
        .size(); centroidIndex++) {
      int x = random.nextInt(maxX - minX + 1) + minX;
      int y = random.nextInt(maxY - minY + 1) + minY;

      initialCentroids[centroidIndex][0] = x;
      initialCentroids[centroidIndex][1] = y;
    }
    return initialCentroids;
  }

  public List<Location> getVehicleLocations() {
    if (clusterCenters == null) {
      computeClusters();
    }

    List<Location> results = new ArrayList<Location>(clusterCenters.length);
    for (int index = 0; index < initialAmbulanceCount.size(); index++) {
      for (int ambulance = 0; ambulance < initialAmbulanceCount
          .get(index); ambulance++) {
        results.add(Location.newInstance((int) clusterCenters[index][0],
            (int) clusterCenters[index][1]));
      }
    }

    return results;
  }

  public List<Patient> getPatients() {
    // todo copy before return
    return patients;
  }

  @Override
  public String toString() {
    StringBuilder sbr = new StringBuilder();
    sbr.append("Problem as given in input:\n\n");

    // hospitals and their ambulances
    sbr.append("Hospitals with ambulance IDs\n");
    sbr.append(hospitalsToString());

    // patients and their locations
    sbr.append("\nPatient locations\n");
    int patientIdCounter = Example.FIRSTCOUNT;
    for (int patientIndex = 0; patientIndex < patients.size(); patientIndex++) {
      Patient patient = patients.get(patientIndex);
      sbr.append(String.format("patient_%d at (%d,%d), dies at %d\n",
          patientIdCounter++, patient.x, patient.y, patient.ttl));
    }

    return sbr.toString();
  }

  public String hospitalsToString() {
    StringBuilder sbr = new StringBuilder();
    int ambulanceCounter = Example.FIRSTCOUNT;
    for (int clusterIndex = 0; clusterIndex < initialAmbulanceCount
        .size(); clusterIndex++) {
      sbr.append(String.format("Hospital:%d|%d,%d,%d|",
          clusterIndex + Example.FIRSTCOUNT, clusterCenters[clusterIndex][0],
          clusterCenters[clusterIndex][1],
          initialAmbulanceCount.get(clusterIndex)));

      String ambulances =
          Arrays
              .toString(
                  IntStream
                      .range(ambulanceCounter,
                          ambulanceCounter
                              + initialAmbulanceCount.get(clusterIndex))
                      .toArray());
      sbr.append(ambulances.substring(1, ambulances.length() - 1));
      sbr.append("\n");
      ambulanceCounter += initialAmbulanceCount.get(clusterIndex);
    }

    return sbr.toString();
  }
}
