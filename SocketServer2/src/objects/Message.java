package objects;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Message  implements Serializable {
	private static final long serialVersionUID = 863053837519833472L;
	private String author;
	private String text;
	private Date datum = new Date();
	

	public String getAuthor() {
		return author;
	}
	
	public void setAuthor(String author) {
		this.author = author;
	}
	
	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public Date getDatum() {
		return datum;
	}
	
	public void setDatum(Date datum) {
		this.datum = datum;
	}	
	@Override
	public String toString() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		return dateFormat.format(datum) + " - " + author + ": "  + text;
	}
}
