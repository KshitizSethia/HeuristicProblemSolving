package api;

import java.util.Random;

import runner.ParallelSolutionFinder;
import common.ImmutableSolution;

public abstract class SolutionOptimizer extends Thread {
	protected ParallelSolutionFinder model;

	protected int N;

	protected final Object denominationUpdateLock;
	protected int[] bestDenominations;
	protected double bestCost;

	protected Random random;

	protected SolutionOptimizer(ParallelSolutionFinder model, int N) {
		this.model = model;
		this.N = N;
		denominationUpdateLock = new Object();
		random = new Random();
	}

	public abstract ImmutableSolution getBestSolution();
}
