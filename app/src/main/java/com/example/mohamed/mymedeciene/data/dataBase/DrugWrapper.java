package com.example.mohamed.mymedeciene.data.dataBase;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.example.mohamed.mymedeciene.data.Drug;
import com.example.mohamed.mymedeciene.data.dataBase.DBshema.TableDrug;

/**
 * Created by mohamed mabrouk
 * 0201152644726
 * on 07/01/2018.  time :00:35
 */

@SuppressWarnings({"unused", "UnnecessaryLocalVariable"})
class DrugWrapper extends CursorWrapper {
    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
    public DrugWrapper(Cursor cursor) {
        super(cursor);
    }

    public Drug getDrug() {
        String id = getString(getColumnIndex(TableDrug.CLOS.ID));
        String name = getString(getColumnIndex(TableDrug.CLOS.NAME));
        String type = getString(getColumnIndex(TableDrug.CLOS.TYPE));
        String price = getString(getColumnIndex(TableDrug.CLOS.PRICE));
        String quantity = getString(getColumnIndex(TableDrug.CLOS.QUANTITY));
        String phId = getString(getColumnIndex(TableDrug.CLOS.PHID));
        String img = getString(getColumnIndex(TableDrug.CLOS.IMG));

        Drug drug = new Drug(name, type, price, quantity, img, phId);
        return drug;
    }
}
