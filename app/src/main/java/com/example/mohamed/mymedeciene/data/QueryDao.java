package com.example.mohamed.mymedeciene.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Maybe;

/**
 * Created by Mohamed mabrouk
 * 0201152644726
 * on 04/03/2018.  time :23:06
 */

@Dao
public interface QueryDao {
    @Query("select * from pharmacy")
    Maybe<List<Pharmacy>> getPharmacies();

    @Query("select * from drug")
    Maybe<List<Drug>> getAllDrugs();

    @Query("select * from drug where phKey= :phKey")
    Flowable<List<Drug>> getDrugsAtPharmcy(String phKey);

    @Query("select * from drug where name like :name")
    Flowable<List<Drug>> getDrugsByName(String name);

    @Insert
    void insertDrug(Drug ... drugs);

    @Insert
    void insertPharmcy(Pharmacy ... pharmacies);

    @Query("delete from drug")
    void deleteAllDataDrugs();


    @Query("delete from Pharmacy")
    void deleteAllDataPhamrcy();



}
