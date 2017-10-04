package kalbefamily.crm.kalbe.kalbefamily;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;

import uk.co.senab.photoview.PhotoView;

/**
 * Created by Rian Andrivani on 10/4/2017.
 */

public class ViewPagerActivity extends AppCompatActivity {
    static Bitmap bmp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager);
        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);

        viewPager.setAdapter(new SamplePagerAdapter());

        byte[] byteArray = getIntent().getByteArrayExtra("gambar profile");
         bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

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
