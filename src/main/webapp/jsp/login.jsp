<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>校园活动报名管理系统 - 登录</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="login-container">
    <div class="login-box">
        <div class="login-header">
            <h2>校园活动报名管理系统</h2>
            <p>数据库课程设计</p>
        </div>
        <div class="login-body">
            <!-- 标签切换 -->
            <div class="login-tabs">
                <button id="tabLogin" class="active" onclick="switchTab('login')">登录</button>
                <button id="tabReg" onclick="switchTab('register')">注册</button>
            </div>

            <!-- 消息提示 -->
            <c:if test="${not empty error}">
                <div class="msg msg-error">${error}</div>
            </c:if>
            <c:if test="${not empty regError}">
                <div class="msg msg-error">${regError}</div>
            </c:if>
            <c:if test="${not empty regSuccess}">
                <div class="msg msg-success">${regSuccess}</div>
            </c:if>

            <!-- 登录表单 -->
            <form id="formLogin" action="${pageContext.request.contextPath}/login" method="post">
                <div class="form-group">
                    <label>用户名</label>
                    <input type="text" name="username" value="${username}" required>
                </div>
                <div class="form-group">
                    <label>密码</label>
                    <input type="password" name="password" required onkeydown="if(event.keyCode==13)document.getElementById('formLogin').submit()">
                </div>
                <button type="submit" class="btn btn-primary btn-login">登录</button>
            </form>

            <!-- 注册表单 -->
            <form id="formReg" action="${pageContext.request.contextPath}/register" method="post" style="display:none;">
                <div class="form-group">
                    <label>用户名</label>
                    <input type="text" name="username" required>
                </div>
                <div class="form-group">
                    <label>密码（至少6位）</label>
                    <input type="password" name="password" required>
                </div>
                <div class="form-group">
                    <label>确认密码</label>
                    <input type="password" name="password2" required>
                </div>
                <div class="form-group">
                    <label>联系方式（选填）</label>
                    <input type="text" name="contact">
                </div>
                <button type="submit" class="btn btn-primary btn-login">注册</button>
            </form>

            <p class="login-hint">测试账号: admin / admin123 &nbsp;|&nbsp; 张三 / student123</p>
        </div>
    </div>
</div>

<script>
function switchTab(tab) {
    if (tab === 'login') {
        document.getElementById('formLogin').style.display = '';
        document.getElementById('formReg').style.display = 'none';
        document.getElementById('tabLogin').classList.add('active');
        document.getElementById('tabReg').classList.remove('active');
    } else {
        document.getElementById('formLogin').style.display = 'none';
        document.getElementById('formReg').style.display = '';
        document.getElementById('tabReg').classList.add('active');
        document.getElementById('tabLogin').classList.remove('active');
    }
}
// 如果注册有错误，切到注册tab
<c:if test="${not empty regError}">
switchTab('register');
</c:if>
</script>
</body>
</html>
