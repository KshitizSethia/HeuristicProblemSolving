import jsprit.core.problem.Location;
import jsprit.core.problem.solution.route.activity.TimeWindow;

public class Patient {

  public final int x, y, ttl;
  private int      assignedDepot_x;
  private int      assignedDepot_y;

  public Patient(String line) {
    String[] parts = line.split(",");

    this.x = Integer.parseInt(parts[0]);
    this.y = Integer.parseInt(parts[1]);
    this.ttl = Integer.parseInt(parts[2]);
  }

  public TimeWindow getTimeWindow() {
    // FIXME put better estimate
    return new TimeWindow(0, ttl);
  }

  public String toString() {
    return "(" + x + ", " + y + ", " + ttl + ")";
  }

  public void setClusterCentroid(int x, int y) {
    this.assignedDepot_x = x;
    this.assignedDepot_y = y;
  }

  public Location getAssignedClusterCentroid() {
    return Location.newInstance(assignedDepot_x, assignedDepot_y);
  }
}
