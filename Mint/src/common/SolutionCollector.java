package common;

import java.util.Arrays;
import java.util.List;
import java.util.TimerTask;

import runner.ParallelSolutionFinder;

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

		System.out.println(Arrays.toString(model.getBestDenominations()));
	}

}