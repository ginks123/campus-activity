<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>我的报名 - 校园活动报名系统</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<div class="navbar">
    <span class="brand">校园活动报名系统</span>
    <div class="user-info">
        <span>${sessionScope.user.username}（学生）</span>
        <a href="${pageContext.request.contextPath}/student/profile" onclick="event.preventDefault();showProfile()">个人信息</a>
        <a href="${pageContext.request.contextPath}/logout">退出</a>
    </div>
</div>

<div class="tab-bar">
    <a href="${pageContext.request.contextPath}/student/activities">活动列表</a>
    <a href="${pageContext.request.contextPath}/student/registrations" class="active">我的报名</a>
    <a href="${pageContext.request.contextPath}/student/announcements">公告信息</a>
</div>

<div class="container">
    <c:if test="${not empty sessionScope.msg}">
        <div class="msg msg-success">${sessionScope.msg}</div>
        <c:remove var="msg" scope="session" />
    </c:if>

    <div class="content">
        <table>
            <thead>
                <tr>
                    <th>ID</th>
                    <th>活动名称</th>
                    <th>分类</th>
                    <th>活动时间</th>
                    <th>地点</th>
                    <th>报名时间</th>
                    <th>审核状态</th>
                    <th>操作</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${registrations}" var="reg">
                <tr>
                    <td>${reg.registrationId}</td>
                    <td style="text-align:left;">${reg.activityName}</td>
                    <td>${reg.categoryName}</td>
                    <td>${reg.activityTime}</td>
                    <td>${reg.location}</td>
                    <td>${reg.registrationTime}</td>
                    <td>
                        <c:choose>
                            <c:when test="${reg.auditStatus == 'pending'}"><span style="color:#FF9800;">待审核</span></c:when>
                            <c:when test="${reg.auditStatus == 'approved'}"><span style="color:#4CAF50;">已通过</span></c:when>
                            <c:when test="${reg.auditStatus == 'rejected'}"><span style="color:#f44336;">未通过</span></c:when>
                        </c:choose>
                    </td>
                    <td>
                        <c:if test="${reg.auditStatus != 'approved'}">
                            <a href="javascript:void(0)" class="btn btn-danger btn-xs"
                               onclick="confirmPost('确认取消报名？',
                               '${pageContext.request.contextPath}/student/registrations',
                               {action:'cancel', id:'${reg.activityId}'})">取消</a>
                        </c:if>
                        <c:if test="${reg.auditStatus == 'approved'}">-</c:if>
                    </td>
                </tr>
                </c:forEach>
                <c:if test="${empty registrations}">
                <tr><td colspan="8" style="color:#999;padding:30px;">暂无报名记录</td></tr>
                </c:if>
            </tbody>
        </table>
    </div>
</div>

<script src="${pageContext.request.contextPath}/js/main.js"></script>
<script>
function showProfile() {
    showModal('个人信息',
        '<form action="${pageContext.request.contextPath}/student/profile" method="post" id="profileForm">' +
        '<div class="form-group"><label>用户名</label><input value="${sessionScope.user.username}" disabled></div>' +
        '<div class="form-group"><label>联系方式</label><input name="contact" value="${sessionScope.user.contact}"></div>' +
        '<div class="form-group"><label>新密码（不修改请留空）</label><input type="password" name="newPassword" placeholder="留空则不修改"></div>' +
        '<div class="modal-actions"><button type="submit" class="btn btn-primary">保存</button>' +
        '<button type="button" class="btn btn-default" onclick="closeModal()">取消</button></div>' +
        '</form>'
    );
}
</script>
</body>
</html>
