// 显示模态框
function showModal(title, content) {
    var overlay = document.createElement('div');
    overlay.className = 'modal-overlay';
    overlay.id = 'modalOverlay';
    overlay.innerHTML = '<div class="modal-box"><h3>' + title + '</h3>' + content + '</div>';
    overlay.addEventListener('click', function(e) {
        if (e.target === overlay) closeModal();
    });
    document.body.appendChild(overlay);
}

function closeModal() {
    var overlay = document.getElementById('modalOverlay');
    if (overlay) overlay.remove();
}

// POST提交（替代GET链接的破坏性操作）
function postAction(url, params) {
    var form = document.createElement('form');
    form.method = 'post';
    form.action = url;
    form.style.display = 'none';
    for (var key in params) {
        if (params.hasOwnProperty(key)) {
            var input = document.createElement('input');
            input.type = 'hidden';
            input.name = key;
            input.value = params[key];
            form.appendChild(input);
        }
    }
    document.body.appendChild(form);
    form.submit();
}

// 确认后POST提交
function confirmPost(msg, url, params) {
    if (confirm(msg)) {
        postAction(url, params);
    }
}

// 自动消失消息
document.addEventListener('DOMContentLoaded', function() {
    var msgs = document.querySelectorAll('.msg');
    msgs.forEach(function(msg) {
        setTimeout(function() {
            msg.style.transition = 'opacity 0.5s';
            msg.style.opacity = '0';
            setTimeout(function() { msg.style.display = 'none'; }, 500);
        }, 3000);
    });
});
