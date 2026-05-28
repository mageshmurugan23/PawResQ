package com.shrinjal.care4paws.data.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "reports")
public class ReportEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;
    public byte[] image;
    public String animalType;
    public double latitude;
    public double longitude;
    public String status;
    public String imagePath;

    public String userId;
    public String userName;// ✅ VERY IMPORTANT

    public ReportEntity() {
    }

    public ReportEntity(String animalType,
                        double latitude,
                        double longitude,
                        String status,
                        String imagePath,
                        String userId) {

        this.animalType = animalType;
        this.latitude = latitude;
        this.longitude = longitude;
        this.status = status;
        this.imagePath = imagePath;
        this.userId = userId;
    }
}