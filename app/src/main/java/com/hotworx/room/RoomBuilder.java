package com.hotworx.room;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.hotworx.global.Constants;

public class RoomBuilder {
    private static HotworxDatabase hotworxDatabase = null;

    public static HotworxDatabase getHotWorxDatabase(Context context) {
        if (hotworxDatabase == null) {
            return Room.databaseBuilder(context, HotworxDatabase.class, Constants.DATABASE_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return hotworxDatabase;
    }
}

