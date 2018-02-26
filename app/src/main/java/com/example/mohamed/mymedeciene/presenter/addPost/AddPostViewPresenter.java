package com.example.mohamed.mymedeciene.presenter.addPost;

import android.app.Activity;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.mohamed.mymedeciene.appliction.MyApp;
import com.example.mohamed.mymedeciene.presenter.base.BasePresenter;
import com.example.mohamed.mymedeciene.utils.AddListener;
import com.example.mohamed.mymedeciene.view.AddPostView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import id.zelory.compressor.Compressor;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Mohamed mabrouk
 * 0201152644726
 * on 30/01/2018.  time :22:58
 */

public class AddPostViewPresenter<v extends AddPostView> extends BasePresenter<v> implements AddPostPresenter<v> {
    private Activity activity;
    private DatabaseReference mDatabaseReference;
    private StorageReference mStorageReference;
    private FirebaseAuth mAuth;
    private String url;
    private String key;
    public AddPostViewPresenter(Activity activity){
        this.activity=activity;
        mAuth= MyApp.getmAuth();
        mStorageReference=MyApp.getmStorageReference();
        mDatabaseReference=MyApp.getmDatabaseReference();
        DatabaseReference push = FirebaseDatabase.getInstance().getReference().push();
        key=push.getKey();
    }

    @Override
    public void Post(String PostContent, final AddListener listener) {

        Map postMap=new HashMap();

        Map<String,String > post=new HashMap<>();
        post.put("id",key);
        post.put("userId",mAuth.getUid());
        post.put("content",PostContent);
        post.put("imgUrl",url);
        postMap.put("Posts/"+mAuth.getUid()+"/"+key,post);
        postMap.put("Posts/AllPosts/"+key,post);
        postMap.put("Likes/"+key,true);
        postMap.put("Comments/"+key,true);
        postMap.put("Shares/"+key,true);
        mDatabaseReference.updateChildren(postMap, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
               if (databaseError==null){
                   listener.onSuccess("done");
               }   else {
                   listener.OnError(databaseError.getMessage());
               }
            }
        });

    }

    @Override
    public void PostImg(final Uri img) {
        Log.d("PostImg", img + "");
      getView().showProgress();
      new Compressor(activity)
                .compressToFileAsFlowable(new File(img.getPath()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<File>() {
                  @Override
                  public void accept(File file) throws Exception {
                      @SuppressWarnings("ConstantConditions")
                      String root = "Posts/" + mAuth.getCurrentUser().getUid() + "/" + key;
                      mStorageReference.child(root).putFile(Uri.fromFile(file)).addOnFailureListener(new OnFailureListener() {
                          @Override
                          public void onFailure(@NonNull Exception e) {
                              getView().hideProgress();
                          }
                      }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                          @Override
                          public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                              double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                              getView().setProgress((int)progress);
                          }
                      }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                          @Override
                          public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                              getView().hideProgress();
                              url = String.valueOf(taskSnapshot.getDownloadUrl());
                              getView().setIMG(url);
                          }
                      });

                  }
              });
    }
}
