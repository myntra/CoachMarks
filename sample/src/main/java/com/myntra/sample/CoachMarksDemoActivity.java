package com.myntra.sample;

import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.myntra.coachmarks.PopUpCoachMark;
import com.myntra.coachmarks.builder.CoachMarkBuilder;
import com.myntra.coachmarks.builder.ImageLayoutInformation;
import com.myntra.coachmarks.builder.InfoForViewToMask;
import com.myntra.coachmarks.common.CoachMarkTextGravity;
import com.myntra.coachmarks.common.DialogDismissButtonPosition;
import com.myntra.coachmarks.common.PopUpPosition;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CoachMarksDemoActivity extends AppCompatActivity {

    @BindView(R.id.bt_show_coach_mark)
    Button mShowCoachMarkButton;

    @BindView(R.id.iv_sample_image)
    ImageView mDemoImageView;

    private static final String FILE_NAME_MYNTRA_FONT_REGULAR = "Whitney-Book-Bas.otf";
    private static final String FILE_NAME_MYNTRA_FONT_BOLD = "Whitney-Semibold-Bas.otf";

    private int mScreenHeight;
    private int mScreenWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coachmarks_demo);
        ButterKnife.bind(this);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        mScreenHeight = displayMetrics.heightPixels;
        mScreenWidth = displayMetrics.widthPixels;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mShowCoachMarkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCoachMark();
            }
        });
    }

    private void showCoachMark() {
        Point anchorTop = new Point(0, 0);
        Point anchorBottom = new Point(mScreenWidth, mScreenHeight);
        InfoForViewToMask infoForViewToMask = InfoForViewToMask.create(new Point(0, 0), mScreenHeight, mScreenWidth).build();
        ArrayList<InfoForViewToMask> infoForViewToMaskArrayList = new ArrayList<>(1);
        infoForViewToMaskArrayList.add(infoForViewToMask);
        ImageLayoutInformation imageLayoutInformation = ImageLayoutInformation.create(R.dimen.image_width, R.dimen.image_height).build();
        CoachMarkBuilder testBuilder = CoachMarkBuilder.create(anchorTop, anchorBottom, R.string.coach_mark_sample_message)
                .setImageLayoutInformation(imageLayoutInformation)
                .setUserDesiredPopUpPositionWithRespectToView(PopUpPosition.TOP)
                .setPopUpCoachMarkDismissButtonPosition(DialogDismissButtonPosition.LEFT)
                .setCoachMarkTextGravity(CoachMarkTextGravity.CENTER)
                .setNotchPosition(.34).setInfoForViewToMaskList(infoForViewToMaskArrayList)
                .setImageDrawableRes(R.drawable.similar_item_drawable)
                .setFontStyleForCoachMarkText(FILE_NAME_MYNTRA_FONT_REGULAR)
                .setAnimationOnImage(R.anim.coach_mark_smaple_animation)
                .setFontStyleForDismissButton(FILE_NAME_MYNTRA_FONT_BOLD).build();
        PopUpCoachMark popUpCoachMark = PopUpCoachMark.newInstance(testBuilder);
        popUpCoachMark.show(getSupportFragmentManager(), "Test");
    }


}
