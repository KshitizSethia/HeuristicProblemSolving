package common;

public class ImmutableSolution {
	public int[] getDenominations() {
		return denominations;
	}

	public double getCost() {
		return cost;
	}

	private int[] denominations;
	private double cost;

	public ImmutableSolution(int[] denominations, double cost) {
		this.denominations = denominations;
		this.cost = cost;
	}
}
