package com.example.mohamed.mymedeciene.presenter.posts;

import android.app.Activity;
import android.app.FragmentManager;

import com.example.mohamed.mymedeciene.fragment.AddDrugCheckFragment;
import com.example.mohamed.mymedeciene.fragment.AddPostFragment;
import com.example.mohamed.mymedeciene.presenter.base.BasePresenter;
import com.example.mohamed.mymedeciene.utils.AddListener;
import com.example.mohamed.mymedeciene.view.PostsView;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by Mohamed mabrouk
 * 0201152644726
 * on 31/01/2018.  time :00:38
 */

public class PostsViewPresenter<v extends PostsView> extends BasePresenter<v> implements PostsPresenter<v> {
    private Activity activity;
    public PostsViewPresenter(Activity activity){
        this.activity=activity;
    }
    @Override
    public void addPost(AddListener listener) {
        FragmentManager fragmentManager=activity.getFragmentManager();
        if (FirebaseAuth.getInstance().getCurrentUser()==null){
            AddDrugCheckFragment fragment=AddDrugCheckFragment.newFragment("register");
            fragment.show(fragmentManager,"postRe");
        }else {
            AddPostFragment fragment = AddPostFragment.newFragment(listener);
            fragment.show(fragmentManager, "addPost");
        }
    }
}
