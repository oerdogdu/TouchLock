package com.atoi.touchlock.Data.Local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.atoi.touchlock.POJO.Advertisement;
import com.atoi.touchlock.POJO.User;

@Database(entities = {User.class, Advertisement.class}, version = 7)
public abstract class UserRoomDB extends RoomDatabase {
    public abstract UserDAO userDAO();
    public abstract AdvertisementDAO advertisementDAO();

    private static volatile UserRoomDB INSTANCE;

    static UserRoomDB getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (UserRoomDB.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            UserRoomDB.class, "user_db")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}
