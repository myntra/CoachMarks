package com.myntra.coachmarks.builder;

import android.os.Parcelable;
import android.support.annotation.DimenRes;

import com.google.auto.value.AutoValue;
import com.myntra.coachmarks.R;

@AutoValue
public abstract class CoachMarkLayoutMargin implements Parcelable {

    public static CoachMarkLayoutMargin.Builder create() {
        return new AutoValue_CoachMarkLayoutMargin.Builder()
                .setLeftMarginForCoachMark(R.dimen.coach_mark_zero_dp)
                .setRightMarginForCoachMark(R.dimen.coach_mark_zero_dp)
                .setTopMarginForCoachMark(R.dimen.coach_mark_zero_dp)
                .setBottomMarginForCoachMark(R.dimen.coach_mark_zero_dp);
    }

    @DimenRes
    public abstract int getLeftMarginForCoachMark();

    @DimenRes
    public abstract int getRightMarginForCoachMark();

    @DimenRes
    public abstract int getTopMarginForCoachMark();

    @DimenRes
    public abstract int getBottomMarginForCoachMark();

    @AutoValue.Builder
    public static abstract class Builder {

        public abstract Builder setLeftMarginForCoachMark(@DimenRes int leftMarginForView);

        public abstract Builder setRightMarginForCoachMark(@DimenRes int rightMarginForView);

        public abstract Builder setTopMarginForCoachMark(@DimenRes int topMarginForView);

        public abstract Builder setBottomMarginForCoachMark(@DimenRes int bottomMarginForView);

        public abstract CoachMarkLayoutMargin build();

    }
}
