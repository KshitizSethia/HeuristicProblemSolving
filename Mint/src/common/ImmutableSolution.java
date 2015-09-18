package common;

public class ImmutableSolution {
	public int[] getDenominations() {
		return denominations;
	}

	public float getCost() {
		return cost;
	}

	private int[] denominations;
	private float cost;

	ImmutableSolution(int[] denominations, float cost) {
		this.denominations = denominations;
		this.cost = cost;
	}
}
