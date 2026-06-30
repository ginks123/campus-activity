package com.campus.servlet.admin;

import com.campus.entity.Activity;
import com.campus.entity.Category;
import com.campus.entity.User;
import com.campus.service.ActivityService;
import com.campus.service.CategoryService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/admin/activities")
public class AdminActivityServlet extends HttpServlet {

    private final ActivityService activityService = new ActivityService();
    private final CategoryService categoryService = new CategoryService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");

        if ("edit".equals(action)) {
            try {
                int id = Integer.parseInt(req.getParameter("id"));
                Activity act = activityService.findById(id);
                List<Category> categories = categoryService.findAll();
                req.setAttribute("activity", act);
                req.setAttribute("categories", categories);
                req.getRequestDispatcher("/jsp/admin/activity_form.jsp").forward(req, resp);
            } catch (NumberFormatException e) {
                resp.sendRedirect(req.getContextPath() + "/admin/activities");
            }
        } else if ("add".equals(action)) {
            List<Category> categories = categoryService.findAll();
            req.setAttribute("categories", categories);
            req.getRequestDispatcher("/jsp/admin/activity_form.jsp").forward(req, resp);
        } else {
            String keyword = req.getParameter("keyword");
            String categoryId = req.getParameter("categoryId");
            List<Activity> activities = activityService.findActivities(keyword, categoryId, "");
            List<Category> categories = categoryService.findAll();
            req.setAttribute("activities", activities);
            req.setAttribute("categories", categories);
            req.setAttribute("keyword", keyword);
            req.setAttribute("categoryId", categoryId);
            req.getRequestDispatcher("/jsp/admin/activities.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");

        if ("delete".equals(action)) {
            try {
                int id = Integer.parseInt(req.getParameter("id"));
                activityService.delete(id);
                req.getSession().setAttribute("msg", "活动已删除");
            } catch (NumberFormatException e) {
                req.getSession().setAttribute("msg", "参数错误");
            }
            resp.sendRedirect(req.getContextPath() + "/admin/activities");
            return;
        }

        // 新增或编辑活动
        try {
            User user = (User) req.getSession().getAttribute("user");
            Activity a = new Activity();
            String idStr = req.getParameter("activityId");
            if (idStr != null && !idStr.isEmpty()) {
                a.setActivityId(Integer.parseInt(idStr));
            }
            String name = req.getParameter("activityName");
            String time = req.getParameter("activityTime");
            if (name == null || name.trim().isEmpty() || time == null || time.trim().isEmpty()) {
                req.getSession().setAttribute("msg", "活动名称和时间不能为空");
                resp.sendRedirect(req.getContextPath() + "/admin/activities");
                return;
            }
            a.setActivityName(name.trim());
            a.setCategoryId(parseInt(req.getParameter("categoryId"), 1));
            a.setOrganizer(req.getParameter("organizer"));
            a.setActivityTime(fixTime(time.trim()));
            a.setLocation(req.getParameter("location"));
            a.setRegistrationStartTime(fixTime(req.getParameter("registrationStartTime")));
            a.setRegistrationEndTime(fixTime(req.getParameter("registrationEndTime")));
            a.setMaxParticipants(parseInt(req.getParameter("maxParticipants"), 0));
            a.setDescription(req.getParameter("description"));
            a.setStatus(req.getParameter("status"));
            a.setCreatedBy(user.getUserId());
            activityService.save(a);
            req.getSession().setAttribute("msg", "活动已保存");
        } catch (Exception e) {
            req.getSession().setAttribute("msg", "保存失败：" + e.getMessage());
        }
        resp.sendRedirect(req.getContextPath() + "/admin/activities");
    }

    private int parseInt(String s, int defaultVal) {
        try { return Integer.parseInt(s); } catch (Exception e) { return defaultVal; }
    }

    private String fixTime(String t) {
        return t != null ? t.replace('T', ' ') : "";
    }
}
