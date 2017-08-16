package kalbefamily.crm.kalbe.kalbefamily;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;

import com.bignerdranch.expandablerecyclerview.Model.ParentObject;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import kalbefamily.crm.kalbe.kalbefamily.Adapter.MyAdapter;
import kalbefamily.crm.kalbe.kalbefamily.Adapter.MyAdapterPoin;
import kalbefamily.crm.kalbe.kalbefamily.Common.clsAvailablePoin;
import kalbefamily.crm.kalbe.kalbefamily.Common.clsUserMember;
import kalbefamily.crm.kalbe.kalbefamily.Data.DatabaseHelper;
import kalbefamily.crm.kalbe.kalbefamily.Data.DatabaseManager;
import kalbefamily.crm.kalbe.kalbefamily.Models.TitleChild;
import kalbefamily.crm.kalbe.kalbefamily.Models.TitleChildPoin;
import kalbefamily.crm.kalbe.kalbefamily.Models.TitleCreator;
import kalbefamily.crm.kalbe.kalbefamily.Models.TitleCreatorPoin;
import kalbefamily.crm.kalbe.kalbefamily.Models.TitleParent;
import kalbefamily.crm.kalbe.kalbefamily.Repo.clsAvailablePoinRepo;
import kalbefamily.crm.kalbe.kalbefamily.Repo.clsUserMemberRepo;

/**
 * Created by Rian Andrivani on 7/18/2017.
 */

public class ExpandablePoinActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    private Toolbar toolbar;

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ExpandablePoinActivity.this, HomeMenu.class);
        finish();
        startActivity(intent);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ((MyAdapterPoin)recyclerView.getAdapter()).onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.primary_color_theme));
        }
        setContentView(R.layout.activity_card_view);

        toolbar = (Toolbar) findViewById(R.id.toolbarCardView);
        toolbar.setTitle("Available Point Customer");
        setSupportActionBar(toolbar);

        // set enable toolbar button back
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // toolbar button for move to before screen
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), HomeMenu.class);
                finish();
                startActivity(intent);
            }
        });

        recyclerView = (RecyclerView)findViewById(R.id.myRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        MyAdapterPoin adapter = new MyAdapterPoin(ExpandablePoinActivity.this, this,initData());
        adapter.setParentClickableViewAnimationDefaultDuration();
        adapter.setParentAndIconExpandOnClick(true);

        recyclerView.setAdapter(adapter);
    }

    private List<ParentObject> initData() {
        TitleCreatorPoin titleCreator = TitleCreatorPoin.get(this);
        List<TitleParent> titles = titleCreator.getAll();
        List<ParentObject> parentObject = new ArrayList<>();
        List<clsUserMember> dataMember = null;
        List<clsAvailablePoin> dataPoin = null;
        try {
            clsUserMemberRepo repoUserMember = new clsUserMemberRepo(getApplicationContext());
            dataMember = (List<clsUserMember>) repoUserMember.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            clsAvailablePoinRepo repoAvailablePoin = new clsAvailablePoinRepo(getApplicationContext());
            dataPoin = (List<clsAvailablePoin>) repoAvailablePoin.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        int i = 0;
        for(TitleParent title:titles ) {
            List<Object> childList = new ArrayList<>();
            childList.add(new TitleChildPoin(dataPoin.get(i).txtPoint.toString() ,"Deskripsi : " +dataPoin.get(i).txtDescription));
            title.setChildObjectList(childList);
            title.setTitle(dataPoin.get(i).txtPeriodePoint.toString());
            parentObject.add(title);
            i++;
        }

        return parentObject;
    }
}
