package com.campus.entity;

public class Activity {
    private Integer activityId;
    private String activityName;
    private Integer categoryId;
    private String organizer;
    private String activityTime;
    private String location;
    private String registrationStartTime;
    private String registrationEndTime;
    private Integer maxParticipants;
    private Integer currentRegistrationCount;
    private String description;
    private String status;
    private Integer createdBy;

    // 联表查询字段
    private String categoryName;
    private String creatorName;

    public Integer getActivityId() { return activityId; }
    public void setActivityId(Integer activityId) { this.activityId = activityId; }
    public String getActivityName() { return activityName; }
    public void setActivityName(String activityName) { this.activityName = activityName; }
    public Integer getCategoryId() { return categoryId; }
    public void setCategoryId(Integer categoryId) { this.categoryId = categoryId; }
    public String getOrganizer() { return organizer; }
    public void setOrganizer(String organizer) { this.organizer = organizer; }
    public String getActivityTime() { return activityTime; }
    public void setActivityTime(String activityTime) { this.activityTime = activityTime; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public String getRegistrationStartTime() { return registrationStartTime; }
    public void setRegistrationStartTime(String registrationStartTime) { this.registrationStartTime = registrationStartTime; }
    public String getRegistrationEndTime() { return registrationEndTime; }
    public void setRegistrationEndTime(String registrationEndTime) { this.registrationEndTime = registrationEndTime; }
    public Integer getMaxParticipants() { return maxParticipants; }
    public void setMaxParticipants(Integer maxParticipants) { this.maxParticipants = maxParticipants; }
    public Integer getCurrentRegistrationCount() { return currentRegistrationCount; }
    public void setCurrentRegistrationCount(Integer currentRegistrationCount) { this.currentRegistrationCount = currentRegistrationCount; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Integer getCreatedBy() { return createdBy; }
    public void setCreatedBy(Integer createdBy) { this.createdBy = createdBy; }
    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
    public String getCreatorName() { return creatorName; }
    public void setCreatorName(String creatorName) { this.creatorName = creatorName; }
}
