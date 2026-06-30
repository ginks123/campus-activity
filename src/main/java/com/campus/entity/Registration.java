package com.campus.entity;

public class Registration {
    private Integer registrationId;
    private Integer userId;
    private Integer activityId;
    private String registrationTime;
    private String auditStatus;
    private String auditTime;
    private Integer auditor;

    // 联表查询字段
    private String username;
    private String activityName;
    private String categoryName;
    private String activityTime;
    private String location;

    public Integer getRegistrationId() { return registrationId; }
    public void setRegistrationId(Integer registrationId) { this.registrationId = registrationId; }
    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
    public Integer getActivityId() { return activityId; }
    public void setActivityId(Integer activityId) { this.activityId = activityId; }
    public String getRegistrationTime() { return registrationTime; }
    public void setRegistrationTime(String registrationTime) { this.registrationTime = registrationTime; }
    public String getAuditStatus() { return auditStatus; }
    public void setAuditStatus(String auditStatus) { this.auditStatus = auditStatus; }
    public String getAuditTime() { return auditTime; }
    public void setAuditTime(String auditTime) { this.auditTime = auditTime; }
    public Integer getAuditor() { return auditor; }
    public void setAuditor(Integer auditor) { this.auditor = auditor; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getActivityName() { return activityName; }
    public void setActivityName(String activityName) { this.activityName = activityName; }
    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
    public String getActivityTime() { return activityTime; }
    public void setActivityTime(String activityTime) { this.activityTime = activityTime; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
}
