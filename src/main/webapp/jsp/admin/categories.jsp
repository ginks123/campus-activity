<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>分类管理 - 管理后台</title>
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
    <a href="${pageContext.request.contextPath}/admin/categories" class="active">分类管理</a>
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
        <form action="${pageContext.request.contextPath}/admin/categories" method="post" class="toolbar">
            <span style="font-size:13px;">分类名称：</span>
            <input type="text" name="categoryName" required style="width:150px;">
            <span style="font-size:13px;">描述：</span>
            <input type="text" name="categoryDescription" style="width:250px;">
            <button type="submit" class="btn btn-primary btn-sm">添加</button>
            <a href="javascript:void(0)" class="btn btn-danger btn-sm" onclick="delCat()">删除选中</a>
        </form>

        <table>
            <thead>
                <tr>
                    <th>ID</th>
                    <th>名称</th>
                    <th>描述</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${categories}" var="cat">
                <tr onclick="selectRow(this, ${cat.categoryId})" style="cursor:pointer;">
                    <td>${cat.categoryId}</td>
                    <td style="text-align:left;">${cat.categoryName}</td>
                    <td style="text-align:left;">${cat.categoryDescription}</td>
                </tr>
                </c:forEach>
                <c:if test="${empty categories}">
                <tr><td colspan="3" style="color:#999;padding:30px;">暂无分类</td></tr>
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
function delCat() {
    if (!selectedId) { alert('请选择一个分类'); return; }
    confirmPost('确认删除该分类？', '${pageContext.request.contextPath}/admin/categories', {action:'delete', id:selectedId});
}
</script>
</body>
</html>
