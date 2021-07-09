package ticketinfo;

//Class to model a Jira Ticket as (ID,ResolutionDate)
public class JiraTicket {
	String key;
	String date;
	public JiraTicket (String key, String date) {
		this.key = key;
		this.date = date;
	}
	
	public String getDate() {
		return this.date;
	}
}