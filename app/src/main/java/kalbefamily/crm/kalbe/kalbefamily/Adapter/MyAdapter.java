package kalbefamily.crm.kalbe.kalbefamily.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.Model.ParentObject;

import java.sql.SQLException;
import java.util.List;

import kalbefamily.crm.kalbe.kalbefamily.Common.clsUserMember;
import kalbefamily.crm.kalbe.kalbefamily.Data.DatabaseHelper;
import kalbefamily.crm.kalbe.kalbefamily.Data.DatabaseManager;
import kalbefamily.crm.kalbe.kalbefamily.HomeMenu;
import kalbefamily.crm.kalbe.kalbefamily.Models.TitleChild;
import kalbefamily.crm.kalbe.kalbefamily.Models.TitleParent;
import kalbefamily.crm.kalbe.kalbefamily.R;
import kalbefamily.crm.kalbe.kalbefamily.Repo.clsUserMemberRepo;
import kalbefamily.crm.kalbe.kalbefamily.ViewHolders.TitleChildViewHolder;
import kalbefamily.crm.kalbe.kalbefamily.ViewHolders.TitleParentViewHolder;

/**
 * Created by reale on 23/11/2016.
 */

public class MyAdapter extends ExpandableRecyclerAdapter<TitleParentViewHolder,TitleChildViewHolder> {
    private Activity activity;
    private Context context2;
    Button button;
    LayoutInflater inflater;
    List<clsUserMember> dataMember = null;
    clsUserMemberRepo repoUserMember = null;

    public MyAdapter(Activity activity, Context context, List<ParentObject> parentItemList) {
        super(context, parentItemList);
        inflater = LayoutInflater.from(context);
        context2 = context;
        this.activity = activity;
    }

    @Override
    public TitleParentViewHolder onCreateParentViewHolder(ViewGroup viewGroup) {
       View view = inflater.inflate(R.layout.list_parent,viewGroup,false);
        return new TitleParentViewHolder(view);
    }

    @Override
    public TitleChildViewHolder onCreateChildViewHolder(ViewGroup viewGroup) {
        View view = inflater.inflate(R.layout.list_child,viewGroup,false);
        return new TitleChildViewHolder(view);
    }

    @Override
    public void onBindParentViewHolder(TitleParentViewHolder titleParentViewHolder, int i, Object o) {
        TitleParent title = (TitleParent)o;
        titleParentViewHolder._textView.setText(title.getTitle());

    }

    @Override
    public void onBindChildViewHolder(TitleChildViewHolder titleChildViewHolder, final int i, Object o) {
        TitleChild title = (TitleChild)o;
        titleChildViewHolder.option1.setText(title.getOption1());
        titleChildViewHolder.option2.setText(title.getOption2());
        final String txtId = title.txtId;

        titleChildViewHolder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    clsUserMemberRepo repoUserMember = new clsUserMemberRepo(context2);
                    dataMember = (List<clsUserMember>) repoUserMember.findByIdString(txtId);
                    DatabaseHelper helper = DatabaseManager.getInstance().getHelper();
                    helper.refreshData();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                clsUserMember dataUser = new clsUserMember();
                dataUser.setTxtKontakId(dataMember.get(0).txtKontakId);
                dataUser.setTxtMemberId(dataMember.get(0).txtMemberId);
                dataUser.setTxtNama(dataMember.get(0).txtNama);
                dataUser.setTxtAlamat(dataMember.get(0).txtAlamat);
                dataUser.setTxtJenisKelamin(dataMember.get(0).txtJenisKelamin);
                dataUser.setTxtEmail(dataMember.get(0).txtEmail);
                dataUser.setTxtNoTelp(dataMember.get(0).txtNoTelp);
                dataUser.setTxtNoKTP(dataMember.get(0).txtNoKTP);
                dataUser.setTxtNamaKeluarga(dataMember.get(0).txtNamaKeluarga);
                dataUser.setTxtNamaPanggilan(dataMember.get(0).txtNamaPanggilan);

                repoUserMember = new clsUserMemberRepo(context2.getApplicationContext());

                int h = 0;
                h = repoUserMember.createOrUpdate(dataUser);
                if(h > -1) {
                    Log.d("Data info", "Data member Berhasil Disimpan");
                }
//                System.exit(0);
                Intent intent = new Intent(context2, HomeMenu.class);
                activity.finish();
                context2.startActivity(intent);
            }
        });
    }
}
