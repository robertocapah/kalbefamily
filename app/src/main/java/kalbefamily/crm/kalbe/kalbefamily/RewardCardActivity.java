package kalbefamily.crm.kalbe.kalbefamily;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AbsoluteLayout;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import kalbefamily.crm.kalbe.kalbefamily.Common.clsUserMember;
import kalbefamily.crm.kalbe.kalbefamily.Repo.clsUserMemberRepo;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by Rian Andrivani on 7/31/2017.
 */

public class RewardCardActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    TextView etNumber, etNama, etTglBerlaku;
    Spinner spinner;
    static AbsoluteLayout layoutDepan;
    static AbsoluteLayout layoutBelakang;
    ImageView tampakDepan, tampakBelakang;
    private String txtLink = "null";
    private static WebView mWebView;
    PhotoViewAttacher photoViewAttacher;

    List<clsUserMember> dataMember = null;

    public void onBackPressed() {
        Intent intent = new Intent(this, HomeMenu.class);
        finish();
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_reward);

        etNumber =(TextView) findViewById(R.id.textView7);
        etNama = (TextView) findViewById(R.id.textView_nama);
        etTglBerlaku = (TextView) findViewById(R.id.textView_berlaku);
        spinner = (Spinner) findViewById(R.id.spinner);
        layoutDepan = (AbsoluteLayout) findViewById(R.id.absoluteLayout);
        layoutBelakang = (AbsoluteLayout) findViewById(R.id.absoluteLayout_belakang);
        tampakDepan = (ImageView) findViewById(R.id.tampakDepan);
        tampakBelakang = (ImageView) findViewById(R.id.tampakBelakang);
        mWebView = (WebView) findViewById(R.id.webViewCard);
//        spinNama = (Spinner) findViewById(R.id.spnNama);

        clsUserMemberRepo repo = new clsUserMemberRepo(getApplicationContext());
        try {
            dataMember = (List<clsUserMember>) repo.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        layoutDepan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layoutDepan.setEnabled(false);
                view.setDrawingCacheEnabled(true);

                view.buildDrawingCache();

                Bitmap bm = view.getDrawingCache();

                File file = new File(Environment.getExternalStorageDirectory() + File.separator + "Gambar" + ".png");
                file.delete();
                FileOutputStream fOut = null;
                try {
                    fOut = new FileOutputStream(file);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                bm.compress(Bitmap.CompressFormat.PNG, 85, fOut);
                try {
                    fOut.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    fOut.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Intent intent = new Intent(RewardCardActivity.this, ZoomRewardCard.class);
                intent.putExtra("gambar", "Gambar");
                startActivity(intent);

            }
        });

        layoutBelakang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layoutBelakang.setClickable(false);
                view.setDrawingCacheEnabled(true);

                view.buildDrawingCache();

                Bitmap bm = view.getDrawingCache();

                File file = new File(Environment.getExternalStorageDirectory() + File.separator + "Gambar" + ".png");
                file.delete();
                FileOutputStream fOut = null;
                try {
                    fOut = new FileOutputStream(file);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                bm.compress(Bitmap.CompressFormat.PNG, 85, fOut);
                try {
                    fOut.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    fOut.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Intent intent = new Intent(RewardCardActivity.this, ZoomRewardCard.class);
                intent.putExtra("gambar", "Gambar");
                startActivity(intent);
            }
        });

        // spinnerTelp name member
//        List<String> dataMemberName = new ArrayList<>();
//        if (dataMember.size() > 0) {
//            for (clsUserMember data : dataMember) {
//                dataMemberName.add(data.getTxtNama());
//            }
//        }
//
//        ArrayAdapter<String> adapterMemberName = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, dataMemberName);
//        spinNama.setAdapter(adapterMemberName);

        // Spinner click listener
        spinner.setOnItemSelectedListener(RewardCardActivity.this);

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("Tampak Depan");
        categories.add("Tampak Belakang");

        // Creating adapter for spinnerTelp
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinnerTelp
        spinner.setAdapter(dataAdapter);

        String sub, sub2, sub3 , sub4;
        String member1 = dataMember.get(0).getTxtMemberId();
//        sub = member1.substring(0, member1.length() - 8);
//        sub2 = member1.substring(4, member1.length() - 4);
//        sub3 = member1.substring(8, member1.length());
        if(member1.length() == 16){
            sub = member1.substring(0, 4);
            sub2 = member1.substring(4, 8);
            sub3 = member1.substring(8, 12);
            sub4 = member1.substring(12, 16);
            etNumber.setText(sub +" "+ sub2 +" "+ sub3+" "+sub4);
        }else if(member1.length() == 12){
            sub = member1.substring(0, 4);
            sub2 = member1.substring(4, 8);
            sub3 = member1.substring(8, 12);
            etNumber.setText(sub +" "+ sub2 +" "+ sub3);
        }

        etNama.setText(dataMember.get(0).getTxtNama().toUpperCase());
        etTglBerlaku.setText(dataMember.get(0).getTxtTglBerlaku());

//        photoViewAttacher = new PhotoViewAttacher(tampakBelakang);
//
//        photoViewAttacher.update();

        // Enable Javascript
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

//        mWebView.loadUrl("http://beta.html5test.com/");
        txtLink = getIntent().getStringExtra("link");
//        txtLink = "file:///android_asset/rewardCard.html";
        if (!txtLink.equals("null")){
            mWebView.loadUrl(txtLink);
        } else {
            mWebView.loadUrl("https://www.google.co.id");
        }

        // Force links and redirects to open in the WebView instead of in a browser
        mWebView.setWebViewClient(new WebViewClient());

        // Stop local links and redirects from opening in browser instead of WebView
        mWebView.setWebViewClient(new MyAppWebViewClient());

        // set background
//        mWebView.setBackgroundResource(R.drawable.kartu_virtual_back_desain);
        mWebView.setBackgroundColor(Color.TRANSPARENT);

        mWebView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                mWebView.setEnabled(false);
                switch(event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        layoutBelakang.setDrawingCacheEnabled(true);

                        layoutBelakang.buildDrawingCache();

                        Bitmap bm = layoutBelakang.getDrawingCache();
                        zoomTampakBelakang(bm);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        layoutBelakang.setDrawingCacheEnabled(true);

                        layoutBelakang.buildDrawingCache();

                        Bitmap bm2 = layoutBelakang.getDrawingCache();
                        break;
                    case MotionEvent.ACTION_UP:
                        layoutBelakang.setDrawingCacheEnabled(true);

                        layoutBelakang.buildDrawingCache();

                        Bitmap bm3 = layoutBelakang.getDrawingCache();
                        break;
                }

                return false;
            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String depan = adapterView.getItemAtPosition(i).toString();

        if (depan == "Tampak Depan") {
            layoutDepan.setVisibility(View.VISIBLE);
            layoutBelakang.setVisibility(View.GONE);
        } else {
            layoutDepan.setVisibility(View.GONE);
            layoutBelakang.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void zoomTampakBelakang(Bitmap bm) {
        File file = new File(Environment.getExternalStorageDirectory() + File.separator + "Gambar" + ".png");
        file.delete();
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        bm.compress(Bitmap.CompressFormat.PNG, 85, fOut);
        try {
            fOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Intent intent = new Intent(RewardCardActivity.this, ZoomRewardCard.class);
        intent.putExtra("gambar", "Gambar");
        startActivity(intent);
    }

    public static void test() {
        layoutDepan.setEnabled(true);
        layoutBelakang.setEnabled(true);
        mWebView.setEnabled(true);
    }
}
