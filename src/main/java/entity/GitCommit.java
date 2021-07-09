package entity;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.eclipse.jgit.lib.ObjectId;
/**
 * Modella un commit Git
 */
public class GitCommit {
	private ObjectId id;
	private String date;
	private String message;
	private final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	
	public GitCommit(ObjectId id, Date date, String message) {
		this.id = id;
		this.date = formatter.format(date);
		this.message = message;
	}
	
	public ObjectId getId() {
		return id;
	}
	public String getDate() {
		return date;
	}
	public String getComment() {
		return message;
	}
}
