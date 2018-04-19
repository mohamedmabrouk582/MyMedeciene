package com.example.mohamed.mymedeciene.presenter.posts;

import android.app.Activity;
import android.app.FragmentManager;

import com.example.mohamed.mymedeciene.appliction.MyApp;
import com.example.mohamed.mymedeciene.fragment.AddDrugCheckFragment;
import com.example.mohamed.mymedeciene.fragment.AddPostFragment;
import com.example.mohamed.mymedeciene.presenter.base.BasePresenter;
import com.example.mohamed.mymedeciene.utils.AddListener;
import com.example.mohamed.mymedeciene.utils.RxBus;
import com.example.mohamed.mymedeciene.view.PostsView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mohamed mabrouk
 * 0201152644726
 * on 31/01/2018.  time :00:38
 */

public class PostsViewPresenter<v extends PostsView> extends BasePresenter<v> implements PostsPresenter<v> {
    private Activity activity;
    private DatabaseReference mLikeReference,mCommentReference;
    private FirebaseAuth mAuth;
    private FragmentManager fragmentManager;
    public PostsViewPresenter(Activity activity){
        this.activity=activity;
        mAuth= MyApp.getmAuth();
        mLikeReference=MyApp.getmDatabaseReference().child("Likes");
        mCommentReference=MyApp.getmDatabaseReference().child("Comments");
        fragmentManager=activity.getFragmentManager();

    }

    @Override
    public void addPost(AddListener listener) {
        if (FirebaseAuth.getInstance().getCurrentUser()==null){
            ShowRegisterDao();
        }else {
            AddPostFragment fragment = AddPostFragment.newFragment(listener);
            fragment.show(fragmentManager, "addPost");
        }
    }

    public void ShowRegisterDao(){
        AddDrugCheckFragment fragment=AddDrugCheckFragment.newFragment("register");
        fragment.show(fragmentManager,"postRe");
    }

    @Override
    public void like(boolean isLike,String postId) {
        Map likeMap=new HashMap<>();
        if (isLike){
            likeMap.put(postId +"/"+mAuth.getUid(),true);
        }else {
            likeMap.put(postId +"/"+mAuth.getUid(),null);
        }
        mLikeReference.updateChildren(likeMap, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError==null){
                    RxBus.getRxBus().sendIsLike(isLike);
                }
            }
        });
    }

    @Override
    public void sendComment(String comment,String postId) {
     Map commentMap=new HashMap();
        long unixTime = System.currentTimeMillis() / 1000L;
     commentMap.put(postId+"/"+unixTime+"/userId",mAuth.getUid());
     commentMap.put(postId+"/"+unixTime+"/content",comment);
     mCommentReference.updateChildren(commentMap, new DatabaseReference.CompletionListener() {
         @Override
         public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
            RxBus.getRxBus().sendComment(databaseError!=null);
         }
     });
    }
}
