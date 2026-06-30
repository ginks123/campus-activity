package com.campus.dao;

import com.campus.entity.ActivityStats;
import com.campus.entity.Registration;
import com.campus.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RegistrationDao {

    public Registration findByUserAndActivity(int userId, int activityId) {
        String sql = "SELECT * FROM registrations WHERE user_id=? AND activity_id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, activityId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rowToRegistration(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @return null=成功, 否则返回错误消息
     */
    public String register(int userId, int activityId) {
        try (Connection conn = DBUtil.getConnection()) {
            conn.setAutoCommit(false);
            try {
                // 检查活动
                String actSql = "SELECT * FROM activities WHERE activity_id=?";
                try (PreparedStatement ps = conn.prepareStatement(actSql)) {
                    ps.setInt(1, activityId);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next() || !"open".equals(rs.getString("status"))) {
                            conn.rollback();
                            return "活动不可报名";
                        }
                        int max = rs.getInt("max_participants");
                        int cur = rs.getInt("current_registration_count");
                        if (max > 0 && cur >= max) {
                            conn.rollback();
                            return "名额已满";
                        }
                    }
                }
                // 检查重复
                String chkSql = "SELECT * FROM registrations WHERE user_id=? AND activity_id=?";
                try (PreparedStatement ps = conn.prepareStatement(chkSql)) {
                    ps.setInt(1, userId);
                    ps.setInt(2, activityId);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            conn.rollback();
                            return "已报名";
                        }
                    }
                }
                // 插入报名
                String insSql = "INSERT INTO registrations (user_id, activity_id) VALUES (?,?)";
                try (PreparedStatement ps = conn.prepareStatement(insSql)) {
                    ps.setInt(1, userId);
                    ps.setInt(2, activityId);
                    ps.executeUpdate();
                }
                // 更新计数
                String updSql = "UPDATE activities SET current_registration_count=current_registration_count+1 WHERE activity_id=?";
                try (PreparedStatement ps = conn.prepareStatement(updSql)) {
                    ps.setInt(1, activityId);
                    ps.executeUpdate();
                }
                conn.commit();
                return null; // 成功
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "系统错误";
        }
    }

    /**
     * @return null=成功, 否则返回错误消息
     */
    public String cancel(int userId, int activityId) {
        try (Connection conn = DBUtil.getConnection()) {
            conn.setAutoCommit(false);
            try {
                String selSql = "SELECT * FROM registrations WHERE user_id=? AND activity_id=?";
                try (PreparedStatement ps = conn.prepareStatement(selSql)) {
                    ps.setInt(1, userId);
                    ps.setInt(2, activityId);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) {
                            conn.rollback();
                            return "未报名";
                        }
                        if ("approved".equals(rs.getString("audit_status"))) {
                            conn.rollback();
                            return "已通过审核不可取消";
                        }
                    }
                }
                try (PreparedStatement ps = conn.prepareStatement(
                        "DELETE FROM registrations WHERE user_id=? AND activity_id=?")) {
                    ps.setInt(1, userId);
                    ps.setInt(2, activityId);
                    ps.executeUpdate();
                }
                try (PreparedStatement ps = conn.prepareStatement(
                        "UPDATE activities SET current_registration_count=current_registration_count-1 WHERE activity_id=?")) {
                    ps.setInt(1, activityId);
                    ps.executeUpdate();
                }
                conn.commit();
                return null;
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "系统错误";
        }
    }

    public List<Registration> findByUser(int userId) {
        List<Registration> list = new ArrayList<>();
        String sql = "SELECT r.*, a.activity_name, a.activity_time, a.location, c.category_name " +
                     "FROM registrations r JOIN activities a ON r.activity_id=a.activity_id " +
                     "LEFT JOIN activity_categories c ON a.category_id=c.category_id " +
                     "WHERE r.user_id=? ORDER BY r.registration_time DESC";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Registration r = rowToRegistration(rs);
                    r.setActivityName(rs.getString("activity_name"));
                    r.setActivityTime(rs.getString("activity_time"));
                    r.setLocation(rs.getString("location"));
                    r.setCategoryName(rs.getString("category_name"));
                    list.add(r);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Registration> findAll(String auditStatus) {
        List<Registration> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
            "SELECT r.*, u.username, a.activity_name FROM registrations r " +
            "JOIN users u ON r.user_id=u.user_id " +
            "JOIN activities a ON r.activity_id=a.activity_id ");
        if (auditStatus != null && !auditStatus.isEmpty()) {
            sql.append("WHERE r.audit_status=? ");
        }
        sql.append("ORDER BY r.registration_time DESC");
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            if (auditStatus != null && !auditStatus.isEmpty()) {
                ps.setString(1, auditStatus);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Registration r = rowToRegistration(rs);
                    r.setUsername(rs.getString("username"));
                    r.setActivityName(rs.getString("activity_name"));
                    list.add(r);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void audit(int registrationId, String status, int auditor) {
        String sql = "UPDATE registrations SET audit_status=?, audit_time=NOW(), auditor=? WHERE registration_id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, auditor);
            ps.setInt(3, registrationId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int countByStatus(String status) {
        String sql = "SELECT COUNT(*) FROM registrations WHERE audit_status=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int countAll() {
        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM registrations")) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<ActivityStats> getStatistics() {
        List<ActivityStats> list = new ArrayList<>();
        String sql = "SELECT a.activity_name, a.max_participants, " +
                     "COUNT(r.registration_id) AS total_reg, " +
                     "SUM(CASE WHEN r.audit_status='approved' THEN 1 ELSE 0 END) AS approved, " +
                     "SUM(CASE WHEN r.audit_status='pending' THEN 1 ELSE 0 END) AS pending, " +
                     "SUM(CASE WHEN r.audit_status='rejected' THEN 1 ELSE 0 END) AS rejected " +
                     "FROM activities a LEFT JOIN registrations r ON a.activity_id=r.activity_id " +
                     "GROUP BY a.activity_id ORDER BY total_reg DESC";
        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                ActivityStats s = new ActivityStats();
                s.setActivityName(rs.getString("activity_name"));
                s.setMaxParticipants(rs.getInt("max_participants"));
                s.setTotalReg(rs.getInt("total_reg"));
                s.setApproved(rs.getInt("approved"));
                s.setPending(rs.getInt("pending"));
                s.setRejected(rs.getInt("rejected"));
                list.add(s);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public java.util.List<java.util.Map<String, Object>> getMonthlyRegistrationStats() {
        java.util.List<java.util.Map<String, Object>> list = new ArrayList<>();
        String sql = "SELECT DATE_FORMAT(registration_time, '%Y-%m') AS month, COUNT(*) AS cnt " +
                     "FROM registrations WHERE registration_time >= DATE_SUB(CURDATE(), INTERVAL 6 MONTH) " +
                     "GROUP BY month ORDER BY month";
        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                java.util.Map<String, Object> m = new java.util.HashMap<>();
                m.put("month", rs.getString("month"));
                m.put("count", rs.getInt("cnt"));
                list.add(m);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    private Registration rowToRegistration(ResultSet rs) throws SQLException {
        Registration r = new Registration();
        r.setRegistrationId(rs.getInt("registration_id"));
        r.setUserId(rs.getInt("user_id"));
        r.setActivityId(rs.getInt("activity_id"));
        r.setRegistrationTime(rs.getString("registration_time"));
        r.setAuditStatus(rs.getString("audit_status"));
        r.setAuditTime(rs.getString("audit_time"));
        r.setAuditor(rs.getObject("auditor") != null ? rs.getInt("auditor") : null);
        return r;
    }
}
