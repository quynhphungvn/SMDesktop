package quynh.java.sm.english.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

import quynh.java.sm.english.dao.DBUtils;
import quynh.java.sm.english.model.Phrase;

public class InitTable {
	Connection conn;
	public InitTable() {
		super();
		try {
			conn = DBUtils.getMySQLConnection();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public List<String> readFile(String filePath) {     
	    ArrayList<String> wordList = new ArrayList<String>(); 
	    try {
	        File myFile = new File(filePath);
	        Scanner myReader = new Scanner(myFile);
	        while (myReader.hasNextLine()) {
	          String[] data = myReader.nextLine().split(" ");
	          for (String s : data)
	          wordList.add(s);
	        }
	        myReader.close();
	      } catch (FileNotFoundException e) {
	        System.out.println("An error occurred.");
	        e.printStackTrace();
	      }
	    return wordList;
	}
	public int findIdByWordContent(String wordContent) {
		String sql = "select id from word where content=?;";
		PreparedStatement pstm = null;
		try {
			pstm = conn.prepareStatement(sql);
			pstm.setString(1, wordContent);
			ResultSet rs = pstm.executeQuery();
			if (rs.next()) 
				return rs.getInt(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			pstm.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
	}
	public void saveNewWord(String word, boolean tempState) {
		String sql = "insert into word (content, temp_state) values (?, ?)";
		try {
			PreparedStatement pstm = conn.prepareStatement(sql);
			pstm.setString(1, word);
			pstm.setBoolean(2, tempState);
			pstm.executeUpdate();
			System.out.println(word);
			pstm.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void saveWordManage(int wordId, int userId, boolean knownState, int appearTimes) {
		String sql = "insert into word_manage (word_id, user_id, known_state, appear_times) values (?, ?, ?, ?)";
		try {
			PreparedStatement pstm = conn.prepareStatement(sql);
			pstm.setInt(1, wordId);
			pstm.setInt(2, userId);
			pstm.setBoolean(3, knownState);
			pstm.setInt(4, appearTimes);
			pstm.executeUpdate();
			pstm.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
//	init user
	public void initUser(String username, String password) {
		System.out.println("Init user!");
		String sql = "insert into user (username, password) values (?,?);";
		PreparedStatement pstm = null;
		try {
			pstm = conn.prepareStatement(sql);
			pstm.setString(1, username);
			pstm.setString(2, password);
			pstm.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				pstm.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
// init english word
	public void initWord(List<String> wordList) {
		System.out.println("Start init word!");
		for (String word : wordList) {
			if (findIdByWordContent(word) == -1) {
				saveNewWord(word, false);
			} else {
				System.out.print(word + " existed!");
			}
		}
	}
	
// init user word known.
	public void initWordKnownForUser(int userId, List<String> wordList) {
		System.out.println("Start init wordknown for userId: " + userId);
		for (String word : wordList) {
			int tempWordId = findIdByWordContent(word);
			if (tempWordId == -1) 
			{
				saveNewWord(word, true);
				saveWordManage(findIdByWordContent(word), userId, true, 0);
			}
			else {
				saveWordManage(tempWordId, userId, true, 0);
			}
			System.out.println(word);
		}
	}
//init symbol table
	public void initSymbol(List<String> symbolInfoList) {
		System.out.println("Start init symbols!");
		String sql = "insert into symbol (content, example, example_phonetic, video_url, type) "
					+ "values (?,?,?,?,?)";
		PreparedStatement pstm = null;
		try {	
			pstm = conn.prepareStatement(sql);	
			for (String s: symbolInfoList) {
				String[] symbolParts = s.split(","); 
				pstm.setString(1, symbolParts[0].trim());
				pstm.setString(2, symbolParts[1].trim());
				pstm.setString(3, symbolParts[2].trim());
				pstm.setString(4, symbolParts[4].trim());
				pstm.setString(5, symbolParts[5].trim());
				pstm.executeUpdate();
			}	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				pstm.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	//init group of user id 1
	public void initVideoGroup(int userId) {
		PreparedStatement pstm = null;
		String groups[] = {"Avatar", "Pronunciation"};
		System.out.println("Init video group!");
			
		try {
			String sql = "insert into video_group (name, user_id) values (?,?);";
			pstm = conn.prepareStatement(sql);
			for (String groupName : groups) {
				pstm.setString(1, groupName);
				pstm.setInt(2, userId);
				pstm.executeUpdate();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				pstm.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("End init video!");
	}
	//init phrase
	public void initPhraseTable() {
		System.out.println("Init phrase!");
		
	}
	public static void main(String[] args) {
		InitTable it = new InitTable();
		String wordFilePath = "src/main/resources/engwords.txt";
		List<String> wordList = it.readFile(wordFilePath);
		String wordManageFilePath = "src/main/resources/wordknown.txt";
		List<String> wordManageList = it.readFile(wordManageFilePath);
		String symbolFilePath ="src/main/resources/ipa-symbol.txt";
		List<String> symbolInfoList = it.readFile(symbolFilePath);
		String phraseFilePath = "src/main/resources/phrase.txt";
		it.initPhrase();
		//it.initUser("quynh", "1");
		//it.initSymbol(symbolInfoList);
		//it.initWord(wordList);
		//it.initWordKnownForUser(1, wordManageList);
		//it.initVideoGroup(1);
	}
	private void initPhrase() {
		String filePath = "src/main/resources/phrases.txt";
		String filePath1500 = "src/main/resources/1500idioms.txt";
		String sql = "insert into phrase(content, meaning, type) values (?,?,?)";
		FileReaderSupport frs = new FileReaderSupport();
		List<Phrase> phraseList = frs.getListPhraseFromFile(filePath);
		List<Phrase> idiomList = frs.getList1500Idioms(filePath1500);
		List<Phrase> tempList = new ArrayList<Phrase>();
		for (Phrase pi : idiomList) {
			boolean flag = false;
			for (Phrase p : phraseList) {
				if (p.getContent().toLowerCase().equals(pi.getContent().toLowerCase()))
				{
					flag = true;
					break;
				}
			}
			if (!flag)
				tempList.add(pi);
		}
		phraseList.addAll(tempList);
		for (int i = 0; i < phraseList.size(); i++) {
			for (int j = i + 1; j < phraseList.size(); j ++) {
				if (phraseList.get(i).getContent().toLowerCase().equals(phraseList.get(j).getContent().toLowerCase())) {
					phraseList.remove(j);
					j--;
				}
			}
		}	
		try {
			PreparedStatement pstm = conn.prepareStatement(sql);
			for (Phrase phrase : phraseList) {
				System.out.println(phrase.getContent());
				pstm.setString(1, phrase.getContent());
				pstm.setString(2, phrase.getMeaning());
				pstm.setString(3, phrase.getType());
				pstm.executeUpdate();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		System.out.print("End");
	}
}
