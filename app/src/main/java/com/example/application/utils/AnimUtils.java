package com.example.application.utils;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;

import com.example.application.R;

/**
 * Created by YeYun on 2021/4/22 14:54.
 *
 * 动画工具类
 */
public class AnimUtils {

    private static long mLastClickTime = 0;
    // 两次点击按钮之间的点击间隔不能少于600毫秒
    private static final long TIME_INTERVAL = 600L;

    private static OnClickAnimEndListener onClickAnimEndListener;

    public interface OnClickAnimEndListener {
        void onClickAnimEndListener(Animation animator);
    }

    /**
     * 点击时缩小动画效果
     *
     * @param view              执行动画的控件
     * @param context           上下文
     * @param onAnimEndListener 动画结束监听
     */
    public static void setOnClickAnimEndListener(View view, Context context, OnClickAnimEndListener onAnimEndListener) {
        long nowTime = System.currentTimeMillis();
        if (nowTime - mLastClickTime > TIME_INTERVAL) {
            mLastClickTime = nowTime;
            ScaleAnimation scaleAnimation = (ScaleAnimation) AnimationUtils.loadAnimation(context, R.anim.view_click_zoom);
            view.startAnimation(scaleAnimation);
            scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    onClickAnimEndListener.onClickAnimEndListener(animation);
                    scaleAnimation.cancel();
                    scaleAnimation.reset();
                    view.clearAnimation();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
        }
        onClickAnimEndListener = onAnimEndListener;
    }

} 