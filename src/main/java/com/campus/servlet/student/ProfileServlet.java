package com.campus.servlet.student;

import com.campus.entity.User;
import com.campus.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/student/profile")
public class ProfileServlet extends HttpServlet {

    private final UserService userService = new UserService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        User user = (User) req.getSession().getAttribute("user");

        String contact = req.getParameter("contact");
        String newPassword = req.getParameter("newPassword");

        if (newPassword != null && !newPassword.isEmpty() && newPassword.length() < 6) {
            req.getSession().setAttribute("msg", "密码不少于6位");
            resp.sendRedirect(req.getContextPath() + "/student/activities");
            return;
        }

        userService.updateProfile(user.getUserId(), contact != null ? contact.trim() : "",
                newPassword != null && !newPassword.isEmpty() ? newPassword : null);
        // 同步更新 session 中的用户信息
        user.setContact(contact != null ? contact.trim() : "");
        req.getSession().setAttribute("msg", "个人信息已更新");
        resp.sendRedirect(req.getContextPath() + "/student/activities");
    }
}
