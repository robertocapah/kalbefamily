package kalbefamily.crm.kalbe.kalbefamily;

import android.accounts.AccountManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.WindowManager;

import com.github.paolorotolo.appintro.AppIntro;

import kalbefamily.crm.kalbe.kalbefamily.BL.AuthenticatorUtil;

import static com.oktaviani.dewi.mylibrary.authenticator.AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS;

/**
 * Created by Rian Andrivani on 6/16/2017.
 */


public class IntroActivity extends AppIntro {
    private AccountManager mAccountManager;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAccountManager = AccountManager.get(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        addSlide(SampleSlide.newInstance(R.layout.slide_1));
        addSlide(SampleSlide.newInstance(R.layout.slide_2));
        addSlide(SampleSlide.newInstance(R.layout.slide_3));
//        addSlide(AppIntroFragment.newInstance("Intro application", "Kalbe Nutritionals", R.drawable.ic_skip_white, getColor(R.color.colorAccent)));

        setFlowAnimation();
//        setContentView(R.layout.activity_intro);
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        new AuthenticatorUtil().showAccountPicker(IntroActivity.this, mAccountManager, AUTHTOKEN_TYPE_FULL_ACCESS);
        Intent intent = new Intent(this, NewMemberActivity.class);
        finish();
        startActivity(intent);
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        new AuthenticatorUtil().showAccountPicker(IntroActivity.this, mAccountManager, AUTHTOKEN_TYPE_FULL_ACCESS);
//        Intent intent = new Intent(this, NewMemberActivity.class);
//        finish();
//        startActivity(intent);
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
        // Do something when the slide changes.
    }


}
