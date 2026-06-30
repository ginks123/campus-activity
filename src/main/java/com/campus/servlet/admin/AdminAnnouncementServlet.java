package com.campus.servlet.admin;

import com.campus.entity.Announcement;
import com.campus.entity.User;
import com.campus.service.AnnouncementService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/admin/announcements")
public class AdminAnnouncementServlet extends HttpServlet {

    private final AnnouncementService announcementService = new AnnouncementService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        List<Announcement> announcements = announcementService.findAll();
        req.setAttribute("announcements", announcements);
        req.getRequestDispatcher("/jsp/admin/announcements.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        req.setCharacterEncoding("UTF-8");
        User user = (User) req.getSession().getAttribute("user");
        String action = req.getParameter("action");

        if ("delete".equals(action)) {
            try {
                int id = Integer.parseInt(req.getParameter("id"));
                announcementService.delete(id);
                req.getSession().setAttribute("msg", "公告已删除");
            } catch (NumberFormatException e) {
                req.getSession().setAttribute("msg", "参数错误");
            }
        } else {
            String title = req.getParameter("title");
            String content = req.getParameter("content");
            if (title != null && !title.trim().isEmpty() && content != null && !content.trim().isEmpty()) {
                announcementService.add(title.trim(), content.trim(), user.getUserId());
                req.getSession().setAttribute("msg", "公告已发布");
            }
        }
        resp.sendRedirect(req.getContextPath() + "/admin/announcements");
    }
}
