package com.campus.servlet.admin;

import com.campus.entity.Category;
import com.campus.service.CategoryService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/admin/categories")
public class AdminCategoryServlet extends HttpServlet {

    private final CategoryService categoryService = new CategoryService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        List<Category> categories = categoryService.findAll();
        req.setAttribute("categories", categories);
        req.getRequestDispatcher("/jsp/admin/categories.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");

        if ("delete".equals(action)) {
            try {
                int id = Integer.parseInt(req.getParameter("id"));
                req.getSession().setAttribute("msg",
                    categoryService.delete(id) ? "分类已删除" : "该分类下有活动，无法删除");
            } catch (NumberFormatException e) {
                req.getSession().setAttribute("msg", "参数错误");
            }
        } else {
            String name = req.getParameter("categoryName");
            String desc = req.getParameter("categoryDescription");
            if (name != null && !name.trim().isEmpty()) {
                if (categoryService.insert(name.trim(), desc != null ? desc.trim() : "")) {
                    req.getSession().setAttribute("msg", "分类已添加");
                } else {
                    req.getSession().setAttribute("msg", "分类名已存在");
                }
            }
        }
        resp.sendRedirect(req.getContextPath() + "/admin/categories");
    }
}
