<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>活动详情 - 校园活动报名系统</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<div class="navbar">
    <span class="brand">校园活动报名系统</span>
    <div class="user-info">
        <span>${sessionScope.user.username}（学生）</span>
        <a href="${pageContext.request.contextPath}/student/activities">返回</a>
        <a href="${pageContext.request.contextPath}/logout">退出</a>
    </div>
</div>

<div class="container">
    <div class="content">
        <h3 style="margin-bottom:20px;">${activity.activityName}</h3>
        <table style="width:auto;">
            <tr><td style="color:#999;width:100px;">分类：</td><td>${activity.categoryName}</td></tr>
            <tr><td style="color:#999;">主办单位：</td><td>${activity.organizer}</td></tr>
            <tr><td style="color:#999;">活动时间：</td><td>${activity.activityTime}</td></tr>
            <tr><td style="color:#999;">活动地点：</td><td>${activity.location}</td></tr>
            <tr><td style="color:#999;">报名时间：</td><td>${activity.registrationStartTime} ~ ${activity.registrationEndTime}</td></tr>
            <tr><td style="color:#999;">报名人数：</td><td>${activity.currentRegistrationCount}/${activity.maxParticipants}</td></tr>
            <tr><td style="color:#999;">状态：</td><td>${activity.status == 'open' ? '报名中' : (activity.status == 'closed' ? '已结束' : (activity.status == 'draft' ? '草稿' : '已取消'))}</td></tr>
        </table>
        <h4 style="margin-top:20px;color:#555;">活动描述</h4>
        <p style="margin-top:8px;line-height:1.8;color:#666;">${activity.description != null ? activity.description : '暂无'}</p>

        <div style="margin-top:25px;">
            <a href="${pageContext.request.contextPath}/student/activities" class="btn btn-default">返回列表</a>
        </div>
    </div>
</div>

</body>
</html>
