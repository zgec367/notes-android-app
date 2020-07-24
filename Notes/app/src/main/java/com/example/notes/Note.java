package com.example.notes;

public class Note {
    private long ID;
    private Double latitude;
    private Double longitude;
    private String photo;
    private String feeling;
    private String creation;
    private String last_modification;
    private String title;
    private String content;
    private int color;
    private String record;

    public Double getLatitude() {
        return latitude;
    }
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }
    public Double getLongitude() {
        return longitude;
    }
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
    public String getPhoto() {
        return photo;
    }
    public void setPhoto(String photo) {
        this.photo = photo;
    }
    public String getFeeling() {
        return feeling;
    }
    public void setFeeling(String feeling) {
        this.feeling = feeling;
    }
    public long getID() {
        return ID;
    }
    public void setID(long ID) {
        this.ID = ID;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getCreation() {
        return creation;
    }
    public void setCreation(String creation) {
        this.creation = creation;
    }
    public String getLast_modification() {
        return last_modification;
    }
    public void setLast_modification(String last_modification) {
        this.last_modification = last_modification;
    }
    public String getRecord() {
        return record;
    }
    public void setRecord(String record) {
        this.record = record;
    }
    public int getColor() {
        return color;
    }
    public void setColor(int color) {
        this.color = color;
    }



    Note(long id, String title,
         String content,
         String photo,
         Double latitude,
         Double longitude,
         String feeling,
         String creation,
         String last_modification,
         String record,
         int color
    ) {
        this.ID = id;
        this.title = title;
        this.content = content;
        this.photo = photo;
        this.latitude = latitude;
        this.longitude = longitude;
        this.feeling = feeling;
        this.creation = creation;
        this.last_modification = last_modification;
        this.record = record;
        this.color = color;
    }

    Note() {
    }

    Note(String title, String content,
         Double latitude,
         Double longitude,
         String photo,
         String feeling,
         String creation,
         String record,
         int color
    ) {
        this.title = title;
        this.content = content;
        this.latitude = latitude;
        this.longitude = longitude;
        this.photo = photo;
        this.feeling = feeling;
        this.creation = creation;
        this.record = record;
        this.color = color;
    }

   }
