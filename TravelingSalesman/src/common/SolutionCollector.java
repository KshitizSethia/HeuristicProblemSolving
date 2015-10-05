package common;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.TimerTask;

import runner.ParallelSolutionFinder;
import common.Input;

public class SolutionCollector extends TimerTask {

	ParallelSolutionFinder model;
	List<SolutionOptimizer> runnerPool;

	public SolutionCollector(ParallelSolutionFinder model,
			List<SolutionOptimizer> runnerPool) {
		this.model = model;
		this.runnerPool = runnerPool;
	}

	@Override
	public void run() {
		for (SolutionOptimizer runner : runnerPool) {
			if (runner.isAlive()) {
				ImmutableSolution solution = runner.getBestSolution();
				model.offerNewSolution(solution.getAdjacencyList(),
						solution.getCost());
				runner.interrupt();
			}
		}

		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(
					Input.getInstance().outFile));
			int[] adjacencyList = model.getBestTour();
			for (int index = 0; index < adjacencyList.length; index++) {
				out.write(Integer.toString(adjacencyList[index]));
				out.write("\n");
			}
			out.flush();
			out.close();
		} catch (IOException ex) {
			throw new IllegalArgumentException(ex);
		}
		/*
		 * if (Helper.DEBUG_MODE) {
		 * System.out.println(StartsWithFivesStepsRandomly.getCount()); }
		 */
		System.exit(1);
	}

}
