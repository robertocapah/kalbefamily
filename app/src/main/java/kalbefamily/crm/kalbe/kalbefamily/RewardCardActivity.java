package kalbefamily.crm.kalbe.kalbefamily;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import kalbefamily.crm.kalbe.kalbefamily.Common.clsUserMember;
import kalbefamily.crm.kalbe.kalbefamily.R;
import kalbefamily.crm.kalbe.kalbefamily.Repo.clsUserMemberRepo;

/**
 * Created by Rian Andrivani on 7/31/2017.
 */

public class RewardCardActivity extends AppCompatActivity {
    TextView etNumber, etNama, etTglBerlaku;
    Spinner spinNama;

    List<clsUserMember> dataMember = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_reward);

        etNumber =(TextView) findViewById(R.id.textView7);
        etNama = (TextView) findViewById(R.id.textView_nama);
        etTglBerlaku = (TextView) findViewById(R.id.textView_berlaku);
        spinNama = (Spinner) findViewById(R.id.spnNama);

        clsUserMemberRepo repo = new clsUserMemberRepo(getApplicationContext());
        try {
            dataMember = (List<clsUserMember>) repo.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // spinner name member
        List<String> dataMemberName = new ArrayList<>();
        if (dataMember.size() > 0) {
            for (clsUserMember data : dataMember) {
                dataMemberName.add(data.getTxtNama());
            }
        }

        ArrayAdapter<String> adapterMemberName = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, dataMemberName);
        spinNama.setAdapter(adapterMemberName);

        String sub, sub2, sub3;
        String member1 = dataMember.get(0).getTxtMemberId();
        sub = member1.substring(0, member1.length() - 8);
        sub2 = member1.substring(4, member1.length() - 4);
        sub3 = member1.substring(8, member1.length());
        etNumber.setText(sub +" "+ sub2 +" "+ sub3);
        etNama.setText(dataMember.get(0).getTxtNama().toUpperCase());
        etTglBerlaku.setText(dataMember.get(0).getTxtTglBerlaku());

    }
}
