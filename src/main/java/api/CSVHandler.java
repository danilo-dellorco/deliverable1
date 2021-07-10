package api;

import java.io.FileWriter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import entity.JiraTicket;

public class CSVHandler {

	private CSVHandler() {}

	/**
	 * Salva su un file CSV i dati relativi al Process Control Chart
	 **/
	public static void writeControlChartOnCSV(List<JiraTicket> fixedBugs, List<String> months) {
		String outputName = "DAFFODIL_DATA.csv";

		try (FileWriter fileWriter = new FileWriter(outputName)) {
			StringBuilder outputBuilder = new StringBuilder("Month;NumBugFixed\n");

			for (String m : months) {
				int count = 0;
				for (JiraTicket t : fixedBugs) {
					if (t.getMonth().equals(m)) {
						count++;
					}
				}
				outputBuilder.append(m + ";" + count + "\n");
			}
			fileWriter.append(outputBuilder.toString());

		} catch (Exception e) {
			Logger logger = Logger.getLogger(CSVHandler.class.getName());
			logger.log(Level.SEVERE, "Report CSV Error", e);
		}
	}
}