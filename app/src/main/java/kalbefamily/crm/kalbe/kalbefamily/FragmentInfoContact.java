package kalbefamily.crm.kalbe.kalbefamily;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.owater.library.CircleTextView;

import java.sql.SQLException;
import java.util.List;

import kalbefamily.crm.kalbe.kalbefamily.Common.clsUserMember;
import kalbefamily.crm.kalbe.kalbefamily.Repo.clsUserMemberRepo;

/**
 * Created by Rian Andrivani on 7/19/2017.
 */

public class FragmentInfoContact extends Fragment {
    View v;
    TextView tvUsername, tvAlamat, tvMember;
    Context context;
    CircleTextView ctvStatus;
    List<clsUserMember> dataMember = null;
    clsUserMemberRepo repoUserMember = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.activity_kontak_detail,container,false);
        context = getActivity().getApplicationContext();
        tvUsername = (TextView) v.findViewById(R.id.tvUsername);
        tvAlamat = (TextView) v.findViewById(R.id.tvAlamat);
        tvMember = (TextView) v.findViewById(R.id.tvMember);
//        ctvStatus = (CircleTextView) v.findViewById(R.id.status);
//        clsUserLoginData data = new clsUserLoginRepo(context).getDataLogin(context);
//        clsAbsenData dataAbsen = new clsAbsenDataRepo(context).getDataCheckinActive(context);
        try {
            repoUserMember = new clsUserMemberRepo(context);
            dataMember = (List<clsUserMember>) repoUserMember.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        tvUsername.setText(dataMember.get(0).getTxtNama().toString());
        tvAlamat.setText(dataMember.get(0).getTxtAlamat().toString());
        tvMember.setText(dataMember.get(0).getTxtMemberId().toString());
//        if (data != null){
//            tvUsername.setText(data.getTxtName().toString());
//            tvEmail.setText(data.getTxtEmail().toString());
//        }
//        if (dataAbsen!=null){
//            ctvStatus.setText("Checkin");
//        }else{
//            ctvStatus.setText("Checkout");
//        }

        return v;
    }

}
