package com.example.mohamed.mymedeciene.presenter.addPost;

import android.net.Uri;

import com.example.mohamed.mymedeciene.presenter.base.MainPresnter;
import com.example.mohamed.mymedeciene.utils.AddListener;
import com.example.mohamed.mymedeciene.view.AddPostView;

/**
 * Created by Mohamed mabrouk
 * 0201152644726
 * on 30/01/2018.  time :22:56
 */

public interface AddPostPresenter<v extends AddPostView>  extends MainPresnter<v>{
    void Post(String PostContent , AddListener listener);
    void PostImg(Uri uri);
}
