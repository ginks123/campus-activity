package com.campus.servlet.student;

import com.campus.entity.Announcement;
import com.campus.service.AnnouncementService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/student/announcements")
public class AnnouncementServlet extends HttpServlet {

    private final AnnouncementService announcementService = new AnnouncementService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        List<Announcement> announcements = announcementService.findAll();
        req.setAttribute("announcements", announcements);
        req.getRequestDispatcher("/jsp/student/announcements.jsp").forward(req, resp);
    }
}
