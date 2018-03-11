package com.example.mohamed.mymedeciene.data;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.ViewModel;
import android.arch.persistence.room.Room;
import android.support.annotation.NonNull;

import com.example.mohamed.mymedeciene.utils.DataBaseCallBack;

import org.reactivestreams.Subscription;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Mohamed mabrouk
 * 0201152644726
 * on 04/03/2018.  time :23:24
 */

public class DbViewModel extends AndroidViewModel {
   private static  DbViewModel model;
   private MyDataBase myDataBase;
   private QueryDao queryDao;


    private<t> DbViewModel(@NonNull Application application) {
        super(application);
        myDataBase= Room.databaseBuilder(application,MyDataBase.class,"drugs").build();
        queryDao=myDataBase.getQueryDao();

    }

    public static DbViewModel getModel(Application application){
       if (model==null){
           synchronized (DbViewModel.class){
               model=new DbViewModel(application);
           }
       }
       return model;
   }

   public void getAllDrugs(final DataBaseCallBack<Drug> callBack){

       queryDao.getAllDrugs().
                observeOn(AndroidSchedulers.mainThread())
               .subscribeOn(Schedulers.io())
               .subscribeWith(new MaybeObserver<List<Drug>>() {
           @Override
           public void onSubscribe(Disposable d) {

           }

           @Override
           public void onSuccess(List<Drug> drugs) {
             callBack.onResponse(drugs);
           }

           @Override
           public void onError(Throwable e) {
             callBack.onError(e);
           }

           @Override
           public void onComplete() {

           }
       });
   }

   public void getAllPhrmacy(final DataBaseCallBack<Pharmacy> callBack){
       queryDao.getPharmacies().
               observeOn(AndroidSchedulers.mainThread())
               .subscribeOn(Schedulers.io()).
               subscribeWith(new MaybeObserver<List<Pharmacy>>() {
           @Override
           public void onSubscribe(Disposable d) {

           }

           @Override
           public void onSuccess(List<Pharmacy> pharmacies) {
             callBack.onResponse(pharmacies);
           }

           @Override
           public void onError(Throwable e) {
            callBack.onError(e);
           }

           @Override
           public void onComplete() {

           }
       });
   }

   public void getDrugsAtPharmcy(String phKey, final DataBaseCallBack<Drug> callBack){
       queryDao.getDrugsAtPharmcy(phKey).observeOn(AndroidSchedulers.mainThread())
               .subscribeOn(Schedulers.io())
               .subscribeWith(new FlowableSubscriber<List<Drug>>() {
                   @Override
                   public void onSubscribe(Subscription s) {

                   }

                   @Override
                   public void onNext(List<Drug> drugs) {
                     callBack.onResponse(drugs);
                   }

                   @Override
                   public void onError(Throwable t) {
                     callBack.onError(t);
                   }

                   @Override
                   public void onComplete() {

                   }
               });
   }

   public void getDrugsByName (String name, final DataBaseCallBack<Drug> callBack){
       queryDao.getDrugsByName(name).observeOn(AndroidSchedulers.mainThread())
               .subscribeOn(Schedulers.io())
               .subscribeWith(new FlowableSubscriber<List<Drug>>() {
                   @Override
                   public void onSubscribe(Subscription s) {

                   }

                   @Override
                   public void onNext(List<Drug> drugs) {
                    callBack.onResponse(drugs);
                   }

                   @Override
                   public void onError(Throwable t) {
                    callBack.onError(t);
                   }

                   @Override
                   public void onComplete() {

                   }
               });
   }






}
