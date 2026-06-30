<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>管理后台 - 校园活动报名系统</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <script src="https://cdn.jsdelivr.net/npm/echarts@5.4.3/dist/echarts.min.js"></script>
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
    <a href="${pageContext.request.contextPath}/admin/dashboard" class="active">仪表盘</a>
    <a href="${pageContext.request.contextPath}/admin/activities">活动管理</a>
    <a href="${pageContext.request.contextPath}/admin/categories">分类管理</a>
    <a href="${pageContext.request.contextPath}/admin/audit">报名审核</a>
    <a href="${pageContext.request.contextPath}/admin/users">用户管理</a>
    <a href="${pageContext.request.contextPath}/admin/announcements">公告管理</a>
    <a href="${pageContext.request.contextPath}/admin/stats">统计报表</a>
</div>

<div class="container">
    <h3 style="margin-bottom:20px;">系统概览</h3>
    <div class="dashboard-cards">
        <div class="stat-card">
            <div class="label">在册学生</div>
            <div class="value">${studentCount}</div>
        </div>
        <div class="stat-card">
            <div class="label">活动总数</div>
            <div class="value">${activityCount}</div>
        </div>
        <div class="stat-card">
            <div class="label">进行中</div>
            <div class="value">${openCount}</div>
        </div>
        <div class="stat-card">
            <div class="label">待审核</div>
            <div class="value">${pendingCount}</div>
        </div>
        <div class="stat-card">
            <div class="label">报名总人次</div>
            <div class="value">${totalRegCount}</div>
        </div>
        <div class="stat-card">
            <div class="label">公告数量</div>
            <div class="value">${announcementCount}</div>
        </div>
    </div>

    <div class="chart-row" style="margin-top:30px;display:grid;grid-template-columns:1fr 1fr;gap:20px;">
        <div class="content">
            <h4 style="margin-bottom:15px;">活动分类分布</h4>
            <div id="categoryChart" style="width:100%;height:300px;"></div>
        </div>
        <div class="content">
            <h4 style="margin-bottom:15px;">近6个月报名趋势</h4>
            <div id="monthlyChart" style="width:100%;height:300px;"></div>
        </div>
    </div>
</div>

<script>
// 分类饼图
var catChart = echarts.init(document.getElementById('categoryChart'));
var catData = [];
<c:forEach items="${categoryStats}" var="item">
catData.push({value: ${item.value}, name: '${item.name}'});
</c:forEach>
catChart.setOption({
    tooltip: { trigger: 'item' },
    legend: { bottom: '0%', left: 'center' },
    series: [{
        type: 'pie',
        radius: ['40%', '70%'],
        avoidLabelOverlap: false,
        itemStyle: { borderRadius: 6, borderColor: '#fff', borderWidth: 2 },
        label: { show: false },
        emphasis: { label: { show: true, fontSize: 14, fontWeight: 'bold' } },
        data: catData
    }]
});

// 月度柱状图
var monthChart = echarts.init(document.getElementById('monthlyChart'));
var monthLabels = [];
var monthValues = [];
<c:forEach items="${monthlyRegStats}" var="item">
monthLabels.push('${item.month}');
monthValues.push(${item.count});
</c:forEach>
monthChart.setOption({
    tooltip: { trigger: 'axis' },
    grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
    xAxis: { type: 'category', data: monthLabels, axisLabel: { rotate: 30 } },
    yAxis: { type: 'value', minInterval: 1 },
    series: [{
        data: monthValues,
        type: 'bar',
        itemStyle: { color: '#2196F3', borderRadius: [4, 4, 0, 0] },
        barWidth: '50%'
    }]
});

window.addEventListener('resize', function() {
    catChart.resize();
    monthChart.resize();
});
</script>

</body>
</html>
