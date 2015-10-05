package api;

import java.util.Random;

import runner.ParallelSolutionFinder;
import common.ImmutableSolution;

public abstract class SolutionOptimizer extends Thread {
	protected ParallelSolutionFinder model;

	protected double N;

	protected final Object denominationUpdateLock;
	protected int[] bestDenominations;
	protected double bestCost;

	protected Random random;

	protected SolutionOptimizer(ParallelSolutionFinder model, double N) {
		this.model = model;
		this.N = N;
		denominationUpdateLock = new Object();
		random = new Random();
	}

	public abstract ImmutableSolution getBestSolution();
}
