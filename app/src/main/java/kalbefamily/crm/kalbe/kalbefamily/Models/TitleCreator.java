package kalbefamily.crm.kalbe.kalbefamily.Models;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import kalbefamily.crm.kalbe.kalbefamily.Common.clsUserMember;
import kalbefamily.crm.kalbe.kalbefamily.Repo.clsUserMemberRepo;

/**
 * Created by reale on 23/11/2016.
 */

public class TitleCreator extends AppCompatActivity {
    static TitleCreator _titleCreator;
    List<TitleParent> _titleParents;
    List<clsUserMember> dataMember = null;

    public TitleCreator(Context context) {
        _titleParents = new ArrayList<>();
        try {
            clsUserMemberRepo repoUserMember = new clsUserMemberRepo(context);
            dataMember = (List<clsUserMember>) repoUserMember.findAll();
            String kontakId = null;
            kontakId = dataMember.get(0).txtKontakId;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for(clsUserMember data : dataMember)
        {
            TitleParent title = new TitleParent(String.format("Nama : " + data.getTxtNama()));
            _titleParents.add(title);
        }
    }

    public static TitleCreator get(Context context)
    {
        if(_titleCreator == null)
            _titleCreator = new TitleCreator(context);
        return _titleCreator;
    }

    public List<TitleParent> getAll() {
        return _titleParents;
    }
}
