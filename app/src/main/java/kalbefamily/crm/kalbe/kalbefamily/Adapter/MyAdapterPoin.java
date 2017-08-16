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
import kalbefamily.crm.kalbe.kalbefamily.Models.TitleChildPoin;
import kalbefamily.crm.kalbe.kalbefamily.Models.TitleParent;
import kalbefamily.crm.kalbe.kalbefamily.R;
import kalbefamily.crm.kalbe.kalbefamily.Repo.clsUserMemberRepo;
import kalbefamily.crm.kalbe.kalbefamily.ViewHolders.TitleChildViewHolder;
import kalbefamily.crm.kalbe.kalbefamily.ViewHolders.TitleChildViewHolderPoin;
import kalbefamily.crm.kalbe.kalbefamily.ViewHolders.TitleParentViewHolder;

/**
 * Created by reale on 23/11/2016.
 */

public class MyAdapterPoin extends ExpandableRecyclerAdapter<TitleParentViewHolder,TitleChildViewHolderPoin> {
    private Activity activity;
    private Context context2;
    Button button;
    LayoutInflater inflater;
    List<clsUserMember> dataMember = null;
    clsUserMemberRepo repoUserMember = null;

    public MyAdapterPoin(Activity activity, Context context, List<ParentObject> parentItemList) {
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
    public TitleChildViewHolderPoin onCreateChildViewHolder(ViewGroup viewGroup) {
        View view = inflater.inflate(R.layout.list_child,viewGroup,false);
        return new TitleChildViewHolderPoin(view);
    }

    @Override
    public void onBindParentViewHolder(TitleParentViewHolder titleParentViewHolder, int i, Object o) {
        TitleParent title = (TitleParent)o;
        titleParentViewHolder._textView.setText(title.getTitle());

    }

    @Override
    public void onBindChildViewHolder(TitleChildViewHolderPoin titleChildViewHolder, final int i, Object o) {
        TitleChildPoin title = (TitleChildPoin) o;
        titleChildViewHolder.option1.setText(title.getOption1());
        titleChildViewHolder.option2.setText(title.getOption2());
    }
}
