package com.example.mohamed.mymedeciene.view;

import android.net.Uri;

/**
 * Created by Mohamed mabrouk
 * 0201152644726
 * on 30/01/2018.  time :22:54
 */

public interface AddPostView extends MainView {
    void close();
    void setIMG(String uri);
    void showProgress();
    void hideProgress();
    void setProgress(int por);
}
