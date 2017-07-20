package kalbefamily.crm.kalbe.kalbefamily;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.bignerdranch.expandablerecyclerview.Model.ParentObject;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import kalbefamily.crm.kalbe.kalbefamily.Adapter.MyAdapter;
import kalbefamily.crm.kalbe.kalbefamily.Common.clsUserMember;
import kalbefamily.crm.kalbe.kalbefamily.Data.DatabaseHelper;
import kalbefamily.crm.kalbe.kalbefamily.Data.DatabaseManager;
import kalbefamily.crm.kalbe.kalbefamily.Models.TitleChild;
import kalbefamily.crm.kalbe.kalbefamily.Models.TitleCreator;
import kalbefamily.crm.kalbe.kalbefamily.Models.TitleParent;
import kalbefamily.crm.kalbe.kalbefamily.Repo.clsUserMemberRepo;

/**
 * Created by Rian Andrivani on 7/18/2017.
 */

public class CardViewActivity extends AppCompatActivity {
    RecyclerView recyclerView;

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(CardViewActivity.this, MemberActivity.class);
        DatabaseHelper helper = DatabaseManager.getInstance().getHelper();
        helper.refreshData();
        finish();
        startActivity(intent);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ((MyAdapter)recyclerView.getAdapter()).onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_view);

        recyclerView = (RecyclerView)findViewById(R.id.myRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        MyAdapter adapter = new MyAdapter(CardViewActivity.this, this,initData());
        adapter.setParentClickableViewAnimationDefaultDuration();
        adapter.setParentAndIconExpandOnClick(true);

        recyclerView.setAdapter(adapter);
    }

    private List<ParentObject> initData() {
        TitleCreator titleCreator = TitleCreator.get(this);
        List<TitleParent> titles = titleCreator.getAll();
        List<ParentObject> parentObject = new ArrayList<>();
        List<clsUserMember> dataMember = null;
        try {
            clsUserMemberRepo repoUserMember = new clsUserMemberRepo(getApplicationContext());
            dataMember = (List<clsUserMember>) repoUserMember.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        int i = 0;
        for(TitleParent title:titles ) {
            List<Object> childList = new ArrayList<>();
            childList.add(new TitleChild("Alamat : " +dataMember.get(i).txtAlamat ,"Jenis Kelamin : " +dataMember.get(i).txtJenisKelamin));
            title.setChildObjectList(childList);
            parentObject.add(title);
            i++;
        }

        return parentObject;
    }
}
