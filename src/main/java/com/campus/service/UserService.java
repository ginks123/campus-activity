package com.campus.service;

import com.campus.dao.UserDao;
import com.campus.entity.User;

import java.util.List;

public class UserService {

    private final UserDao userDao = new UserDao();

    public User login(String username, String password) {
        return userDao.login(username, password);
    }

    public String register(String username, String password, String contact) {
        return userDao.register(username, password, contact);
    }

    public void updateProfile(int userId, String contact, String newPassword) {
        userDao.updateProfile(userId, contact, newPassword);
    }

    public List<User> findAll(String keyword) {
        return userDao.findAll(keyword);
    }

    public void toggleStatus(int userId) {
        userDao.toggleStatus(userId);
    }

    public int countStudents() {
        return userDao.countStudents();
    }
}
