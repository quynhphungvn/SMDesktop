package quynh.java.sm.english.model;

import lombok.Data;

@Data
public class SymbolInfo extends Symbol{
	private int userId;
	private String username;
	private int viewCount;
}
