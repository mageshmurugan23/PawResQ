package com.shrinjal.care4paws.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.shrinjal.care4paws.data.entity.FeedbackEntity;

import java.util.List;

@Dao
public interface FeedbackDao {

    @Insert
    void insert(FeedbackEntity feedback);

    @Query("SELECT * FROM feedback ORDER BY id DESC")
    List<FeedbackEntity> getAllFeedback();

    // 🔥 ADD THIS (for user count)
    @Query("SELECT COUNT(DISTINCT userName) FROM feedback")
    int getTotalUsers();

    // 🔥 OPTIONAL (for user-specific feedback)
    @Query("SELECT * FROM feedback WHERE userName = :name ORDER BY id DESC")
    List<FeedbackEntity> getFeedbackByUser(String name);
}