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
//        byte[] byteArray = getIntent().getByteArrayExtra("gambar profile");
        String fNameProfile = getIntent().getStringExtra("gambar profile");
        String pathProfile = Environment.getExternalStorageDirectory() + File.separator + fNameProfile + ".png";

        // zoom image for image 1 ktp
        String fNameKTP1 = getIntent().getStringExtra("imageKTP1");
        String pathKTP1 = Environment.getExternalStorageDirectory() + File.separator + fNameKTP1 + ".png";

        // zoom image for image ktp 2
        String fNameKTP2 = getIntent().getStringExtra("imageKTP2");
        String pathKTP2 = Environment.getExternalStorageDirectory() + File.separator + fNameKTP2 + ".png";

        // zoom image for image struk
        String fNameStruk = getIntent().getStringExtra("gambar struk");
        String pathStruk = Environment.getExternalStorageDirectory() + File.separator + fNameStruk + ".png";

        if (pathProfile.equals("/storage/emulated/0/Gambar.png")) {
//            bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            bmp = BitmapFactory.decodeFile(pathProfile);
        } else if (pathKTP1.equals("/storage/emulated/0/GambarKTP1.png")) {
            bmp = BitmapFactory.decodeFile(pathKTP1);
        } else if (pathKTP2.equals("/storage/emulated/0/GambarKTP2.png")) {
            bmp = BitmapFactory.decodeFile(pathKTP2);
        } else if (pathStruk.equals("/storage/emulated/0/ImageStruk.png")) {
            bmp = BitmapFactory.decodeFile(pathStruk);
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
