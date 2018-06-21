package kalbefamily.crm.kalbe.kalbefamily;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import kalbefamily.crm.kalbe.kalbefamily.Common.clsImageStruk;
import kalbefamily.crm.kalbe.kalbefamily.Repo.clsImageStrukRepo;

public class InputStrukDetailActivity extends AppCompatActivity {

    private Toolbar toolbar;
    TextView tvImageName, tvHasilOCR, tvReason;
    Button btnStatus;
    ImageView detailImageInputStruk;
    private String txtID = "";
    private String strHasilOCR, strReason, strUrlImgInputStruk;

    private clsImageStrukRepo repoImageStruk;
    List<clsImageStruk> listData;

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.primary_color_theme));
        }

        setContentView(R.layout.activity_input_struk_detail);
        toolbar = (Toolbar) findViewById(R.id.toolbarInputStruk);
        toolbar.setTitle("Input Struk Detail");
        setSupportActionBar(toolbar);

        // set enable toolbar button back
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // toolbar button for move to before screen
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
            }
        });

        detailImageInputStruk = (ImageView) findViewById(R.id.detailImageInputStruk);
        tvImageName = (TextView) findViewById(R.id.tvImageName);
        tvHasilOCR = (TextView) findViewById(R.id.tvHasilOCR);
        tvReason = (TextView) findViewById(R.id.tvReason);
        btnStatus = (Button) findViewById(R.id.btnStatus);
        btnStatus.setEnabled(false);

        // get put extra from
        txtID = getIntent().getStringExtra("id");

        try {
            repoImageStruk = new clsImageStrukRepo(getApplicationContext());
            listData = repoImageStruk.findByIdString(txtID);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        strUrlImgInputStruk = listData.get(0).getTxtPath();
        String imageName = listData.get(0).txtPath.substring(listData.get(0).txtPath.lastIndexOf("/") + 1);
        tvImageName.setText(imageName);

        if (strUrlImgInputStruk != null) {
            Picasso.with(getApplicationContext()).load(strUrlImgInputStruk)
                    .placeholder(R.drawable.loading2)
                    .error(R.drawable.ic_silang)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                    .fit()
                    .into(detailImageInputStruk);
        }

        detailImageInputStruk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (strUrlImgInputStruk != null) {
                    File file = new File(Environment.getExternalStorageDirectory() + File.separator + "ImageStruk" + ".png");
                    file.delete();
                    FileOutputStream fOut = null;
                    try {
                        fOut = new FileOutputStream(file);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    Bitmap mybitmap;
                    mybitmap = ((BitmapDrawable)detailImageInputStruk.getDrawable()).getBitmap();
                    mybitmap.compress(Bitmap.CompressFormat.PNG, 85, fOut);
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

                    Intent intent = new Intent(InputStrukDetailActivity.this, ViewPagerActivity.class);
                    intent.putExtra("gambar struk", "ImageStruk");
                    startActivity(intent);
                }
            }
        });

        String valid = "";
        if (listData.get(0).txtValidate.equals("true")) {
            valid = "Tervalidasi";
            btnStatus.setBackgroundColor(btnStatus.getContext().getResources().getColor(R.color.colorAccent));
        } else if (listData.get(0).txtValidate.equals("false") && listData.get(0).txtActiveOcr.equals("0")) {
            valid = "On Progress";
            btnStatus.setBackgroundColor(btnStatus.getContext().getResources().getColor(R.color.title_background));
        } else if (listData.get(0).txtValidate.equals("false")) {
            valid = "Tidak Valid";
            btnStatus.setBackgroundColor(btnStatus.getContext().getResources().getColor(R.color.red_btn_bg_pressed_color));
        } else {
            valid = "OnProgress";
            btnStatus.setBackgroundColor(btnStatus.getContext().getResources().getColor(R.color.title_background));
        }

        if (listData.get(0).getTxtFileEdit().equals("null")) {
            strHasilOCR = "";
        } else {
            strHasilOCR = listData.get(0).getTxtFileEdit();
        }
        if (listData.get(0).getTxtReason().equals("null")) {
            strReason = "";
        } else {
            strReason = listData.get(0).getTxtReason();
        }

        tvHasilOCR.setText(strHasilOCR);
        tvReason.setText(strReason);
        btnStatus.setText("Status : " + valid);
    }
}
