package com.campus.service;

import com.campus.dao.CategoryDao;
import com.campus.entity.Category;

import java.util.List;

public class CategoryService {

    private final CategoryDao categoryDao = new CategoryDao();

    public List<Category> findAll() {
        return categoryDao.findAll();
    }

    public boolean insert(String name, String description) {
        return categoryDao.insert(name, description);
    }

    public boolean delete(int categoryId) {
        return categoryDao.delete(categoryId);
    }
}
