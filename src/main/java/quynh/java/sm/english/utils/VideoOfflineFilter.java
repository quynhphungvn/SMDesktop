package quynh.java.sm.english.utils;

import java.io.File;
import java.io.FilenameFilter;

public class VideoOfflineFilter {
	public static String[] filterGroups(String url) {
		File file = new File(url);
		String[] directories = file.list(new FilenameFilter() {
		  @Override
		  public boolean accept(File current, String name) {
		    return new File(current, name).isDirectory();
		  }
		});
		return directories;
	}
	public static String[] filterVideosInGroup(String url) {
		File file = new File(url);
		String[] files = file.list(new FilenameFilter() {
		  @Override
		  public boolean accept(File file, String name) {
		    if (new File(file, name).isFile() && !name.endsWith(".srt")) {
		    	return true;
		    } 
		    return false;
		  }
		});
		return files;
	}	
}
