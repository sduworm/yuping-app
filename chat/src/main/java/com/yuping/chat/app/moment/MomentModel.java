package com.yuping.chat.app.moment;

import java.time.LocalDateTime;

public class MomentModel {
    private String uid;
    private String displayName;
    private String avatar;
    private String content;
    private String images;
    private Long momentId;
    private String dateTime;
    private String updateDateTime;


    public MomentModel() {

    }

    public MomentModel(String displayName, String content, String avatar){
        this.displayName = displayName;
        this.avatar = avatar;
        this.content = content;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public Long getMomentId() {
        return momentId;
    }

    public void setMomentId(Long momentId) {
        this.momentId = momentId;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getUpdateDateTime() {
        return updateDateTime;
    }

    public void setUpdateDateTime(String updateDateTime) {
        this.updateDateTime = updateDateTime;
    }

//    public LocalDateTime getDateTime() {
//        return dateTime;
//    }
//
//    public void setDateTime(LocalDateTime dateTime) {
//        this.dateTime = dateTime;
//    }
//
//    public LocalDateTime getUpdateDateTime() {
//        return updateDateTime;
//    }
//
//    public void setUpdateDateTime(LocalDateTime updateDateTime) {
//        this.updateDateTime = updateDateTime;
//    }
}
