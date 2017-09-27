package kalbefamily.crm.kalbe.kalbefamily;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsoluteLayout;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import kalbefamily.crm.kalbe.kalbefamily.Common.clsUserMember;
import kalbefamily.crm.kalbe.kalbefamily.R;
import kalbefamily.crm.kalbe.kalbefamily.Repo.clsUserMemberRepo;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by Rian Andrivani on 7/31/2017.
 */

public class RewardCardActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    TextView etNumber, etNama, etTglBerlaku;
    Spinner spinner;
    AbsoluteLayout layoutDepan, layoutBelakang;
    ImageView tampakDepan, tampakBelakang;
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
//        spinNama = (Spinner) findViewById(R.id.spnNama);

        clsUserMemberRepo repo = new clsUserMemberRepo(getApplicationContext());
        try {
            dataMember = (List<clsUserMember>) repo.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

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

        String sub, sub2, sub3;
        String member1 = dataMember.get(0).getTxtMemberId();
        sub = member1.substring(0, member1.length() - 8);
        sub2 = member1.substring(4, member1.length() - 4);
        sub3 = member1.substring(8, member1.length());
        etNumber.setText(sub +" "+ sub2 +" "+ sub3);
        etNama.setText(dataMember.get(0).getTxtNama().toUpperCase());
        etTglBerlaku.setText(dataMember.get(0).getTxtTglBerlaku());

        photoViewAttacher = new PhotoViewAttacher(tampakBelakang);

        photoViewAttacher.update();

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
}
