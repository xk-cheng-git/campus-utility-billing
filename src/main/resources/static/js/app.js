/**
 * 校园水电费管理系统 — 前端核心工具库
 * 提供 API 调用、认证管理、Toast 通知、工具函数
 */

// ============================================
// API 基础配置
// ============================================
const API_BASE = '';

/**
 * 统一 API 请求封装
 * @param {string} url - 请求路径（不含 base）
 * @param {object} options - fetch 选项
 * @returns {Promise<any>} - 返回 ApiResponse.data
 */
async function request(url, options = {}) {
    const config = {
        headers: { 'Content-Type': 'application/json' },
        ...options,
    };

    try {
        const response = await fetch(API_BASE + url, config);
        const result = await response.json();

        if (result.code === 200) {
            return result.data;
        } else {
            throw new Error(result.message || '请求失败');
        }
    } catch (error) {
        if (error.message && !error.message.includes('Failed to fetch')) {
            throw error;
        }
        throw new Error('网络连接失败，请检查服务器是否启动');
    }
}

// 便捷方法
const api = {
    get: (url) => request(url),
    post: (url, data) => request(url, { method: 'POST', body: JSON.stringify(data) }),
    put: (url, data) => request(url, { method: 'PUT', body: JSON.stringify(data) }),
};

// ============================================
// 认证管理（localStorage）
// ============================================
const auth = {
    KEY: 'campus_user',

    save(user) {
        localStorage.setItem(this.KEY, JSON.stringify(user));
    },

    get() {
        const raw = localStorage.getItem(this.KEY);
        return raw ? JSON.parse(raw) : null;
    },

    getStudentNo() {
        const user = this.get();
        return user ? user.studentNo : null;
    },

    isAdmin() {
        const user = this.get();
        return user && (user.admin === true || user.isAdmin === true);
    },

    isLoggedIn() {
        return !!this.get();
    },

    clear() {
        localStorage.removeItem(this.KEY);
    },

    requireLogin() {
        if (!this.isLoggedIn()) {
            window.location.href = '/login';
            return false;
        }
        return true;
    },

    requireAdmin() {
        if (!this.isLoggedIn()) {
            window.location.href = '/login';
            return false;
        }
        if (!this.isAdmin()) {
            toast.error('无管理员权限');
            window.location.href = '/student';
            return false;
        }
        return true;
    }
};

// ============================================
// Toast 通知
// ============================================
const toast = {
    container: null,

    init() {
        if (this.container) return;
        this.container = document.createElement('div');
        this.container.className = 'toast-container';
        document.body.appendChild(this.container);
    },

    show(message, type = 'success', duration = 3000) {
        this.init();
        const icons = { success: '✓', error: '✕', warning: '⚠' };
        const el = document.createElement('div');
        el.className = `toast toast-${type}`;
        el.innerHTML = `
            <span class="toast-icon">${icons[type] || 'ℹ'}</span>
            <span class="toast-msg">${message}</span>
            <button class="toast-close" onclick="this.parentElement.remove()">✕</button>
        `;
        this.container.appendChild(el);
        setTimeout(() => el.remove(), duration);
    },

    success(msg) { this.show(msg, 'success'); },
    error(msg) { this.show(msg, 'error', 5000); },
    warning(msg) { this.show(msg, 'warning', 4000); },
};

// ============================================
// Modal 模态框
// ============================================
function openModal(id) {
    const el = document.getElementById(id);
    if (el) el.classList.add('active');
}

function closeModal(id) {
    const el = document.getElementById(id);
    if (el) el.classList.remove('active');
}

// 点击遮罩关闭
document.addEventListener('click', function(e) {
    if (e.target.classList.contains('modal-overlay')) {
        e.target.classList.remove('active');
    }
});

// ============================================
// 工具函数
// ============================================

/**
 * 格式化金额
 */
function formatMoney(amount) {
    if (amount == null) return '0.00';
    return parseFloat(amount).toFixed(2);
}

/**
 * 格式化日期时间
 */
function formatDateTime(dateStr) {
    if (!dateStr) return '-';
    const d = new Date(dateStr);
    if (isNaN(d.getTime())) return dateStr;
    const pad = (n) => String(n).padStart(2, '0');
    return `${d.getFullYear()}-${pad(d.getMonth()+1)}-${pad(d.getDate())} ${pad(d.getHours())}:${pad(d.getMinutes())}`;
}

/**
 * 格式化日期（不含时间）
 */
function formatDate(dateStr) {
    if (!dateStr) return '-';
    const d = new Date(dateStr);
    if (isNaN(d.getTime())) return dateStr;
    const pad = (n) => String(n).padStart(2, '0');
    return `${d.getFullYear()}-${pad(d.getMonth()+1)}-${pad(d.getDate())}`;
}

/**
 * 支付状态标签
 */
function payStatusTag(status) {
    if (status === 1) return '<span class="tag tag-success">已缴费</span>';
    return '<span class="tag tag-danger">未缴费</span>';
}

/**
 * 消息类型标签
 */
function msgTypeTag(type) {
    if (type === 'warning') return '<span class="tag tag-warning">警告</span>';
    return '<span class="tag tag-info">通知</span>';
}

/**
 * 缴费类型标签
 */
function payTypeTag(type) {
    if (type === 1) return '<span class="tag tag-info">缴账单</span>';
    return '<span class="tag tag-success">余额充值</span>';
}

/**
 * 获取当前月份（YYYY-MM格式）
 */
function getCurrentYearMonth() {
    const now = new Date();
    const pad = (n) => String(n).padStart(2, '0');
    return `${now.getFullYear()}-${pad(now.getMonth() + 1)}`;
}

/**
 * 加载未读消息数并更新导航徽章
 */
async function loadUnreadCount() {
    const studentNo = auth.getStudentNo();
    if (!studentNo) return;
    try {
        const data = await api.get(`/api/messages/unread-count?studentNo=${studentNo}`);
        const badge = document.getElementById('msgBadge');
        if (badge) {
            if (data.unreadCount > 0) {
                badge.textContent = data.unreadCount;
                badge.style.display = 'inline';
            } else {
                badge.style.display = 'none';
            }
        }
    } catch (e) {
        // 静默失败
    }
}

/**
 * 加载用户余额并更新顶部显示
 */
async function loadBalance() {
    const studentNo = auth.getStudentNo();
    if (!studentNo) return;
    try {
        const user = await api.get(`/api/users/${studentNo}`);
        const el = document.getElementById('balanceDisplay');
        if (el) el.textContent = '¥' + formatMoney(user.studentMoney);
        // 更新本地缓存
        const cached = auth.get();
        if (cached) {
            cached.studentMoney = user.studentMoney;
            auth.save(cached);
        }
    } catch (e) {
        // 静默失败
    }
}

/**
 * 通用确认弹窗
 */
function confirmAction(message) {
    return confirm(message);
}

/**
 * 设置侧边栏当前页高亮
 */
function setActiveNav(path) {
    document.querySelectorAll('.nav-item').forEach(item => {
        item.classList.toggle('active', item.getAttribute('href') === path);
    });
}

/**
 * 页面初始化：检查登录状态 + 加载未读数 + 加载余额
 */
function pageInit(options = {}) {
    if (options.requireAdmin) {
        if (!auth.requireAdmin()) return false;
    } else if (options.requireLogin !== false) {
        if (!auth.requireLogin()) return false;
    }

    // 填充用户信息
    const user = auth.get();
    if (user) {
        const nameEl = document.getElementById('userName');
        const roleEl = document.getElementById('userRole');
        const avatarEl = document.getElementById('userAvatar');
        if (nameEl) nameEl.textContent = user.username;
        if (roleEl) roleEl.textContent = (user.admin || user.isAdmin) ? '管理员' : '学生';
        if (avatarEl) avatarEl.textContent = user.username.charAt(0);
    }

    // 加载未读数和余额
    loadUnreadCount();
    if (!options.requireAdmin) {
        loadBalance();
    }

    return true;
}
