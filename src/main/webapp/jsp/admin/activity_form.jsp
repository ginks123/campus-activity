<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>${not empty activity ? '编辑活动' : '新增活动'} - 管理后台</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<div class="navbar">
    <span class="brand">管理后台</span>
    <div class="user-info">
        <span>${sessionScope.user.username}（管理员）</span>
        <a href="${pageContext.request.contextPath}/admin/activities">返回</a>
        <a href="${pageContext.request.contextPath}/logout">退出</a>
    </div>
</div>

<div class="container">
    <div class="content" style="max-width:600px;margin:0 auto;">
        <h3 style="margin-bottom:20px;">${not empty activity ? '编辑活动' : '新增活动'}</h3>

        <form action="${pageContext.request.contextPath}/admin/activities" method="post">
            <c:if test="${not empty activity}">
                <input type="hidden" name="activityId" value="${activity.activityId}">
            </c:if>

            <div class="form-group">
                <label>活动名称</label>
                <input type="text" name="activityName" value="${activity.activityName}" required>
            </div>
            <div class="form-group">
                <label>分类</label>
                <select name="categoryId" required>
                    <c:forEach items="${categories}" var="cat">
                        <option value="${cat.categoryId}" ${activity.categoryId == cat.categoryId ? 'selected' : ''}>${cat.categoryName}</option>
                    </c:forEach>
                </select>
            </div>
            <div class="form-group">
                <label>主办单位</label>
                <input type="text" name="organizer" value="${activity.organizer}">
            </div>
            <div class="form-group">
                <label>活动时间</label>
                <input type="datetime-local" name="activityTime" value="${fn:replace(activity.activityTime, ' ', 'T')}" required>
            </div>
            <div class="form-group">
                <label>活动地点</label>
                <input type="text" name="location" value="${activity.location}">
            </div>
            <div class="form-group">
                <label>报名开始时间</label>
                <input type="datetime-local" name="registrationStartTime" value="${fn:replace(activity.registrationStartTime, ' ', 'T')}">
            </div>
            <div class="form-group">
                <label>报名截止时间</label>
                <input type="datetime-local" name="registrationEndTime" value="${fn:replace(activity.registrationEndTime, ' ', 'T')}">
            </div>
            <div class="form-group">
                <label>人数上限</label>
                <input type="text" name="maxParticipants" value="${not empty activity ? activity.maxParticipants : '100'}">
            </div>
            <div class="form-group">
                <label>活动描述</label>
                <textarea name="description" rows="4">${activity.description}</textarea>
            </div>
            <div class="form-group">
                <label>状态</label>
                <select name="status">
                    <option value="draft" ${activity.status == 'draft' ? 'selected' : ''}>草稿</option>
                    <option value="open" ${activity.status == 'open' ? 'selected' : ''}>进行中</option>
                    <option value="closed" ${activity.status == 'closed' ? 'selected' : ''}>已结束</option>
                    <option value="cancelled" ${activity.status == 'cancelled' ? 'selected' : ''}>已取消</option>
                </select>
            </div>

            <div style="margin-top:20px;">
                <button type="submit" class="btn btn-primary">保存</button>
                <a href="${pageContext.request.contextPath}/admin/activities" class="btn btn-default">取消</a>
            </div>
        </form>
    </div>
</div>

<script>
document.querySelector('form').addEventListener('submit', function() {
    ['activityTime', 'registrationStartTime', 'registrationEndTime'].forEach(function(name) {
        var el = document.querySelector('input[name="' + name + '"]');
        if (el && el.value) el.value = el.value.replace('T', ' ');
    });
});
</script>

</body>
</html>
