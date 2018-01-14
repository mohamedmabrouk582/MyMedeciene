package com.example.mohamed.mymedeciene.widght;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import android.widget.Toast;

import com.example.mohamed.mymedeciene.R;
import com.example.mohamed.mymedeciene.activity.SplashActivity;
import com.example.mohamed.mymedeciene.data.Drug;
import com.example.mohamed.mymedeciene.data.dataBase.DBoperations;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mohamed mabrouk
 * 0201152644726
 * on 13/01/2018.  time :15:49
 */

public class MedicineDataProvider implements RemoteViewsService.RemoteViewsFactory {
    List<Drug> mCollections = new ArrayList();

    Context mContext = null;

    public MedicineDataProvider(Context context, Intent intent) {
        mContext = context;
    }

    @Override
    public void onCreate() {
       initData();
    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return mCollections.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        Drug  drug=mCollections.get(position);
        RemoteViews mViews=new RemoteViews(mContext.getPackageName(), R.layout.item_view);
        mViews.setTextViewText(R.id.w_drug_name,drug.getName());
        mViews.setTextViewText(R.id.w_drug_price,drug.getPrice());
        mViews.setTextViewText(R.id.w_drug_quantity,drug.getQuantity());
        mViews.setTextViewText(R.id.w_drug_type,drug.getType());
        try {
            URL url = new URL(drug.getImg());
            Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            mViews.setImageViewBitmap(R.id.item_img,image);
        } catch(IOException e) {
            System.out.println(e);
        }

     //   Intent fillInIntent = new Intent();
//        fillInIntent.setAction(MedicineWidget.ACTION_TOAST);
//        mViews.setOnClickFillInIntent(R.id.item_layout, fillInIntent);

        return mViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    private void initData() {
        mCollections.clear();
         mCollections.addAll(DBoperations.getInstance(mContext).getDrugs());
        Log.d("alldrugs", mCollections.size() + "");
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
