<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>报名审核 - 管理后台</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
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
    <a href="${pageContext.request.contextPath}/admin/audit" class="active">报名审核</a>
    <a href="${pageContext.request.contextPath}/admin/users">用户管理</a>
    <a href="${pageContext.request.contextPath}/admin/announcements">公告管理</a>
    <a href="${pageContext.request.contextPath}/admin/stats">统计报表</a>
</div>

<div class="container">
    <c:if test="${not empty sessionScope.msg}">
        <div class="msg msg-success">${sessionScope.msg}</div>
        <c:remove var="msg" scope="session" />
    </c:if>

    <div class="content">
        <div class="toolbar">
            <select id="filter" onchange="changeFilter()">
                <option value="pending" ${filter == 'pending' ? 'selected' : ''}>待审核</option>
                <option value="approved" ${filter == 'approved' ? 'selected' : ''}>已通过</option>
                <option value="rejected" ${filter == 'rejected' ? 'selected' : ''}>未通过</option>
                <option value="" ${filter == '' ? 'selected' : ''}>全部</option>
            </select>
            <button class="btn btn-primary btn-sm" onclick="changeFilter()">刷新</button>
            <span class="spacer"></span>
            <a href="javascript:void(0)" class="btn btn-success btn-sm" onclick="doAudit('approve')">通过</a>
            <a href="javascript:void(0)" class="btn btn-danger btn-sm" onclick="doAudit('reject')">拒绝</a>
        </div>

        <table>
            <thead>
                <tr>
                    <th>ID</th>
                    <th>学生</th>
                    <th>活动名称</th>
                    <th>报名时间</th>
                    <th>审核状态</th>
                    <th>审核时间</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${registrations}" var="reg">
                <tr onclick="selectRow(this, ${reg.registrationId})" style="cursor:pointer;">
                    <td>${reg.registrationId}</td>
                    <td>${reg.username}</td>
                    <td style="text-align:left;">${reg.activityName}</td>
                    <td>${reg.registrationTime}</td>
                    <td>
                        <c:choose>
                            <c:when test="${reg.auditStatus == 'pending'}"><span style="color:#FF9800;">待审核</span></c:when>
                            <c:when test="${reg.auditStatus == 'approved'}"><span style="color:#4CAF50;">已通过</span></c:when>
                            <c:when test="${reg.auditStatus == 'rejected'}"><span style="color:#f44336;">未通过</span></c:when>
                        </c:choose>
                    </td>
                    <td>${reg.auditTime}</td>
                </tr>
                </c:forEach>
                <c:if test="${empty registrations}">
                <tr><td colspan="6" style="color:#999;padding:30px;">暂无记录</td></tr>
                </c:if>
            </tbody>
        </table>
    </div>
</div>

<script src="${pageContext.request.contextPath}/js/main.js"></script>
<script>
var selectedId = null;
function selectRow(tr, id) {
    var rows = document.querySelectorAll('table tbody tr');
    rows.forEach(function(r) { r.style.background = ''; });
    tr.style.background = '#e6f7ff';
    selectedId = id;
}
function changeFilter() {
    var f = document.getElementById('filter').value;
    window.location.href = '${pageContext.request.contextPath}/admin/audit?filter=' + f;
}
function doAudit(action) {
    if (!selectedId) { alert('请选择一条报名记录'); return; }
    postAction('${pageContext.request.contextPath}/admin/audit', {action:action, id:selectedId});
}
</script>
</body>
</html>
