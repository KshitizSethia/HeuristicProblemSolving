package lkh;

import java.util.List;

import runner.ParallelSolutionFinder;

import common.ImmutableSolution;
import common.SolutionOptimizer;

public class LKH_Thread extends SolutionOptimizer {

	LKH_algo solver;
	
	public LKH_Thread(ParallelSolutionFinder model, List<double[]> coordinates) {
		super(model);
		this.solver = new LKH_algo(coordinates);
	}

	@Override
	public void run() {
		solver.optimizeTour();
		ImmutableSolution bestSolution = solver.getBestSolution();
		model.offerNewSolution(bestSolution.getAdjacencyList(),
				bestSolution.getCost());
	}

	/*
	 * public void printTour() { String newLine =
	 * System.getProperty("line.separator"); int[] nextFrom = solver.getTour();
	 * 
	 * StringBuilder sbr = new StringBuilder(); int node = 0; do {
	 * sbr.append(cityIds[node] + newLine); node = nextFrom[node]; } while (node
	 * != 0);
	 * 
	 * System.out.println(sbr.toString()); }
	 */

	@Override
	public ImmutableSolution getBestSolution() {
		return solver.getBestSolution();
	}

}
