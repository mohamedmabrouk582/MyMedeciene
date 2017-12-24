package com.example.mohamed.mymedeciene.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.mohamed.mymedeciene.R;

/**
 * Created by mohamed mabrouk
 * 0201152644726
 * on 03/12/2017.  time :03:41
 */

public class ZoomIMG {
    private Animator mCurrentAnimator;
    public void zoomImageFromThumb(Activity activity, final View thumbView, String imageResId, Animator CurrentAnimator, final ImageView img_preview
    , View zoomContainer, final int mShortAnimationDuration
    ){
        mCurrentAnimator=CurrentAnimator;

        if (mCurrentAnimator != null) {
            mCurrentAnimator.cancel();
        }

        if (imageResId!=null) {
            Glide.with(activity).load(Uri.parse(imageResId))
                  //  .error(R.drawable.logo)
                    .into(img_preview);
        }else {
             img_preview.setImageResource(R.drawable.logo);

        }
        final Rect startBounds = new Rect();
        final Rect finalBounds = new Rect();
        final Point globalOffset = new Point();
        thumbView.getGlobalVisibleRect(startBounds);
        zoomContainer
                .getGlobalVisibleRect(finalBounds, globalOffset);
        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);

        float startScale;
        if ((float) finalBounds.width() / finalBounds.height()
                > (float) startBounds.width() / startBounds.height()) {
            // Extend start bounds horizontally
            startScale = (float) startBounds.height() / finalBounds.height();
            float startWidth = startScale * finalBounds.width();
            float deltaWidth = (startWidth - startBounds.width()) / 2;
            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        } else {
            // Extend start bounds vertically
            startScale = (float) startBounds.width() / finalBounds.width();
            float startHeight = startScale * finalBounds.height();
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }


        thumbView.setAlpha(0f);
        img_preview.setVisibility(View.VISIBLE);

        img_preview.setPivotX(0f);
        img_preview.setPivotY(0f);

        AnimatorSet set = new AnimatorSet();
        set
                .play(ObjectAnimator.ofFloat(img_preview, View.X,
                        startBounds.left, finalBounds.left))
                .with(ObjectAnimator.ofFloat(img_preview, View.Y,
                        startBounds.top, finalBounds.top))
                .with(ObjectAnimator.ofFloat(img_preview, View.SCALE_X,
                        startScale, 1f)).with(ObjectAnimator.ofFloat(img_preview,
                View.SCALE_Y, startScale, 1f));
        set.setDuration(mShortAnimationDuration);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mCurrentAnimator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mCurrentAnimator = null;
            }
        });
        set.start();
        mCurrentAnimator = set;

        final float startScaleFinal = startScale;

        img_preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCurrentAnimator != null) {
                    mCurrentAnimator.cancel();
                }

                // Animate the four positioning/sizing properties in parallel,
                // back to their original values.
                AnimatorSet set = new AnimatorSet();
                set.play(ObjectAnimator
                        .ofFloat(img_preview, View.X, startBounds.left))
                        .with(ObjectAnimator
                                .ofFloat(img_preview,
                                        View.Y,startBounds.top))
                        .with(ObjectAnimator
                                .ofFloat(img_preview,
                                        View.SCALE_X, startScaleFinal))
                        .with(ObjectAnimator
                                .ofFloat(img_preview,
                                        View.SCALE_Y, startScaleFinal));
                set.setDuration(mShortAnimationDuration);
                set.setInterpolator(new DecelerateInterpolator());
                set.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        thumbView.setAlpha(1f);
                        img_preview.setVisibility(View.GONE);
                        mCurrentAnimator = null;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        thumbView.setAlpha(1f);
                        img_preview.setVisibility(View.GONE);
                        mCurrentAnimator = null;
                    }
                });
                set.start();
                mCurrentAnimator = set;
            }
        });

    }
}
