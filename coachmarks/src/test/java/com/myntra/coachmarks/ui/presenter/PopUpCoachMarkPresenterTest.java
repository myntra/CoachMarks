package com.myntra.coachmarks.ui.presenter;

import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.annotation.StringRes;

import com.myntra.coachmarks.builder.CoachMarkBuilder;
import com.myntra.coachmarks.common.CoachMarkTextGravity;
import com.myntra.coachmarks.providers.interfaces.IDimensionResourceProvider;
import com.myntra.coachmarks.providers.interfaces.IScreenInfoProvider;
import com.myntra.coachmarks.providers.interfaces.IStringResourceProvider;
import com.myntra.coachmarks.providers.interfaces.ITypeFaceProvider;
import com.myntra.coachmarks.ui.presentation.IPopUpCoachMarkPresentation;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.regex.Matcher;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;

@RunWith(PowerMockRunner.class)
@PrepareForTest(PopUpCoachMarkPresenter.class)
public class PopUpCoachMarkPresenterTest {

    @Mock
    CoachMarkBuilder mCoachMarkBuilder;

    @Mock
    IPopUpCoachMarkPresentation mPopUpCoachMarkPresentation;

    @Mock
    IStringResourceProvider mStringResourceProvider;

    @Mock
    IDimensionResourceProvider mDimensionResourceProvider;

    @Mock
    IScreenInfoProvider mScreenInfoProvider;

    @Mock
    ITypeFaceProvider mTypeFaceProvider;

    @Mock
    @StringRes
    int mockedStringRes;

    private PopUpCoachMarkPresenter mPopUpCoachMarkPresenter;

    @Before
    public void setUp() throws Exception {
        mPopUpCoachMarkPresenter = new PopUpCoachMarkPresenter(mStringResourceProvider,
                mDimensionResourceProvider, mTypeFaceProvider, mScreenInfoProvider);

        mPopUpCoachMarkPresenter.onCreate(mCoachMarkBuilder);
        mPopUpCoachMarkPresenter.onCreateView(mPopUpCoachMarkPresentation);
    }

    @Test
    public void onCreate() throws Exception {
        PopUpCoachMarkPresenter popUpCoachMarkPresenter = Mockito.mock(PopUpCoachMarkPresenter.class);
        popUpCoachMarkPresenter.onDestroy();
        Mockito.verifyNoMoreInteractions(mPopUpCoachMarkPresentation);
    }

    @Test
    public void onCreateView() throws Exception {
        PopUpCoachMarkPresenter popUpCoachMarkPresenter = Mockito.mock(PopUpCoachMarkPresenter.class);
        popUpCoachMarkPresenter.onCreateView(mPopUpCoachMarkPresentation);
        Mockito.verify(popUpCoachMarkPresenter, Mockito.times(1)).onCreateView(mPopUpCoachMarkPresentation);
        Mockito.verifyNoMoreInteractions(mPopUpCoachMarkPresentation);
    }

    @Test
    public void onDestroy() throws Exception {
        PopUpCoachMarkPresenter popUpCoachMarkPresenter = Mockito.mock(PopUpCoachMarkPresenter.class);
        popUpCoachMarkPresenter.onDestroy();
        Mockito.verify(popUpCoachMarkPresenter, Mockito.times(1)).onDestroy();
        Mockito.verifyNoMoreInteractions(mPopUpCoachMarkPresentation);
    }

    @Test
    public void onDestroyViewTest(){
       PopUpCoachMarkPresenter popUpCoachMarkPresenter = Mockito.mock(PopUpCoachMarkPresenter.class);
       popUpCoachMarkPresenter.onDestroyView();
       Mockito.verifyNoMoreInteractions(mPopUpCoachMarkPresentation);
    }

    @Test
    public void onOkButtonClicked() throws Exception {
        mPopUpCoachMarkPresenter.onOkButtonClicked();
        Mockito.verify(mPopUpCoachMarkPresentation, Mockito.times(1)).closeCoachMarkDialog();
        Mockito.verifyNoMoreInteractions(mPopUpCoachMarkPresentation);
    }

    @Test
    public void onShimClicked() throws Exception {
        mPopUpCoachMarkPresenter.onShimClicked();
        Mockito.verify(mPopUpCoachMarkPresentation, Mockito.times(1)).closeCoachMarkDialog();
        Mockito.verifyNoMoreInteractions(mPopUpCoachMarkPresentation);
    }

    @Test
    public void setMessageForCoachMarkTextTest(){
        Mockito.when(mStringResourceProvider.getStringFromResource(mockedStringRes)).thenReturn("MockedString");
        mPopUpCoachMarkPresenter.setMessageForCoachMarkText(mockedStringRes);

        Mockito.verify(mPopUpCoachMarkPresentation, Mockito.times(1)).setCoachMarkMessage("MockedString");
        Mockito.verifyNoMoreInteractions(mPopUpCoachMarkPresentation);
    }

    @Test
    public void setTypeFaceForDismissButtonNullFontFileTest(){
       mPopUpCoachMarkPresenter.setTypeFaceForDismissButton(null);
       Mockito.verify(mTypeFaceProvider, Mockito.times(0)).getTypeFaceFromAssets(Matchers.anyString());
       Mockito.verify(mPopUpCoachMarkPresentation, Mockito.times(0))
               .setTypeFaceForDismissButton((Typeface) Matchers.anyObject());
    }

    @Test
    public void setTypeFaceForDismissButtonNullTypeFaceTest(){
        Mockito.when(mTypeFaceProvider.getTypeFaceFromAssets(Matchers.anyString())).thenReturn(null);

        mPopUpCoachMarkPresenter.setTypeFaceForDismissButton(Matchers.anyString());

        Mockito.verify(mTypeFaceProvider, Mockito.times(1)).getTypeFaceFromAssets(Matchers.anyString());
        Mockito.verify(mPopUpCoachMarkPresentation, Mockito.times(0))
                .setTypeFaceForDismissButton((Typeface) Matchers.anyObject());

        Mockito.verifyNoMoreInteractions(mPopUpCoachMarkPresentation);
    }

    @Test
    public void setTypeFaceForDismissButtonTest(){

        Typeface typeface = Mockito.mock(Typeface.class);

        Mockito.when(mTypeFaceProvider.getTypeFaceFromAssets(Matchers.anyString())).thenReturn(typeface);

        mPopUpCoachMarkPresenter.setTypeFaceForDismissButton("fontFilePath");
        Mockito.verify(mTypeFaceProvider, Mockito.times(1)).getTypeFaceFromAssets(Matchers.anyString());
        Mockito.verify(mPopUpCoachMarkPresentation, Mockito.times(1))
                .setTypeFaceForDismissButton(typeface);

        Mockito.verifyNoMoreInteractions(mPopUpCoachMarkPresentation);
    }

    @Test
    public void setTypeFaceForCoachMarkTextNullFontFileTest(){
        mPopUpCoachMarkPresenter.setTypeFaceForCoachMarkText(null);
        Mockito.verify(mTypeFaceProvider, Mockito.times(0)).getTypeFaceFromAssets(Matchers.anyString());
        Mockito.verify(mPopUpCoachMarkPresentation, Mockito.times(0))
                .setTypeFaceForPopUpText((Typeface) Matchers.anyObject());
    }

    @Test
    public void setTypeFaceForCoachMarkTextNullTypefaceTest(){
        Mockito.when(mTypeFaceProvider.getTypeFaceFromAssets(Matchers.anyString())).thenReturn(null);

        mPopUpCoachMarkPresenter.setTypeFaceForCoachMarkText("fontFilePath");

        Mockito.verify(mTypeFaceProvider, Mockito.times(1)).getTypeFaceFromAssets("fontFilePath");
        Mockito.verify(mPopUpCoachMarkPresentation, Mockito.times(0))
                .setTypeFaceForPopUpText((Typeface) Matchers.anyObject());

        Mockito.verifyNoMoreInteractions(mPopUpCoachMarkPresentation);
    }

    @Test
    public void setTypeFaceForCoachMarkTextTest(){
        Typeface typeface = Mockito.mock(Typeface.class);

        Mockito.when(mTypeFaceProvider.getTypeFaceFromAssets(Matchers.anyString())).thenReturn(typeface);

        mPopUpCoachMarkPresenter.setTypeFaceForCoachMarkText("fontFilePath");
        Mockito.verify(mTypeFaceProvider, Mockito.times(1)).getTypeFaceFromAssets(Matchers.anyString());
        Mockito.verify(mPopUpCoachMarkPresentation, Mockito.times(1))
                .setTypeFaceForPopUpText(typeface);

        Mockito.verifyNoMoreInteractions(mPopUpCoachMarkPresentation);
    }

    @Test
    public void setGravityForCoachMarkTextLeftGravityTest(){
        mPopUpCoachMarkPresenter.setGravityForCoachMarkText(CoachMarkTextGravity.CENTER);

        Mockito.verify(mPopUpCoachMarkPresentation, Mockito.times(1))
                .setUpGravityForCoachMarkText(CoachMarkTextGravity.CENTER);
        Mockito.verifyNoMoreInteractions(mPopUpCoachMarkPresentation);
    }

    @Test
    public void setGravityForCoachMarkTextRightGravityTest(){
        mPopUpCoachMarkPresenter.setGravityForCoachMarkText(CoachMarkTextGravity.RIGHT);

        Mockito.verify(mPopUpCoachMarkPresentation, Mockito.times(1))
                .setUpGravityForCoachMarkText(CoachMarkTextGravity.RIGHT);
        Mockito.verifyNoMoreInteractions(mPopUpCoachMarkPresentation);
    }

    @Test
    public void setGravityForCoachMarkTextCenterGravityTest(){
        mPopUpCoachMarkPresenter.setGravityForCoachMarkText(CoachMarkTextGravity.LEFT);

        Mockito.verify(mPopUpCoachMarkPresentation, Mockito.times(1))
                .setUpGravityForCoachMarkText(CoachMarkTextGravity.LEFT);
        Mockito.verifyNoMoreInteractions(mPopUpCoachMarkPresentation);
    }

    @Test
    public void getMarginLeftForNotchTest(){
        assertEquals(140, mPopUpCoachMarkPresenter.getMarginLeftForNotch(.75,190, 50));
        assertEquals(25, mPopUpCoachMarkPresenter.getMarginLeftForNotch(.25,100, 25));
    }

}