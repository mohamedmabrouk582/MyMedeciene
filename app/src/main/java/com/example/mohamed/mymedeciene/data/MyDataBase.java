package com.example.mohamed.mymedeciene.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;

/**
 * Created by Mohamed mabrouk
 * 0201152644726
 * on 04/03/2018.  time :23:11
 */

@Database(entities = {Drug.class,Pharmacy.class},exportSchema = true,version = 1)
public abstract class MyDataBase extends RoomDatabase {
    public  abstract QueryDao getQueryDao();
}
