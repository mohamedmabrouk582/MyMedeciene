package com.example.mohamed.mymedeciene.Holder;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mohamed.mymedeciene.R;
import com.example.mohamed.mymedeciene.data.Comment;
import com.example.mohamed.mymedeciene.data.Pharmacy;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Mohammad mabrouk
 * 0201152644726
 * on 4/19/2018.  time :01:24
 */
public class CommentHolder extends RecyclerView.ViewHolder {
    private Activity activity;
    CircleImageView userImageView;
    TextView userComment,userName;
    public CommentHolder(View itemView,Activity activity) {
        super(itemView);
        this.activity=activity;
        ini();
    }

    private void ini(){
        userImageView=itemView.findViewById(R.id.co_user_img);
        userName=itemView.findViewById(R.id.co_user_name);
        userComment=itemView.findViewById(R.id.co_user_comment);
    }

    public void bind(Comment comment){
        Pharmacy pharmacy=comment.getPharmacy();
        if (pharmacy!=null)
            Glide.with(activity).load(pharmacy.getPhImgURL()).into(userImageView);
        userName.setText(pharmacy.getPhName());
        userComment.setText(comment.getContent());
    }
}
