package common;

public class ImmutableSolution {
	public int[] getAdjacencyList() {
		return adjacencyList;
	}

	public double getCost() {
		return cost;
	}

	private int[] adjacencyList;
	private double cost;

	public ImmutableSolution(int[] adjacencyList, double cost) {
		this.adjacencyList = adjacencyList;
		this.cost = cost;
	}
}
