package com.example.mohamed.mymedeciene.utils;

import android.annotation.SuppressLint;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * Created by Mohammad mabrouk
 * 0201152644726
 * on 4/18/2018.  time :22:29
 */
public class RxBus {
    private static RxBus rxBus;
    private CompositeDisposable compositeDisposable=new CompositeDisposable();
    public static RxBus getRxBus(){
        if (rxBus==null){
            synchronized (RxBus.class){
                rxBus=new RxBus();
            }
        }
        return rxBus;
    }


    private Subject<Boolean> isLike;
    private Subject<Boolean> comment;

    /************* all *********/
    @SuppressLint("CheckResult")
    private Subject<Boolean> getComment(){
        if (comment==null){
            comment=PublishSubject.create();
            comment.observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io());
        }
        return comment;
    }

    public Disposable subscribeComment(Consumer<Boolean> consumer){
        Disposable subscribe = getComment().subscribe(consumer);
        compositeDisposable.add(subscribe);
        return subscribe;
    }

    public void sendComment(Boolean o){
        getComment().onNext(o);
    }

    /************** is liked *******************/
    @SuppressLint("CheckResult")
    private Subject<Boolean> getIsLike(){
        if (isLike==null){
            isLike= PublishSubject.create();
            isLike.observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io());
        }
        return isLike;
    }

    public Disposable subscribeIsLike(Consumer<Boolean> consumer){
        Disposable subscribe = getIsLike().subscribe(consumer);
        compositeDisposable.add(subscribe);
        return subscribe;
    }

    public void sendIsLike(Boolean isLike){
        getIsLike().onNext(isLike);
    }

    public void unSubscribe(Disposable disposable){
        compositeDisposable.remove(disposable);
    }
}
