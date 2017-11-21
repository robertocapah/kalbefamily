package kalbefamily.crm.kalbe.kalbefamily;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import java.io.File;

import uk.co.senab.photoview.PhotoView;

public class ZoomRewardCard extends AppCompatActivity {
    static Bitmap bmp;
    String link;

    public void onBackPressed() {
        RewardCardActivity.test();

        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom_reward_card);
        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);

        viewPager.setAdapter(new SamplePagerAdapter());

        // zoom image for profile photo
//        byte[] byteArray = getIntent().getByteArrayExtra("gambar profile");
        String fName = getIntent().getStringExtra("gambar");
        String path = Environment.getExternalStorageDirectory() + File.separator + fName + ".png";

        bmp = BitmapFactory.decodeFile(path);

//        if (path.equals("/storage/emulated/0/Gambar.png")) {
////            bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
//            bmp = BitmapFactory.decodeFile(path);
//        }

        String asd = "";
    }

    static class SamplePagerAdapter extends PagerAdapter {

        private static final int[] sDrawables = { R.mipmap.banner, R.mipmap.banner, R.mipmap.banner,
                R.mipmap.banner, R.mipmap.banner, R.mipmap.banner };

        @Override
        public int getCount() {
            return 1;
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {
            PhotoView photoView = new PhotoView(container.getContext());
//            photoView.setImageResource(sDrawables[position]);
            photoView.setImageBitmap(bmp);

            // Now just add PhotoView to ViewPager and return it
            container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

            return photoView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }
}
