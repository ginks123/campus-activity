package com.campus.servlet.student;

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

@WebServlet("/student/registrations")
public class MyRegistrationServlet extends HttpServlet {

    private final RegistrationService registrationService = new RegistrationService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        List<Registration> registrations = registrationService.findByUser(user.getUserId());
        req.setAttribute("registrations", registrations);
        req.getRequestDispatcher("/jsp/student/registrations.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        req.setCharacterEncoding("UTF-8");
        User user = (User) req.getSession().getAttribute("user");
        try {
            int activityId = Integer.parseInt(req.getParameter("id"));
            String result = registrationService.cancel(user.getUserId(), activityId);
            req.getSession().setAttribute("msg", result != null ? result : "已取消报名");
        } catch (NumberFormatException e) {
            req.getSession().setAttribute("msg", "参数错误");
        }
        resp.sendRedirect(req.getContextPath() + "/student/registrations");
    }
}
