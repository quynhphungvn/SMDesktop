package quynh.java.sm.english.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
}
