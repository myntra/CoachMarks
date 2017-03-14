package com.myntra.coachmarks.ui.presentation;

import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.annotation.AnimRes;

import com.myntra.coachmarks.common.CoachMarkAlignPosition;
import com.myntra.coachmarks.common.CoachMarkLayoutOrientation;
import com.myntra.coachmarks.common.CoachMarkTextGravity;

public interface IPopUpCoachMarkPresentation {
    void createViewToBeMaskedOut(int startX, int startY, int height, int width);

    void setImageInformation(double centerX, double centerY, int imageWidth,
                             int imageHeight, int backGroundTint, int imageDrawableRes);

    void dismissWithError(String message);

    void closeCoachMarkDialog();

    void setDismissButtonPositionLeft();

    void setDismissButtonPositionRight();

    void setTypeFaceForDismissButton(Typeface typeface);

    void setTypeFaceForPopUpText(Typeface typeface);

    void setUpGravityForCoachMarkText(@CoachMarkTextGravity int gravity);

    void setPopUpViewTopLeft(Rect margin, @CoachMarkLayoutOrientation int orientation);

    void setPopUpViewBottomRight(Rect margin, @CoachMarkLayoutOrientation int orientation);

    void setNotchPositionIfPopUpTopLeft(Rect margin, float rotation);

    void setNotchPositionIfPopUpBottomRight(Rect margin, float rotation);

    void uiAdjustmentForNotchIfPopUpRight(Rect margin);

    void uiAdjustmentForNotchIfPopUpBottom(Rect margin);

    void setCoachMarkMessage(String message);

    void setPopUpViewPositionWithRespectToImage(@CoachMarkAlignPosition int alignPosition);

    void startAnimationOnImage(@AnimRes int animationRes);

}
