package logic;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONException;
import org.json.JSONObject;
import api.CSVHandler;
import api.GitAPI;
import entity.GitCommit;
import entity.JiraTicket;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.json.JSONArray;
import api.JsonHandler;

public class AnalyzeProject {
	private static final String JIRA_PROECT_NAME = "DAFFODIL";
	private static final String GIT_PROJECT_NAME = "daffodil";
	private static Logger logger = Logger.getLogger(AnalyzeProject.class.getName());
	
	/**
	 * Ottiene la lista dei JiraTicket di tipo FixBug relativi al progetto considerato
	 */
	public static List<JiraTicket> getFixedBugs(String projName) throws JSONException, IOException {
		Integer j = 0;
		Integer i = 0;
		Integer total = 1;
		System.out.println("PROVA");
		List<JiraTicket> fixedBugs = new ArrayList<>();
		while (i < total) {
			j = i + 1000;

			// Query Rest per ottenere tutti i ticket relativi al progetto considerato
			String url = "https://issues.apache.org/jira/rest/api/2/search?jql=project=%22" + projName
					+ "%22AND%22issueType%22=%22Bug%22AND(%22status%22=%22closed%22OR"
					+ "%22status%22=%22resolved%22)AND%22resolution%22=%22fixed%22&fields=key,resolutiondate,versions,created&startAt="
					+ i.toString() + "&maxResults=" + j.toString();

			// Ottengo il JSON relativo al ticket
			JSONObject json = JsonHandler.readJsonFromUrl(url);
			JSONArray issues = json.getJSONArray("issues");
			total = json.getInt("total");

			// Creo l'oggetto JiraTicket partendo dai dati del JSON
			for (; i < total && i < j; i++) {
				String date = issues.getJSONObject(i % 1000).getJSONObject("fields").get("resolutiondate").toString();
				String key = issues.getJSONObject(i % 1000).get("key").toString();
				JiraTicket entry = new JiraTicket(key, date);
				fixedBugs.add(entry);
			}
		}
		return fixedBugs;
	}
	
	
	/**
	 * Mantiene soltanto i ticket che hanno un commit git associato. Risolve inoltre eventuali incoerenze
	 * tra la data riportata su Git e su Jira
	 */
	public static void bindJiraToGit(List<JiraTicket> tickets) throws IOException, GitAPIException {
		GitAPI git = new GitAPI(GIT_PROJECT_NAME, "output/");
		git.init();
		List<GitCommit> commits = git.getCommits();
		int notConsistent = 0; 
		for (GitCommit c : commits) {
			for (JiraTicket t : tickets) {
				if (c.getComment().contains(t.getKey()) && (t.getDate().compareTo(c.getDate()) < 0)) {
					t.setDate(c.getDate());
					notConsistent++;
				}
			}
		}
		logger.log(Level.INFO, "Fix Bug Tickets Corrected: {0}", notConsistent);
	}

	
	/**
	 * Ordina la lista dei ticket Jira in ordine cronologico
	 */
	public static void orderJiraTicket(List<JiraTicket> list) {
			list.sort(Comparator.comparing(JiraTicket::getDate));
	}
	
	public static void main(String[] args) throws IOException, JSONException, GitAPIException {
		List<JiraTicket> tickets = getFixedBugs(JIRA_PROECT_NAME);
		logger.log(Level.INFO, "Fix Bug Tickets Found: {0}", tickets.size());
		bindJiraToGit(tickets);
		logger.log(Level.INFO, "Fix Bug Tickets Found: {0}", tickets.size());
		orderJiraTicket(tickets);

		List<String> months = new ArrayList<>();
		// Conta quante volte un mese appare all'interno della lista dei bug fixati
		// in modo da vedere quanti bug sono stati fixati in quel mese
		for (JiraTicket t : tickets) {
			if (!months.contains(t.getMonth())) {
				months.add(t.getMonth());
			}
		}
		CSVHandler.writeControlChartOnCSV(tickets,months);
	}
}