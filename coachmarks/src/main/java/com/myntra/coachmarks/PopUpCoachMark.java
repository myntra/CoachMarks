package com.myntra.coachmarks;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.f2prateek.bundler.FragmentBundlerCompat;
import com.myntra.coachmarks.builder.CoachMarkBuilder;
import com.myntra.coachmarks.common.CoachMarkAlignPosition;
import com.myntra.coachmarks.common.CoachMarkLayoutOrientation;
import com.myntra.coachmarks.common.CoachMarkTextGravity;
import com.myntra.coachmarks.providers.DefaultDimensionResourceProvider;
import com.myntra.coachmarks.providers.DefaultScreenInfoProvider;
import com.myntra.coachmarks.providers.DefaultStringResourceProvider;
import com.myntra.coachmarks.providers.DefaultTypeFaceProvider;
import com.myntra.coachmarks.providers.interfaces.IDimensionResourceProvider;
import com.myntra.coachmarks.providers.interfaces.IScreenInfoProvider;
import com.myntra.coachmarks.providers.interfaces.IStringResourceProvider;
import com.myntra.coachmarks.providers.interfaces.ITypeFaceProvider;
import com.myntra.coachmarks.ui.common.BaseViews;
import com.myntra.coachmarks.ui.presentation.IPopUpCoachMarkPresentation;
import com.myntra.coachmarks.ui.presenter.PopUpCoachMarkPresenter;
import com.myntra.coachmarks.ui.utils.TransitionUtils;

import butterknife.BindView;
import zeta.android.utils.view.ViewUtils;

public class PopUpCoachMark extends DialogFragment implements IPopUpCoachMarkPresentation, View.OnClickListener {

    private static final String TAG = PopUpCoachMark.class.getSimpleName();

    private static final int NO_MARGIN = 0;
    private static final String ARG_POP_UP_COACH_MARK_BUILDER_PARCEL = "ARG_POP_UP_COACH_MARK_BUILDER_PARCEL";

    private Views mViews;
    private PopUpCoachMarkPresenter mPresenter;

    static class Views extends BaseViews {

        @BindView(R2.id.rl_coachmark_parent)
        RelativeLayout rlCoachMarkParent;

        @BindView(R2.id.tv_coachmark_text)
        AppCompatTextView tvPopUpCoachMarkText;

        @BindView(R2.id.tv_ok_button)
        AppCompatTextView tvPopUpDismissButton;

        @BindView(R2.id.v_separator)
        ImageView vSeparator;

        @BindView(R2.id.iv_coachmark_image)
        ImageView ivCoachMarkImage;

        @BindView(R2.id.rl_shim_out_view_parent)
        FrameLayout rlShimOutViewParent;

        @BindView(R2.id.rl_coachmark_text_part)
        RelativeLayout rlCoachMarkTextPart;

        @BindView(R2.id.ll_coach_mark_pop_up_parent)
        LinearLayout llPopUpCoachMarkParent;

        @BindView(R2.id.right_bottom_notch)
        View vRightBottomNotch;

        @BindView(R2.id.left_top_notch)
        View vLeftTopNotch;

        @BindView(R2.id.v_notch_base_white_left)
        View vNotchBaseWhiteLeft;

        @BindView(R2.id.v_notch_base_white_top)
        View vNotchBaseWhiteTop;

        @BindView(R2.id.ll_coachmark_text_wrapper_layout)
        LinearLayout llCoachMarkTextWrapperLayout;

        Views(@NonNull View root) {
            super(root);
        }
    }

    public static PopUpCoachMark newInstance(CoachMarkBuilder builder) {
        return FragmentBundlerCompat.create(new PopUpCoachMark())
                .put(ARG_POP_UP_COACH_MARK_BUILDER_PARCEL, builder)
                .build();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        Window window = dialog.getWindow();
        if (window == null) {
            Log.wtf(TAG, "getWindow() should not be null ever");
            return dialog;
        }
        window.getAttributes().windowAnimations = R.style.coach_mark_dialog_animation;
        window.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(getContext(),
                R.color.coach_mark_transparent_color)));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_FRAME, R.style.AppTheme);

        final Context context = getContext();
        final IStringResourceProvider stringResourceProvider = new DefaultStringResourceProvider(context);
        final IDimensionResourceProvider dimensionResourceProvider = new DefaultDimensionResourceProvider(context);
        final ITypeFaceProvider typeFaceProvider = new DefaultTypeFaceProvider(context);
        final IScreenInfoProvider screenInfoProvider = new DefaultScreenInfoProvider(context);

        mPresenter = new PopUpCoachMarkPresenter(stringResourceProvider, dimensionResourceProvider,
                typeFaceProvider, screenInfoProvider);
        //coach mark presenter params is injected in onCreate as it's available from bundle
        //If we decide to migrate to DI pattern this will be useful
        final CoachMarkBuilder coachMarkBuilderFromBundle = getCoachMarkBuilderFromBundle();
        assert coachMarkBuilderFromBundle != null;
        mPresenter.onCreate(coachMarkBuilderFromBundle);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        final View view = inflater.inflate(R.layout.pop_up_coach_mark, container, false);
        mViews = new Views(view);
        mPresenter.onCreateView(this);
        registerClickListener();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.onViewCreated();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.onDestroyView();
        unRegisterClickListener();
        mViews = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
        mPresenter = null;
    }

    //region presentation methods
    @Override
    public void createViewToBeMaskedOut(int startX, int startY, int height, int width) {
        View view = new View(getContext());
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(width, height);
        layoutParams.setMargins(startX, startY, NO_MARGIN, NO_MARGIN);
        view.setLayoutParams(layoutParams);
        view.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.coach_mark_black_translucent));
        mViews.rlShimOutViewParent.addView(view);
    }

    @Override
    public void dismissWithError(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
        mViews.rlCoachMarkParent.setVisibility(View.INVISIBLE);
    }

    @Override
    public void closeCoachMarkDialog() {
        dismiss();
    }

    @Override
    public void setImageInformation(double centerX, double centerY, int imageWidth, int imageHeight, int backGroundTint, int imageDrawableRes) {
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(imageWidth, imageHeight);
        layoutParams.setMargins((int) (centerX - (imageWidth / 2)), (int) (centerY - (imageHeight / 2)), NO_MARGIN, NO_MARGIN);
        mViews.ivCoachMarkImage.setLayoutParams(layoutParams);
        mViews.ivCoachMarkImage.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.coach_mark_transparent_color));
        mViews.ivCoachMarkImage.setImageDrawable(ContextCompat.getDrawable(getContext(), imageDrawableRes));
        mViews.ivCoachMarkImage.setColorFilter(ContextCompat.getColor(getContext(), backGroundTint));
    }

    @Override
    public void setPopUpViewPositionWithRespectToImage(@CoachMarkAlignPosition int alignPosition) {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mViews.llPopUpCoachMarkParent.getLayoutParams();
        layoutParams.addRule(alignPosition, mViews.ivCoachMarkImage.getId());
        mViews.llPopUpCoachMarkParent.setLayoutParams(layoutParams);
    }

    @Override
    public void setPopUpViewTopLeft(@NonNull Rect margin, @CoachMarkLayoutOrientation int orientation) {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mViews.llPopUpCoachMarkParent.getLayoutParams();
        layoutParams.setMargins(margin.left, margin.top, margin.right, margin.bottom);
        mViews.llPopUpCoachMarkParent.setLayoutParams(layoutParams);
        mViews.llPopUpCoachMarkParent.setOrientation(orientation);
        mViews.vRightBottomNotch.setVisibility(View.VISIBLE);
        mViews.vLeftTopNotch.setVisibility(View.GONE);
    }

    @Override
    public void setPopUpViewBottomRight(@NonNull Rect margin, @CoachMarkLayoutOrientation int orientation) {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mViews.llPopUpCoachMarkParent.getLayoutParams();
        layoutParams.setMargins(margin.left, margin.top, margin.right, margin.bottom);
        mViews.llPopUpCoachMarkParent.setLayoutParams(layoutParams);
        mViews.llPopUpCoachMarkParent.setOrientation(orientation);
        mViews.vRightBottomNotch.setVisibility(View.GONE);
        mViews.vLeftTopNotch.setVisibility(View.VISIBLE);
    }

    @Override
    public void setDismissButtonPositionLeft() {
        RelativeLayout.LayoutParams okButtonLayoutParams = (RelativeLayout.LayoutParams) mViews.tvPopUpDismissButton.getLayoutParams();
        RelativeLayout.LayoutParams separatorLayoutParams = (RelativeLayout.LayoutParams) mViews.vSeparator.getLayoutParams();
        RelativeLayout.LayoutParams coachMarkTextParams = (RelativeLayout.LayoutParams) mViews.llCoachMarkTextWrapperLayout.getLayoutParams();
        okButtonLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, mViews.rlCoachMarkTextPart.getId());
        mViews.tvPopUpDismissButton.setLayoutParams(okButtonLayoutParams);
        separatorLayoutParams.addRule(RelativeLayout.RIGHT_OF, mViews.tvPopUpDismissButton.getId());
        mViews.vSeparator.setLayoutParams(separatorLayoutParams);
        coachMarkTextParams.addRule(RelativeLayout.RIGHT_OF, mViews.vSeparator.getId());
        mViews.llCoachMarkTextWrapperLayout.setLayoutParams(coachMarkTextParams);
    }

    @Override
    public void setDismissButtonPositionRight() {
        RelativeLayout.LayoutParams okButtonLayoutParams = (RelativeLayout.LayoutParams) mViews.tvPopUpDismissButton.getLayoutParams();
        RelativeLayout.LayoutParams separatorLayoutParams = (RelativeLayout.LayoutParams) mViews.vSeparator.getLayoutParams();
        RelativeLayout.LayoutParams coachMarkTextParams = (RelativeLayout.LayoutParams) mViews.llCoachMarkTextWrapperLayout.getLayoutParams();
        coachMarkTextParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, mViews.rlCoachMarkTextPart.getId());
        mViews.llCoachMarkTextWrapperLayout.setLayoutParams(coachMarkTextParams);
        separatorLayoutParams.addRule(RelativeLayout.RIGHT_OF, mViews.llCoachMarkTextWrapperLayout.getId());
        mViews.vSeparator.setLayoutParams(separatorLayoutParams);
        okButtonLayoutParams.addRule(RelativeLayout.RIGHT_OF, mViews.vSeparator.getId());
        mViews.tvPopUpDismissButton.setLayoutParams(okButtonLayoutParams);
    }

    @Override
    public void startScaleAnimationOnImage() {
        mViews.ivCoachMarkImage.startAnimation(TransitionUtils.createScaleAnimation());
    }


    @Override
    public void startThrobAnimationOnImage() {
        mViews.ivCoachMarkImage.startAnimation(TransitionUtils.createThrobAnimation());
    }

    @Override
    public void startAlphaAnimationOnImage() {
        mViews.ivCoachMarkImage.startAnimation(TransitionUtils.createAlphaAnimation());
    }

    @Override
    public void setNotchPositionIfPopUpTopLeft(@NonNull Rect margin, float rotation) {
        mViews.vRightBottomNotch.setRotation(rotation);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mViews.vRightBottomNotch.getLayoutParams();
        layoutParams.setMargins(margin.left, margin.top, margin.right, margin.bottom);
        mViews.vRightBottomNotch.setLayoutParams(layoutParams);
    }

    @Override
    public void setNotchPositionIfPopUpBottomRight(@NonNull Rect margin, float rotation) {
        mViews.vLeftTopNotch.setRotation(rotation);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mViews.vLeftTopNotch.getLayoutParams();
        layoutParams.setMargins(margin.left, margin.top, margin.right, margin.bottom);
        mViews.vLeftTopNotch.setLayoutParams(layoutParams);
    }

    @Override
    public void setTypeFaceForDismissButton(Typeface typeface) {
        mViews.tvPopUpDismissButton.setTypeface(typeface);
    }

    @Override
    public void setTypeFaceForPopUpText(Typeface typeface) {
        mViews.tvPopUpCoachMarkText.setTypeface(typeface);
    }

    @Override
    public void setUpGravityForCoachMarkText(@CoachMarkTextGravity int gravity) {
        mViews.tvPopUpCoachMarkText.setGravity(gravity);
    }

    @Override
    public void uiAdjustmentForNotchIfPopUpRight(@NonNull Rect margin) {
        ViewUtils.setToVisible(mViews.vNotchBaseWhiteLeft);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mViews.vNotchBaseWhiteLeft.getLayoutParams();
        layoutParams.setMargins(margin.left, margin.top, margin.right, margin.bottom);
        mViews.vNotchBaseWhiteLeft.setLayoutParams(layoutParams);
    }

    @Override
    public void uiAdjustmentForNotchIfPopUpBottom(@NonNull Rect margin) {
        ViewUtils.setToVisible(mViews.vNotchBaseWhiteTop);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mViews.vNotchBaseWhiteTop.getLayoutParams();
        layoutParams.setMargins(margin.left, margin.top, margin.right, margin.bottom);
        mViews.vNotchBaseWhiteTop.setLayoutParams(layoutParams);
    }

    @Override
    public void setCoachMarkMessage(String message) {
        mViews.tvPopUpCoachMarkText.setText(message);
    }

    //endregion

    //region click listeners
    @Override
    public void onClick(@Nullable View v) {
        if (v == null) {
            return;
        }
        if (v.getId() == R.id.tv_ok_button) {
            if (mPresenter != null) {
                mPresenter.onOkButtonClicked();
            }
        } else if (v.getId() == R.id.rl_shim_out_view_parent) {
            mPresenter.onShimClicked();
        }
    }
    //endregion

    //region helper methods
    @Nullable
    private CoachMarkBuilder getCoachMarkBuilderFromBundle() {
        return getArguments().getParcelable(ARG_POP_UP_COACH_MARK_BUILDER_PARCEL);
    }

    private void registerClickListener() {
        mViews.tvPopUpDismissButton.setOnClickListener(this);
        mViews.rlShimOutViewParent.setOnClickListener(this);
    }

    private void unRegisterClickListener() {
        mViews.tvPopUpDismissButton.setOnClickListener(null);
        mViews.rlShimOutViewParent.setOnClickListener(null);
    }
    //endregion

}
