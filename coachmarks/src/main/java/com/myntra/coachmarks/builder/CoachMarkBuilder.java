package com.myntra.coachmarks.builder;

import android.graphics.Point;
import android.os.Parcelable;
import android.support.annotation.AnimRes;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;

import com.google.auto.value.AutoValue;
import com.myntra.coachmarks.R;
import com.myntra.coachmarks.common.CoachMarkTextGravity;
import com.myntra.coachmarks.common.DialogDismissButtonPosition;
import com.myntra.coachmarks.common.PopUpPosition;

import java.util.ArrayList;

@AutoValue
public abstract class CoachMarkBuilder implements Parcelable {

    public static Builder create(Point coachMarkViewAnchorTop,
                                 Point coachMarkViewAnchorBottom,
                                 @StringRes int coachMarkMessage) {
        return new AutoValue_CoachMarkBuilder.Builder()
                .setAnchorTop(coachMarkViewAnchorTop)
                .setCoachMarkMessage(coachMarkMessage)
                .setNotchPosition(0.0)
                .setAnchorBottom(coachMarkViewAnchorBottom)
                .setFooterHeight(R.dimen.coach_mark_zero_dp)
                .setActionBarHeight(R.dimen.coach_mark_zero_dp)
                .setCoachMarkTextGravity(CoachMarkTextGravity.LEFT)
                .setInfoForViewToMaskList(new ArrayList<InfoForViewToMask>(0))
                .setImageDrawableRes(R.drawable.coachmark_drawable_no_image)
                .setBackGroundTintForImage(R.color.coach_mark_transparent_color)
                .setCoachMarkLayoutMargin(CoachMarkLayoutMargin.create().build())
                .setUserDesiredPopUpPositionWithRespectToView(PopUpPosition.RIGHT)
                .setImageLayoutInformation(ImageLayoutInformation.create(R.dimen.coach_mark_zero_dp, R.dimen.coach_mark_zero_dp).build())
                .setPopUpCoachMarkDismissButtonPosition(DialogDismissButtonPosition.RIGHT)
                .setAnimationOnImage(R.anim.coach_mark_animation);
    }

    public Builder newBuilder() {
        return new AutoValue_CoachMarkBuilder.Builder(this);
    }

    public abstract Point getAnchorTop();

    public abstract Point getAnchorBottom();

    @StringRes
    public abstract int getCoachMarkMessage();

    @DimenRes
    public abstract int getActionBarHeight();

    @DimenRes
    public abstract int getFooterHeight();

    @Nullable
    public abstract ArrayList<InfoForViewToMask> getInfoForViewToMaskList();

    @CoachMarkTextGravity
    public abstract int getCoachMarkTextGravity();

    @Nullable
    public abstract String getFontStyleForDismissButton();

    @Nullable
    public abstract String getFontStyleForCoachMarkText();

    @DialogDismissButtonPosition
    public abstract int getPopUpCoachMarkDismissButtonPosition();

    public abstract double getNotchPosition();

    @PopUpPosition
    public abstract int getUserDesiredPopUpPositionWithRespectToView();

    @ColorRes
    public abstract int getBackGroundTintForImage();

    @DrawableRes
    public abstract int getImageDrawableRes();

    public abstract CoachMarkLayoutMargin getCoachMarkLayoutMargin();

    public abstract ImageLayoutInformation getImageLayoutInformation();

    @AnimRes
    public abstract int getAnimationOnImage();

    @AutoValue.Builder
    public static abstract class Builder {

        public abstract Builder setAnchorTop(Point anchorTop);

        public abstract Builder setAnchorBottom(Point anchorBottom);

        public abstract Builder setCoachMarkMessage(@StringRes int coachMarkMessage);

        public abstract Builder setCoachMarkTextGravity(@CoachMarkTextGravity int coachMarkTextGravity);

        public abstract Builder setFontStyleForDismissButton(@Nullable String fontStyleForDismissButton);

        public abstract Builder setFontStyleForCoachMarkText(@Nullable String fontStyleForCoachMarkText);

        public abstract Builder setActionBarHeight(@DimenRes int actionBarHeight);

        public abstract Builder setFooterHeight(@DimenRes int footerHeight);

        public abstract Builder setInfoForViewToMaskList(@Nullable ArrayList<InfoForViewToMask> infoForViewToMaskList);

        public abstract Builder setPopUpCoachMarkDismissButtonPosition(@DialogDismissButtonPosition int popUpCoachMarkDismissButtonPosition);

        public abstract Builder setNotchPosition(double notchPosition);

        public abstract Builder setUserDesiredPopUpPositionWithRespectToView(@PopUpPosition int userDesiredPopUpPositionWithRespectToView);

        public abstract Builder setBackGroundTintForImage(@ColorRes int backGroundTintForImage);

        public abstract Builder setImageDrawableRes(@DrawableRes int imageDrawableRes);

        public abstract Builder setCoachMarkLayoutMargin(CoachMarkLayoutMargin coachMarkLayoutMargin);

        public abstract Builder setImageLayoutInformation(ImageLayoutInformation imageLayoutInformation);

        public abstract Builder setAnimationOnImage(@AnimRes int animationOnImage);

        public abstract CoachMarkBuilder build();

    }
}
