package com.myntra.coachmarks.ui.presenter;

import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.util.Log;

import com.myntra.coachmarks.R;
import com.myntra.coachmarks.builder.CoachMarkBuilder;
import com.myntra.coachmarks.builder.CoachMarkLayoutMargin;
import com.myntra.coachmarks.builder.CoachMarkPixelInfo;
import com.myntra.coachmarks.builder.InfoForViewToMask;
import com.myntra.coachmarks.common.CoachMarkAlignPosition;
import com.myntra.coachmarks.common.CoachMarkLayoutOrientation;
import com.myntra.coachmarks.common.CoachMarkTextGravity;
import com.myntra.coachmarks.common.DialogDismissButtonPosition;
import com.myntra.coachmarks.common.PopUpPosition;
import com.myntra.coachmarks.providers.interfaces.IDimensionResourceProvider;
import com.myntra.coachmarks.providers.interfaces.IScreenInfoProvider;
import com.myntra.coachmarks.providers.interfaces.IStringResourceProvider;
import com.myntra.coachmarks.providers.interfaces.ITypeFaceProvider;
import com.myntra.coachmarks.ui.presentation.IPopUpCoachMarkPresentation;

import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import zeta.android.utils.lang.StringUtils;

@ParametersAreNonnullByDefault
public class PopUpCoachMarkPresenter {

    private static final String TAG = PopUpCoachMarkPresenter.class.getSimpleName();

    private static final double MAX_NOTCH_RANGE = .85;
    private static final double MIN_NOTCH_RANGE = 0.0;

    private static final int NO_MARGIN = 0;

    private static final float ROTATION_0 = 0;
    private static final float ROTATION_90 = 90;
    private static final float ROTATION_180 = 180;
    private static final float ROTATION_270 = 270;

    private static final double MULTIPLICATION_FACTOR_NOTCH_POSITION = 1.8;
    private static final double MULTIPLICATION_FACTOR_FOR_NOTCH_UI_ADJUSTMENT_BOTTOM = 2.9;
    private static final double MULTIPLICATION_FACTOR_FOR_UI_ADJUSTMENT_RIGHT = 0.4;
    private static final double PIXEL_ADJUSTMENT_NOTCH = 0.5;


    private CoachMarkBuilder mCoachMarkBuilder;
    private IPopUpCoachMarkPresentation mPresentation;

    private ITypeFaceProvider mTypeFaceProvider;
    private IScreenInfoProvider mScreenInfoProvider;
    private IStringResourceProvider mStringResourceProvider;
    private IDimensionResourceProvider mDimensionResourceProvider;

    public PopUpCoachMarkPresenter(final IStringResourceProvider stringResourceProvider,
                                   final IDimensionResourceProvider dimensionResourceProvider,
                                   final ITypeFaceProvider typeFaceProvider,
                                   final IScreenInfoProvider screenInfoProvider) {
        mStringResourceProvider = stringResourceProvider;
        mDimensionResourceProvider = dimensionResourceProvider;
        mTypeFaceProvider = typeFaceProvider;
        mScreenInfoProvider = screenInfoProvider;
    }

    public void onCreate(CoachMarkBuilder coachMarkBuilder) {
        mCoachMarkBuilder = coachMarkBuilder;
    }

    public void onCreateView(IPopUpCoachMarkPresentation popUpCoachMarkPresentation) {
        mPresentation = popUpCoachMarkPresentation;
    }

    public void onViewCreated() {
        displayCoachMark();
    }

    public void onDestroyView() {
        mPresentation = null;
    }

    public void onDestroy() {
        mScreenInfoProvider = null;
        mDimensionResourceProvider = null;
        mStringResourceProvider = null;
        mTypeFaceProvider = null;
    }

    public void onOkButtonClicked() {
        mPresentation.closeCoachMarkDialog();
    }

    public void onShimClicked() {
        if (mPresentation != null) {
            mPresentation.closeCoachMarkDialog();
        }
    }

    //region helper methods
    protected void displayCoachMark() {
        CoachMarkPixelInfo coachMarkDimenInPixel = createCoachMarkPixelInfo();
        @PopUpPosition
        int popUpPosition = findCoachMarkTextPopUpDisplayPosition(mCoachMarkBuilder.getAnchorTop(),
                mCoachMarkBuilder.getAnchorBottom(),
                mCoachMarkBuilder.getUserDesiredPopUpPositionWithRespectToView(),
                coachMarkDimenInPixel);

        if (popUpPosition == PopUpPosition.NONE) {
            mPresentation.dismissWithError(
                    mStringResourceProvider.getStringFromResource(R.string.coachmark_no_position_found));
            return;
        }

        setTypeFaceForDismissButton(mCoachMarkBuilder.getFontStyleForDismissButton());
        setTypeFaceForCoachMarkText(mCoachMarkBuilder.getFontStyleForCoachMarkText());
        setGravityForCoachMarkText(mCoachMarkBuilder.getCoachMarkTextGravity());
        setMessageForCoachMarkText(mCoachMarkBuilder.getCoachMarkMessage());

        setNotchDisplayEdge(popUpPosition,
                mCoachMarkBuilder.getAnchorTop().y,
                mCoachMarkBuilder.getAnchorBottom().y,
                mCoachMarkBuilder.getAnchorTop().x,
                coachMarkDimenInPixel);
        detectAndCreateShimOutViews(mCoachMarkBuilder.getInfoForViewToMaskList());
        setImageParamsAndPosition(mCoachMarkBuilder.getAnchorTop(),
                mCoachMarkBuilder.getAnchorBottom(),
                coachMarkDimenInPixel.getImageWidthInPixels(),
                coachMarkDimenInPixel.getImageHeightInPixels(),
                mCoachMarkBuilder.getBackGroundTintForImage(),
                mCoachMarkBuilder.getImageDrawableRes());
        mPresentation.startAnimationOnImage(mCoachMarkBuilder.getAnimationOnImage());
        showCoachMark(mCoachMarkBuilder.getPopUpCoachMarkDismissButtonPosition(),
                popUpPosition);
    }

    protected void setMessageForCoachMarkText(@StringRes int messageForCoachMarkTextRes) {
        mPresentation.setCoachMarkMessage(
                mStringResourceProvider.getStringFromResource(messageForCoachMarkTextRes));
    }

    protected void setTypeFaceForDismissButton(@Nullable String fontFileForDismissButton) {
        if (StringUtils.isNull(fontFileForDismissButton)) {
            return;
        }
        Typeface typeface = mTypeFaceProvider.getTypeFaceFromAssets(fontFileForDismissButton);
        if (typeface != null) {
            mPresentation.setTypeFaceForDismissButton(typeface);
        }

    }

    protected void setTypeFaceForCoachMarkText(@Nullable String fontFileForPopUpText) {
        if (StringUtils.isNotNullOrEmpty(fontFileForPopUpText)) {
            Typeface typeface = mTypeFaceProvider.getTypeFaceFromAssets(fontFileForPopUpText);
            if (typeface != null) {
                mPresentation.setTypeFaceForPopUpText(typeface);
            }
        }
    }

    protected void setGravityForCoachMarkText(int textAlignmentForPopUpText) {
        switch (textAlignmentForPopUpText) {
            case CoachMarkTextGravity.CENTER:
                mPresentation.setUpGravityForCoachMarkText(CoachMarkTextGravity.CENTER);
                break;
            case CoachMarkTextGravity.LEFT:
                mPresentation.setUpGravityForCoachMarkText(CoachMarkTextGravity.LEFT);
                break;
            case CoachMarkTextGravity.RIGHT:
                mPresentation.setUpGravityForCoachMarkText(CoachMarkTextGravity.RIGHT);
                break;
        }
    }

    protected void setNotchDisplayEdge(int position,
                                     int anchorTopY,
                                     int anchorBottomY,
                                     int anchorTopX,
                                     CoachMarkPixelInfo coachMarkDimenInPixel) {
        int notchPosition;
        int actualTopMargin;
        int actualLeftMargin;
        int centerY = (anchorTopY + anchorBottomY) / 2;

        Rect notchMarginRect;
        Rect coachMarkMarginRect;
        Rect notchUiAdjustmentMarginRect;

        switch (position) {
            case PopUpPosition.LEFT:
                actualTopMargin = getActualTopMargin(centerY, coachMarkDimenInPixel);
                coachMarkMarginRect = new Rect(coachMarkDimenInPixel.getMarginRectInPixels().left,
                        actualTopMargin - coachMarkDimenInPixel.getMarginRectInPixels().bottom,
                        coachMarkDimenInPixel.getMarginRectInPixels().right +
                                coachMarkDimenInPixel.getImageWidthInPixels(),
                        NO_MARGIN);
                mPresentation.setPopUpViewTopLeft(coachMarkMarginRect,
                        CoachMarkLayoutOrientation.HORIZONTAL);
                notchPosition = getMarginTopForNotch(mCoachMarkBuilder.getNotchPosition(),
                        coachMarkDimenInPixel.getPopUpHeightInPixels(),
                        coachMarkDimenInPixel.getNotchDimenInPixels());
                notchMarginRect = new Rect(-coachMarkDimenInPixel.getMarginOffsetForNotchInPixels(),
                        notchPosition, NO_MARGIN, NO_MARGIN);
                mPresentation.setNotchPositionIfPopUpTopLeft(notchMarginRect, ROTATION_90);
                break;
            case PopUpPosition.TOP:
                actualLeftMargin = getActualLeftMargin(anchorTopX, coachMarkDimenInPixel);
                coachMarkMarginRect = new Rect(actualLeftMargin -
                        coachMarkDimenInPixel.getMarginRectInPixels().right,
                        coachMarkDimenInPixel.getMarginRectInPixels().top,
                        NO_MARGIN,
                        coachMarkDimenInPixel.getMarginRectInPixels().bottom +
                                coachMarkDimenInPixel.getImageHeightInPixels());
                mPresentation.setPopUpViewTopLeft(coachMarkMarginRect,
                        CoachMarkLayoutOrientation.VERTICAL);
                notchPosition = getMarginLeftForNotch(mCoachMarkBuilder.getNotchPosition(),
                        coachMarkDimenInPixel.getPopUpWidthInPixels(),
                        coachMarkDimenInPixel.getNotchDimenInPixels());
                notchMarginRect = new Rect(notchPosition +
                        coachMarkDimenInPixel.getMarginOffsetForNotchInPixels(),
                        -coachMarkDimenInPixel.getMarginOffsetForNotchInPixels(),
                        NO_MARGIN, NO_MARGIN);
                mPresentation.setNotchPositionIfPopUpTopLeft(notchMarginRect, ROTATION_180);
                break;
            case PopUpPosition.RIGHT:
                actualTopMargin = getActualTopMargin(centerY, coachMarkDimenInPixel);
                coachMarkMarginRect = new Rect(coachMarkDimenInPixel.getMarginRectInPixels().left +
                        coachMarkDimenInPixel.getImageWidthInPixels(),
                        actualTopMargin - coachMarkDimenInPixel.getMarginRectInPixels().bottom,
                        coachMarkDimenInPixel.getMarginRectInPixels().right,
                        NO_MARGIN);
                mPresentation.setPopUpViewBottomRight(coachMarkMarginRect,
                        CoachMarkLayoutOrientation.HORIZONTAL);
                notchPosition = getMarginTopForNotch(mCoachMarkBuilder.getNotchPosition(),
                        coachMarkDimenInPixel.getPopUpHeightInPixels(),
                        coachMarkDimenInPixel.getNotchDimenInPixels());
                notchMarginRect = new Rect(NO_MARGIN,
                        notchPosition - (int) (MULTIPLICATION_FACTOR_NOTCH_POSITION * coachMarkDimenInPixel.getMarginOffsetForNotchInPixels()),
                        NO_MARGIN,
                        NO_MARGIN);
                mPresentation.setNotchPositionIfPopUpBottomRight(notchMarginRect, ROTATION_270);
                notchUiAdjustmentMarginRect = new Rect(NO_MARGIN,
                        notchPosition + (int) (MULTIPLICATION_FACTOR_FOR_UI_ADJUSTMENT_RIGHT * coachMarkDimenInPixel.getMarginOffsetForNotchInPixels() + PIXEL_ADJUSTMENT_NOTCH),
                        NO_MARGIN,
                        NO_MARGIN);
                mPresentation.uiAdjustmentForNotchIfPopUpRight(notchUiAdjustmentMarginRect);
                break;
            case PopUpPosition.BOTTOM:
                actualLeftMargin = getActualLeftMargin(anchorTopX, coachMarkDimenInPixel);
                coachMarkMarginRect = new Rect(actualLeftMargin -
                        coachMarkDimenInPixel.getMarginRectInPixels().right,
                        coachMarkDimenInPixel.getMarginRectInPixels().top +
                                coachMarkDimenInPixel.getImageHeightInPixels(),
                        NO_MARGIN,
                        coachMarkDimenInPixel.getMarginRectInPixels().bottom);
                mPresentation.setPopUpViewBottomRight(coachMarkMarginRect,
                        CoachMarkLayoutOrientation.VERTICAL);
                notchPosition = getMarginLeftForNotch(mCoachMarkBuilder.getNotchPosition(),
                        coachMarkDimenInPixel.getPopUpWidthInPixels(),
                        coachMarkDimenInPixel.getNotchDimenInPixels());
                notchMarginRect = new Rect(notchPosition +
                        coachMarkDimenInPixel.getMarginOffsetForNotchInPixels(),
                        NO_MARGIN,
                        NO_MARGIN,
                        NO_MARGIN);
                mPresentation.setNotchPositionIfPopUpBottomRight(notchMarginRect, ROTATION_0);
                notchUiAdjustmentMarginRect = new Rect(notchPosition +
                        (int) (MULTIPLICATION_FACTOR_FOR_NOTCH_UI_ADJUSTMENT_BOTTOM *
                                coachMarkDimenInPixel.getMarginOffsetForNotchInPixels()),
                        NO_MARGIN,
                        NO_MARGIN,
                        NO_MARGIN);
                mPresentation.uiAdjustmentForNotchIfPopUpBottom(notchUiAdjustmentMarginRect);
                break;
        }
    }

    protected int getMarginLeftForNotch(double notchPosition, int popUpWidth, int notchDimen) {
        if (notchPosition > MAX_NOTCH_RANGE) {
            notchPosition = MAX_NOTCH_RANGE;
        }
        if (notchPosition <= MIN_NOTCH_RANGE) {
            notchPosition = MIN_NOTCH_RANGE;
        }

        int marginLeft = (int) (popUpWidth * notchPosition);
        if (marginLeft > popUpWidth - notchDimen) {
            return (popUpWidth - notchDimen);
        }
        return marginLeft;
    }

    protected int getMarginTopForNotch(double notchPosition, int popUpHeight, int notchDimen) {
        if (notchPosition > MAX_NOTCH_RANGE) {
            notchPosition = MAX_NOTCH_RANGE;
        }
        if (notchPosition <= MIN_NOTCH_RANGE) {
            notchPosition = MIN_NOTCH_RANGE;
        }

        int marginTop = (int) (popUpHeight * notchPosition);
        if (marginTop > popUpHeight - notchDimen) {
            return (popUpHeight - notchDimen);
        }
        return marginTop;
    }

    protected void detectAndCreateShimOutViews(@Nullable List<InfoForViewToMask> infoForViewToMaskList) {
        if (infoForViewToMaskList == null) {
            return;
        }
        for (InfoForViewToMask infoForViewToMask : infoForViewToMaskList) {
            mPresentation.createViewToBeMaskedOut(
                    infoForViewToMask.getViewToMaskStartPosition().x,
                    infoForViewToMask.getViewToMaskStartPosition().y,
                    infoForViewToMask.getViewToMaskHeight(),
                    infoForViewToMask.getViewToMaskWidth());
        }
    }

    @PopUpPosition
    protected int findCoachMarkTextPopUpDisplayPosition(Point anchorTop,
                                                      Point anchorBottom,
                                                      @PopUpPosition int defaultPopUpPosition,
                                                      CoachMarkPixelInfo coachMarkDimenInPixel) {
        int centerX = (anchorTop.x + anchorBottom.x) / 2;
        int centerY = (anchorTop.y + anchorBottom.y) / 2;
        Point centerViewPoint = new Point(centerX, centerY);
        return getDisplayPosition(centerViewPoint,
                defaultPopUpPosition, coachMarkDimenInPixel);
    }


    protected void setImageParamsAndPosition(Point anchorTop, Point anchorBottom,
                                           int imageWidth, int imageHeight,
                                           @ColorRes int backGroundTintForImage,
                                           @DrawableRes int imageDrawableRes) {
        double centerX = (anchorTop.x + anchorBottom.x) / 2;
        double centerY = (anchorTop.y + anchorBottom.y) / 2;
        mPresentation.setImageInformation(centerX, centerY, imageWidth,
                imageHeight, backGroundTintForImage, imageDrawableRes);
    }

    protected void showCoachMark(@DialogDismissButtonPosition int dismissButtonPosition,
                               @PopUpPosition int popUpPosition) {

        switch (popUpPosition) {
            case PopUpPosition.LEFT:
                mPresentation.setPopUpViewPositionWithRespectToImage(
                        CoachMarkAlignPosition.ALIGN_RIGHT);
                break;
            case PopUpPosition.TOP:
                mPresentation.setPopUpViewPositionWithRespectToImage(
                        CoachMarkAlignPosition.ALIGN_BOTTOM);
                break;
            case PopUpPosition.RIGHT:
                mPresentation.setPopUpViewPositionWithRespectToImage(
                        CoachMarkAlignPosition.ALIGN_LEFT);
                break;
            case PopUpPosition.BOTTOM:
                mPresentation.setPopUpViewPositionWithRespectToImage(
                        CoachMarkAlignPosition.ALIGN_TOP);
                break;
            case PopUpPosition.NONE:
                Log.wtf(TAG, "This should not have happened here. No position found case already handled");
                break;
        }

        if(popUpPosition == PopUpPosition.NONE){
            return;
        }

        switch (dismissButtonPosition) {
            case DialogDismissButtonPosition.LEFT:
                mPresentation.setDismissButtonPositionLeft();
                break;
            case DialogDismissButtonPosition.RIGHT:
                mPresentation.setDismissButtonPositionRight();
                break;
        }
    }

    @PopUpPosition
    protected int getDisplayPosition(Point viewCenterPoint,
                                   @PopUpPosition int defaultPopUpPosition,
                                   CoachMarkPixelInfo coachMarkDimenInPixel) {
        @PopUpPosition
        int correctPosition = 0;
        switch (defaultPopUpPosition) {
            case PopUpPosition.LEFT:
                if (checkIfLeftPossible(viewCenterPoint, coachMarkDimenInPixel)) {
                    correctPosition = defaultPopUpPosition;
                } else {
                    correctPosition = getCorrectPositionOfCoachMarkIfDefaultFails(viewCenterPoint,
                            coachMarkDimenInPixel);
                }
                break;
            case PopUpPosition.RIGHT:
                if (checkIfRightPossible(viewCenterPoint, coachMarkDimenInPixel)) {
                    correctPosition = defaultPopUpPosition;
                } else {
                    correctPosition = getCorrectPositionOfCoachMarkIfDefaultFails(viewCenterPoint,
                            coachMarkDimenInPixel);
                }
                break;

            case PopUpPosition.BOTTOM:
                if (checkIfBottomPossible(viewCenterPoint, coachMarkDimenInPixel)) {
                    correctPosition = defaultPopUpPosition;
                } else {
                    correctPosition = getCorrectPositionOfCoachMarkIfDefaultFails(viewCenterPoint,
                            coachMarkDimenInPixel);
                }
                break;

            case PopUpPosition.TOP:
                if (checkIfTopPossible(viewCenterPoint, coachMarkDimenInPixel)) {
                    correctPosition = defaultPopUpPosition;
                } else {
                    correctPosition = getCorrectPositionOfCoachMarkIfDefaultFails(viewCenterPoint,
                            coachMarkDimenInPixel);
                }
                break;
            case PopUpPosition.NONE:
                //if user selects no position by default check clockwise to find the correct position
                correctPosition = getCorrectPositionOfCoachMarkIfDefaultFails(viewCenterPoint,
                        coachMarkDimenInPixel);
                break;
        }
        return correctPosition;
    }

    protected boolean checkIfLeftPossible(Point viewCenterPoint,
                                        CoachMarkPixelInfo coachMarkDimenInPixel) {
        int centerX = viewCenterPoint.x;
        return (coachMarkDimenInPixel.getPopUpWidthInPixelsWithOffset()) < centerX &&
                ((coachMarkDimenInPixel.getPopUpHeightInPixelsWithOffset() +
                        coachMarkDimenInPixel.getMarginRectInPixels().top +
                        coachMarkDimenInPixel.getActionBarHeightPixels() +
                        coachMarkDimenInPixel.getFooterHeightPixels() +
                        coachMarkDimenInPixel.getMarginRectInPixels().bottom)
                        <= coachMarkDimenInPixel.getScreenHeightInPixels());
    }

    protected boolean checkIfRightPossible(Point viewCenterPoint,
                                         CoachMarkPixelInfo coachMarkDimenInPixel) {
        int centerX = viewCenterPoint.x;
        return (coachMarkDimenInPixel.getPopUpWidthInPixelsWithOffset()) <=
                (coachMarkDimenInPixel.getScreenWidthInPixels() - centerX) &&
                ((coachMarkDimenInPixel.getPopUpHeightInPixelsWithOffset() +
                        coachMarkDimenInPixel.getMarginRectInPixels().top +
                        coachMarkDimenInPixel.getActionBarHeightPixels() +
                        coachMarkDimenInPixel.getFooterHeightPixels() +
                        coachMarkDimenInPixel.getMarginRectInPixels().bottom) <=
                        coachMarkDimenInPixel.getScreenHeightInPixels());
    }

    protected boolean checkIfTopPossible(Point viewCenterPoint,
                                       CoachMarkPixelInfo coachMarkDimenInPixel) {
        int centerY = viewCenterPoint.y;
        return (coachMarkDimenInPixel.getPopUpHeightInPixelsWithOffset() +
                coachMarkDimenInPixel.getMarginRectInPixels().top +
                coachMarkDimenInPixel.getMarginRectInPixels().bottom +
                coachMarkDimenInPixel.getActionBarHeightPixels()) <=
                centerY && ((coachMarkDimenInPixel.getWidthHeightOffsetForCoachMarkPopUp()
                + coachMarkDimenInPixel.getMarginRectInPixels().left +
                coachMarkDimenInPixel.getMarginRectInPixels().right) <=
                coachMarkDimenInPixel.getScreenWidthInPixels());
    }

    protected boolean checkIfBottomPossible(Point viewCenterPoint,
                                          CoachMarkPixelInfo coachMarkDimenInPixel) {
        int centerY = viewCenterPoint.y;
        return (coachMarkDimenInPixel.getPopUpHeightInPixelsWithOffset() +
                coachMarkDimenInPixel.getMarginRectInPixels().top +
                coachMarkDimenInPixel.getMarginRectInPixels().bottom +
                coachMarkDimenInPixel.getFooterHeightPixels()) <=
                coachMarkDimenInPixel.getScreenHeightInPixels() - centerY &&
                ((coachMarkDimenInPixel.getPopUpWidthInPixelsWithOffset()
                        + coachMarkDimenInPixel.getMarginRectInPixels().left +
                        coachMarkDimenInPixel.getMarginRectInPixels().right) <=
                        coachMarkDimenInPixel.getScreenWidthInPixels());
    }

    @PopUpPosition
    protected int getCorrectPositionOfCoachMarkIfDefaultFails(Point viewCenterPoint,
                                                            CoachMarkPixelInfo coachMarkDimenInPixel) {

        @PopUpPosition
        int correctPopUpPosition = PopUpPosition.NONE;

        if (checkIfRightPossible(viewCenterPoint, coachMarkDimenInPixel))
            correctPopUpPosition = PopUpPosition.RIGHT;
        else if (checkIfBottomPossible(viewCenterPoint, coachMarkDimenInPixel))
            correctPopUpPosition = PopUpPosition.BOTTOM;
        else if (checkIfLeftPossible(viewCenterPoint, coachMarkDimenInPixel))
            correctPopUpPosition = PopUpPosition.LEFT;
        else if (checkIfTopPossible(viewCenterPoint, coachMarkDimenInPixel))
            correctPopUpPosition = PopUpPosition.TOP;

        return correctPopUpPosition;
    }

    protected CoachMarkPixelInfo createCoachMarkPixelInfo() {
        final CoachMarkLayoutMargin coachMarkLayoutMargin =
                mCoachMarkBuilder.getCoachMarkLayoutMargin();
        int leftMargin = mDimensionResourceProvider.getDimensionInPixel(
                coachMarkLayoutMargin.getLeftMarginForCoachMark());
        int rightMargin = mDimensionResourceProvider.getDimensionInPixel(
                coachMarkLayoutMargin.getRightMarginForCoachMark());
        int topMargin = mDimensionResourceProvider.getDimensionInPixel(
                coachMarkLayoutMargin.getTopMarginForCoachMark());
        int bottomMargin = mDimensionResourceProvider.getDimensionInPixel(
                coachMarkLayoutMargin.getBottomMarginForCoachMark());

        int actionBarHeightPixels = mDimensionResourceProvider.getDimensionInPixel(
                mCoachMarkBuilder.getActionBarHeight());
        int footerHeightPixels = mDimensionResourceProvider.getDimensionInPixel(
                mCoachMarkBuilder.getFooterHeight());

        int imageHeightInPixels = mDimensionResourceProvider.getDimensionInPixel(
                mCoachMarkBuilder.getImageLayoutInformation().getImageHeight());
        int imageWidthInPixels = mDimensionResourceProvider.getDimensionInPixel(
                mCoachMarkBuilder.getImageLayoutInformation().getImageWidth());

        int coachMarkPopUpHeightInPixels = mDimensionResourceProvider.getDimensionInPixel(R.dimen.coach_mark_pop_up_height);
        int coachMarkPopUpWidthInPixels = mDimensionResourceProvider.getDimensionInPixel(R.dimen.coach_mark_pop_up_width);

        int notchDimenInPixels = mDimensionResourceProvider.getDimensionInPixel(R.dimen.coach_mark_notch_width_height);
        int marginOffsetForNotchInPixels = mDimensionResourceProvider.getDimensionInPixel(R.dimen.coach_mark_notch_margin_offset);

        int widthHeightOffsetForCoachMarkPopUp = mDimensionResourceProvider.getDimensionInPixel(R.dimen.coach_mark_pop_up_width_height_offset);
        int coachMarkPopUpWidthInPixelsWithOffset = coachMarkPopUpWidthInPixels +
                widthHeightOffsetForCoachMarkPopUp + imageWidthInPixels;
        int coachMarkPopUpHeightInPixelsWithOffset = coachMarkPopUpHeightInPixels +
                widthHeightOffsetForCoachMarkPopUp + imageHeightInPixels;

        int screenHeightInPixels = mScreenInfoProvider.getScreenHeightInPixels();
        int screenWidthInPixels = mScreenInfoProvider.getScreenWidthInPixels();

        return CoachMarkPixelInfo.create()
                .setMarginRectInPixels(new Rect(leftMargin, topMargin, rightMargin, bottomMargin))
                .setPopUpHeightInPixels(coachMarkPopUpHeightInPixels)
                .setPopUpHeightInPixelsWithOffset(coachMarkPopUpHeightInPixelsWithOffset)
                .setPopUpWidthInPixels(coachMarkPopUpWidthInPixels)
                .setPopUpWidthInPixelsWithOffset(coachMarkPopUpWidthInPixelsWithOffset)
                .setWidthHeightOffsetForCoachMarkPopUp(widthHeightOffsetForCoachMarkPopUp)
                .setActionBarHeightPixels(actionBarHeightPixels)
                .setFooterHeightPixels(footerHeightPixels)
                .setNotchDimenInPixels(notchDimenInPixels)
                .setMarginOffsetForNotchInPixels(marginOffsetForNotchInPixels)
                .setImageHeightInPixels(imageHeightInPixels)
                .setImageWidthInPixels(imageWidthInPixels)
                .setScreenHeightInPixels(screenHeightInPixels)
                .setScreenWidthInPixels(screenWidthInPixels)
                .build();
    }

    protected int getActualTopMargin(int centerTopY, CoachMarkPixelInfo coachMarkDimenInPixel) {
        if (centerTopY + coachMarkDimenInPixel.getPopUpHeightInPixels() +
                coachMarkDimenInPixel.getWidthHeightOffsetForCoachMarkPopUp() +
                coachMarkDimenInPixel.getMarginRectInPixels().top +
                coachMarkDimenInPixel.getMarginRectInPixels().bottom >
                coachMarkDimenInPixel.getScreenHeightInPixels()) {
            return coachMarkDimenInPixel.getScreenHeightInPixels() -
                    coachMarkDimenInPixel.getPopUpHeightInPixels() +
                    coachMarkDimenInPixel.getWidthHeightOffsetForCoachMarkPopUp() +
                    coachMarkDimenInPixel.getMarginRectInPixels().top +
                    coachMarkDimenInPixel.getMarginRectInPixels().bottom;
        }
        return centerTopY + coachMarkDimenInPixel.getMarginRectInPixels().top;
    }

    protected int getActualLeftMargin(int anchorTopX, CoachMarkPixelInfo coachMarkDimenInPixel) {
        if (anchorTopX + coachMarkDimenInPixel.getPopUpWidthInPixels() +
                coachMarkDimenInPixel.getWidthHeightOffsetForCoachMarkPopUp() +
                coachMarkDimenInPixel.getMarginRectInPixels().right +
                coachMarkDimenInPixel.getMarginRectInPixels().left >
                coachMarkDimenInPixel.getScreenWidthInPixels()) {
            return coachMarkDimenInPixel.getScreenWidthInPixels() -
                    coachMarkDimenInPixel.getPopUpWidthInPixels() +
                    coachMarkDimenInPixel.getWidthHeightOffsetForCoachMarkPopUp() +
                    coachMarkDimenInPixel.getMarginRectInPixels().right +
                    coachMarkDimenInPixel.getMarginRectInPixels().left;
        }
        return anchorTopX + coachMarkDimenInPixel.getMarginRectInPixels().left;
    }
    //endregion
}
