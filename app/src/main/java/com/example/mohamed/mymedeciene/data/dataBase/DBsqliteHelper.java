package com.example.mohamed.mymedeciene.data.dataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.mohamed.mymedeciene.data.dataBase.DBshema.TableDrug;

/**
 * Created by mohamed mabrouk
 * 0201152644726
 * on 07/01/2018.  time :00:11
 */

class DBsqliteHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String dbNAME = "drugs.db";

    public DBsqliteHelper(Context context) {
        super(context, dbNAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TableDrug.NAME + "(" +
                " _id integer primary key autoincrement, " +
                TableDrug.CLOS.ID + " UNIQUE, " +
                TableDrug.CLOS.NAME + " TEXT, " +
                TableDrug.CLOS.QUANTITY + " , " +
                TableDrug.CLOS.TYPE + " , " +
                TableDrug.CLOS.IMG + " , " +
                TableDrug.CLOS.PRICE + " , " +
                TableDrug.CLOS.PHID +
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("ALTER " + TableDrug.NAME);
        onCreate(db);
    }
}
