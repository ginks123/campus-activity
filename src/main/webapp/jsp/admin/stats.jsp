<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>统计报表 - 管理后台</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <script src="https://cdn.jsdelivr.net/npm/chart.js@4.4.1/dist/chart.umd.min.js"></script>
</head>
<body>

<div class="navbar">
    <span class="brand">管理后台</span>
    <div class="user-info">
        <span>${sessionScope.user.username}（管理员）</span>
        <a href="${pageContext.request.contextPath}/logout">退出</a>
    </div>
</div>

<div class="tab-bar">
    <a href="${pageContext.request.contextPath}/admin/dashboard">仪表盘</a>
    <a href="${pageContext.request.contextPath}/admin/activities">活动管理</a>
    <a href="${pageContext.request.contextPath}/admin/categories">分类管理</a>
    <a href="${pageContext.request.contextPath}/admin/audit">报名审核</a>
    <a href="${pageContext.request.contextPath}/admin/users">用户管理</a>
    <a href="${pageContext.request.contextPath}/admin/announcements">公告管理</a>
    <a href="${pageContext.request.contextPath}/admin/stats" class="active">统计报表</a>
</div>

<div class="container">
    <div class="content">
        <div class="toolbar">
            <a href="${pageContext.request.contextPath}/admin/stats?action=export" class="btn btn-success btn-sm">导出Excel</a>
        </div>

        <!-- 图表区域 -->
        <c:if test="${not empty stats}">
        <div style="display:grid;grid-template-columns:1fr 1fr;gap:20px;margin-bottom:20px;">
            <div style="background:#fff;padding:15px;border-radius:6px;">
                <h4 style="margin-bottom:10px;color:#333;">各活动报名总数</h4>
                <canvas id="barChart" height="200"></canvas>
            </div>
            <div style="background:#fff;padding:15px;border-radius:6px;">
                <h4 style="margin-bottom:10px;color:#333;">报名状态分布</h4>
                <canvas id="pieChart" height="200"></canvas>
            </div>
        </div>
        </c:if>

        <table>
            <thead>
                <tr>
                    <th>活动名称</th>
                    <th>人数上限</th>
                    <th>报名总数</th>
                    <th>已通过</th>
                    <th>待审核</th>
                    <th>未通过</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${stats}" var="s">
                <tr>
                    <td style="text-align:left;">${s.activityName}</td>
                    <td>${s.maxParticipants}</td>
                    <td>${s.totalReg}</td>
                    <td>${s.approved}</td>
                    <td>${s.pending}</td>
                    <td>${s.rejected}</td>
                </tr>
                </c:forEach>
                <c:if test="${empty stats}">
                <tr><td colspan="6" style="color:#999;padding:30px;">暂无数据</td></tr>
                </c:if>
            </tbody>
        </table>
    </div>
</div>

<script>
<c:if test="${not empty stats}">
// 柱状图：各活动报名总数
var actLabels = [];
var actData = [];
var approvedData = [];
var pendingData = [];
var rejectedData = [];
<c:forEach items="${stats}" var="s">
    actLabels.push('${s.activityName}');
    actData.push(${s.totalReg});
    approvedData.push(${s.approved});
    pendingData.push(${s.pending});
    rejectedData.push(${s.rejected});
</c:forEach>

new Chart(document.getElementById('barChart'), {
    type: 'bar',
    data: {
        labels: actLabels,
        datasets: [{
            label: '报名总数',
            data: actData,
            backgroundColor: '#2196F3'
        }, {
            label: '已通过',
            data: approvedData,
            backgroundColor: '#4CAF50'
        }, {
            label: '待审核',
            data: pendingData,
            backgroundColor: '#FF9800'
        }, {
            label: '未通过',
            data: rejectedData,
            backgroundColor: '#f44336'
        }]
    },
    options: {
        responsive: true,
        scales: { y: { beginAtZero: true } }
    }
});

// 饼图：整体状态分布
var totalApproved = approvedData.reduce((a,b) => a+b, 0);
var totalPending = pendingData.reduce((a,b) => a+b, 0);
var totalRejected = rejectedData.reduce((a,b) => a+b, 0);

new Chart(document.getElementById('pieChart'), {
    type: 'doughnut',
    data: {
        labels: ['已通过', '待审核', '未通过'],
        datasets: [{
            data: [totalApproved, totalPending, totalRejected],
            backgroundColor: ['#4CAF50', '#FF9800', '#f44336']
        }]
    },
    options: {
        responsive: true,
        plugins: { legend: { position: 'bottom' } }
    }
});
</c:if>
</script>

</body>
</html>
