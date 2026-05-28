package com.shrinjal.care4paws.data.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.Ignore;   // 🔥 ADD THIS

@Entity(tableName = "notifications")
public class NotificationEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String userId;
    public String message;
    public boolean isRead;

    // ✅ Room will use this
    public NotificationEntity() {
    }

    // ❌ Tell Room to ignore this constructor
    @Ignore
    public NotificationEntity(String userId, String message) {
        this.userId = userId;
        this.message = message;
        this.isRead = false;
    }
}