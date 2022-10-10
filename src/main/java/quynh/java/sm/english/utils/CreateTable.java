package quynh.java.sm.english.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import quynh.java.sm.english.dao.DBUtils;

public class CreateTable {
	public void createAllTable() {
		Connection conn = null;
		try {
			conn = DBUtils.getMySQLConnection();
			String sqlIpaSymbol = "create table if not exists symbol("
					+ " id int not null auto_increment,"
					+ "	content varchar(10) not null,"
					+ "	example varchar(20),"
					+ "	example_phonetic varchar(20),"
					+ "	video_url varchar(100),"
					+ "	type varchar(20),"
					+ " primary key (id))";
			this.createTable(sqlIpaSymbol, conn);
			
			String sqlGroupVideo = "create table if not exists video_group("
					+ "id int not null auto_increment,"	
					+ "name varchar(100) not null,"
					+ "user_id int,"
					+ "unique (name, user_id),"
					+ "primary key (id))";
			this.createTable(sqlGroupVideo, conn);
			
			String sqlVideoLearning = "create table if not exists video("
					+ "id int not null auto_increment,"
					+ "title varchar(100) not null,"
					+ "url varchar(100) not null,"
					+ "subtitle text,"
					+ "group_id int not null,"
					+ "view_count int,"
					+ "unique (url, group_id),"
					+ "primary key (id))";
			this.createTable(sqlVideoLearning, conn);
		
			String sqlUser = "create table if not exists user("
					+ "id int not null auto_increment,"
					+ "username varchar(100) not null unique,"
					+ "password varchar(32) not null,"
					+ "primary key (id))";
			this.createTable(sqlUser, conn);
			
			String sqlWord = "create table if not exists word("
					+ "id int not null auto_increment,"
					+ "content varchar(25) not null unique,"
					+ "phonetic varchar(20),"
					+ "temp_state boolean,"
					+ "primary key (id))";
			this.createTable(sqlWord, conn);
			
			String sqlWordManage = "create table if not exists word_manage("
					+ "id int not null auto_increment,"
					+ "word_id int,"
					+ "user_id int,"
					+ "known_state boolean,"
					+ "appear_times int,"
					+ "unique (word_id, user_id),"
					+ "primary key (id))";
			this.createTable(sqlWordManage, conn);
			
			String sqlSymbolManage = "create table if not exists symbol_manage("
					+ "id int not null auto_increment,"					
					+ "symbol_id int,"
					+ "user_id int,"
					+ "view_count int,"
					+ "primary key (id))";
			this.createTable(sqlSymbolManage, conn);
			String sqlPhrase = "create table if not exists phrase("
					+ "id int not null auto_increment,"					
					+ "content nvarchar(255) not null unique,"
					+ "meaning text,"
					+ "type varchar(20),"
					+ "primary key (id))";
			this.createTable(sqlPhrase, conn);
			
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public int createTable(String sql, Connection conn) {
		int result = 0;
		try {
			PreparedStatement pstm = conn.prepareStatement(sql);
			result = pstm.executeUpdate();
			pstm.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	public static void main(String[] args) {
		System.out.println("Creating table!");
		CreateTable ct = new CreateTable();
		ct.createAllTable();
		System.out.println("Creating table end!");
	}
}
