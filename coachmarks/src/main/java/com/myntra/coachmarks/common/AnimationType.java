package com.myntra.coachmarks.common;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({AnimationType.THROB_ANIMATION,
        AnimationType.SCALE_ANIMATION,
        AnimationType.ALPHA_ANIMATION,
        AnimationType.ANIMATION_NONE})
@Retention(RetentionPolicy.SOURCE)
public @interface AnimationType {
    int THROB_ANIMATION = 0;
    int SCALE_ANIMATION = 1;
    int ALPHA_ANIMATION = 2;
    int ANIMATION_NONE = 3;
}