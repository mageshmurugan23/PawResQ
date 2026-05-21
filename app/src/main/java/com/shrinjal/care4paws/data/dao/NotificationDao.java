package com.shrinjal.care4paws.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.shrinjal.care4paws.data.entity.NotificationEntity;

import java.util.List;

@Dao
public interface NotificationDao {

    @Insert
    void insert(NotificationEntity notification);

    @Query("SELECT * FROM notifications WHERE userId = :userId AND isRead = 0")
    List<NotificationEntity> getUnreadNotifications(String userId);

    @Query("UPDATE notifications SET isRead = 1 WHERE userId = :userId")
    void markAllRead(String userId);
}