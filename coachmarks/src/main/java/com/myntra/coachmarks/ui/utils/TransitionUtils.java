package com.myntra.coachmarks.ui.utils;

import android.animation.ValueAnimator;
import android.support.annotation.NonNull;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;

public class TransitionUtils {

    private static final double ALPHA_ANIMATION_FROM = 1;
    private static final double ALPHA_ANIMATION_TO = 0;

    private static final double SCALE_ANIMATION_FROM_X = 1;
    private static final double SCALE_ANIMATION_TO_X = 1.5;
    private static final double SCALE_ANIMATION_FROM_Y = 1;
    private static final double SCALE_ANIMATION_TO_Y = 1.5;

    private static final double SCALE_ANIMATION_PIVOT_X = .5;
    private static final double SCALE_ANIMATION_PIVOT_Y = .5;

    private static final int ALPHA_ANIMATION_DURATION = 2000;
    private static final int SCALE_ANIMATION_DURATION = 2000;

    private TransitionUtils() {
        //Do nothing, This is added to avoid style check errors
    }

    @NonNull
    public static AnimationSet createThrobAnimation() {
        AnimationSet animationSet = new AnimationSet(false);
        animationSet.addAnimation(createAlphaAnimation());
        animationSet.addAnimation(createScaleAnimation());
        return animationSet;
    }

    @NonNull
    public static Animation createAlphaAnimation() {
        ScaleAnimation scaleAnimation = new ScaleAnimation((float) SCALE_ANIMATION_FROM_X,
                (float) SCALE_ANIMATION_TO_X, (float) SCALE_ANIMATION_FROM_Y,
                (float) SCALE_ANIMATION_TO_Y, Animation.RELATIVE_TO_SELF,
                (float) SCALE_ANIMATION_PIVOT_X, Animation.RELATIVE_TO_SELF,
                (float) SCALE_ANIMATION_PIVOT_Y);
        scaleAnimation.setDuration(SCALE_ANIMATION_DURATION);
        scaleAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        scaleAnimation.setRepeatCount(ValueAnimator.INFINITE);
        scaleAnimation.setRepeatMode(ValueAnimator.RESTART);
        return scaleAnimation;
    }

    @NonNull
    public static Animation createScaleAnimation() {
        AlphaAnimation alphaAnimation = new AlphaAnimation((float) ALPHA_ANIMATION_FROM,
                (float) ALPHA_ANIMATION_TO);
        alphaAnimation.setDuration(ALPHA_ANIMATION_DURATION);
        alphaAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        alphaAnimation.setRepeatCount(ValueAnimator.INFINITE);
        alphaAnimation.setRepeatMode(ValueAnimator.RESTART);
        return alphaAnimation;
    }

}
