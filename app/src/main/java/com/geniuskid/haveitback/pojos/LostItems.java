package com.geniuskid.haveitback.pojos;

public class LostItems {

    private String id, name, image, date;
    private String desc, place;
    private String postedName, postedNum;
    private String isClaimed;

    public LostItems() {

    }

    public String getDesc() {
        return desc;
    }

    public String getPostedName() {
        return postedName;
    }

    public void setPostedName(String postedName) {
        this.postedName = postedName;
    }

    public String getPostedNum() {
        return postedNum;
    }

    public void setPostedNum(String postedNum) {
        this.postedNum = postedNum;
    }

    public String getIsClaimed() {
        return isClaimed;
    }

    public void setIsClaimed(String isClaimed) {
        this.isClaimed = isClaimed;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public LostItems(String id, String name, String image, String date) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
