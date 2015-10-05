import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class ExactChange {

	static int[] exactChangeNumberCache;
	static int[] exchangeNumberCache;

	public ExactChange() {
		exactChangeNumberCache = new int[240];
		exchangeNumberCache = new int[240];

		for (int i = 0; i < 240; i++) {
			exactChangeNumberCache[i] = -1;
			exchangeNumberCache[i] = -1;
		}
	}

	public static void renewCache() {
		for (int i = 0; i < 240; i++) {
			exactChangeNumberCache[i] = -1;
			exchangeNumberCache[i] = -1;
		}
	}

	public static void generateIndividualCostsGivenDenominations(Denominations soln) {
		getIndividualCostsGivenDenominations(soln, 239);
		for(int i = 0; i<exactChangeNumberCache.length; i++) {
			exactChangeNumberCache[i] = exactChangeNumberCache[i] - 1;
		}
	}

	private static int getIndividualCostsGivenDenominations(Denominations soln,
			int value) {
		int min = Integer.MAX_VALUE;

		int[] solution_list = soln.getSolutionList();
		int[] tempCostMinArray = new int[solution_list.length];

		if (value < 0)
			return 0;

		if (value == 0)
			return 1;

		if (exactChangeNumberCache[value] != -1) {
			return exactChangeNumberCache[value];
		}

		for (int i = 0; i < solution_list.length; i++) {
			tempCostMinArray[i] = getIndividualCostsGivenDenominations(soln,
					value - solution_list[i]);
			if (tempCostMinArray[i] < min && tempCostMinArray[i] != 0) {
				min = tempCostMinArray[i];
			}
		}

		exactChangeNumberCache[value] = min + 1;
		return exactChangeNumberCache[value];
	}

	public static double calculateCost(double N) {
		double cost = 0;

		for (int i = 1; i<exactChangeNumberCache.length; i++) {
			
				if ( i % 5 == 0 ) {
					cost = cost + N * exactChangeNumberCache[i];
				} 
				else {
					cost = cost + exactChangeNumberCache[i];
				}
		}
		
		return cost;
}

	/*
	 * Start at some configuration of the denominations; randomly choose index
	 * and change the value at that index.
	 */
	public void runLocalSearch(int iterations, Denominations instance, double N, int UpperLimit,
		 int numberOfStartingPoints, int maxLocalMinimaCount, int stepSize) {
		Random r = new Random();

		double min_avg = Integer.MAX_VALUE;

		for (int kkk = 0; kkk < numberOfStartingPoints; kkk++) {
			/*
			int[] current = { 1, r.nextInt(UpperLimit - 1) + 2,
					r.nextInt(UpperLimit - 1) + 2, r.nextInt(UpperLimit - 1) + 2,
					r.nextInt(UpperLimit - 1) + 2, r.nextInt(UpperLimit - 1) + 2,
					r.nextInt(UpperLimit - 1) + 2 };
			*/
			
			int[] current = { 1, 5, 10, 25, 50, 75, 100 };

			renewCache();
			
			System.out.println("Starting point : ");
			for (int i : current) {
				System.out.print(i + " ");
			}
			System.out.println();

			instance.modifyAll_SolutionList(current);
			generateIndividualCostsGivenDenominations(instance);
			double previous_cost = calculateCost(N);
			double current_cost = 0;
			int iteration_count = 0;
			int countToCheckForLocalMinima = 0;
			
			while (true) {

				renewCache();
				int index = r.nextInt(7 - 1) + 1;
				int tempVal = current[index];
				//current[index] = Math.abs((current[index] + r.nextInt())) % UpperLimit;
				current[index] = Math.abs((current[index] + stepSize)) % UpperLimit;
				
				while (current[index] == 0) {
					//current[index] = Math.abs((current[index] + r.nextInt())) % UpperLimit;
					current[index] = Math.abs((current[index] + stepSize)) % UpperLimit;
				}

				instance.modifyAll_SolutionList(current);
				generateIndividualCostsGivenDenominations(instance);
				current_cost = calculateCost(N);

				if (current_cost > previous_cost) {
					if(countToCheckForLocalMinima >= maxLocalMinimaCount) {
						countToCheckForLocalMinima = 0;
						instance.printDemoninationList();
						System.out.println("Cost = " + previous_cost + " N = " + N + 
						" Starting Point Number= " + kkk);
						break;
					}
					else {
						countToCheckForLocalMinima ++;
					}
					current[index] = tempVal;
				} 
				
				else if (current_cost <= previous_cost) {
					//System.out.println(current_cost);
					iteration_count++;
					previous_cost = current_cost;
					if (iteration_count >= iterations) {
						instance.printDemoninationList();
						System.out.println("Cost = " + previous_cost + " N = " + N + 
						" Starting Point Number= " + kkk);
						
						System.out.println();
						break;
					}
				}
			}
		}
	}
	
	public void runLocalSearchTwo(int iterations, Denominations instance, double N, int UpperLimit,
			 int numberOfStartingPoints, int maxLocalMinimaCount, int stepSize) {
			Random r = new Random();

			double min_avg = Integer.MAX_VALUE;
			
			
			for (int kkk = 0; kkk < numberOfStartingPoints; kkk++) {
				
				int[] current = { 1, (r.nextInt(48) + 1)*5,
						(r.nextInt(48) + 1)*5, (r.nextInt(48) + 1)*5,
						(r.nextInt(48) + 1)*5, (r.nextInt(48) + 1)*5,
						(r.nextInt(48) + 1)*5 };
				
				
				//int[] current = { 1, 5, 10, 25, 50, 75, 100 };

				renewCache();
				
				System.out.println("Starting point : ");
				for (int i : current) {
					System.out.print(i + " ");
				}
				System.out.println();

				instance.modifyAll_SolutionList(current);
				generateIndividualCostsGivenDenominations(instance);
				double previous_cost = calculateCost(N);
				double current_cost = 0;
				int iteration_count = 0;
				int countToCheckForLocalMinima = 0;
				
				while (true) {

					renewCache();
					int index = r.nextInt(7 - 1) + 1;
					int tempVal = current[index];
					//current[index] = Math.abs((current[index] + r.nextInt())) % UpperLimit;
					current[index] = Math.abs((current[index] + stepSize)) % UpperLimit;
					
					while (current[index] == 0) {
						//current[index] = Math.abs((current[index] + r.nextInt())) % UpperLimit;
						current[index] = Math.abs((current[index] + stepSize)) % UpperLimit;
					}

					instance.modifyAll_SolutionList(current);
					generateIndividualCostsGivenDenominations(instance);
					current_cost = calculateCost(N);

					if (current_cost > previous_cost) {
						if(countToCheckForLocalMinima >= maxLocalMinimaCount) {
							countToCheckForLocalMinima = 0;
							instance.printDemoninationList();
							System.out.println("Cost = " + previous_cost + " N = " + N + 
							" Starting Point Number= " + kkk);
							break;
						}
						else {
							countToCheckForLocalMinima ++;
						}
						current[index] = tempVal;
					} 
					
					else if (current_cost <= previous_cost) {
						//System.out.println(current_cost);
						iteration_count++;
						previous_cost = current_cost;
						if (iteration_count >= iterations) {
							instance.printDemoninationList();
							System.out.println("Cost = " + previous_cost + " N = " + N + 
							" Starting Point Number= " + kkk);
							
							System.out.println();
							break;
						}
					}
				}
			}
		}
	
	public void runLocalSearchThree(int iterations, Denominations instance, double N, int UpperLimit,
			 int numberOfStartingPoints) {
			Random r = new Random();

			double min_avg = Integer.MAX_VALUE;
			int[] stepSizes = {1,2,3,5,7,11,37,10,15, 25, -5, -2,-3,-5,-7,-11,-37, -10, -15, -25};
			
			ArrayList<Integer> a = new ArrayList<Integer>();
			
			for(int x : stepSizes) {
				a.add(x);
			}
			
			Collections.shuffle(a);
			
			for (int kkk = 0; kkk < numberOfStartingPoints; kkk++) {
				
				int[] current = { 1, (r.nextInt(48) + 1)*5,
						(r.nextInt(48) + 1)*5, (r.nextInt(48) + 1)*5,
						(r.nextInt(48) + 1)*5, (r.nextInt(48) + 1)*5,
						(r.nextInt(48) + 1)*5 };
				
				
				//int[] current = { 1, 5, 10, 25, 50, 75, 100 };

				renewCache();
				
				System.out.println("Starting point : ");
				for (int i : current) {
					System.out.print(i + " ");
				}
				System.out.println();

				instance.modifyAll_SolutionList(current);
				generateIndividualCostsGivenDenominations(instance);
				double previous_cost = calculateCost(N);
				double current_cost = 0;
				double previous_cost2 = calculateCost(1);
				double current_cost2 = 0;
				
				int iteration_count = 0;
				int countToCheckForLocalMinima = 0;
				
				while (true) {

					renewCache();
					int index = r.nextInt(7-1) + 1; //(7-1) + 1
					int stepSizeIndex = r.nextInt(stepSizes.length);
					int stepSize = a.get(stepSizeIndex);
					
					int tempVal = current[index];
					current[index] = Math.abs((current[index] + r.nextInt())) % UpperLimit;
					//current[index] = Math.abs((current[index] + stepSize)) % UpperLimit;
					
					while (current[index] == 0) {
						current[index] = Math.abs((current[index] + r.nextInt())) % UpperLimit;
						//current[index] = Math.abs((current[index] + stepSize)) % UpperLimit;
					}

					instance.modifyAll_SolutionList(current);
					generateIndividualCostsGivenDenominations(instance);
					current_cost = calculateCost(N);
					current_cost2 = calculateCost(1);

					if (current_cost > previous_cost) {
						current[index] = tempVal;
					} 
					
					else if (current_cost <= previous_cost) {
						//System.out.println(current_cost);
						iteration_count++;
						previous_cost = current_cost;
						previous_cost2 = current_cost2;
						
						if (//iteration_count >= iterations || 
								countToCheckForLocalMinima == iterations) {
							instance.printDemoninationList();
							System.out.println("Cost = " + previous_cost + " N = " + N + 
							" Starting Point Number= " + kkk  + " For N = 1, Cost = " + previous_cost2);
							
							countToCheckForLocalMinima = 0;
							System.out.println();
							break;
						}
						
						if(previous_cost == current_cost) {
							countToCheckForLocalMinima ++;
						}
						else {
							countToCheckForLocalMinima = 0;
						}
					}
				}
			}
		}
	
	
	public void runLocalSearchFour(int iterations, Denominations instance, double N, int UpperLimit,
			 int numberOfStartingPoints) {
			Random r = new Random();

			double min_avg = Integer.MAX_VALUE;
			
			int[] stepSizes = {1,2,3,5,7,11,37,10,15, 25, -5, -2,-3,-5,-7,-11,-37, -10, -15, -25};
			
			ArrayList<Integer> a = new ArrayList<Integer>();
			
			for(int x : stepSizes) {
				a.add(x);
			}
			
			Collections.shuffle(a);
			
			for (int kkk = 0; kkk < numberOfStartingPoints; kkk++) {
				
				int[] current = { 1, (r.nextInt(48) + 1)*5,
						(r.nextInt(48) + 1)*5, (r.nextInt(48) + 1)*5,
						(r.nextInt(48) + 1)*5, (r.nextInt(48) + 1)*5,
						(r.nextInt(48) + 1)*5 };
				
				
				//int[] current = { 1, 5, 10, 25, 50, 75, 100 };

				renewCache();
				
				System.out.println("Starting point : ");
				for (int i : current) {
					System.out.print(i + " ");
				}
				System.out.println();

				instance.modifyAll_SolutionList(current);
				generateIndividualCostsGivenDenominations(instance);
				double previous_cost = calculateCost(N);
				double current_cost = 0;
				double previous_cost2 = calculateCost(1);
				double current_cost2 = 0;
				
				int iteration_count = 0;
				int countToCheckForLocalMinima = 0;
				
				int index = 1;
				while (true) {

					renewCache();
					index = r.nextInt(7-1) + 1; //(7-1) + 1
					int stepSizeIndex = r.nextInt(stepSizes.length);
					int stepSize = a.get(stepSizeIndex);
					
					int tempVal = current[index];
					current[index] = Math.abs((current[index] + r.nextInt())) % UpperLimit;
					//current[index] = Math.abs((current[index] + stepSize)) % UpperLimit;
					
					while (current[index] == 0) {
						current[index] = Math.abs((current[index] + r.nextInt())) % UpperLimit;
						//current[index] = Math.abs((current[index] + stepSize)) % UpperLimit;
					}

					instance.modifyAll_SolutionList(current);
					generateIndividualCostsGivenDenominations(instance);
					current_cost = calculateCost(N);
					current_cost2 = calculateCost(1);

					if (current_cost > previous_cost) {
						//current[index] = tempVal;
					    int bestVal = 0;
						//int min = Integer.MAX_VALUE;
						for(int ci = 2; ci<240; ci ++) {
							current[index] = ci;
							instance.modifyAll_SolutionList(current);
							renewCache();
							generateIndividualCostsGivenDenominations(instance);
							double cost = calculateCost(N);
							
							if(cost < current_cost) {
								current_cost = cost;
								bestVal = ci;
								//break;
							}
						}
						
						current[index] = bestVal;
						previous_cost = current_cost;
					}
						
					    else if (current_cost <= previous_cost) {
						//System.out.println(current_cost);
						iteration_count++;
						previous_cost = current_cost;
						previous_cost2 = current_cost2;
						
						if (//iteration_count >= iterations || 
								countToCheckForLocalMinima == 10) {
							instance.printDemoninationList();
							System.out.println("Cost = " + previous_cost + " N = " + N + 
							" Starting Point Number= " + kkk  + " For N = 1, Cost = " + previous_cost2);
							
							countToCheckForLocalMinima = 0;
							System.out.println();
							break;
						}
						
						if(previous_cost == current_cost) {
							countToCheckForLocalMinima ++;
						}
						else {
							countToCheckForLocalMinima = 0;
						}
					}
				}
			}
		}

	public static void main(String args[]) {
		ExactChange ec = new ExactChange();
		Denominations instance = Denominations.getDenominationsInstance();

		int[] a = { 1, 5, 10, 25, 50, 75, 100};
		instance.modifyAll_SolutionList(a);
		
		Random r = new Random();
		double min = Double.MAX_VALUE;
		double N = 1;
		long start = System.currentTimeMillis();

		/*
		 * for(int i = 0; i<100; i++) { int[] temp = {1, r.nextInt(240 - 1) + 2,
		 * r.nextInt(240 - 1) + 2, r.nextInt(240 - 1) + 2, r.nextInt(240 - 1) +
		 * 2, r.nextInt(240 - 1) + 2, r.nextInt(240 - 1) + 2};
		 * 
		 * instance.modifyAll_SolutionList(temp);
		 * ec.generateIndividualCostsGivenDenominations(instance);
		 * 
		 * double x = ec.calculateCost(N); if(x < min) { min = x; a = temp; }
		 * renewCache(); }
		 */
		double[] NArray = {10, 2000};
		
		for(double NN : NArray) {
			ec.runLocalSearchThree(200, instance, NN, 240, 20);
		}

		/*for(int ii = 0; ii<10; ii++) {
		int[] current = { 1, (r.nextInt(48) + 1)*5,
				(r.nextInt(48) + 1)*5, (r.nextInt(48) + 1)*5,
				(r.nextInt(48) + 1)*5, (r.nextInt(48) + 1)*5,
				(r.nextInt(48) + 1)*5 };
		for(int i : current) {
			System.out.print(i + " ");
		}
		System.out.println();
		}
		*/
		ExactChange.renewCache();
		ExactChange.generateIndividualCostsGivenDenominations(instance);
		
		//for(int i = 0; i<exactChangeNumberCache.length; i++) {
			//System.out.println(i + ": " + exactChangeNumberCache[i]);
		//}
		long stop = System.currentTimeMillis();
		System.out.println("Time Elapsed " + (stop - start));
		System.out.println("Minimum Value = " + min);

	}
}
