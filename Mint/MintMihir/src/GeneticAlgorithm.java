import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;


public class GeneticAlgorithm {
	
	static int populationSize;
	static int sizeAfterSelection;
	static int[] bestDenomination;
	
	static List<int[]> population_MultiplesOfFive; //= new ArrayList<>();
	static List<int[]> population_RandomIntegers; // = new ArrayList<>();
	static List<int[]> population_Mixture; //= new ArrayList<>();
	
	GeneticAlgorithm(int popSize, int postSelectionSize) {
		populationSize = popSize;
		sizeAfterSelection = postSelectionSize;
	}
	
	GeneticAlgorithm(int popSize) {
		populationSize = popSize;
	}
	
	public static void generatePopulation() {
		population_MultiplesOfFive = new ArrayList<>();
		population_RandomIntegers = new ArrayList<>();
		population_Mixture = new ArrayList<>();
		
		Random r = new Random();
		
		for(int i = 0; i<populationSize; i++) {
			int[] denominationSet_MultiplesOfFive = { 1, (r.nextInt(48) + 1)*5,
					(r.nextInt(48) + 1)*5, (r.nextInt(48) + 1)*5,
					(r.nextInt(48) + 1)*5, (r.nextInt(48) + 1)*5,
					(r.nextInt(48) + 1)*5 };
			
			int[] denominationSet_RandomIntegers = { 1, 5, //r.nextInt(240 - 1) + 2,
					r.nextInt(240 - 1) + 2, r.nextInt(240 - 1) + 2,
					r.nextInt(240 - 1) + 2, r.nextInt(240 - 1) + 2,
					r.nextInt(240 - 1) + 2 };
			
			population_MultiplesOfFive.add(denominationSet_MultiplesOfFive);
			population_RandomIntegers.add(denominationSet_RandomIntegers);
			
			population_Mixture.add(denominationSet_MultiplesOfFive);
			population_Mixture.add(denominationSet_MultiplesOfFive);
		}
	}
	
	public static List<int[]> selectFromPopulation(List<int[]> populationList, double N, 
			double cutoff_cost) {
		Denominations instance = Denominations.getDenominationsInstance();
		double minimum_cost = Integer.MAX_VALUE;
		ExactChange ec = new ExactChange(); 
		
		List<int[]> BestSelectionFromPopulation = new ArrayList<>();
		
		for(int[] i : populationList) {
			instance.modifyAll_SolutionList(i);
			ExactChange.generateIndividualCostsGivenDenominations(instance);
			double cost = ExactChange.calculateCost(N);
			minimum_cost = Math.min(cost,  minimum_cost);
			ExactChange.renewCache();
		}
		
		double cutoff = minimum_cost + cutoff_cost;
		
		for(int[] i : populationList) {
			instance.modifyAll_SolutionList(i);
			ExactChange.generateIndividualCostsGivenDenominations(instance);
			double cost = ExactChange.calculateCost(N);
			
			if(cost <= cutoff) {
				BestSelectionFromPopulation.add(i);
			}
			
			ExactChange.renewCache();
		}
		
		System.out.println(minimum_cost);
		return BestSelectionFromPopulation;
	}
	
	public static int[] recombineTwo(int[] parent1, int[] parent2) {
		//Random r = new Random();
		//int[] child = new int[parent1.length * 2];
		List<Integer> child = new ArrayList<>();
		
		for(int i = 1; i<parent1.length; i++) {
			child.add(parent1[i]);
			child.add(parent2[i]);
		}
		
		Collections.shuffle(child);
		
		//If you start from 1; 1 will always be maintained at the start 
		//of the denomination set.
		
		for(int i = 1; i<parent1.length; i++) {
			parent1[i] = child.get(i);
		}
		
		return parent1;
	}
	
	public static int[] shuffleArray(int[] ar)
	  {
	    Random rnd = new Random();
	    for (int i = ar.length - 1; i > 0; i--)
	    {
	      int index = rnd.nextInt(i + 1);
	      int a = ar[index];
	      ar[index] = ar[i];
	      ar[i] = a;
	    }
	    return ar;
	  }
	
	public static List<int[]> recombinePopulation(List<int[]> BestSelectionFromPopulation) {
		Random rnd = new Random();
		List<int[]> newPopulation = new ArrayList<int[]>();
		//Map<Integer, Boolean> usedIndexMap = new HashMap<>();
		
		while(newPopulation.size() < populationSize) {
			int parent1_index = rnd.nextInt(BestSelectionFromPopulation.size());
			int[] parent1 = BestSelectionFromPopulation.get(parent1_index);
		
			int parent2_index = rnd.nextInt(BestSelectionFromPopulation.size());
			int[] parent2 = BestSelectionFromPopulation.get(parent2_index);
			
			int parent3_index = rnd.nextInt(BestSelectionFromPopulation.size());
			int[] parent3 = BestSelectionFromPopulation.get(parent3_index);
		
			//Arrays.sort(parent1);
			//Arrays.sort(parent2);
		
			/*
			 * while(Arrays.equals(parent1, parent2)) {
				parent2 = BestSelectionFromPopulation.get(
						rnd.nextInt(BestSelectionFromPopulation.size()));
				Arrays.sort(parent2);
			}
			*/
			
			int[] child = recombineTwo(parent1, parent2);
			newPopulation.add(recombineTwo(child, parent3));
			newPopulation.add(parent1);
			//newPopulation.add(parent2);
			
		}
		
		return newPopulation;
	}
	
	public static void main(String args[]) {
		GeneticAlgorithm ga = new GeneticAlgorithm(10000);
		
		generatePopulation();
		
		List<int[]> BestSelection = selectFromPopulation(population_RandomIntegers, 1, 500);
		
		for(int i = 0; i<50; i++) {
			List<int[]> newPopulation = recombinePopulation(BestSelection);
		
			BestSelection = selectFromPopulation(newPopulation, 1, 5000);
		}
	}
}
