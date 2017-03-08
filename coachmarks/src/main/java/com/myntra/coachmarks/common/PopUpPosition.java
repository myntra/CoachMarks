package com.myntra.coachmarks.common;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({PopUpPosition.LEFT,
        PopUpPosition.RIGHT,
        PopUpPosition.BOTTOM,
        PopUpPosition.TOP,
        PopUpPosition.NONE})
@Retention(RetentionPolicy.SOURCE)
public @interface PopUpPosition {
    int LEFT = 0;
    int RIGHT = 1;
    int TOP = 2;
    int BOTTOM = 3;
    int NONE = 4;
}