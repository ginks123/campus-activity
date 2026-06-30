-- ============================================
-- 校园活动报名管理系统 - 数据库初始化脚本
-- ============================================

CREATE DATABASE IF NOT EXISTS campus_activity
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_unicode_ci;

USE campus_activity;

-- 用户表
CREATE TABLE IF NOT EXISTS users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(64) NOT NULL,
    role ENUM('admin', 'student') NOT NULL DEFAULT 'student',
    contact VARCHAR(100) DEFAULT '',
    status TINYINT NOT NULL DEFAULT 1 COMMENT '1-正常 0-禁用',
    registration_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 活动分类表
CREATE TABLE IF NOT EXISTS activity_categories (
    category_id INT AUTO_INCREMENT PRIMARY KEY,
    category_name VARCHAR(50) NOT NULL UNIQUE,
    category_description VARCHAR(200) DEFAULT ''
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 活动表
CREATE TABLE IF NOT EXISTS activities (
    activity_id INT AUTO_INCREMENT PRIMARY KEY,
    activity_name VARCHAR(100) NOT NULL,
    category_id INT NOT NULL,
    organizer VARCHAR(100) DEFAULT '',
    activity_time VARCHAR(50) DEFAULT '',
    location VARCHAR(100) DEFAULT '',
    registration_start_time VARCHAR(50) DEFAULT '',
    registration_end_time VARCHAR(50) DEFAULT '',
    max_participants INT DEFAULT 0,
    current_registration_count INT DEFAULT 0,
    description TEXT,
    status ENUM('draft', 'open', 'closed', 'cancelled') DEFAULT 'draft',
    created_by INT,
    FOREIGN KEY (category_id) REFERENCES activity_categories(category_id),
    FOREIGN KEY (created_by) REFERENCES users(user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 报名表
CREATE TABLE IF NOT EXISTS registrations (
    registration_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    activity_id INT NOT NULL,
    registration_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    audit_status ENUM('pending', 'approved', 'rejected') DEFAULT 'pending',
    audit_time TIMESTAMP NULL,
    auditor INT,
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (activity_id) REFERENCES activities(activity_id) ON DELETE CASCADE,
    FOREIGN KEY (auditor) REFERENCES users(user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 公告表
CREATE TABLE IF NOT EXISTS announcements (
    announcement_id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    content TEXT NOT NULL,
    publisher INT,
    publish_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (publisher) REFERENCES users(user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================
-- 测试数据
-- ============================================

-- 默认管理员 (密码: admin123)
INSERT INTO users (username, password, role) VALUES
('admin', '0192023a7bbd73250516f069df18b500', 'admin');

-- 20名测试学生 (密码: student123)
INSERT INTO users (username, password, role, contact) VALUES
('张三', 'ad6a280417a0f533d8b670c61667e1a0', 'student', '13800138001'),
('李四', 'ad6a280417a0f533d8b670c61667e1a0', 'student', '13800138002'),
('王五', 'ad6a280417a0f533d8b670c61667e1a0', 'student', '13800138003'),
('赵六', 'ad6a280417a0f533d8b670c61667e1a0', 'student', '13800138004'),
('孙七', 'ad6a280417a0f533d8b670c61667e1a0', 'student', '13800138005'),
('周八', 'ad6a280417a0f533d8b670c61667e1a0', 'student', '13800138006'),
('吴九', 'ad6a280417a0f533d8b670c61667e1a0', 'student', '13800138007'),
('郑十', 'ad6a280417a0f533d8b670c61667e1a0', 'student', '13800138008'),
('钱十一', 'ad6a280417a0f533d8b670c61667e1a0', 'student', '13800138009'),
('冯十二', 'ad6a280417a0f533d8b670c61667e1a0', 'student', '13800138010'),
('陈十三', 'ad6a280417a0f533d8b670c61667e1a0', 'student', '13800138011'),
('褚十四', 'ad6a280417a0f533d8b670c61667e1a0', 'student', '13800138012'),
('卫十五', 'ad6a280417a0f533d8b670c61667e1a0', 'student', '13800138013'),
('蒋十六', 'ad6a280417a0f533d8b670c61667e1a0', 'student', '13800138014'),
('沈十七', 'ad6a280417a0f533d8b670c61667e1a0', 'student', '13800138015'),
('韩十八', 'ad6a280417a0f533d8b670c61667e1a0', 'student', '13800138016'),
('杨十九', 'ad6a280417a0f533d8b670c61667e1a0', 'student', '13800138017'),
('朱二十', 'ad6a280417a0f533d8b670c61667e1a0', 'student', '13800138018'),
('秦二一', 'ad6a280417a0f533d8b670c61667e1a0', 'student', '13800138019'),
('尤二二', 'ad6a280417a0f533d8b670c61667e1a0', 'student', '13800138020');

-- 活动分类
INSERT INTO activity_categories (category_name, category_description) VALUES
('学术讲座', '各类学术报告、讲座、论坛'),
('文体活动', '文艺演出、体育比赛'),
('志愿服务', '社区服务、支教、环保'),
('科技竞赛', '编程比赛、创新创业'),
('社团活动', '社团组织的各类活动');

-- 10个示例活动
INSERT INTO activities (activity_name, category_id, organizer, activity_time,
    location, registration_start_time, registration_end_time, max_participants,
    description, status, created_by) VALUES
('人工智能前沿技术讲座', 1, '计算机学院', '2026-07-15 14:00',
 '学术报告厅A101', '2026-07-01 08:00', '2026-07-14 18:00', 200,
 '邀请知名教授分享人工智能领域最新研究进展，包括大语言模型、计算机视觉等内容。', 'open', 1),
('校园篮球联赛', 2, '体育部', '2026-07-20 09:00',
 '体育馆主馆', '2026-07-05 08:00', '2026-07-18 18:00', 100,
 '面向全体在校学生的篮球比赛，采用小组赛+淘汰赛制，冠军队伍将获得丰厚奖品。', 'open', 1),
('暑期支教志愿者招募', 3, '校团委', '2026-08-01 08:00',
 '贵州省某山区小学', '2026-07-10 08:00', '2026-07-25 18:00', 30,
 '面向全校招募暑期支教志愿者，活动为期两周，提供食宿和交通补贴。', 'open', 1),
('ACM程序设计大赛', 4, 'ACM协会', '2026-07-22 13:00',
 '信息楼实验室301', '2026-07-08 08:00', '2026-07-20 18:00', 60,
 '一年一度的ACM校内选拔赛，个人赛制，使用C/C++/Java/Python语言，前三名可获推荐参加区域赛。', 'open', 1),
('合唱团招新', 5, '艺术团', '2026-07-18 18:30',
 '大学生活动中心302', '2026-07-01 08:00', '2026-07-16 18:00', 50,
 '校合唱团面向全校招募新成员，欢迎热爱音乐的同学报名参加面试。', 'open', 1),
('云计算技术分享会', 1, '云计算实验室', '2026-07-25 14:00',
 '信息楼报告厅', '2026-07-15 08:00', '2026-07-24 18:00', 150,
 '邀请阿里云技术专家分享云计算架构设计与实践经验，涵盖容器化、微服务、DevOps等内容。', 'open', 1),
('校园马拉松', 2, '体育部', '2026-07-28 07:00',
 '校园环形跑道', '2026-07-10 08:00', '2026-07-26 18:00', 300,
 '面向全校师生的校园马拉松活动，设5公里健康跑和10公里挑战跑两个组别，完赛者可获得纪念奖牌。', 'open', 1),
('创新创业大赛', 4, '创业学院', '2026-08-05 09:00',
 '创业孵化中心', '2026-07-20 08:00', '2026-08-03 18:00', 40,
 '面向全校学生的创新创业项目展示与竞赛，优秀项目可获得创业基金支持和导师一对一辅导。', 'open', 1);

-- 大量报名数据（混合状态：已通过、待审核、未通过，分散到6个月）
-- 活动1：人工智能讲座（200人上限）
INSERT INTO registrations (user_id, activity_id, registration_time, audit_status, audit_time, auditor) VALUES
(2, 1, '2026-01-15 10:00', 'approved', '2026-01-15 14:00', 1), (3, 1, '2026-01-16 09:30', 'approved', '2026-01-16 10:00', 1),
(4, 1, '2026-02-10 11:00', 'approved', '2026-02-10 14:00', 1), (5, 1, '2026-02-12 08:00', 'approved', '2026-02-12 10:00', 1),
(6, 1, '2026-03-05 14:00', 'approved', '2026-03-05 16:00', 1), (7, 1, '2026-03-08 10:00', 'approved', '2026-03-08 12:00', 1),
(8, 1, '2026-04-01 09:00', 'approved', '2026-04-01 11:00', 1), (9, 1, '2026-04-05 13:00', 'approved', '2026-04-05 15:00', 1),
(10, 1, '2026-05-10 10:00', 'approved', '2026-05-10 12:00', 1), (11, 1, '2026-05-12 08:30', 'approved', '2026-05-12 10:00', 1),
(12, 1, '2026-06-01 09:00', 'approved', '2026-06-01 11:00', 1), (13, 1, '2026-06-05 14:00', 'pending', NULL, NULL),
(14, 1, '2026-06-10 10:00', 'pending', NULL, NULL), (15, 1, '2026-06-15 08:00', 'pending', NULL, NULL),
(16, 1, '2026-03-20 11:00', 'rejected', '2026-03-20 14:00', 1), (17, 1, '2026-04-22 09:00', 'rejected', '2026-04-22 12:00', 1),
(18, 1, '2026-05-25 13:00', 'rejected', '2026-05-25 15:00', 1), (19, 1, '2026-06-20 10:00', 'rejected', '2026-06-20 14:00', 1),
(20, 1, '2026-06-25 08:00', 'pending', NULL, NULL), (21, 1, '2026-06-28 14:00', 'pending', NULL, NULL);

-- 活动2：篮球联赛（100人上限）
INSERT INTO registrations (user_id, activity_id, registration_time, audit_status, audit_time, auditor) VALUES
(2, 2, '2026-02-01 09:00', 'approved', '2026-02-01 11:00', 1), (3, 2, '2026-02-05 10:00', 'approved', '2026-02-05 12:00', 1),
(4, 2, '2026-03-10 08:00', 'approved', '2026-03-10 10:00', 1), (5, 2, '2026-03-15 14:00', 'approved', '2026-03-15 16:00', 1),
(6, 2, '2026-04-05 09:30', 'approved', '2026-04-05 11:00', 1), (7, 2, '2026-04-10 10:00', 'approved', '2026-04-10 12:00', 1),
(8, 2, '2026-05-01 08:00', 'approved', '2026-05-01 10:00', 1), (9, 2, '2026-05-08 13:00', 'approved', '2026-05-08 15:00', 1),
(10, 2, '2026-06-01 09:00', 'approved', '2026-06-01 11:00', 1), (11, 2, '2026-06-05 10:00', 'pending', NULL, NULL),
(12, 2, '2026-06-10 14:00', 'pending', NULL, NULL), (13, 2, '2026-06-15 08:00', 'pending', NULL, NULL),
(14, 2, '2026-04-20 11:00', 'rejected', '2026-04-20 14:00', 1), (15, 2, '2026-05-22 09:00', 'rejected', '2026-05-22 12:00', 1);

-- 活动3：支教招募（30人上限）
INSERT INTO registrations (user_id, activity_id, registration_time, audit_status, audit_time, auditor) VALUES
(3, 3, '2026-01-20 10:00', 'approved', '2026-01-20 14:00', 1), (5, 3, '2026-02-15 09:00', 'approved', '2026-02-15 11:00', 1),
(7, 3, '2026-03-10 08:00', 'approved', '2026-03-10 10:00', 1), (9, 3, '2026-04-05 14:00', 'approved', '2026-04-05 16:00', 1),
(11, 3, '2026-05-01 10:00', 'approved', '2026-05-01 12:00', 1), (13, 3, '2026-06-05 09:00', 'pending', NULL, NULL),
(15, 3, '2026-06-10 13:00', 'pending', NULL, NULL), (17, 3, '2026-05-20 11:00', 'rejected', '2026-05-20 14:00', 1);

-- 活动4：ACM大赛（60人上限）
INSERT INTO registrations (user_id, activity_id, registration_time, audit_status, audit_time, auditor) VALUES
(2, 4, '2026-01-10 09:00', 'approved', '2026-01-10 11:00', 1), (4, 4, '2026-02-08 10:00', 'approved', '2026-02-08 12:00', 1),
(6, 4, '2026-03-15 08:00', 'approved', '2026-03-15 10:00', 1), (8, 4, '2026-04-10 14:00', 'approved', '2026-04-10 16:00', 1),
(10, 4, '2026-05-05 09:30', 'approved', '2026-05-05 11:00', 1), (12, 4, '2026-05-20 10:00', 'approved', '2026-05-20 12:00', 1),
(14, 4, '2026-06-01 08:00', 'approved', '2026-06-01 10:00', 1), (16, 4, '2026-06-10 13:00', 'pending', NULL, NULL),
(18, 4, '2026-06-15 09:00', 'pending', NULL, NULL), (20, 4, '2026-06-20 14:00', 'pending', NULL, NULL),
(21, 4, '2026-05-25 11:00', 'rejected', '2026-05-25 14:00', 1);

-- 活动5：合唱团招新（50人上限）
INSERT INTO registrations (user_id, activity_id, registration_time, audit_status, audit_time, auditor) VALUES
(2, 5, '2026-01-05 10:00', 'approved', '2026-01-05 14:00', 1), (3, 5, '2026-02-10 09:00', 'approved', '2026-02-10 11:00', 1),
(5, 5, '2026-03-08 08:00', 'approved', '2026-03-08 10:00', 1), (7, 5, '2026-04-12 14:00', 'approved', '2026-04-12 16:00', 1),
(9, 5, '2026-05-15 10:00', 'approved', '2026-05-15 12:00', 1), (11, 5, '2026-06-01 09:00', 'approved', '2026-06-01 11:00', 1),
(13, 5, '2026-06-10 13:00', 'pending', NULL, NULL), (15, 5, '2026-06-15 08:00', 'pending', NULL, NULL),
(17, 5, '2026-05-20 11:00', 'pending', NULL, NULL), (19, 5, '2026-04-22 14:00', 'rejected', '2026-04-22 16:00', 1);

-- 活动6：云计算分享会（150人上限）
INSERT INTO registrations (user_id, activity_id, registration_time, audit_status, audit_time, auditor) VALUES
(2, 6, '2026-01-15 09:00', 'approved', '2026-01-15 11:00', 1), (4, 6, '2026-02-10 10:00', 'approved', '2026-02-10 12:00', 1),
(6, 6, '2026-03-05 08:00', 'approved', '2026-03-05 10:00', 1), (8, 6, '2026-04-01 14:00', 'approved', '2026-04-01 16:00', 1),
(10, 6, '2026-04-20 09:30', 'approved', '2026-04-20 11:00', 1), (12, 6, '2026-05-10 10:00', 'approved', '2026-05-10 12:00', 1),
(14, 6, '2026-05-25 08:00', 'approved', '2026-05-25 10:00', 1), (16, 6, '2026-06-01 13:00', 'approved', '2026-06-01 15:00', 1),
(18, 6, '2026-06-10 09:00', 'approved', '2026-06-10 11:00', 1), (20, 6, '2026-06-15 14:00', 'approved', '2026-06-15 16:00', 1),
(21, 6, '2026-06-20 10:00', 'pending', NULL, NULL), (3, 6, '2026-06-22 08:00', 'pending', NULL, NULL),
(5, 6, '2026-06-25 11:00', 'pending', NULL, NULL), (7, 6, '2026-05-28 14:00', 'rejected', '2026-05-28 16:00', 1),
(9, 6, '2026-04-30 09:00', 'rejected', '2026-04-30 11:00', 1);

-- 活动7：校园马拉松（300人上限）
INSERT INTO registrations (user_id, activity_id, registration_time, audit_status, audit_time, auditor) VALUES
(2, 7, '2026-01-08 10:00', 'approved', '2026-01-08 14:00', 1), (3, 7, '2026-01-20 09:00', 'approved', '2026-01-20 11:00', 1),
(4, 7, '2026-02-05 08:00', 'approved', '2026-02-05 10:00', 1), (5, 7, '2026-02-15 14:00', 'approved', '2026-02-15 16:00', 1),
(6, 7, '2026-03-01 10:00', 'approved', '2026-03-01 12:00', 1), (7, 7, '2026-03-20 09:00', 'approved', '2026-03-20 11:00', 1),
(8, 7, '2026-04-05 08:00', 'approved', '2026-04-05 10:00', 1), (9, 7, '2026-04-15 13:00', 'approved', '2026-04-15 15:00', 1),
(10, 7, '2026-05-01 10:00', 'approved', '2026-05-01 12:00', 1), (11, 7, '2026-05-10 09:00', 'approved', '2026-05-10 11:00', 1),
(12, 7, '2026-05-20 14:00', 'approved', '2026-05-20 16:00', 1), (13, 7, '2026-06-01 08:00', 'approved', '2026-06-01 10:00', 1),
(14, 7, '2026-06-05 10:00', 'approved', '2026-06-05 12:00', 1), (15, 7, '2026-06-10 09:00', 'approved', '2026-06-10 11:00', 1),
(16, 7, '2026-06-15 13:00', 'pending', NULL, NULL), (17, 7, '2026-06-18 08:00', 'pending', NULL, NULL),
(18, 7, '2026-06-20 14:00', 'pending', NULL, NULL), (19, 7, '2026-06-22 10:00', 'pending', NULL, NULL),
(20, 7, '2026-06-25 09:00', 'pending', NULL, NULL), (21, 7, '2026-06-28 11:00', 'pending', NULL, NULL),
(2, 7, '2026-05-30 14:00', 'rejected', '2026-05-30 16:00', 1), (4, 7, '2026-04-28 10:00', 'rejected', '2026-04-28 12:00', 1);

-- 活动8：创新创业大赛（40人上限）
INSERT INTO registrations (user_id, activity_id, registration_time, audit_status, audit_time, auditor) VALUES
(3, 8, '2026-01-25 10:00', 'approved', '2026-01-25 14:00', 1), (6, 8, '2026-02-20 09:00', 'approved', '2026-02-20 11:00', 1),
(9, 8, '2026-03-15 08:00', 'approved', '2026-03-15 10:00', 1), (12, 8, '2026-04-10 14:00', 'approved', '2026-04-10 16:00', 1),
(15, 8, '2026-05-05 10:00', 'approved', '2026-05-05 12:00', 1), (18, 8, '2026-06-01 09:00', 'pending', NULL, NULL),
(21, 8, '2026-06-10 13:00', 'pending', NULL, NULL), (2, 8, '2026-05-20 11:00', 'rejected', '2026-05-20 14:00', 1),
(5, 8, '2026-04-25 14:00', 'rejected', '2026-04-25 16:00', 1);

-- 同步报名人数计数
UPDATE activities SET current_registration_count = 22 WHERE activity_id = 1;
UPDATE activities SET current_registration_count = 15 WHERE activity_id = 2;
UPDATE activities SET current_registration_count = 8 WHERE activity_id = 3;
UPDATE activities SET current_registration_count = 11 WHERE activity_id = 4;
UPDATE activities SET current_registration_count = 10 WHERE activity_id = 5;
UPDATE activities SET current_registration_count = 15 WHERE activity_id = 6;
UPDATE activities SET current_registration_count = 24 WHERE activity_id = 7;
UPDATE activities SET current_registration_count = 9 WHERE activity_id = 8;

-- 示例公告
INSERT INTO announcements (title, content, publisher) VALUES
('系统上线通知', '校园活动报名管理系统已正式上线，欢迎同学们使用。如有问题请联系管理员。', 1),
('关于报名审核的说明', '所有报名将在24小时内完成审核，请同学们耐心等待。审核通过后不可取消报名，请确认时间安排后再报名。', 1),
('暑期支教志愿者面试安排', '已通过审核的志愿者请于7月28日上午9点到校团委办公室参加面试，请携带个人简历一份。', 1),
('ACM校内选拔赛赛前培训', '为了帮助同学们更好地备战ACM校内选拔赛，ACM协会将于7月15日晚7点在信息楼实验室301举办赛前培训，欢迎报名参加。', 1),
('校园马拉松装备领取通知', '已报名校园马拉松的同学请于7月26日至27日到体育馆领取参赛装备包（含号码布、T恤、纪念手环），领取时需携带学生证。', 1);
