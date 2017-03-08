package com.myntra.coachmarks.ui.presenter;

import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;

import com.myntra.coachmarks.R;
import com.myntra.coachmarks.builder.CoachMarkBuilder;
import com.myntra.coachmarks.builder.CoachMarkLayoutMargin;
import com.myntra.coachmarks.builder.CoachMarkPixelInfo;
import com.myntra.coachmarks.builder.InfoForViewToMask;
import com.myntra.coachmarks.common.AnimationType;
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

    private static final double MAX_NOTCH_RANGE = .85;
    private static final double MIN_NOTCH_RANGE = 0.0;

    private static final int NO_MARGIN = 0;

    private static final float ROTATION_0 = 0;
    private static final float ROTATION_90 = 90;
    private static final float ROTATION_180 = 180;
    private static final float ROTATION_270 = 270;

    private static final int MULTIPLICATION_FACTOR_FOR_NOTCH_UI_ADJUSTMENT = 3;

    private CoachMarkBuilder mCoachMarkBuilder;
    private IPopUpCoachMarkPresentation mPopUpCoachMarkPresentation;

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
        mPopUpCoachMarkPresentation = popUpCoachMarkPresentation;
    }

    public void onViewCreated() {
        displayCoachMark();
    }

    public void onDestroy() {
        mPopUpCoachMarkPresentation = null;
        mScreenInfoProvider = null;
        mDimensionResourceProvider = null;
        mStringResourceProvider = null;
        mTypeFaceProvider = null;
    }

    public void onOkButtonClicked() {
        if (mPopUpCoachMarkPresentation != null) {
            mPopUpCoachMarkPresentation.onDismiss();
        }
    }

    public void onShimClicked() {
        if (mPopUpCoachMarkPresentation != null) {
            mPopUpCoachMarkPresentation.onDismiss();
        }
    }

    //region helper methods
    private void displayCoachMark() {
        CoachMarkPixelInfo coachMarkDimenInPixel = createCoachMarkPixelInfo();
        @PopUpPosition
        int popUpPosition = findCoachMarkTextPopUpDisplayPosition(mCoachMarkBuilder.getAnchorTop(),
                mCoachMarkBuilder.getAnchorBottom(),
                mCoachMarkBuilder.getUserDesiredPopUpPositionWithRespectToView(), coachMarkDimenInPixel);

        if (popUpPosition == PopUpPosition.NONE) {
            mPopUpCoachMarkPresentation.dismissWithError(
                    mStringResourceProvider.getStringFromResource(R.string.coachmark_no_position_found));
            return;
        }

        setTypeFaceForDismissButton(mCoachMarkBuilder.getFontStyleForDismissButton());
        setTypeFaceForCoachMarkText(mCoachMarkBuilder.getFontStyleForCoachMarkText());
        setGravityForCoachMarkText(mCoachMarkBuilder.getCoachMarkTextGravity());
        setMessageForCoachMarkText(mCoachMarkBuilder.getCoachMarkMessage());

        setNotchDisplayEdge(popUpPosition, mCoachMarkBuilder.getAnchorTop().y, mCoachMarkBuilder.getAnchorBottom().y, mCoachMarkBuilder.getAnchorTop().x, coachMarkDimenInPixel);
        detectAndCreateShimOutViews(mCoachMarkBuilder.getInfoForViewToMaskList());
        setImageParamsAndPosition(mCoachMarkBuilder.getAnchorTop(), mCoachMarkBuilder.getAnchorBottom(), coachMarkDimenInPixel.getImageWidthInPixels(), coachMarkDimenInPixel.getImageHeightInPixels(), mCoachMarkBuilder.getBackGroundTintForImage(), mCoachMarkBuilder.getImageDrawableRes());
        createAnimationOnImage(mCoachMarkBuilder.getAnimationTypeOnImage());
        showCoachMark(mCoachMarkBuilder.getPopUpCoachMarkDismissButtonPosition(), popUpPosition);
    }

    private void setMessageForCoachMarkText(@StringRes int messageForCoachMarkTextRes) {
        mPopUpCoachMarkPresentation.setCoachMarkMessage(mStringResourceProvider.getStringFromResource(messageForCoachMarkTextRes));
    }

    private void setTypeFaceForDismissButton(@Nullable String fontFileForDismissButton) {
        if (StringUtils.isNotNullOrEmpty(fontFileForDismissButton)) {
            Typeface typeface = mTypeFaceProvider.getTypeFaceFromAssets(fontFileForDismissButton);
            if (typeface != null)
                mPopUpCoachMarkPresentation.setTypeFaceForDismissButton(typeface);
        }
    }

    private void setTypeFaceForCoachMarkText(@Nullable String fontFileForPopUpText) {
        if (StringUtils.isNotNullOrEmpty(fontFileForPopUpText)) {
            Typeface typeface = mTypeFaceProvider.getTypeFaceFromAssets(fontFileForPopUpText);
            if (typeface != null)
                mPopUpCoachMarkPresentation.setTypeFaceForPopUpText(typeface);
        }
    }

    private void setGravityForCoachMarkText(int textAlignmentForPopUpText) {
        switch (textAlignmentForPopUpText) {
            case CoachMarkTextGravity.CENTER:
                mPopUpCoachMarkPresentation.setUpGravityForCoachMarkText(CoachMarkTextGravity.CENTER);
                break;
            case CoachMarkTextGravity.LEFT:
                mPopUpCoachMarkPresentation.setUpGravityForCoachMarkText(CoachMarkTextGravity.LEFT);
                break;
            case CoachMarkTextGravity.RIGHT:
                mPopUpCoachMarkPresentation.setUpGravityForCoachMarkText(CoachMarkTextGravity.RIGHT);
                break;
        }
    }

    private void setNotchDisplayEdge(int position, int anchorTopY, int anchorBottomY, int anchorTopX, CoachMarkPixelInfo coachMarkDimenInPixel) {
        int centerY = (anchorTopY + anchorBottomY) / 2;
        int actualTopMargin;
        int actualLeftMargin;
        Rect coachMarkMarginRect;
        int notchPosition;
        Rect notchMarginRect;
        Rect notchUiAdjustmentMarginRect;
        switch (position) {
            case PopUpPosition.LEFT:
                actualTopMargin = getActualTopMargin(centerY, coachMarkDimenInPixel);
                coachMarkMarginRect = new Rect(coachMarkDimenInPixel.getMarginRectInPixels().left, actualTopMargin - coachMarkDimenInPixel.getMarginRectInPixels().bottom, coachMarkDimenInPixel.getMarginRectInPixels().right + coachMarkDimenInPixel.getImageWidthInPixels(), NO_MARGIN);
                mPopUpCoachMarkPresentation.setPopUpViewTopLeft(coachMarkMarginRect, CoachMarkLayoutOrientation.HORIZONTAL);
                notchPosition = getMarginTopForNotch(mCoachMarkBuilder.getNotchPosition(), coachMarkDimenInPixel.getPopUpHeightInPixels(), coachMarkDimenInPixel.getNotchDimenInPixels());
                notchMarginRect = new Rect(-coachMarkDimenInPixel.getMarginOffsetForNotchInPixels(), notchPosition, NO_MARGIN, NO_MARGIN);
                mPopUpCoachMarkPresentation.setNotchPositionIfPopUpTopLeft(notchMarginRect, ROTATION_90);
                break;
            case PopUpPosition.TOP:
                actualLeftMargin = getActualLeftMargin(anchorTopX, coachMarkDimenInPixel);
                coachMarkMarginRect = new Rect(actualLeftMargin - coachMarkDimenInPixel.getMarginRectInPixels().right, coachMarkDimenInPixel.getMarginRectInPixels().top, NO_MARGIN, coachMarkDimenInPixel.getMarginRectInPixels().bottom + coachMarkDimenInPixel.getImageHeightInPixels());
                mPopUpCoachMarkPresentation.setPopUpViewTopLeft(coachMarkMarginRect, CoachMarkLayoutOrientation.VERTICAL);
                notchPosition = getMarginLeftForNotch(mCoachMarkBuilder.getNotchPosition(), coachMarkDimenInPixel.getPopUpWidthInPixels(), coachMarkDimenInPixel.getNotchDimenInPixels());
                notchMarginRect = new Rect(notchPosition + coachMarkDimenInPixel.getMarginOffsetForNotchInPixels(), -coachMarkDimenInPixel.getMarginOffsetForNotchInPixels(), NO_MARGIN, NO_MARGIN);
                mPopUpCoachMarkPresentation.setNotchPositionIfPopUpTopLeft(notchMarginRect, ROTATION_180);
                break;
            case PopUpPosition.RIGHT:
                actualTopMargin = getActualTopMargin(centerY, coachMarkDimenInPixel);
                coachMarkMarginRect = new Rect(coachMarkDimenInPixel.getMarginRectInPixels().left + coachMarkDimenInPixel.getImageWidthInPixels(), actualTopMargin - coachMarkDimenInPixel.getMarginRectInPixels().bottom, coachMarkDimenInPixel.getMarginRectInPixels().right, NO_MARGIN);
                mPopUpCoachMarkPresentation.setPopUpViewBottomRight(coachMarkMarginRect, CoachMarkLayoutOrientation.HORIZONTAL);
                notchPosition = getMarginTopForNotch(mCoachMarkBuilder.getNotchPosition(), coachMarkDimenInPixel.getPopUpHeightInPixels(), coachMarkDimenInPixel.getNotchDimenInPixels());
                notchMarginRect = new Rect(NO_MARGIN, notchPosition - (2 * coachMarkDimenInPixel.getMarginOffsetForNotchInPixels()), NO_MARGIN, NO_MARGIN);
                mPopUpCoachMarkPresentation.setNotchPositionIfPopUpBottomRight(notchMarginRect, ROTATION_270);
                notchUiAdjustmentMarginRect = new Rect(NO_MARGIN, notchPosition - coachMarkDimenInPixel.getMarginOffsetForNotchInPixels(), NO_MARGIN, NO_MARGIN);
                mPopUpCoachMarkPresentation.uiAdjustmentForNotchIfPopUpRight(notchUiAdjustmentMarginRect);
                break;
            case PopUpPosition.BOTTOM:
                actualLeftMargin = getActualLeftMargin(anchorTopX, coachMarkDimenInPixel);
                coachMarkMarginRect = new Rect(actualLeftMargin - coachMarkDimenInPixel.getMarginRectInPixels().right, coachMarkDimenInPixel.getMarginRectInPixels().top + coachMarkDimenInPixel.getImageHeightInPixels(), NO_MARGIN, coachMarkDimenInPixel.getMarginRectInPixels().bottom);
                mPopUpCoachMarkPresentation.setPopUpViewBottomRight(coachMarkMarginRect, CoachMarkLayoutOrientation.VERTICAL);
                notchPosition = getMarginLeftForNotch(mCoachMarkBuilder.getNotchPosition(), coachMarkDimenInPixel.getPopUpWidthInPixels(), coachMarkDimenInPixel.getNotchDimenInPixels());
                notchMarginRect = new Rect(notchPosition + coachMarkDimenInPixel.getMarginOffsetForNotchInPixels(), NO_MARGIN, NO_MARGIN, NO_MARGIN);
                mPopUpCoachMarkPresentation.setNotchPositionIfPopUpBottomRight(notchMarginRect, ROTATION_0);
                notchUiAdjustmentMarginRect = new Rect(notchPosition + MULTIPLICATION_FACTOR_FOR_NOTCH_UI_ADJUSTMENT * coachMarkDimenInPixel.getMarginOffsetForNotchInPixels(), NO_MARGIN, NO_MARGIN, NO_MARGIN);
                mPopUpCoachMarkPresentation.uiAdjustmentForNotchIfPopUpBottom(notchUiAdjustmentMarginRect);
                break;
        }
    }

    private int getMarginLeftForNotch(double notchPosition, int popUpWidth, int notchDimen) {
        if (notchPosition > MAX_NOTCH_RANGE)
            notchPosition = MAX_NOTCH_RANGE;
        if (notchPosition <= MIN_NOTCH_RANGE)
            notchPosition = MIN_NOTCH_RANGE;
        int marginLeft = (int) (popUpWidth * notchPosition);
        if (marginLeft > popUpWidth - notchDimen)
            return (popUpWidth - notchDimen);
        return marginLeft;
    }

    private int getMarginTopForNotch(double notchPosition, int popUpHeight, int notchDimen) {
        if (notchPosition > MAX_NOTCH_RANGE)
            notchPosition = MAX_NOTCH_RANGE;
        if (notchPosition <= MIN_NOTCH_RANGE)
            notchPosition = MIN_NOTCH_RANGE;
        int marginTop = (int) (popUpHeight * notchPosition);
        if (marginTop > popUpHeight - notchDimen)
            return (popUpHeight - notchDimen);
        return marginTop;
    }

    private void detectAndCreateShimOutViews(@Nullable List<InfoForViewToMask> infoForViewToMaskList) {
        if (infoForViewToMaskList == null) {
            return;
        }
        for (InfoForViewToMask infoForViewToMask : infoForViewToMaskList) {
            mPopUpCoachMarkPresentation.createViewToBeMaskedOut(
                    infoForViewToMask.getViewToMaskStartPosition().x,
                    infoForViewToMask.getViewToMaskStartPosition().y,
                    infoForViewToMask.getViewToMaskHeight(),
                    infoForViewToMask.getViewToMaskWidth());
        }
    }

    private int findCoachMarkTextPopUpDisplayPosition(Point anchorTop, Point anchorBottom,
                                                      @PopUpPosition int defaultPopUpPosition,
                                                      CoachMarkPixelInfo coachMarkDimenInPixel) {
        int centerX = (anchorTop.x + anchorBottom.x) / 2;
        int centerY = (anchorTop.y + anchorBottom.y) / 2;
        Point centerViewPoint = new Point(centerX, centerY);
        @PopUpPosition
        int popUpPosition = getDisplayPosition(centerViewPoint,
                defaultPopUpPosition, coachMarkDimenInPixel);
        return popUpPosition;
    }

    private void createAnimationOnImage(@AnimationType int animationType) {
        switch (animationType) {
            case AnimationType.THROB_ANIMATION:
                mPopUpCoachMarkPresentation.startThrobAnimationOnImage();
                break;
            case AnimationType.ALPHA_ANIMATION:
                mPopUpCoachMarkPresentation.startAlphaAnimationOnImage();
                break;
            case AnimationType.SCALE_ANIMATION:
                mPopUpCoachMarkPresentation.startScaleAnimationOnImage();
                break;
            case AnimationType.ANIMATION_NONE:
                break;
        }
    }

    private void setImageParamsAndPosition(Point anchorTop, Point anchorBottom,
                                           int imageWidth, int imageHeight,
                                           @ColorRes int backGroundTintForImage,
                                           @DrawableRes int imageDrawableRes) {
        double centerX = (anchorTop.x + anchorBottom.x) / 2;
        double centerY = (anchorTop.y + anchorBottom.y) / 2;
        mPopUpCoachMarkPresentation.setImageInformation(centerX, centerY, imageWidth,
                imageHeight, backGroundTintForImage, imageDrawableRes);
    }

    private void showCoachMark(@DialogDismissButtonPosition int dismissButtonPosition,
                               @PopUpPosition int popUpPosition) {

        switch (popUpPosition) {
            case PopUpPosition.LEFT:
                mPopUpCoachMarkPresentation.setPopUpViewPositionWithRespectToImage(CoachMarkAlignPosition.ALIGN_RIGHT);
                break;
            case PopUpPosition.TOP:
                mPopUpCoachMarkPresentation.setPopUpViewPositionWithRespectToImage(CoachMarkAlignPosition.ALIGN_BOTTOM);
                break;
            case PopUpPosition.RIGHT:
                mPopUpCoachMarkPresentation.setPopUpViewPositionWithRespectToImage(CoachMarkAlignPosition.ALIGN_LEFT);
                break;
            case PopUpPosition.BOTTOM:
                mPopUpCoachMarkPresentation.setPopUpViewPositionWithRespectToImage(CoachMarkAlignPosition.ALIGN_TOP);
                break;
            case PopUpPosition.NONE:
                //TODO:: Handle this case
                break;
        }

        switch (dismissButtonPosition) {
            case DialogDismissButtonPosition.LEFT:
                mPopUpCoachMarkPresentation.setDismissButtonPositionLeft();
                break;
            case DialogDismissButtonPosition.RIGHT:
                mPopUpCoachMarkPresentation.setDismissButtonPositionRight();
                break;
        }
    }

    @PopUpPosition
    private int getDisplayPosition(Point viewCenterPoint,
                                   @PopUpPosition int defaultPopUpPosition,
                                   CoachMarkPixelInfo coachMarkDimenInPixel) {
        @PopUpPosition
        int correctPosition = 0;
        switch (defaultPopUpPosition) {
            case PopUpPosition.LEFT:
                if (checkIfLeftPossible(viewCenterPoint, coachMarkDimenInPixel)) {
                    correctPosition = defaultPopUpPosition;
                } else {
                    correctPosition = getCorrectPositionOfCoachMarkIfDefaultFails(viewCenterPoint, coachMarkDimenInPixel);
                }
                break;
            case PopUpPosition.RIGHT:
                if (checkIfRightPossible(viewCenterPoint, coachMarkDimenInPixel)) {
                    correctPosition = defaultPopUpPosition;
                } else {
                    correctPosition = getCorrectPositionOfCoachMarkIfDefaultFails(viewCenterPoint, coachMarkDimenInPixel);
                }
                break;

            case PopUpPosition.BOTTOM:
                if (checkIfBottomPossible(viewCenterPoint, coachMarkDimenInPixel)) {
                    correctPosition = defaultPopUpPosition;
                } else {
                    correctPosition = getCorrectPositionOfCoachMarkIfDefaultFails(viewCenterPoint, coachMarkDimenInPixel);
                }
                break;

            case PopUpPosition.TOP:
                if (checkIfTopPossible(viewCenterPoint, coachMarkDimenInPixel)) {
                    correctPosition = defaultPopUpPosition;
                } else {
                    correctPosition = getCorrectPositionOfCoachMarkIfDefaultFails(viewCenterPoint, coachMarkDimenInPixel);
                }
                break;
            case PopUpPosition.NONE:
                //TODO:: Handle this
                break;
        }
        return correctPosition;
    }

    private boolean checkIfLeftPossible(Point viewCenterPoint, CoachMarkPixelInfo coachMarkDimenInPixel) {
        int centerX = viewCenterPoint.x;
        return (coachMarkDimenInPixel.getPopUpWidthInPixelsWithOffset() + coachMarkDimenInPixel.getMarginRectInPixels().right + coachMarkDimenInPixel.getMarginRectInPixels().left) < centerX && ((coachMarkDimenInPixel.getPopUpHeightInPixelsWithOffset() + coachMarkDimenInPixel.getMarginRectInPixels().top + coachMarkDimenInPixel.getActionBarHeightPixels() + coachMarkDimenInPixel.getFooterHeightPixels() + coachMarkDimenInPixel.getMarginRectInPixels().bottom) <= coachMarkDimenInPixel.getScreenHeightInPixels());
    }

    private boolean checkIfRightPossible(Point viewCenterPoint, CoachMarkPixelInfo coachMarkDimenInPixel) {
        int centerX = viewCenterPoint.x;
        return (coachMarkDimenInPixel.getPopUpWidthInPixelsWithOffset() + coachMarkDimenInPixel.getMarginRectInPixels().right + coachMarkDimenInPixel.getMarginRectInPixels().left) <= (coachMarkDimenInPixel.getScreenWidthInPixels() - centerX) && ((coachMarkDimenInPixel.getPopUpHeightInPixelsWithOffset() + coachMarkDimenInPixel.getMarginRectInPixels().top + coachMarkDimenInPixel.getActionBarHeightPixels() + coachMarkDimenInPixel.getFooterHeightPixels() + coachMarkDimenInPixel.getMarginRectInPixels().bottom) <= coachMarkDimenInPixel.getScreenHeightInPixels());
    }

    private boolean checkIfTopPossible(Point viewCenterPoint, CoachMarkPixelInfo coachMarkDimenInPixel) {
        int centerY = viewCenterPoint.y;
        return (coachMarkDimenInPixel.getPopUpHeightInPixelsWithOffset() + coachMarkDimenInPixel.getMarginRectInPixels().top + coachMarkDimenInPixel.getMarginRectInPixels().bottom + coachMarkDimenInPixel.getActionBarHeightPixels()) <= centerY && ((coachMarkDimenInPixel.getWidthHeightOffsetForCoachMarkPopUp() + coachMarkDimenInPixel.getMarginRectInPixels().left + coachMarkDimenInPixel.getMarginRectInPixels().right) <= coachMarkDimenInPixel.getScreenWidthInPixels());
    }

    private boolean checkIfBottomPossible(Point viewCenterPoint, CoachMarkPixelInfo coachMarkDimenInPixel) {
        int centerY = viewCenterPoint.y;
        return (coachMarkDimenInPixel.getPopUpHeightInPixelsWithOffset() + coachMarkDimenInPixel.getMarginRectInPixels().top + coachMarkDimenInPixel.getMarginRectInPixels().bottom + coachMarkDimenInPixel.getFooterHeightPixels()) <= coachMarkDimenInPixel.getScreenHeightInPixels() - centerY && ((coachMarkDimenInPixel.getPopUpWidthInPixelsWithOffset() + coachMarkDimenInPixel.getMarginRectInPixels().left + coachMarkDimenInPixel.getMarginRectInPixels().right) <= coachMarkDimenInPixel.getScreenWidthInPixels());
    }

    @PopUpPosition
    private int getCorrectPositionOfCoachMarkIfDefaultFails(Point viewCenterPoint, CoachMarkPixelInfo coachMarkDimenInPixel) {

        @PopUpPosition int correctPopUpPosition = PopUpPosition.NONE;

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

    private CoachMarkPixelInfo createCoachMarkPixelInfo() {
        final CoachMarkLayoutMargin coachMarkLayoutMargin = mCoachMarkBuilder.getCoachMarkLayoutMargin();
        int leftMargin = mDimensionResourceProvider.getDimensionInPixel(coachMarkLayoutMargin.getLeftMarginForCoachMark());
        int rightMargin = mDimensionResourceProvider.getDimensionInPixel(coachMarkLayoutMargin.getRightMarginForCoachMark());
        int topMargin = mDimensionResourceProvider.getDimensionInPixel(coachMarkLayoutMargin.getTopMarginForCoachMark());
        int bottomMargin = mDimensionResourceProvider.getDimensionInPixel(coachMarkLayoutMargin.getBottomMarginForCoachMark());

        int actionBarHeightPixels = mDimensionResourceProvider.getDimensionInPixel(mCoachMarkBuilder.getActionBarHeight());
        int footerHeightPixels = mDimensionResourceProvider.getDimensionInPixel(mCoachMarkBuilder.getFooterHeight());

        int imageHeightInPixels = mDimensionResourceProvider.getDimensionInPixel(mCoachMarkBuilder.getImageLayoutInformation().getImageHeight());
        int imageWidthInPixels = mDimensionResourceProvider.getDimensionInPixel(mCoachMarkBuilder.getImageLayoutInformation().getImageWidth());

        int coachMarkPopUpHeightInPixels = mDimensionResourceProvider.getDimensionInPixel(R.dimen.coach_mark_pop_up_height);
        int coachMarkPopUpWidthInPixels = mDimensionResourceProvider.getDimensionInPixel(R.dimen.coach_mark_pop_up_width);

        int notchDimenInPixels = mDimensionResourceProvider.getDimensionInPixel(R.dimen.coach_mark_notch_width_height);
        int marginOffsetForNotchInPixels = mDimensionResourceProvider.getDimensionInPixel(R.dimen.coach_mark_notch_margin_offset);

        int widthHeightOffsetForCoachMarkPopUp = mDimensionResourceProvider.getDimensionInPixel(R.dimen.coach_mark_pop_up_width_height_offset);
        int coachMarkPopUpWidthInPixelsWithOffset = coachMarkPopUpWidthInPixels + widthHeightOffsetForCoachMarkPopUp + imageWidthInPixels;
        int coachMarkPopUpHeightInPixelsWithOffset = coachMarkPopUpHeightInPixels + widthHeightOffsetForCoachMarkPopUp + imageHeightInPixels;

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

    private int getActualTopMargin(int centerTopY, CoachMarkPixelInfo coachMarkDimenInPixel) {
        if (centerTopY + coachMarkDimenInPixel.getPopUpHeightInPixels() + coachMarkDimenInPixel.getWidthHeightOffsetForCoachMarkPopUp() + coachMarkDimenInPixel.getMarginRectInPixels().top + coachMarkDimenInPixel.getMarginRectInPixels().bottom > coachMarkDimenInPixel.getScreenHeightInPixels()) {
            return coachMarkDimenInPixel.getScreenHeightInPixels() - coachMarkDimenInPixel.getPopUpHeightInPixels() + coachMarkDimenInPixel.getWidthHeightOffsetForCoachMarkPopUp() + coachMarkDimenInPixel.getMarginRectInPixels().top + coachMarkDimenInPixel.getMarginRectInPixels().bottom;
        }
        return centerTopY + coachMarkDimenInPixel.getMarginRectInPixels().top;
    }

    private int getActualLeftMargin(int anchorTopX, CoachMarkPixelInfo coachMarkDimenInPixel) {
        if (anchorTopX + coachMarkDimenInPixel.getPopUpWidthInPixels() + coachMarkDimenInPixel.getWidthHeightOffsetForCoachMarkPopUp() + coachMarkDimenInPixel.getMarginRectInPixels().right + coachMarkDimenInPixel.getMarginRectInPixels().left > coachMarkDimenInPixel.getScreenWidthInPixels()) {
            return coachMarkDimenInPixel.getScreenWidthInPixels() - coachMarkDimenInPixel.getPopUpWidthInPixels() + coachMarkDimenInPixel.getWidthHeightOffsetForCoachMarkPopUp() + coachMarkDimenInPixel.getMarginRectInPixels().right + coachMarkDimenInPixel.getMarginRectInPixels().left;
        }
        return anchorTopX + coachMarkDimenInPixel.getMarginRectInPixels().left;
    }
    //endregion
}
