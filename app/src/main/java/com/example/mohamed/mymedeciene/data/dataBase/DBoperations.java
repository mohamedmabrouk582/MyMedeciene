package com.example.mohamed.mymedeciene.data.dataBase;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.example.mohamed.mymedeciene.data.Drug;
import com.example.mohamed.mymedeciene.data.dataBase.DBshema.TableDrug;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mohamed mabrouk
 * 0201152644726
 * on 07/01/2018.  time :00:06
 */

@SuppressWarnings("unused")
public class DBoperations {
    @SuppressLint("StaticFieldLeak")
    private static DBoperations mD_BOPERATIONS = null;
    private final Context mContext;
    private final SQLiteDatabase mDatabase;

    private DBoperations(Context context) {
        this.mContext = context.getApplicationContext();
        mDatabase = new DBsqliteHelper(mContext).getWritableDatabase();
        if (mD_BOPERATIONS != null)
            throw new RuntimeException("not java reflection with me");
    }

    public static DBoperations getInstance(Context context) {
        if (mD_BOPERATIONS == null) {
            synchronized (DBoperations.class) {
                mD_BOPERATIONS = new DBoperations(context);
            }
        }
        return mD_BOPERATIONS;
    }

    private ContentValues getValues(Drug drug) {
        ContentValues values = new ContentValues();
        values.put(TableDrug.CLOS.ID, drug.getPhKey() + drug.getName() + drug.getType());
        values.put(TableDrug.CLOS.NAME, drug.getName());
        values.put(TableDrug.CLOS.TYPE, drug.getType());
        values.put(TableDrug.CLOS.IMG, drug.getImg());
        values.put(TableDrug.CLOS.PRICE, drug.getPrice());
        values.put(TableDrug.CLOS.PHID, drug.getPhKey());
        values.put(TableDrug.CLOS.QUANTITY, drug.getQuantity());
        return values;
    }

    private DrugWrapper quary(String Name, String whereClause, String[] whereArgs) {
        @SuppressLint("Recycle") Cursor cursor = mDatabase.query(
                Name,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        return new DrugWrapper(cursor);
    }

    public void insertDrug(Drug drug) {
        ContentValues values = getValues(drug);
        Uri insert = mContext.getContentResolver().insert(DrugProvider.CONTENT_URI, getValues(drug));
        // return  mDatabase.insert(TableDrug.NAME,null,values)>0?"done":"error";
    }

    public void deleteAll() {
        mContext.getContentResolver().delete(DrugProvider.CONTENT_URI, null, null);
    }

    public List<Drug> getDrugs() {
        DrugWrapper wrapper = new DrugWrapper(mContext.getContentResolver().query(DrugProvider.CONTENT_URI, null, null, null, null));

        // DrugWrapper wrapper=quary(TableDrug.NAME,null,null);

        List<Drug> drugs = new ArrayList<>();
        wrapper.moveToFirst();
        try {
            while (!wrapper.isAfterLast()) {
                drugs.add(wrapper.getDrug());
                wrapper.moveToNext();
            }
        } finally {
            wrapper.close();
        }
        return drugs;
    }


}
