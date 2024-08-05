package com.yuping.chat.app.moment;

public class MomentModel {
    private String uid;
    private String displayName;
    private String avatar;
    private String content;
    private String images;

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
}
