package lkh;

import java.util.List;

import runner.ParallelSolutionFinder;

import common.ImmutableSolution;
import common.SolutionOptimizer;

public class LKH_Thread_random extends SolutionOptimizer {

	LKH_algo_random solver;
	private final List<double[]> coordinates;

	public LKH_Thread_random(ParallelSolutionFinder model,
			List<double[]> coordinates) {
		super(model);
		// this.solver = new LKH_algo(coordinates);
		this.coordinates = coordinates;
	}

	@Override
	public void run() {
		// todo: run forever
		while (true) {
			try {
				if (solver == null) {
					solver = new LKH_algo_random(coordinates);
				} else {
					solver.randomize();
				}
				solver.optimizeTour();
				ImmutableSolution bestSolution = solver.getBestSolution();
				model.offerNewSolution(bestSolution.getAdjacencyList(),
						bestSolution.getCost());
			} catch (Exception ignored) {
			}
		}
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
