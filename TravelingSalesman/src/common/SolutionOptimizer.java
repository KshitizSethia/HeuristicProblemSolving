package common;

import runner.ParallelSolutionFinder;

public abstract class SolutionOptimizer extends Thread {
	protected ParallelSolutionFinder model;


	protected SolutionOptimizer(ParallelSolutionFinder model) {
		this.model = model;
	}

	public abstract ImmutableSolution getBestSolution();
}
