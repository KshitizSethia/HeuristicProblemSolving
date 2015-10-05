package common;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Input {

	private static Input instance=null;
	public final List<double[]> locations;
	public final int[] cityIds;
	public final String outFile;
	

	private Input(String[] args) throws FileNotFoundException, IOException {
		List<Integer> cityIDList = new LinkedList<Integer>();
		locations = new ArrayList<double[]>();

		try (BufferedReader inFile = new BufferedReader(new FileReader(args[0]))) {
			String line;
			while ((line = inFile.readLine()) != null) {
				String[] vals = line.split("[ ]+");

				cityIDList.add(new Double(vals[0]).intValue());
				double[] location = new double[] { Double.parseDouble(vals[1]),
						Double.parseDouble(vals[2]),
						Double.parseDouble(vals[3]) };
				locations.add(location);
			}
		}

		cityIds = new int[cityIDList.size()];
		for (int index = 0; index < cityIds.length; index++) {
			cityIds[index] = cityIDList.get(index);
		}
		
		outFile = args[1];
	}

	public static void init(String[] args) throws FileNotFoundException, IOException{
		instance = new Input(args);
	}
	
	public static Input getInstance(){
		return instance;
	}
}
