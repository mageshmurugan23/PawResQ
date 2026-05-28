package com.shrinjal.care4paws.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.shrinjal.care4paws.data.entity.ReportEntity;

import java.util.List;

@Dao
public interface ReportDao {

    @Insert
    void insert(ReportEntity report);

    @Delete
    void delete(ReportEntity report);

    @Query("SELECT * FROM reports WHERE userId = :uid ORDER BY id DESC")
    List<ReportEntity> getReportsByUser(String uid);

    @Query("SELECT COUNT(*) FROM reports WHERE userId = :uid")
    int getTotalByUser(String uid);

    @Query("SELECT COUNT(*) FROM reports WHERE userId = :uid AND status = 'Pending'")
    int getPendingByUser(String uid);

    @Query("SELECT COUNT(*) FROM reports WHERE userId = :uid AND status = 'Rescued'")
    int getRescuedByUser(String uid);

    @Query("SELECT * FROM reports ORDER BY id DESC")
    List<ReportEntity> getAllReports();

    @Query("SELECT COUNT(*) FROM reports")
    int getTotalCount();
    @Query("SELECT COUNT(*) FROM reports WHERE userId = :uid")
    int getUserReportCount(String uid);
    @Query("SELECT COUNT(*) FROM reports WHERE status = 'Rescued'")
    int getRescuedCount();

    @Query("SELECT COUNT(*) FROM reports WHERE status = 'Pending'")
    int getPendingCount();

    @Query("UPDATE reports SET status = :status WHERE id = :id")
    void updateStatus(int id, String status);


    @Query("SELECT * FROM reports WHERE status = 'Pending' ORDER BY id DESC")
    List<ReportEntity> getPendingReports();

    @Query("SELECT * FROM reports WHERE status = 'Rescued' ORDER BY id DESC")
    List<ReportEntity> getRescuedReports();


    @Query("SELECT * FROM reports WHERE userId LIKE '%' || :uid || '%' ORDER BY id DESC")
    List<ReportEntity> searchByUser(String uid);
    @Query("SELECT * FROM reports WHERE animalType LIKE '%' || :text || '%' OR status LIKE '%' || :text || '%'")
    List<ReportEntity> searchReports(String text);

    @Query("SELECT COUNT(DISTINCT userId) FROM reports")
    int getTotalUsers();
}