package com.campus.servlet.admin;

import com.campus.entity.Registration;
import com.campus.entity.User;
import com.campus.service.RegistrationService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/admin/audit")
public class AdminAuditServlet extends HttpServlet {

    private final RegistrationService registrationService = new RegistrationService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String filter = req.getParameter("filter");
        if (filter == null) filter = "pending";
        List<Registration> registrations = registrationService.findAll(filter);
        req.setAttribute("registrations", registrations);
        req.setAttribute("filter", filter);
        req.getRequestDispatcher("/jsp/admin/audit.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        req.setCharacterEncoding("UTF-8");
        User user = (User) req.getSession().getAttribute("user");
        String action = req.getParameter("action");
        try {
            int id = Integer.parseInt(req.getParameter("id"));
            String status = "approve".equals(action) ? "approved" : "rejected";
            registrationService.audit(id, status, user.getUserId());
            req.getSession().setAttribute("msg", "approve".equals(action) ? "已通过" : "已拒绝");
        } catch (NumberFormatException e) {
            req.getSession().setAttribute("msg", "参数错误");
        }
        resp.sendRedirect(req.getContextPath() + "/admin/audit");
    }
}
