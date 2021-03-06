package entity;

/**
 * Modella un Ticket presente su Jira
 */
public class JiraTicket {
	String key;
	String date;
	
	public JiraTicket (String key, String date) {
		this.key = key;
		this.date = date.substring(0,10);
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getMonth() {
		return date.substring(0, 7);
	}
}