<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>公告信息 - 校园活动报名系统</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<div class="navbar">
    <span class="brand">校园活动报名系统</span>
    <div class="user-info">
        <span>${sessionScope.user.username}（学生）</span>
        <a href="${pageContext.request.contextPath}/logout">退出</a>
    </div>
</div>

<div class="tab-bar">
    <a href="${pageContext.request.contextPath}/student/activities">活动列表</a>
    <a href="${pageContext.request.contextPath}/student/registrations">我的报名</a>
    <a href="${pageContext.request.contextPath}/student/announcements" class="active">公告信息</a>
</div>

<div class="container">
    <div class="content">
        <c:choose>
            <c:when test="${not empty announcements}">
                <c:forEach items="${announcements}" var="ann" varStatus="st">
                    <div class="announcement-item">
                        <div class="ann-title">【${ann.title}】</div>
                        <div class="ann-meta">${ann.publishTime} &nbsp; 发布人：${ann.pubName}</div>
                        <div class="ann-content">${ann.content}</div>
                    </div>
                    <c:if test="${!st.last}">
                        <hr class="announcement-divider">
                    </c:if>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <p style="text-align:center;color:#999;padding:30px;">暂无公告</p>
            </c:otherwise>
        </c:choose>
    </div>
</div>

</body>
</html>
