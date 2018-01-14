package com.example.mohamed.mymedeciene.data.dataBase;

/**
 * Created by mohamed mabrouk
 * 0201152644726
 * on 07/01/2018.  time :00:11
 */

@SuppressWarnings("unused")
class DBshema {
    public static class TableDrug {
        public static final String NAME = "drugs";

        public static class CLOS {
            public static final String ID = "id";
            public static final String NAME = "name";
            public static final String TYPE = "type";
            public static final String PRICE = "price";
            public static final String QUANTITY = "quantity";
            public static final String IMG = "img";
            public static final String PHID = "phId";

        }

    }
}
