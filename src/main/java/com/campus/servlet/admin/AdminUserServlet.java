package com.campus.servlet.admin;

import com.campus.entity.User;
import com.campus.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/admin/users")
public class AdminUserServlet extends HttpServlet {

    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String keyword = req.getParameter("keyword");
        List<User> users = userService.findAll(keyword != null ? keyword : "");
        req.setAttribute("users", users);
        req.setAttribute("keyword", keyword);
        req.getRequestDispatcher("/jsp/admin/users.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        req.setCharacterEncoding("UTF-8");
        try {
            int id = Integer.parseInt(req.getParameter("id"));
            userService.toggleStatus(id);
            req.getSession().setAttribute("msg", "用户状态已更新");
        } catch (NumberFormatException e) {
            req.getSession().setAttribute("msg", "参数错误");
        }
        resp.sendRedirect(req.getContextPath() + "/admin/users");
    }
}
