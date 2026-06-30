package com.campus.servlet.admin;

import com.campus.service.ActivityService;
import com.campus.service.AnnouncementService;
import com.campus.service.RegistrationService;
import com.campus.service.UserService;
import com.campus.dao.ActivityDao;
import com.campus.dao.RegistrationDao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet("/admin/dashboard")
public class DashboardServlet extends HttpServlet {

    private final UserService userService = new UserService();
    private final ActivityService activityService = new ActivityService();
    private final RegistrationService registrationService = new RegistrationService();
    private final AnnouncementService announcementService = new AnnouncementService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setAttribute("studentCount", userService.countStudents());
        req.setAttribute("activityCount", activityService.countAll());
        req.setAttribute("openCount", activityService.countOpen());
        req.setAttribute("pendingCount", registrationService.countByStatus("pending"));
        req.setAttribute("totalRegCount", registrationService.countAll());
        req.setAttribute("announcementCount", announcementService.countAll());

        // 图表数据：按分类统计活动数量
        ActivityDao activityDao = new ActivityDao();
        List<Map<String, Object>> categoryStats = activityDao.getCategoryStats();
        req.setAttribute("categoryStats", categoryStats);

        // 图表数据：按月份统计报名趋势（最近6个月）
        RegistrationDao registrationDao = new RegistrationDao();
        List<Map<String, Object>> monthlyRegStats = registrationDao.getMonthlyRegistrationStats();
        req.setAttribute("monthlyRegStats", monthlyRegStats);

        req.getRequestDispatcher("/jsp/admin/dashboard.jsp").forward(req, resp);
    }
}
