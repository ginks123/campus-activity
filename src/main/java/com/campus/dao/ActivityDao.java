package com.campus.dao;

import com.campus.entity.Activity;
import com.campus.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ActivityDao {

    public List<Activity> findActivitiesPaginated(String keyword, String categoryId, String status, int offset, int limit) {
        List<Activity> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
            "SELECT a.*, c.category_name FROM activities a " +
            "LEFT JOIN activity_categories c ON a.category_id=c.category_id WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (keyword != null && !keyword.isEmpty()) {
            sql.append(" AND (a.activity_name LIKE ? OR a.organizer LIKE ? OR a.location LIKE ?)");
            String kw = "%" + keyword + "%";
            params.add(kw); params.add(kw); params.add(kw);
        }
        if (categoryId != null && !categoryId.isEmpty()) {
            sql.append(" AND a.category_id=?");
            params.add(Integer.parseInt(categoryId));
        }
        if (status != null && !status.isEmpty()) {
            sql.append(" AND a.status=?");
            params.add(status);
        }
        sql.append(" ORDER BY a.activity_time DESC LIMIT ?, ?");
        params.add(offset);
        params.add(limit);

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(rowToActivity(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public int countActivities(String keyword, String categoryId, String status) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM activities a WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (keyword != null && !keyword.isEmpty()) {
            sql.append(" AND (a.activity_name LIKE ? OR a.organizer LIKE ? OR a.location LIKE ?)");
            String kw = "%" + keyword + "%";
            params.add(kw); params.add(kw); params.add(kw);
        }
        if (categoryId != null && !categoryId.isEmpty()) {
            sql.append(" AND a.category_id=?");
            params.add(Integer.parseInt(categoryId));
        }
        if (status != null && !status.isEmpty()) {
            sql.append(" AND a.status=?");
            params.add(status);
        }

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<Activity> findActivities(String keyword, String categoryId, String status) {
        List<Activity> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
            "SELECT a.*, c.category_name FROM activities a " +
            "LEFT JOIN activity_categories c ON a.category_id=c.category_id WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (keyword != null && !keyword.isEmpty()) {
            sql.append(" AND (a.activity_name LIKE ? OR a.organizer LIKE ? OR a.location LIKE ?)");
            String kw = "%" + keyword + "%";
            params.add(kw); params.add(kw); params.add(kw);
        }
        if (categoryId != null && !categoryId.isEmpty()) {
            sql.append(" AND a.category_id=?");
            params.add(Integer.parseInt(categoryId));
        }
        if (status != null && !status.isEmpty()) {
            sql.append(" AND a.status=?");
            params.add(status);
        }
        sql.append(" ORDER BY a.activity_time DESC");

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(rowToActivity(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Activity findById(int activityId) {
        String sql = "SELECT a.*, c.category_name, u.username AS creator_name FROM activities a " +
                     "LEFT JOIN activity_categories c ON a.category_id=c.category_id " +
                     "LEFT JOIN users u ON a.created_by=u.user_id WHERE a.activity_id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, activityId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Activity a = rowToActivity(rs);
                    a.setCreatorName(rs.getString("creator_name"));
                    return a;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void insert(Activity a) {
        String sql = "INSERT INTO activities (activity_name,category_id,organizer,activity_time," +
                     "location,registration_start_time,registration_end_time,max_participants," +
                     "description,status,created_by) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, a.getActivityName());
            ps.setInt(2, a.getCategoryId());
            ps.setString(3, a.getOrganizer());
            ps.setString(4, a.getActivityTime());
            ps.setString(5, a.getLocation());
            ps.setString(6, a.getRegistrationStartTime());
            ps.setString(7, a.getRegistrationEndTime());
            ps.setInt(8, a.getMaxParticipants() != null ? a.getMaxParticipants() : 0);
            ps.setString(9, a.getDescription());
            ps.setString(10, a.getStatus());
            ps.setInt(11, a.getCreatedBy());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(Activity a) {
        String sql = "UPDATE activities SET activity_name=?,category_id=?,organizer=?," +
                     "activity_time=?,location=?,registration_start_time=?," +
                     "registration_end_time=?,max_participants=?,description=?,status=? " +
                     "WHERE activity_id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, a.getActivityName());
            ps.setInt(2, a.getCategoryId());
            ps.setString(3, a.getOrganizer());
            ps.setString(4, a.getActivityTime());
            ps.setString(5, a.getLocation());
            ps.setString(6, a.getRegistrationStartTime());
            ps.setString(7, a.getRegistrationEndTime());
            ps.setInt(8, a.getMaxParticipants() != null ? a.getMaxParticipants() : 0);
            ps.setString(9, a.getDescription());
            ps.setString(10, a.getStatus());
            ps.setInt(11, a.getActivityId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int activityId) {
        try (Connection conn = DBUtil.getConnection()) {
            conn.setAutoCommit(false);
            try {
                try (PreparedStatement ps = conn.prepareStatement(
                        "DELETE FROM registrations WHERE activity_id=?")) {
                    ps.setInt(1, activityId);
                    ps.executeUpdate();
                }
                try (PreparedStatement ps = conn.prepareStatement(
                        "DELETE FROM activities WHERE activity_id=?")) {
                    ps.setInt(1, activityId);
                    ps.executeUpdate();
                }
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int countAll() { return count("SELECT COUNT(*) FROM activities"); }
    public int countOpen() { return count("SELECT COUNT(*) FROM activities WHERE status='open'"); }

    private int count(String sql) {
        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public java.util.List<java.util.Map<String, Object>> getCategoryStats() {
        java.util.List<java.util.Map<String, Object>> list = new ArrayList<>();
        String sql = "SELECT c.category_name, COUNT(a.activity_id) AS cnt " +
                     "FROM activity_categories c LEFT JOIN activities a ON c.category_id=a.category_id " +
                     "GROUP BY c.category_id ORDER BY cnt DESC";
        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                java.util.Map<String, Object> m = new java.util.HashMap<>();
                m.put("name", rs.getString("category_name"));
                m.put("value", rs.getInt("cnt"));
                list.add(m);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    private Activity rowToActivity(ResultSet rs) throws SQLException {
        Activity a = new Activity();
        a.setActivityId(rs.getInt("activity_id"));
        a.setActivityName(rs.getString("activity_name"));
        a.setCategoryId(rs.getInt("category_id"));
        a.setOrganizer(rs.getString("organizer"));
        a.setActivityTime(rs.getString("activity_time"));
        a.setLocation(rs.getString("location"));
        a.setRegistrationStartTime(rs.getString("registration_start_time"));
        a.setRegistrationEndTime(rs.getString("registration_end_time"));
        a.setMaxParticipants(rs.getInt("max_participants"));
        a.setCurrentRegistrationCount(rs.getInt("current_registration_count"));
        a.setDescription(rs.getString("description"));
        a.setStatus(rs.getString("status"));
        a.setCreatedBy(rs.getInt("created_by"));
        a.setCategoryName(rs.getString("category_name"));
        return a;
    }
}
