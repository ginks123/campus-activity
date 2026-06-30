package com.campus.servlet;

import com.campus.entity.User;
import com.campus.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private final UserService userService = new UserService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        User user = null;
        String dbError = null;
        try {
            user = userService.login(username, password);
        } catch (Exception e) {
            dbError = e.getClass().getSimpleName() + ": " + e.getMessage();
            e.printStackTrace();
        }
        if (user != null) {
            HttpSession session = req.getSession();
            session.setAttribute("user", user);
            if ("admin".equals(user.getRole())) {
                resp.sendRedirect(req.getContextPath() + "/admin/dashboard");
            } else {
                resp.sendRedirect(req.getContextPath() + "/student/activities");
            }
        } else {
            String msg = "用户名或密码错误";
            if (dbError != null) {
                msg += " [DB: " + dbError + "]";
            }
            req.setAttribute("error", msg);
            req.setAttribute("username", username);
            req.getRequestDispatcher("/jsp/login.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/jsp/login.jsp").forward(req, resp);
    }
}
