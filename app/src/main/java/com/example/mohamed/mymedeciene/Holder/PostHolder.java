package com.example.mohamed.mymedeciene.Holder;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mohamed.mymedeciene.R;
import com.example.mohamed.mymedeciene.data.Post;

/**
 * Created by Mohamed mabrouk
 * 0201152644726
 * on 31/01/2018.  time :00:50
 */

public class PostHolder extends RecyclerView.ViewHolder {
    private Activity activity;
   public ImageView postIMG;
    public TextView postContent;

    public PostHolder(View itemView, Activity activity) {
        super(itemView);
        this.activity=activity;
        init();
    }

    private void init(){
        postIMG=itemView.findViewById(R.id.img_post);
        postContent=itemView.findViewById(R.id.txt_post);

    }

    public void bind(Post post){
        if (!TextUtils.isEmpty(post.getImgUrl())) {
            postIMG.setVisibility(View.VISIBLE);
            Glide.with(activity).load(post.getImgUrl()).into(postIMG);
        }
        postContent.setText(post.getContent());

    }

}
