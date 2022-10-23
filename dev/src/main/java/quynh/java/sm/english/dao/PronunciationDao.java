package quynh.java.sm.english.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import quynh.java.sm.english.model.Symbol;
import quynh.java.sm.english.model.SymbolInfo;

public class PronunciationDao {
	Connection conn;

	public PronunciationDao() {
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

	public SymbolInfo findSymbolById(int symbolId, int userId) {
		SymbolInfo symbol = null;
		PreparedStatement pstm = null;
		try {
			String sql = "select content, example, example_phonetic, video_url, type from symbol "
					+ "where symbol.id=?; ";
			pstm = conn.prepareStatement(sql);
			pstm.setInt(1, symbolId);
			ResultSet rs = pstm.executeQuery();
			if (rs.next()) {
				symbol = new SymbolInfo();
				symbol.setContent(rs.getString(1));
				symbol.setExample(rs.getString(2));
				symbol.setExamplePhonetic(rs.getString(3));		
				symbol.setVideoUrl(rs.getString(4));
				symbol.setType(rs.getString(5));
				int viewCount = this.getViewCount(symbolId, userId);
				if (viewCount == 0) {
					this.insertSymbolManage(symbolId, userId);
					symbol.setViewCount(1);
				}
				else
				{
					this.updateSymbolViewCount(symbolId, userId);
					symbol.setViewCount(viewCount + 1);
				}
			}
			pstm.close();
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
		return symbol;
	}
	private void insertSymbolManage(int symbolId, int userId) {
		String sqlNewSymbolManage = "insert into symbol_manage (symbol_id, user_id, view_count) values (?,?,?);";
		PreparedStatement pstm;
		try {
			pstm = conn.prepareStatement(sqlNewSymbolManage);
			pstm.setInt(1, symbolId);
			pstm.setInt(2, userId);
			pstm.setInt(3, 1);
			pstm.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private int getViewCount(int symbolId, int userId) {
		int viewCount = 0;
		String sql = "select view_count from symbol_manage "
				+ "where symbol_id=? and user_id=?;";
		PreparedStatement pstm;
		try {
			pstm = conn.prepareStatement(sql);
			pstm.setInt(1, symbolId);
			pstm.setInt(2, userId);
			ResultSet rsViewCount = pstm.executeQuery();
			if (rsViewCount.next()) {
				viewCount = rsViewCount.getInt(1);
			} 
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		return viewCount;
	}
	public List<Symbol> findAllSymbol() {
		List<Symbol> listSymbol = new ArrayList<Symbol>();
		Statement stm = null;
		String sql = "select id, content, type from symbol;";
		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery(sql);
			while (rs.next()) {
				Symbol symbol = new Symbol();
				symbol.setId(rs.getInt(1));
				symbol.setContent(rs.getString(2));
				symbol.setType(rs.getString(3));
				listSymbol.add(symbol);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return listSymbol;
	}

	public void updateSymbolViewCount(int symbolId, int userId) {
		String sql = "update symbol_manage set view_count=view_count + 1 " + "where symbol_id=? " + "and user_id=?";
		PreparedStatement pstm;
		try {
			pstm = conn.prepareStatement(sql);
			pstm.setInt(1, symbolId);
			pstm.setInt(2, userId);
			pstm.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
