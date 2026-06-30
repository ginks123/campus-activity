package com.campus.servlet.student;

import com.campus.entity.Activity;
import com.campus.entity.Category;
import com.campus.entity.User;
import com.campus.service.ActivityService;
import com.campus.service.CategoryService;
import com.campus.service.RegistrationService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/student/activities")
public class ActivityServlet extends HttpServlet {

    private final ActivityService activityService = new ActivityService();
    private final RegistrationService registrationService = new RegistrationService();
    private final CategoryService categoryService = new CategoryService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");

        if ("detail".equals(action)) {
            try {
                int id = Integer.parseInt(req.getParameter("id"));
                Activity act = activityService.findById(id);
                req.setAttribute("activity", act);
                req.getRequestDispatcher("/jsp/student/detail.jsp").forward(req, resp);
            } catch (NumberFormatException e) {
                resp.sendRedirect(req.getContextPath() + "/student/activities");
            }
        } else {
            String keyword = req.getParameter("keyword");
            String categoryId = req.getParameter("categoryId");
            int page = 1;
            int pageSize = 10;
            try {
                page = Integer.parseInt(req.getParameter("page"));
                if (page < 1) page = 1;
            } catch (NumberFormatException ignored) {}
            try {
                pageSize = Integer.parseInt(req.getParameter("pageSize"));
                if (pageSize < 1 || pageSize > 50) pageSize = 10;
            } catch (NumberFormatException ignored) {}

            java.util.Map<String, Object> result = activityService.findActivitiesPaginated(keyword, categoryId, "open", page, pageSize);
            List<Category> categories = categoryService.findAll();
            req.setAttribute("activities", result.get("list"));
            req.setAttribute("categories", categories);
            req.setAttribute("keyword", keyword);
            req.setAttribute("categoryId", categoryId);
            req.setAttribute("page", result.get("page"));
            req.setAttribute("pageSize", result.get("pageSize"));
            req.setAttribute("total", result.get("total"));
            req.setAttribute("totalPages", result.get("totalPages"));
            req.getRequestDispatcher("/jsp/student/activities.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        User user = (User) req.getSession().getAttribute("user");
        String action = req.getParameter("action");
        try {
            int activityId = Integer.parseInt(req.getParameter("id"));

            if ("register".equals(action)) {
                String result = registrationService.register(user.getUserId(), activityId);
                req.getSession().setAttribute("msg", result != null ? result : "报名成功");
            } else if ("cancel".equals(action)) {
                String result = registrationService.cancel(user.getUserId(), activityId);
                req.getSession().setAttribute("msg", result != null ? result : "已取消报名");
            }
        } catch (NumberFormatException e) {
            req.getSession().setAttribute("msg", "参数错误");
        }
        resp.sendRedirect(req.getContextPath() + "/student/activities");
    }
}
