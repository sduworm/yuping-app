package com.yuping.chat.app.bottle;

public class MyBottleModel {
    private Long bid;
    private String uid;
    private String content;
    private String images;
    private String dateTime;
    private Integer picked;

    public MyBottleModel() {

    }

    @Override
    public boolean equals(Object obj) {
        try {
            if (this == obj) return true; // 检查是否为同一对象
            if (obj == null || getClass() != obj.getClass()) return false; // 检查是否为null或不同类型

            MyBottleModel m = (MyBottleModel) obj;
            return m.getBid().equals(this.bid);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return this.bid.hashCode();
    }

    public Long getBid() {
        return bid;
    }

    public void setBid(Long bid) {
        this.bid = bid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public Integer getPicked() {
        return picked;
    }

    public void setPicked(Integer picked) {
        this.picked = picked;
    }
}
