package com.example.Diary2.model;

import java.io.Serializable;
import java.util.Date;

public class HistoryItem implements Serializable {
    private String id;
    private boolean create;
    private String username;
    private String contentChanged;
    private Date dateChanged;

    public HistoryItem() {
    }

    public HistoryItem(String id,boolean create, String username, String contentChanged, Date dateChanged) {
        this.id = id;
        this.create = create;
        this.username = username;
        this.contentChanged = contentChanged;
        this.dateChanged = dateChanged;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isCreate() {
        return create;
    }

    public void setCreate(boolean create) {
        this.create = create;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContentChanged() {
        return contentChanged;
    }

    public void setContentChanged(String contentChanged) {
        this.contentChanged = contentChanged;
    }

    public Date getDateChanged() {
        return dateChanged;
    }

    public void setDateChanged(Date dateChanged) {
        this.dateChanged = dateChanged;
    }
}
