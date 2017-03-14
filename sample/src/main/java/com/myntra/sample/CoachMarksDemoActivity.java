package com.myntra.sample;

import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.myntra.coachmarks.PopUpCoachMark;
import com.myntra.coachmarks.builder.CoachMarkBuilder;
import com.myntra.coachmarks.builder.ImageLayoutInformation;
import com.myntra.coachmarks.builder.InfoForViewToMask;
import com.myntra.coachmarks.common.DialogDismissButtonPosition;
import com.myntra.coachmarks.common.PopUpPosition;

import java.util.ArrayList;

public class CoachMarksDemoActivity extends AppCompatActivity {

    private Button button;

    private static final String FILE_NAME_MYNTRA_FONT_REGULAR = "Whitney-Book-Bas.otf";
    private static final String FILE_NAME_MYNTRA_FONT_BOLD = "Whitney-Semibold-Bas.otf";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coachmarks_demo);
        button = (Button) findViewById(R.id.bt);
    }

    @Override
    protected void onResume() {
        super.onResume();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCoachMark();
            }
        });
    }

    private void showCoachMark() {
        Point anchorTop = new Point(0, 800);
        Point anchorBottom = new Point(200, 1000);
        InfoForViewToMask infoForViewToMask = InfoForViewToMask.create(new Point(500, 500), 800, 400).build();
        ArrayList<InfoForViewToMask> infoForViewToMaskArrayList = new ArrayList<>(1);
        infoForViewToMaskArrayList.add(infoForViewToMask);
        ImageLayoutInformation imageLayoutInformation = ImageLayoutInformation.create(R.dimen.image_width, R.dimen.image_height).build();
        CoachMarkBuilder testBuilder = CoachMarkBuilder.create(anchorTop, anchorBottom, R.string.coach_mark_pdp_collections)
                .setImageLayoutInformation(imageLayoutInformation)
                .setUserDesiredPopUpPositionWithRespectToView(PopUpPosition.RIGHT)
                .setPopUpCoachMarkDismissButtonPosition(DialogDismissButtonPosition.RIGHT)
                .setNotchPosition(.34).setInfoForViewToMaskList(infoForViewToMaskArrayList)
                .setImageDrawableRes(R.drawable.similar_item_drawable)
                .setFontStyleForCoachMarkText(FILE_NAME_MYNTRA_FONT_REGULAR)
                .setAnimationOnImage(R.anim.coach_mark_smaple_animation)
                .setFontStyleForDismissButton(FILE_NAME_MYNTRA_FONT_BOLD).build();
        PopUpCoachMark popUpCoachMark = PopUpCoachMark.newInstance(testBuilder);
        popUpCoachMark.show(getSupportFragmentManager(), "Test");
    }
}
