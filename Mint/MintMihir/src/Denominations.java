import java.util.Arrays;
import java.util.Set;


public class Denominations {
	private int[] solution_list;
	private static Denominations instance = null;
	private Set<Integer> setDenominations;
	
	private Denominations() {
		solution_list = new int[7];
	}
	
	public static Denominations getDenominationsInstance() {
		if(instance == null) {
			instance = new Denominations();
		}
		return instance;
	}
	
	public int[] getSolutionList() {
		return solution_list;
	}
	
	public void modifyAll_SolutionList(int[] A) {
		try {
			for(int i = 0; i<solution_list.length; i++) {
				solution_list[i] = A[i];
			}
			
		}
		catch(Exception e) {
			System.out.println(e);
		}
	}
	
	
	public void printDemoninationList() {
		System.out.println("Denominations : ");
		for(int i : solution_list) {
			System.out.print(i + " ");
		}
		System.out.println();
	}
	
	public void modifyOne_SolutionList(int i, int value) {
		try {
			solution_list[i] = value;
		}
		
		catch(Exception e) {
			System.out.println(e);
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(solution_list);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Denominations other = (Denominations) obj;
		if (!Arrays.equals(solution_list, other.solution_list))
			return false;
		return true;
	}
	
	
}
