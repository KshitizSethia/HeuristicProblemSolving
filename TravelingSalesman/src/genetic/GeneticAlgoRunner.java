package genetic;
class GeneticAlgoRunner{
	
}
/*package genetic;


import java.util.List;

import org.jgap.Configuration;
import org.jgap.Gene;
import org.jgap.GeneticOperator;
import org.jgap.IChromosome;
import org.jgap.InvalidConfigurationException;
import org.jgap.impl.CrossoverOperator;
import org.jgap.impl.IntegerGene;
import org.jgap.impl.SwappingMutationOperator;

public class GeneticAlgoRunner extends Thread {

	private double[][] locations;
	private boolean goBackToSource;
	private int[] cityIDs;
	private TravelingSalesman salesman;
	private int maxRounds;

	public GeneticAlgoRunner(List<double[]> locations, List<Integer> cityIDs, boolean goBackToSource) throws InvalidConfigurationException {
		this.locations = new double[locations.size()][];
		for (int index = 0; index < locations.size(); index++) {
			this.locations[index] = locations.get(index);
		}
		
		this.cityIDs = new int[cityIDs.size()];
		for(int index=0;index<cityIDs.size();index++){
			this.cityIDs[index] = cityIDs.get(index);
		}
		
		this.goBackToSource = goBackToSource;
		salesman = new TravelingSalesman(this.locations, goBackToSource);
		Configuration conf = salesman.createConfiguration(null);
		
		//see if we can erase existing operators
		conf.getGeneticOperators().clear();
		assert conf.getGeneticOperators().size()==0;
		
		//conf.addGeneticOperator(new CrossoverOperator(conf, 0.5));
		//conf.addGeneticOperator(new SwappingMutationOperator(conf, 100));
		this.maxRounds = 2000;
	}

	public void run() {
		// while (true) {
		try {
			salesman.setMaxEvolution(maxRounds);
			Configuration.reset();
			IChromosome optimalSolution = salesman.findOptimalPath(null);

			System.out.println(maxRounds + " rounds: "
					+ getCost(optimalSolution.getGenes()) + ", path: "
					+ getPath(optimalSolution));
			// maxRounds += 10;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// }
	}

	private String getPath(IChromosome optimalSolution) {
		StringBuilder sbr = new StringBuilder();
		for (Gene gene : optimalSolution.getGenes()) {
			int id = ((IntegerGene) gene).intValue();
			sbr.append(cityIDs[id] + ", ");
		}
		return sbr.toString();
	}

	private String getCost(Gene[] optimalPath) {
		double cost = 0;
		for (int index = 0; index < optimalPath.length - 1; index++) {
			double edgeCost = salesman.distance(optimalPath[index],
					optimalPath[index + 1]);
			cost += edgeCost;
		}
		if (goBackToSource) {
			cost += salesman.distance(optimalPath[0],
					optimalPath[optimalPath.length - 1]);
		}

		return Double.toString(cost);
	}

}
*/