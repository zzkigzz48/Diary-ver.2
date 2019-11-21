package com.example.Diary2.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class JournalItem implements Serializable {
    private String id;
    private Date date;
    private String title;
    private String content;
    private int color;
    private List<HistoryItem> historyList;

    public JournalItem() {
        historyList = new ArrayList<>();
    }

    public JournalItem(Date date, String title, String content, int color) {
        this.date = date;
        this.title = title;
        this.content = content;
        this.color = color;
        historyList = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
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

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public List<HistoryItem> getHistoryList() {
        return historyList;
    }

    public void setHistoryList(List<HistoryItem> historyList) {
        this.historyList = historyList;
    }
}
