package com.example.mohamed.mymedeciene.widght;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by mohamed mabrouk
 * 0201152644726
 * on 13/01/2018.  time :15:56
 */

public class WidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {

        return new MedicineDataProvider(getApplicationContext(), intent);
    }
}
