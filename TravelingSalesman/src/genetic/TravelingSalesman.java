package genetic;

class TravelingSalesman {
} /*
 * package genetic;
 * 
 * 
 * import java.util.ArrayList; import java.util.Collections; import
 * java.util.List;
 * 
 * import org.jgap.Chromosome; import org.jgap.Gene; import
 * org.jgap.IChromosome; import org.jgap.InvalidConfigurationException; import
 * org.jgap.impl.IntegerGene; import org.jgap.impl.salesman.Salesman;
 * 
 * //todo: this returns the locations to visit based on index in file, convert
 * to cityID in calling code public class TravelingSalesman extends Salesman {
 * 
 * private static final long serialVersionUID = 1L; private double[][]
 * locations; private boolean goBackToSource; private List<Integer>
 * shuffleIndices;
 * 
 * TravelingSalesman(double[][] locations, boolean goBackToSource) {
 * 
 * this.locations = locations; this.goBackToSource = goBackToSource;
 * this.shuffleIndices = new ArrayList<Integer>(locations.length);
 * 
 * for (int index = 0; index < locations.length; index++) {
 * this.shuffleIndices.add(index); } }
 * 
 * @Override public IChromosome createSampleChromosome(Object ignored) { try {
 * Collections.shuffle(shuffleIndices);
 * 
 * Gene[] genes = new Gene[locations.length];
 * 
 * for (int index : shuffleIndices) { genes[index] = new
 * IntegerGene(getConfiguration(), 0, locations.length - 1);
 * genes[index].setAllele(new Integer(index)); }
 * 
 * IChromosome result = new Chromosome(getConfiguration(), genes); return
 * result;
 * 
 * } catch (InvalidConfigurationException propagate) { throw new
 * IllegalStateException(propagate); } }
 * 
 * @Override public double distance(Gene arg0, Gene arg1) { int city0 =
 * ((IntegerGene) arg0).intValue(); int city1 = ((IntegerGene) arg1).intValue();
 * 
 * if (!goBackToSource) { if ((city0 == 0 && city1 == locations.length - 1) ||
 * (city0 == locations.length - 1 && city1 == 0)) { return 0; } }
 * 
 * double dist = Math.pow((locations[city0][0] - locations[city1][0]), 2); dist
 * += Math.pow((locations[city0][1] - locations[city1][1]), 2); dist +=
 * Math.pow((locations[city0][2] - locations[city1][2]), 2);
 * 
 * dist = Math.sqrt(dist); return dist; }
 * 
 * }
 */