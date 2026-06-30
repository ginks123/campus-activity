<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>用户管理 - 管理后台</title>
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
    <a href="${pageContext.request.contextPath}/admin/audit">报名审核</a>
    <a href="${pageContext.request.contextPath}/admin/users" class="active">用户管理</a>
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
            <a href="javascript:void(0)" class="btn btn-warning btn-sm" onclick="toggleUser()">禁用/启用</a>
            <span class="spacer"></span>
            <input type="text" id="keyword" placeholder="用户名..." value="${keyword}" style="width:150px;"
                   onkeydown="if(event.keyCode==13)searchUser()">
            <button class="btn btn-primary btn-sm" onclick="searchUser()">搜索</button>
        </div>

        <table>
            <thead>
                <tr>
                    <th>ID</th>
                    <th>用户名</th>
                    <th>角色</th>
                    <th>联系方式</th>
                    <th>注册时间</th>
                    <th>状态</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${users}" var="u">
                <tr onclick="selectRow(this, ${u.userId})" style="cursor:pointer;">
                    <td>${u.userId}</td>
                    <td style="text-align:left;">${u.username}</td>
                    <td>${u.role == 'admin' ? '管理员' : '学生'}</td>
                    <td>${u.contact}</td>
                    <td>${u.registrationTime}</td>
                    <td style="color:${u.status == 1 ? '#4CAF50' : '#f44336'};">
                        ${u.status == 1 ? '正常' : '已禁用'}
                    </td>
                </tr>
                </c:forEach>
                <c:if test="${empty users}">
                <tr><td colspan="6" style="color:#999;padding:30px;">暂无用户</td></tr>
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
function searchUser() {
    var kw = document.getElementById('keyword').value;
    window.location.href = '${pageContext.request.contextPath}/admin/users?keyword=' + encodeURIComponent(kw);
}
function toggleUser() {
    if (!selectedId) { alert('请选择一个用户'); return; }
    postAction('${pageContext.request.contextPath}/admin/users', {id:selectedId});
}
</script>
</body>
</html>
