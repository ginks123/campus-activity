<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>公告管理 - 管理后台</title>
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
    <a href="${pageContext.request.contextPath}/admin/users">用户管理</a>
    <a href="${pageContext.request.contextPath}/admin/announcements" class="active">公告管理</a>
    <a href="${pageContext.request.contextPath}/admin/stats">统计报表</a>
</div>

<div class="container">
    <c:if test="${not empty sessionScope.msg}">
        <div class="msg msg-success">${sessionScope.msg}</div>
        <c:remove var="msg" scope="session" />
    </c:if>

    <div class="content">
        <div class="toolbar">
            <a href="javascript:void(0)" class="btn btn-primary btn-sm" onclick="showAddAnn()">发布公告</a>
            <a href="javascript:void(0)" class="btn btn-danger btn-sm" onclick="delAnn()">删除公告</a>
        </div>

        <table>
            <thead>
                <tr>
                    <th>ID</th>
                    <th>标题</th>
                    <th>发布时间</th>
                    <th>发布人</th>
                    <th>操作</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${announcements}" var="ann">
                <tr onclick="selectRow(this, ${ann.announcementId})" style="cursor:pointer;">
                    <td>${ann.announcementId}</td>
                    <td style="text-align:left;">${ann.title}</td>
                    <td>${ann.publishTime}</td>
                    <td>${ann.pubName}</td>
                    <td>
                        <a href="javascript:void(0)" class="btn btn-primary btn-xs" onclick="event.stopPropagation();showAnnContent('${ann.title}', '${ann.content}')">查看</a>
                    </td>
                </tr>
                </c:forEach>
                <c:if test="${empty announcements}">
                <tr><td colspan="5" style="color:#999;padding:30px;">暂无公告</td></tr>
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
function delAnn() {
    if (!selectedId) { alert('请选择一个公告'); return; }
    confirmPost('确认删除该公告？', '${pageContext.request.contextPath}/admin/announcements', {action:'delete', id:selectedId});
}
function showAnnContent(title, content) {
    event.stopPropagation();
    showModal(title, '<div style="max-height:400px;overflow-y:auto;line-height:1.8;font-size:14px;">' + content.replace(/\n/g, '<br>') + '</div>');
}
function showAddAnn() {
    showModal('发布公告',
        '<form action="${pageContext.request.contextPath}/admin/announcements" method="post">' +
        '<div class="form-group"><label>标题</label><input type="text" name="title" required></div>' +
        '<div class="form-group"><label>内容</label><textarea name="content" rows="5" required></textarea></div>' +
        '<div class="modal-actions"><button type="submit" class="btn btn-primary">发布</button>' +
        '<button type="button" class="btn btn-default" onclick="closeModal()">取消</button></div>' +
        '</form>'
    );
}
</script>
</body>
</html>
