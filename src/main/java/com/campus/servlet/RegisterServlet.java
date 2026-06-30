package com.campus.servlet;

import com.campus.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        resp.sendRedirect(req.getContextPath() + "/login");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String password2 = req.getParameter("password2");
        String contact = req.getParameter("contact");

        // 验证
        if (username == null || username.trim().isEmpty() ||
                password == null || password.trim().isEmpty()) {
            req.setAttribute("regError", "用户名和密码不能为空");
            req.getRequestDispatcher("/jsp/login.jsp").forward(req, resp);
            return;
        }
        if (!password.equals(password2)) {
            req.setAttribute("regError", "两次密码不一致");
            req.getRequestDispatcher("/jsp/login.jsp").forward(req, resp);
            return;
        }
        if (password.length() < 6) {
            req.setAttribute("regError", "密码不少于6位");
            req.getRequestDispatcher("/jsp/login.jsp").forward(req, resp);
            return;
        }

        String result = userService.register(username.trim(), password, contact != null ? contact.trim() : "");
        if (result == null) {
            req.setAttribute("regSuccess", "注册成功，请登录");
            req.getRequestDispatcher("/jsp/login.jsp").forward(req, resp);
        } else {
            req.setAttribute("regError", result);
            req.getRequestDispatcher("/jsp/login.jsp").forward(req, resp);
        }
    }
}
