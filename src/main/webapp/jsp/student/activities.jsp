<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>活动列表 - 校园活动报名系统</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<!-- 导航栏 -->
<div class="navbar">
    <span class="brand">校园活动报名系统</span>
    <div class="user-info">
        <span>${sessionScope.user.username}（学生）</span>
        <a href="javascript:void(0)" onclick="showProfile()">个人信息</a>
        <a href="${pageContext.request.contextPath}/logout">退出</a>
    </div>
</div>

<!-- 标签栏 -->
<div class="tab-bar">
    <a href="${pageContext.request.contextPath}/student/activities" class="active">活动列表</a>
    <a href="${pageContext.request.contextPath}/student/registrations">我的报名</a>
    <a href="${pageContext.request.contextPath}/student/announcements">公告信息</a>
</div>

<div class="container">
    <!-- 消息提示 -->
    <c:if test="${not empty sessionScope.msg}">
        <div class="msg msg-success">${sessionScope.msg}</div>
        <c:remove var="msg" scope="session" />
    </c:if>

    <div class="content">
        <!-- 搜索栏 -->
        <div class="toolbar">
            <input type="text" id="keyword" placeholder="搜索活动..." value="${keyword}"
                   style="width:180px;" onkeydown="if(event.keyCode==13)search()">
            <select id="categoryId" onchange="search()">
                <option value="">全部分类</option>
                <c:forEach items="${categories}" var="cat">
                    <option value="${cat.categoryId}" ${cat.categoryId == categoryId ? 'selected' : ''}>${cat.categoryName}</option>
                </c:forEach>
            </select>
            <button class="btn btn-primary btn-sm" onclick="search()">搜索</button>
            <span class="spacer"></span>
            <button class="btn btn-primary btn-sm" onclick="detailAct()">查看详情</button>
            <button class="btn btn-success btn-sm" onclick="registerAct()">立即报名</button>
            <button class="btn btn-danger btn-sm" onclick="cancelAct()">取消报名</button>
        </div>

        <!-- 活动表格 -->
        <table>
            <thead>
                <tr>
                    <th>ID</th>
                    <th>活动名称</th>
                    <th>分类</th>
                    <th>主办单位</th>
                    <th>活动时间</th>
                    <th>地点</th>
                    <th>报名情况</th>
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
                            <c:when test="${act.currentRegistrationCount >= act.maxParticipants}">
                                <span class="tag tag-full">已满</span>
                            </c:when>
                            <c:otherwise>
                                <span class="tag tag-open">报名中</span>
                            </c:otherwise>
                        </c:choose>
                    </td>
                </tr>
                </c:forEach>
                <c:if test="${empty activities}">
                <tr><td colspan="8" style="color:#999;padding:30px;">暂无活动</td></tr>
                </c:if>
            </tbody>
        </table>

        <!-- 分页 -->
        <c:if test="${totalPages > 0}">
        <div class="pagination">
            <c:if test="${page > 1}">
                <a href="${pageContext.request.contextPath}/student/activities?page=${page - 1}&pageSize=${pageSize}&keyword=${keyword}&categoryId=${categoryId}">上一页</a>
            </c:if>
            <c:forEach begin="1" end="${totalPages}" var="p">
                <c:choose>
                    <c:when test="${p == page}">
                        <span class="current">${p}</span>
                    </c:when>
                    <c:otherwise>
                        <a href="${pageContext.request.contextPath}/student/activities?page=${p}&pageSize=${pageSize}&keyword=${keyword}&categoryId=${categoryId}">${p}</a>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
            <c:if test="${page < totalPages}">
                <a href="${pageContext.request.contextPath}/student/activities?page=${page + 1}&pageSize=${pageSize}&keyword=${keyword}&categoryId=${categoryId}">下一页</a>
            </c:if>
            <span class="info">共 ${total} 条，${totalPages} 页</span>
        </div>
        </c:if>
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
    window.location.href = '${pageContext.request.contextPath}/student/activities?keyword=' + encodeURIComponent(kw) + '&categoryId=' + cid;
}

function detailAct() {
    if (!selectedId) { alert('请先选择一个活动'); return; }
    window.location.href = '${pageContext.request.contextPath}/student/activities?action=detail&id=' + selectedId;
}

function registerAct() {
    if (!selectedId) { alert('请先选择一个活动'); return; }
    confirmPost('确认报名该活动？', '${pageContext.request.contextPath}/student/activities', {action:'register', id:selectedId});
}

function cancelAct() {
    if (!selectedId) { alert('请先选择一个活动'); return; }
    confirmPost('确认取消报名该活动？', '${pageContext.request.contextPath}/student/activities', {action:'cancel', id:selectedId});
}

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
