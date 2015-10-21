import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class InputReader {

	enum ReadState {
		patients, hospitals
	}

	public static InitialState read(String pathOfFile) throws FileNotFoundException {
		Scanner scanner = new Scanner(new File(pathOfFile));

		List<Patient> patients = new ArrayList<Patient>();
		List<Integer> initialAmbulances = new ArrayList<Integer>();

		ReadState state = null;
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			if (line.startsWith("person")) {
				state = ReadState.patients;
			} else if (line.startsWith("hospital")) {
				state = ReadState.hospitals;
			} else if (line.length() != 0) {
				switch (state) {
				case patients:
					patients.add(new Patient(line));
					break;
				case hospitals:
					initialAmbulances.add(Integer.parseInt(line));
					break;
				}
			}
		}

		scanner.close();

		return new InitialState(patients, initialAmbulances);
	}
}
