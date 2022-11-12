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
import quynh.java.sm.english.model.SubtitleBlockTimer;

public class FileReaderSupport {
	public static String readStringFromFile(String filePath) {
		String s = null;
		try {
			s = Files.readString(Paths.get(filePath));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return s;
	}
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
