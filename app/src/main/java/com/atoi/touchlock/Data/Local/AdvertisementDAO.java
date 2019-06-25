package com.atoi.touchlock.Data.Local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.atoi.touchlock.POJO.Advertisement;

import java.util.List;

@Dao
public interface AdvertisementDAO {
    @Insert
    void insert(Advertisement advertisement);

    @Delete
    void delete(Advertisement... advertisements);

    @Query("SELECT * FROM advertisement WHERE userEmail=:userEmail")
    List<Advertisement> findAdsForUser(final String userEmail);
}
