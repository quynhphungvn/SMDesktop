package quynh.java.sm.english.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import quynh.java.sm.english.model.Phrase;
import quynh.java.sm.english.model.SubtitleTimer;

public class FileReaderSupport {
	public static List<String> readFileByLine(String filePath) {     
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
	public static List<SubtitleTimer> readSubtitleBlockFile(String filePath) {     
		List<SubtitleTimer> subs = new ArrayList<SubtitleTimer>(); 
	    String[] subContent  = null;
	    try {
	    	String temp = Files.readString(Paths.get(filePath));
	    	boolean windowCFLF = temp.contains("\r\n\r\n");
	    	if (windowCFLF)
	    		subContent = temp.split("\r\n\r\n");
	    	else
	    		subContent = temp.split("\n\n");	        
	        if (subContent.length > 0) {
	        	for (String subBlock : subContent)
	        	{
	        		if (!subBlock.isEmpty()) {
		        		String[] subBlockParts = null;
		        		if (windowCFLF)
		        			subBlockParts = subBlock.split("\r\n");
		        		else 
		        			subBlockParts = subBlock.split("\n");
		        		SubtitleTimer subBlockObj = new SubtitleTimer();
		        		subBlockObj.setTime(subBlockParts[1]);
		        		String tempContent = "";
		        		if (subBlockParts.length > 3) 
		        			for (int i = 2; i < subBlockParts.length; i++)
		        				tempContent = tempContent + subBlockParts[i].trim() + " ";
		        		else 
		        			tempContent = subBlockParts[2];
		        		System.out.println(tempContent);
		        		subBlockObj.setContent(tempContent);     
		        		subs.add(subBlockObj);
	        		}
	        	}	
	        }        	      
	      } catch (IOException e) {
	        System.out.println("An error occurred.");
	        e.printStackTrace();
	      }	
	    return subs;
	}
	public List<Phrase> getListPhraseFromFile(String filePath) {
		List<Phrase> phrases = new ArrayList<Phrase>();
		String fileContent;
		try {
			fileContent = Files.readString(Paths.get(filePath));
//			fileContent.replaceAll("/\\d\\d\\n/g", "");
			String[] phraseBlocks = fileContent.split("\n\n");
			for (String pb : phraseBlocks) {
				int indexLB = pb.indexOf("\n");
				String content = "";
				String meaning = "";
				if (indexLB < 0)
					content = pb;
				else 
				{
					content = pb.substring(0, indexLB -1);
					meaning= pb.substring(indexLB, pb.length() - 1).replaceAll("\n", " ");
				}
				Phrase phrase = new Phrase();
				phrase.setContent(content);
				phrase.setMeaning(meaning);
				phrases.add(phrase);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		return phrases;
	}
	public List<Phrase> getList1500Idioms(String filePath) {
		List<Phrase> phrases = new ArrayList<Phrase>();
		String fileContent;
			try {
				fileContent = Files.readString(Paths.get(filePath));
				String idiomBlocks[] = fileContent.split("\n");
				for (String ib : idiomBlocks) {
					String[] iParts = ib.split(":");
					Phrase phrase = new Phrase();
					phrase.setContent(iParts[0]);
					phrase.setMeaning(iParts[1]);
					phrase.setType("idiom");
					phrases.add(phrase);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return phrases;
	}
	public static void main(String[] args) {
		String filePath = "src/main/resources/phrases.txt";
		String filePath1500 = "src/main/resources/1500idioms.txt";
		FileReaderSupport frs = new FileReaderSupport();
//		List<Phrase> lp = frs.getListPhraseFromFile(filePath);
		List<Phrase> lp = frs.getList1500Idioms(filePath1500);
		System.out.println(lp.size());
		for (Phrase p : lp) 
			System.out.println(p.getContent() + " : " + p.getMeaning());
	}
}
