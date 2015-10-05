package runner;

public class FirstRunner {
}
/*
 * package runner;
 * 
 * import java.io.BufferedReader; import java.io.FileReader; import
 * java.io.IOException; import java.util.ArrayList; import java.util.LinkedList;
 * import java.util.List;
 * 
 * import lkh.LKH_Thread;
 * 
 * public class FirstRunner {
 * 
 * public static void main(String[] args) throws NumberFormatException,
 * IOException {
 * 
 * List<Integer> cityIDList = new LinkedList<Integer>(); List<double[]>
 * locations = new ArrayList<double[]>();
 * 
 * try (BufferedReader inFile = new BufferedReader(new FileReader(
 * "travelingtest.txt"))) {// "small.txt", "travelingtest.txt" String line;
 * while ((line = inFile.readLine()) != null) { String[] vals =
 * line.split("[ ]+");
 * 
 * cityIDList.add(new Integer(vals[0])); double[] location = new double[] {
 * Double.parseDouble(vals[1]), Double.parseDouble(vals[2]),
 * Double.parseDouble(vals[3]) }; locations.add(location); } }
 * 
 * int[] cityIds = new int[cityIDList.size()]; for (int index = 0; index <
 * cityIds.length; index++) { cityIds[index] = cityIDList.get(index); }
 * 
 * // GeneticAlgoRunner runner = new GeneticAlgoRunner(locations, cityIds, //
 * false); // runner.start();
 * 
 * LKH_Thread runner = new LKH_Thread(locations, cityIds); runner.start(); } }
 */