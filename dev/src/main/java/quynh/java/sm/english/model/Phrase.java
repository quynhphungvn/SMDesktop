package quynh.java.sm.english.model;

import lombok.Data;

@Data
public class Phrase {
	private int id;
	private String content;
	private String meaning;
	private String type;


	public String toString() {
		return this.content;
	}
}