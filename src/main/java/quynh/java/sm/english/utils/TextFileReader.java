package quynh.java.sm.english.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TextFileReader {
	public static List<String> readFile(String filePath) {     
	    ArrayList<String> textLines = new ArrayList<String>(); 
	    try {
	        File myFile = new File(filePath);
	        Scanner myReader = new Scanner(myFile);
	        while (myReader.hasNextLine()) {
	        	textLines.add(myReader.nextLine());
	        }
	        myReader.close();
	      } catch (FileNotFoundException e) {
	        System.out.println("An error occurred.");
	        e.printStackTrace();
	      }
	    return textLines;
	}
}
