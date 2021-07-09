package ticketinfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

public class RetrieveTicketsInfo {

   private static String readAll(Reader rd) throws IOException {
	   StringBuilder sb = new StringBuilder();
	   int cp;
	   while ((cp = rd.read()) != -1) {
		   sb.append((char) cp);
	   }
	   return sb.toString();
   }

   public static JSONArray readJsonArrayFromUrl(String url) throws IOException, JSONException {
      InputStream is = new URL(url).openStream();
      try {
    	  BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
          String jsonText = readAll(rd);
          JSONArray json = new JSONArray(jsonText);
          return json;
          }
      finally {
        	  is.close();
          }
   }
   
   public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
	      InputStream is = new URL(url).openStream();
	      try {
	         BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
	         String jsonText = readAll(rd);
	         JSONObject json = new JSONObject(jsonText);
	         return json;
	       } finally {
	         is.close();
	       }
	   }
   
   // Create a list of Ticket based on the Rest Query passed
   public static List<JiraTicket> getFixedBugs(String projName) throws JSONException, IOException {
	   Integer j = 0, i = 0, total = 1;
	   List<JiraTicket> fixedBugs = new ArrayList<JiraTicket>();
	   while (i<total) {
		   j = i + 1000;
		   
		   // Rest API Query to get all the Fixed Bugs
		   String url = "https://issues.apache.org/jira/rest/api/2/search?jql=project=%22"
		            + projName + "%22AND%22issueType%22=%22Bug%22AND(%22status%22=%22closed%22OR"
		            + "%22status%22=%22resolved%22)AND%22resolution%22=%22fixed%22&fields=key,resolutiondate,versions,created&startAt="
		            + i.toString() + "&maxResults=" + j.toString();
		   
		   // Get the Json Object
		   JSONObject json = readJsonFromUrl(url);
		   JSONArray issues = json.getJSONArray("issues");
		   total = json.getInt("total");
		   
		   // Create the Ticket Object from json and add it to the list
		   for (; i < total && i < j; i++) {
			   String date = issues.getJSONObject(i%1000).getJSONObject("fields").get("resolutiondate").toString().substring(0,7);
			   String key = issues.getJSONObject(i%1000).get("key").toString();
			   JiraTicket entry = new JiraTicket(key,date);
			   fixedBugs.add(entry);
		   }
	   }
	   
	   // Order the Tickets in the List comparing on the Resolution Date
	   Comparator<JiraTicket> ticketComparator = Comparator.comparing(JiraTicket::getDate);
	   fixedBugs.sort(ticketComparator);
	   return fixedBugs;
   }
  
   
   
   public static void main(String[] args) throws IOException, JSONException {
	   List<JiraTicket> fixedBugs = getFixedBugs("DAFFODIL");
	   List<String> months = new ArrayList<>();
	   
	   
	   // Conta quante volte un mese appare all'interno della lista dei bug fixati 
	   // in modo da vedere quanti bug sono stati fixati in quel mese
	   for (JiraTicket b:fixedBugs) {
		   if (!months.contains(b.getDate())){
			   months.add(b.getDate());
		   }
	   }
	   
	   for (String m:months) {
		   int count = 0;
		   for (JiraTicket b:fixedBugs) {
			   if (b.getDate().equals(m)) {
				   count++;
			   }
		   }
		   
		   // Print Month | #bug fixed in the month
		   String msg = String.format("%s | %d",m,count);
		   System.out.println(msg);
		   
		   // [DEBUG] Permette di copiare facilmente i dati all'interno della colonna Excel
		   // System.out.println(m);
		   // System.out.println(count)
		   
	   }
	}
}
