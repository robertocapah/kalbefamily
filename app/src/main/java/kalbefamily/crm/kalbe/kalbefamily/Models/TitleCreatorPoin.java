package kalbefamily.crm.kalbe.kalbefamily.Models;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import kalbefamily.crm.kalbe.kalbefamily.Common.clsAvailablePoin;
import kalbefamily.crm.kalbe.kalbefamily.Common.clsUserMember;
import kalbefamily.crm.kalbe.kalbefamily.Repo.clsAvailablePoinRepo;
import kalbefamily.crm.kalbe.kalbefamily.Repo.clsUserMemberRepo;

/**
 * Created by reale on 23/11/2016.
 */

public class TitleCreatorPoin extends AppCompatActivity {
    static TitleCreatorPoin _titleCreator;
    List<TitleParent> _titleParents;
    List<clsUserMember> dataMember = null;
    List<clsAvailablePoin> dataPoin = null;

    public TitleCreatorPoin(Context context) {
        _titleParents = new ArrayList<>();
        try {
            clsUserMemberRepo repoUserMember = new clsUserMemberRepo(context);
            dataMember = (List<clsUserMember>) repoUserMember.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            clsAvailablePoinRepo repoAvailabePoin = new clsAvailablePoinRepo(context);
            dataPoin = (List<clsAvailablePoin>) repoAvailabePoin.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for(clsAvailablePoin data : dataPoin)
        {
            TitleParent title = new TitleParent(String.format("Nama : " + data.getTxtPeriodePoint()));
            _titleParents.add(title);
        }
    }

    public static TitleCreatorPoin get(Context context)
    {
        if(_titleCreator == null)
            _titleCreator = new TitleCreatorPoin(context);
        return _titleCreator;
    }

    public List<TitleParent> getAll() {
        return _titleParents;
    }
}
