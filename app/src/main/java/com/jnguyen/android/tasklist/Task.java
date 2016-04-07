package com.jnguyen.android.tasklist;

import java.util.Date;
import java.util.UUID;

public class Task {

    private UUID mId;
    private String mTitle;
    private Date mDate;
    private boolean mDone;
    private String mContact;

    public Task() {
        this(UUID.randomUUID());
    }

    public Task(UUID id) {
        mId = id;
        mDate = new Date();
    }

    public UUID getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public void setTime(Date date) {
        mDate = date;
    }

    public boolean isDone() {
        return mDone;
    }

    public void setDone(boolean done) {
        mDone = done;
    }

    public String getContact() {
        return mContact;
    }

    public void setContact(String contact) {
        mContact = contact;
    }
}