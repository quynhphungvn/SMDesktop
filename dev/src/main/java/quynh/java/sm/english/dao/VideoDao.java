package quynh.java.sm.english.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import quynh.java.sm.english.model.Phrase;
import quynh.java.sm.english.model.VideoGroup;

public class VideoDao {
	private Connection conn;
	public VideoDao() {
		try {
			conn = DBUtils.getMySQLConnection();
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public List<VideoGroup> findAllGroup(int userId) {
		List<VideoGroup> listVideoGroup = new ArrayList<VideoGroup>();
		String sql = "select id, name from video_group where user_id=?;";
		PreparedStatement pstm;
		try {
			pstm = conn.prepareStatement(sql);
			pstm.setInt(1, userId);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				VideoGroup group = new VideoGroup();
				group.setId(rs.getInt(1));
				group.setName(rs.getString(2));
				listVideoGroup.add(group);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return listVideoGroup;
	}
	public String findWordsKnown(int userId) {
		String sql = "select word.content from word, word_manage where user_id=? and word_id = word.id and known_state=true;";
		String result = " ";
		PreparedStatement pstm;
		try {
			pstm = conn.prepareStatement(sql);
			pstm.setInt(1, userId);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				result = result + rs.getString(1) + " ";
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	public void addWordToKnown(String word) {
		int wordId = findWordIdByContent(word);
		if (wordId == 0)
		{
			saveNewWord(word);
			wordId = findWordIdByContent(word);
			saveNewWordManage(1, wordId, true);
		} else {
			if (findWordIdInWordManage(wordId)) {
				updateKnownStateWord(1, wordId, true);
			} else {
				saveNewWordManage(1, wordId, true);
			}
		}
	}
	private void saveNewWordManage(int userId, int wordId, boolean knownState) {
		// TODO Auto-generated method stub
		String sql = "insert into word_manage(user_id, word_id, known_state) values(?,?,?);";
		PreparedStatement pstm;
		try {
			pstm = conn.prepareStatement(sql);
			pstm.setInt(1, userId);
			pstm.setInt(2, wordId);
			pstm.setBoolean(3, knownState);
			pstm.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	private void saveNewWord(String word) {
		String sql = "insert into word(content, temp_state) values(?,?);";
		PreparedStatement pstm;
		try {
			pstm = conn.prepareStatement(sql);
			pstm.setString(1, word);
			pstm.setBoolean(2, true);
			pstm.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public boolean findWordIdInWordManage(int wordId) {
		String sql = "select id from word_manage where word_manage.word_id=?;";
		boolean result = false;
		PreparedStatement pstm;
		try {
			pstm = conn.prepareStatement(sql);
			pstm.setInt(1, wordId);
			ResultSet rs = pstm.executeQuery();
			if (rs.next())
				result = true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	public int findWordIdByContent(String word) {
		String sql = "select id from word where word.content=?;";
		int wordId = 0;
		PreparedStatement pstm;
		try {
			pstm = conn.prepareStatement(sql);
			pstm.setString(1, word);
			ResultSet rs = pstm.executeQuery();
			if (rs.next())
				wordId = rs.getInt(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return wordId;
	}
	public void updateKnownStateWord(int userId, int wordId, boolean knownState) {
		String sql = "update word_manage set known_state=? where user_id=? and word_id=?;";
		PreparedStatement pstm;
		try {
			pstm = conn.prepareStatement(sql);
			pstm.setBoolean(1, knownState);
			pstm.setInt(2, userId);
			pstm.setInt(3, wordId);
			pstm.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void updateWordToUnknown(String word) {
		int wordId = findWordIdByContent(word);
		String sql = "update word_manage set known_state=? where user_id=? and word_id=?;";
		PreparedStatement pstm;
		try {
			pstm = conn.prepareStatement(sql);
			pstm.setBoolean(1, false);
			pstm.setInt(2, 1);
			pstm.setInt(3, wordId);
			pstm.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public List<Phrase> findPhraseByWord(String word) {
		List<Phrase> listPhrase = new ArrayList<Phrase>();
		String sql = "select id, content, meaning, type from phrase where content like ?;";
		PreparedStatement pstm;
		try {
			pstm = conn.prepareStatement(sql);
			pstm.setString(1, "%" + word + "%");
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				Phrase p = new Phrase();
				p.setId(rs.getInt(1));
				p.setContent(rs.getString(2));
				p.setMeaning(rs.getString(3));
				p.setType(rs.getString(4));
				listPhrase.add(p);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return listPhrase;
	}
	public String findMeaningOfPhrase(int phraseId) {
		String sql = "select meaning from phrase where id=?;";
		String meaning = "";
		PreparedStatement pstm;
		try {
			pstm = conn.prepareStatement(sql);
			pstm.setInt(1, phraseId);
			ResultSet rs = pstm.executeQuery();
			if (rs.next())
				meaning = rs.getString(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return meaning;
	}
}
