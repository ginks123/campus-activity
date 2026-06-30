package com.campus.dao;

import com.campus.entity.User;
import com.campus.util.DBUtil;
import com.campus.util.MD5Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDao {

    public User login(String username, String password) {
        String sql = "SELECT * FROM users WHERE username=? AND password=? AND status=1";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, MD5Util.md5(password));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rowToUser(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String register(String username, String password, String contact) {
        try (Connection conn = DBUtil.getConnection()) {
            // 检查用户名
            String checkSql = "SELECT user_id FROM users WHERE username=?";
            try (PreparedStatement ps = conn.prepareStatement(checkSql)) {
                ps.setString(1, username);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return "用户名已存在";
                    }
                }
            }
            String sql = "INSERT INTO users (username, password, role, contact) VALUES (?,?,?,?)";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, username);
                ps.setString(2, MD5Util.md5(password));
                ps.setString(3, "student");
                ps.setString(4, contact);
                ps.executeUpdate();
            }
            return null; // 成功
        } catch (SQLException e) {
            e.printStackTrace();
            return "系统错误，请重试";
        }
    }

    public void updateProfile(int userId, String contact, String newPassword) {
        String sql1 = "UPDATE users SET contact=? WHERE user_id=?";
        String sql2 = "UPDATE users SET password=? WHERE user_id=?";
        try (Connection conn = DBUtil.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(sql1)) {
                ps.setString(1, contact);
                ps.setInt(2, userId);
                ps.executeUpdate();
            }
            if (newPassword != null && !newPassword.isEmpty()) {
                try (PreparedStatement ps = conn.prepareStatement(sql2)) {
                    ps.setString(1, MD5Util.md5(newPassword));
                    ps.setInt(2, userId);
                    ps.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<User> findAll(String keyword) {
        List<User> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM users WHERE 1=1");
        List<Object> params = new ArrayList<>();
        if (keyword != null && !keyword.isEmpty()) {
            sql.append(" AND username LIKE ?");
            params.add("%" + keyword + "%");
        }
        sql.append(" ORDER BY role, user_id");
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(rowToUser(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void toggleStatus(int userId) {
        try (Connection conn = DBUtil.getConnection()) {
            String sel = "SELECT role, status FROM users WHERE user_id=?";
            try (PreparedStatement ps = conn.prepareStatement(sel)) {
                ps.setInt(1, userId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next() && !"admin".equals(rs.getString("role"))) {
                        int newStatus = rs.getInt("status") == 1 ? 0 : 1;
                        String upd = "UPDATE users SET status=? WHERE user_id=?";
                        try (PreparedStatement u = conn.prepareStatement(upd)) {
                            u.setInt(1, newStatus);
                            u.setInt(2, userId);
                            u.executeUpdate();
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 统计方法从 AdminDao 移到这里
    public int countStudents() {
        return countByCondition("SELECT COUNT(*) FROM users WHERE role='student' AND status=1");
    }

    private int countByCondition(String sql) {
        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private User rowToUser(ResultSet rs) throws SQLException {
        User u = new User();
        u.setUserId(rs.getInt("user_id"));
        u.setUsername(rs.getString("username"));
        u.setPassword(rs.getString("password"));
        u.setRole(rs.getString("role"));
        u.setContact(rs.getString("contact"));
        u.setStatus(rs.getInt("status"));
        u.setRegistrationTime(rs.getString("registration_time"));
        return u;
    }
}
