package com.myntra.coachmarks.common;

import android.support.annotation.IntDef;
import android.view.Gravity;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({CoachMarkTextGravity.LEFT,
        CoachMarkTextGravity.CENTER,
        CoachMarkTextGravity.RIGHT})
@Retention(RetentionPolicy.SOURCE)
public @interface CoachMarkTextGravity {
    int LEFT = Gravity.LEFT;
    int CENTER = Gravity.CENTER;
    int RIGHT = Gravity.RIGHT;
}