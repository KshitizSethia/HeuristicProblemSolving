import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;


public class ExchangeNumber {
	
	static int[] exchangeNumberCache = new int[240];
	static private int size = 240;
	
	public static void initializeExchangeNumber(Denominations soln) {
		//exchangeNumberCache = new int[size];
		Set<Integer> denominations = new HashSet<Integer>();
		
		for(int i = 0; i<soln.getSolutionList().length; i++) {
			denominations.add(soln.getSolutionList()[i]);
		}
		
		for (int i = 0; i < size; i++) {
			if(denominations.contains(i)) {
				exchangeNumberCache[i] = 1;
			}
			else {
				exchangeNumberCache[i] = -1;
			}
		}
	}

	public static void renewCache() {
		for (int i = 0; i < size; i++) {
			exchangeNumberCache[i] = -1;
		}
	}
	
	public static void generateIndividualExchangeCostsGivenDenominations(Denominations soln) {		
		getIndividualExchangeCostsGivenDenominations(soln);
	}
	
	private static void getIndividualExchangeCostsGivenDenominations(Denominations soln) {
		
		int[] solution_list = soln.getSolutionList();		
		//int exactChange = exactChangeNumberCache[value];
		
		for(int valueTemp = 1; valueTemp <= 120; valueTemp ++) {
			
			int minx = Integer.MAX_VALUE; int x, y = 0;
			for (int i = 0; i < solution_list.length; i++) {
				int diff = solution_list[i] - valueTemp;
				y = getExactChangeNumber(valueTemp);
				if(diff > 0) {
					x = getExactChangeNumber(diff) + 1;
					int temp = Math.min(x, y);
					minx = Math.min(minx, temp);
				}
				else {
					minx = Math.min(minx, y);
				}
			}
			exchangeNumberCache[valueTemp] = minx;
		}
		
		for( int n=1; n<120; n++ ) {
			//exchangeNumberCache[n + 120] = exchangeNumberCache[120-n];
			exchangeNumberCache[n + 120] = Math.min(ExactChange.exactChangeNumberCache[n+120],
					ExactChange.exactChangeNumberCache[120-n]);
		}
	}
	
	public static double calculateExchangeCost(double N) {
			double cost = 0;

			for (int i = 1; i<exchangeNumberCache.length; i++) {
				
					if ( i % 5 == 0 ) {
						cost = cost + N * exchangeNumberCache[i];
					} 
					else {
						cost = cost + exchangeNumberCache[i];
					}
			}
			
			return cost;
	}
	
	public static void runExchangeLocalSearch(int iterations, Denominations instance, double N, int UpperLimit,
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
								
				System.out.println("Starting point : ");
				for (int i : current) {
					System.out.print(i + " ");
				}
				System.out.println();

				instance.modifyAll_SolutionList(current);
				ExactChange.renewCache();
				ExactChange.generateIndividualCostsGivenDenominations(instance);
				initializeExchangeNumber(instance);
				generateIndividualExchangeCostsGivenDenominations(instance);
				
				double previous_cost = calculateExchangeCost(N);
				double current_cost = 0;
				double previous_cost2 = calculateExchangeCost(1);
				double current_cost2 = 0;
				
				int iteration_count = 0;
				int countToCheckForLocalMinima = 0;
				
				while (true) {
					
					int index = r.nextInt(7-1) + 1; //(7-1) + 1
					int stepSizeIndex = r.nextInt(stepSizes.length);
					int stepSize = a.get(stepSizeIndex);
					
					int tempVal = current[index];
					current[index] = Math.abs((current[index] + r.nextInt())) % UpperLimit;
					
					while (current[index] == 0) {
						current[index] = Math.abs((current[index] + r.nextInt())) % UpperLimit;
					}
					
					ExactChange.renewCache();
					instance.modifyAll_SolutionList(current);
					ExactChange.generateIndividualCostsGivenDenominations(instance);
					initializeExchangeNumber(instance);
					generateIndividualExchangeCostsGivenDenominations(instance);
					
					current_cost = calculateExchangeCost(N);
					current_cost2 = calculateExchangeCost(1);

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
							//System.out.println();
							System.out.println("ExchangeCache");
							for(int i = 0; i<exchangeNumberCache.length; i++) {
								System.out.print(i+": "+exchangeNumberCache[i] + ", ");
							}
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
		Denominations instance = Denominations.getDenominationsInstance();
		int[] a = {1, 5, 10, 25, 50, 75, 100};
		instance.modifyAll_SolutionList(a);
		
		//generateIndividualExchangeCostsGivenDenominations(instance);
		
		//for(int i = 0; i<exchangeNumberCache.length; i++) {
			//System.out.print(i + ": " + exchangeNumberCache[i]);
			//System.out.println();
			
		//}
		ExactChange ec = new ExactChange();
		
		runExchangeLocalSearch(1000, instance, 1, 240,
				 20);
		
	}
	
	private static int getExactChangeNumber(int value) {
		if(value < 0) {
			return Integer.MAX_VALUE;
		}
		return ExactChange.exactChangeNumberCache[value];
	}
	
}
