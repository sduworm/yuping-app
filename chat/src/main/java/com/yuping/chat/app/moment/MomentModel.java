package com.yuping.chat.app.moment;

public class MomentModel {
    private String uid;
    private String displayName;
    private String avatar;
    private String content;
    private String images;
    private Long momentId;
    private String dateTime;
    private String updateDateTime;
    private Integer likeCount;
    private Boolean isLike;
    private Boolean isSelf;

    public MomentModel() {

    }

    public MomentModel(String displayName, String content, String avatar) {
        this.displayName = displayName;
        this.avatar = avatar;
        this.content = content;
    }

    @Override
    public boolean equals(Object obj) {
        try {
            if (this == obj) return true; // 检查是否为同一对象
            if (obj == null || getClass() != obj.getClass()) return false; // 检查是否为null或不同类型

            MomentModel m = (MomentModel) obj;
            return m.getMomentId().equals(this.getMomentId());
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return this.momentId.hashCode();
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

    public Integer getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

    public Boolean getLike() {
        return isLike;
    }

    public void setLike(Boolean like) {
        isLike = like;
    }

    public Boolean getSelf() {
        return isSelf;
    }

    public void setSelf(Boolean self) {
        isSelf = self;
    }

}
