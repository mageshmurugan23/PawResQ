package com.shrinjal.care4paws.data.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "feedback")
public class FeedbackEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String message;
    public String userName;
    public long timestamp;
}