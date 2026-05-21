package com.shrinjal.care4paws.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.shrinjal.care4paws.data.dao.FeedbackDao;
import com.shrinjal.care4paws.data.dao.NotificationDao;
import com.shrinjal.care4paws.data.dao.ReportDao;
import com.shrinjal.care4paws.data.entity.FeedbackEntity;
import com.shrinjal.care4paws.data.entity.NotificationEntity;
import com.shrinjal.care4paws.data.entity.ReportEntity;

@Database(
        entities = {ReportEntity.class, FeedbackEntity.class, NotificationEntity.class},
        version = 5,   // 🔥 increase version because new table added
        exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;

    public abstract ReportDao reportDao();

    public abstract NotificationDao notificationDao();


    public abstract FeedbackDao feedbackDao();   // ✅ Added

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "care4paws_db"
                    )
                    .fallbackToDestructiveMigration()  // 🔥 resets DB on schema change
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }
}