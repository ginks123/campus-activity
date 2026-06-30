package com.campus.servlet.admin;

import com.campus.entity.ActivityStats;
import com.campus.service.RegistrationService;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

@WebServlet("/admin/stats")
public class AdminStatsServlet extends HttpServlet {

    private final RegistrationService registrationService = new RegistrationService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");

        if ("export".equals(action)) {
            exportExcel(resp);
            return;
        }

        List<ActivityStats> stats = registrationService.getStatistics();
        req.setAttribute("stats", stats);
        req.getRequestDispatcher("/jsp/admin/stats.jsp").forward(req, resp);
    }

    private void exportExcel(HttpServletResponse resp) throws IOException {
        List<ActivityStats> stats = registrationService.getStatistics();

        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("报名统计");

        Row header = sheet.createRow(0);
        String[] titles = {"活动名称", "人数上限", "报名总数", "已通过", "待审核", "未通过"};
        for (int i = 0; i < titles.length; i++) {
            header.createCell(i).setCellValue(titles[i]);
        }

        int rowNum = 1;
        for (ActivityStats s : stats) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(s.getActivityName());
            row.createCell(1).setCellValue(s.getMaxParticipants());
            row.createCell(2).setCellValue(s.getTotalReg());
            row.createCell(3).setCellValue(s.getApproved());
            row.createCell(4).setCellValue(s.getPending());
            row.createCell(5).setCellValue(s.getRejected());
        }

        resp.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        resp.setHeader("Content-Disposition",
                "attachment; filename=" + URLEncoder.encode("报名统计.xlsx", "UTF-8"));

        ServletOutputStream out = resp.getOutputStream();
        wb.write(out);
        out.flush();
        wb.close();
    }
}
