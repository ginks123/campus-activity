package com.campus.dao;

import com.campus.entity.Category;
import com.campus.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryDao {

    public List<Category> findAll() {
        List<Category> list = new ArrayList<>();
        String sql = "SELECT * FROM activity_categories ORDER BY category_id";
        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Category c = new Category();
                c.setCategoryId(rs.getInt("category_id"));
                c.setCategoryName(rs.getString("category_name"));
                c.setCategoryDescription(rs.getString("category_description"));
                list.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean insert(String name, String description) {
        String sql = "INSERT INTO activity_categories (category_name, category_description) VALUES (?,?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, description);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            // MySQL 1062 = duplicate key
            if (e.getErrorCode() == 1062) return false;
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(int categoryId) {
        String sql = "DELETE FROM activity_categories WHERE category_id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, categoryId);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            // MySQL 1451 = foreign key constraint
            if (e.getErrorCode() == 1451) return false;
            e.printStackTrace();
            return false;
        }
    }
}
