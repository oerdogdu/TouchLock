package com.atoi.touchlock.Data.Local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.atoi.touchlock.POJO.User;

@Dao
public interface UserDAO {
    @Insert
    long insert(User user);

    @Query("SELECT * FROM user WHERE email LIKE :email")
    User getUserByEmail(String email);

}
