package exactChange;

import common.Helper;
import common.InvalidDenominationsException;

public class ExactChangeCalculator {

	public static float getCost(int[] denominations, int N) {
		ExactChangeCalculator calci = new ExactChangeCalculator(denominations,
				N);
		calci.calculateMinCoinsDriver();
		return calci.calculateCost();
	}

	private float calculateCost() {
		float cost = 0.0f;
		minCoinsNeeded[0]=0;
		
		for(int coinCount : minCoinsNeeded){
			cost+=coinCount;
		}
		
		if(N>1.0f){
			float penalty = 0.0f; 
			for(int index=5;index<Helper.PENCES_IN_POUND;index+=5){
				penalty+=minCoinsNeeded[index];
			}
			cost += (penalty*(N-1.0f));
		}
		return cost;
	}

	private int[] minCoinsNeeded;
	private int[] denominations;
	private int N;

	private ExactChangeCalculator(int[] denominations, int N) {
		minCoinsNeeded = new int[Helper.PENCES_IN_POUND];
		for (int index = minCoinsNeeded.length - 1; index >= 0; index--) {
			minCoinsNeeded[index] = Helper.INF;
		}
		minCoinsNeeded[0]=0;
		this.denominations = denominations;
		this.N = N;
	}

	private void calculateMinCoinsDriver() {
		for (int denomination : denominations) {
			minCoinsNeeded[denomination] = 1;
		}
		calculateMinCoins(minCoinsNeeded.length - 1);
	}

	private int calculateMinCoins(int index) {
		if (index < 1) {
			return Helper.INF;
		}

		if (minCoinsNeeded[index] == Helper.INF) {
			// TODO: never try denomination bigger than parent call's
			for (int denomination : denominations) {
				if (denomination <= index) {
					int costWithThisDenomination = calculateMinCoins(index
							- denomination) + 1;
					if (costWithThisDenomination < minCoinsNeeded[index]) {
						// better result found
						minCoinsNeeded[index] = costWithThisDenomination;
					}
				}
			}
			// TODO: comment this when competing
			if (minCoinsNeeded[index] == Helper.INF) {
				throw new InvalidDenominationsException();
			}
		}
		return minCoinsNeeded[index];
	}

	/*
	 * protected int[] results;
	 * 
	 * ExactChangeCalculator() { results = new int[Helper.PENCES_IN_POUND];
	 * resetCosts(); }
	 * 
	 * final void resetCosts() { for (int index = results.length - 1; index >=
	 * 0; index--) { results[index] = Helper.INF; } }
	 * 
	 * final void calculateCostsForDenominations(int[] denominations) { for (int
	 * denomination : denominations) { results[denomination] = 1; }
	 * 
	 * calculateResult(results.length - 1, denominations); }
	 *//**
	 * 
	 * @return ignore index 0
	 */
	/*
	 * private final int[] getCosts() { return results; }
	 * 
	 * final float getCostGivenN(float N) { // todo: get float array to save
	 * conversion time float cost = 0; for (int result : results) { cost +=
	 * result; } if (N > 1.0) { float penalty = 0.0f; float n = N - 1.0f; for
	 * (int index = Helper.MULT_OF; index < Helper.PENCES_IN_POUND; index +=
	 * Helper.MULT_OF) { penalty += results[index]; } penalty *= n; cost +=
	 * penalty; } return cost; }
	 * 
	 * private final int calculateResult(int index, int[] denominations) { if
	 * (index < 1) { return Helper.INF; }
	 * 
	 * if (results[index] == Helper.INF) { // TODO: never try denomination
	 * bigger than parent call's for (int denomination : denominations) { if
	 * (denomination <= index) { int costWithThisDenomination =
	 * calculateResult(index - denomination, denominations) + 1; if
	 * (costWithThisDenomination < results[index]) { // better result found
	 * results[index] = costWithThisDenomination; } } } // TODO: comment this
	 * when competing if (results[index] == Helper.INF) { throw new
	 * InvalidDenominationsException(); } } return results[index]; }
	 */
}