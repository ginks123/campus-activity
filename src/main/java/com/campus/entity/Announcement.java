package com.campus.entity;

public class Announcement {
    private Integer announcementId;
    private String title;
    private String content;
    private Integer publisher;
    private String publishTime;

    // 联表查询字段
    private String pubName;

    public Integer getAnnouncementId() { return announcementId; }
    public void setAnnouncementId(Integer announcementId) { this.announcementId = announcementId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public Integer getPublisher() { return publisher; }
    public void setPublisher(Integer publisher) { this.publisher = publisher; }
    public String getPublishTime() { return publishTime; }
    public void setPublishTime(String publishTime) { this.publishTime = publishTime; }
    public String getPubName() { return pubName; }
    public void setPubName(String pubName) { this.pubName = pubName; }
}
