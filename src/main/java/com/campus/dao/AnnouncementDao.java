package com.campus.dao;

import com.campus.entity.Announcement;
import com.campus.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AnnouncementDao {

    public List<Announcement> findAll() {
        List<Announcement> list = new ArrayList<>();
        String sql = "SELECT a.*, u.username AS pub_name FROM announcements a " +
                     "LEFT JOIN users u ON a.publisher=u.user_id ORDER BY a.publish_time DESC";
        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Announcement a = new Announcement();
                a.setAnnouncementId(rs.getInt("announcement_id"));
                a.setTitle(rs.getString("title"));
                a.setContent(rs.getString("content"));
                a.setPublisher(rs.getInt("publisher"));
                a.setPublishTime(rs.getString("publish_time"));
                a.setPubName(rs.getString("pub_name"));
                list.add(a);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void insert(String title, String content, int publisher) {
        String sql = "INSERT INTO announcements (title, content, publisher) VALUES (?,?,?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, title);
            ps.setString(2, content);
            ps.setInt(3, publisher);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int announcementId) {
        String sql = "DELETE FROM announcements WHERE announcement_id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, announcementId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int countAll() {
        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM announcements")) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
