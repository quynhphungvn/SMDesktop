package quynh.java.sm.english.model;

import java.time.LocalTime;
import java.util.List;

import lombok.Data;

@Data
public class Video {
	private String rootPath;
	private String groupName;
	private String fullName;
	private String title;
	private List<BlockSub> subtitles;
	private List<BlockNote> subNotes;
	
	@Data
	public class BlockSub {
		private int index;
		private LocalTime timeStart;
		private LocalTime timeEnd;
		private String content;
	}
		
	@Data
	public class BlockNote {
		private int index;
		private String content;
	}
}
