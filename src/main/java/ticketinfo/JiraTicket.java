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
	
	public void print() {
		String ticket = String.format("(%s,%s)", this.key,this.date);
		System.out.println(ticket);
	}
}