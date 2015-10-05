package common;

import java.util.List;
import java.util.TimerTask;

import runner.ParallelSolutionFinder;
import api.SolutionOptimizer;

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
			ImmutableSolution solution = runner.getBestSolution();
			model.offerNewSolution(solution.getDenominations(),
					solution.getCost());
			// TODO: see if this works
			runner.interrupt();
		}

		int[] bestDenominations = model.getBestDenominations();
		for (int index = 0; index < bestDenominations.length - 1; index++) {
			System.out.print(bestDenominations[index] + " ");
		}
		System.out.println(bestDenominations[bestDenominations.length - 1]);
		/*
		 * if (Helper.DEBUG_MODE) {
		 * System.out.println(StartsWithFivesStepsRandomly.getCount()); }
		 */
		System.exit(1);
	}

}
