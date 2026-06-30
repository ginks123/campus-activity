package com.campus.entity;

public class ActivityStats {
    private String activityName;
    private Integer maxParticipants;
    private Integer totalReg;
    private Integer approved;
    private Integer pending;
    private Integer rejected;

    public String getActivityName() { return activityName; }
    public void setActivityName(String activityName) { this.activityName = activityName; }
    public Integer getMaxParticipants() { return maxParticipants; }
    public void setMaxParticipants(Integer maxParticipants) { this.maxParticipants = maxParticipants; }
    public Integer getTotalReg() { return totalReg; }
    public void setTotalReg(Integer totalReg) { this.totalReg = totalReg; }
    public Integer getApproved() { return approved; }
    public void setApproved(Integer approved) { this.approved = approved; }
    public Integer getPending() { return pending; }
    public void setPending(Integer pending) { this.pending = pending; }
    public Integer getRejected() { return rejected; }
    public void setRejected(Integer rejected) { this.rejected = rejected; }
}
