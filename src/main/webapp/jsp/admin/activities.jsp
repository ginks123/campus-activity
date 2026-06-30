<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>活动管理 - 管理后台</title>
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
    <a href="${pageContext.request.contextPath}/admin/activities" class="active">活动管理</a>
    <a href="${pageContext.request.contextPath}/admin/categories">分类管理</a>
    <a href="${pageContext.request.contextPath}/admin/audit">报名审核</a>
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
            <a href="${pageContext.request.contextPath}/admin/activities?action=add" class="btn btn-primary btn-sm">新增活动</a>
            <a href="javascript:void(0)" class="btn btn-warning btn-sm" onclick="editAct()">编辑活动</a>
            <a href="javascript:void(0)" class="btn btn-danger btn-sm" onclick="deleteAct()">删除活动</a>
            <span class="spacer"></span>
            <input type="text" id="keyword" placeholder="搜索..." value="${keyword}" style="width:150px;"
                   onkeydown="if(event.keyCode==13)search()">
            <select id="categoryId" onchange="search()">
                <option value="">全部分类</option>
                <c:forEach items="${categories}" var="cat">
                    <option value="${cat.categoryId}" ${cat.categoryId == categoryId ? 'selected' : ''}>${cat.categoryName}</option>
                </c:forEach>
            </select>
            <button class="btn btn-primary btn-sm" onclick="search()">搜索</button>
        </div>

        <table>
            <thead>
                <tr>
                    <th>ID</th>
                    <th>活动名称</th>
                    <th>分类</th>
                    <th>主办单位</th>
                    <th>活动时间</th>
                    <th>地点</th>
                    <th>报名</th>
                    <th>状态</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${activities}" var="act">
                <tr onclick="selectRow(this, ${act.activityId})" style="cursor:pointer;">
                    <td>${act.activityId}</td>
                    <td style="text-align:left;">${act.activityName}</td>
                    <td>${act.categoryName}</td>
                    <td>${act.organizer}</td>
                    <td>${act.activityTime}</td>
                    <td>${act.location}</td>
                    <td>${act.currentRegistrationCount}/${act.maxParticipants}</td>
                    <td>
                        <c:choose>
                            <c:when test="${act.status == 'open'}">进行中</c:when>
                            <c:when test="${act.status == 'closed'}">已结束</c:when>
                            <c:when test="${act.status == 'draft'}">草稿</c:when>
                            <c:when test="${act.status == 'cancelled'}">已取消</c:when>
                        </c:choose>
                    </td>
                </tr>
                </c:forEach>
                <c:if test="${empty activities}">
                <tr><td colspan="8" style="color:#999;padding:30px;">暂无活动</td></tr>
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
function search() {
    var kw = document.getElementById('keyword').value;
    var cid = document.getElementById('categoryId').value;
    window.location.href = '${pageContext.request.contextPath}/admin/activities?keyword=' + encodeURIComponent(kw) + '&categoryId=' + cid;
}
function editAct() {
    if (!selectedId) { alert('请先选择一个活动'); return; }
    window.location.href = '${pageContext.request.contextPath}/admin/activities?action=edit&id=' + selectedId;
}
function deleteAct() {
    if (!selectedId) { alert('请先选择一个活动'); return; }
    confirmPost('删除活动将同时删除所有相关报名记录，确认删除？',
        '${pageContext.request.contextPath}/admin/activities',
        {action:'delete', id:selectedId});
}
</script>
</body>
</html>
