package com.example.mohamed.mymedeciene.data.dataBase;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.mohamed.mymedeciene.data.dataBase.DBshema.TableDrug;

import java.util.HashMap;

/**
 * Created by mohamed mabrouk
 * 0201152644726
 * on 07/01/2018.  time :01:22
 */

public class DrugProvider extends ContentProvider {
    private DBsqliteHelper dbHelper;
    private static final int ALL_DRUGS= 1;
    private static final int SINGLE_DRUG= 2;
    private static final String AUTHORITY = "com.example.mohamed.mymedeciene.ContentProvider.contentprovider";
    public static final Uri CONTENT_URI =
            Uri.parse("content://" + AUTHORITY + "/drugs");
    private static SQLiteDatabase mDatabase;
    private static HashMap<String, String> values;
    static final String id = "id";
    static final String title = "title";

    private static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, "drugs", ALL_DRUGS);
        uriMatcher.addURI(AUTHORITY, "drugs/#", SINGLE_DRUG);
    }
    @Override
    public boolean onCreate() {
        dbHelper = new DBsqliteHelper(getContext());
        mDatabase=dbHelper.getWritableDatabase();
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(TableDrug.NAME);
        switch (uriMatcher.match(uri)) {
            case ALL_DRUGS:
                queryBuilder.setProjectionMap(values);
                break;
            case SINGLE_DRUG:
//                String id = uri.getPathSegments().get(1);
//                queryBuilder.appendWhere(TableFav.CLOS.ID+ " =" + id);
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        Cursor cursor = queryBuilder.query(mDatabase, projection, selection,
                selectionArgs, null, null, sortOrder);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (uriMatcher.match(uri)) {
            case ALL_DRUGS:
                return "vnd.android.cursor.dir/vnd.com.example.mohamed.mymedeciene.ContentProvider.contentprovider.drugs";
            case SINGLE_DRUG:
                return "vnd.android.cursor.item/vnd.com.example.mohamed.mymedeciene.ContentProvider.contentprovider.drugs";
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        switch (uriMatcher.match(uri)) {
            case ALL_DRUGS:
                //do nothing
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        long id = mDatabase.insert(TableDrug.NAME, null, values);

        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(CONTENT_URI + "/" + id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        switch (uriMatcher.match(uri)) {
            case ALL_DRUGS:
                //do nothing
                break;
            case SINGLE_DRUG:
//                String id = uri.getPathSegments().get(1);
//                selection = TableFav.CLOS.ID + "=" + id
//                        + (!TextUtils.isEmpty(selection) ?
//                        " AND (" + selection + ')' : "");
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        int deleteCount =mDatabase.delete(TableDrug.NAME, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return deleteCount;
        // int deleteCount = mDatabase.delete(TableFav.NAME,TableFav.CLOS.ID+" = "+id, selectionArgs);

    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }




}
