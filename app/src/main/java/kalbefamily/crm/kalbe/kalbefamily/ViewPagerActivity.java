package kalbefamily.crm.kalbe.kalbefamily;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import java.io.File;

import uk.co.senab.photoview.PhotoView;

/**
 * Created by Rian Andrivani on 10/4/2017.
 */

public class ViewPagerActivity extends AppCompatActivity {
    static Bitmap bmp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.primary_color_theme));
        }
        setContentView(R.layout.activity_view_pager);
        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);

        viewPager.setAdapter(new SamplePagerAdapter());

        // zoom image for profile photo
        byte[] byteArray = getIntent().getByteArrayExtra("gambar profile");

        // zoom image for image 1 ktp
        byte[] byteArrayKTP1 = getIntent().getByteArrayExtra("takeImageKTP1");
        String fName = getIntent().getStringExtra("imageKTP1");
        String path = Environment.getExternalStorageDirectory() + File.separator + fName + ".png";

        if (byteArray != null) {
            bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        } else if (byteArrayKTP1 == null) {
            bmp = BitmapFactory.decodeFile(path);
        } else if (byteArrayKTP1 != null) {
            bmp = BitmapFactory.decodeByteArray(byteArrayKTP1, 0, byteArrayKTP1.length);
        }

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
