package kalbefamily.crm.kalbe.kalbefamily;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kalbefamily.crm.kalbe.kalbefamily.Common.clsMediaKontakDetail;
import kalbefamily.crm.kalbe.kalbefamily.Common.clsUserMember;
import kalbefamily.crm.kalbe.kalbefamily.Repo.clsMediaKontakDetailRepo;
import kalbefamily.crm.kalbe.kalbefamily.Repo.clsUserMemberRepo;

/**
 * Created by Rian Andrivani on 9/7/2017.
 */

public class FragmentNewDetailPersonal extends Fragment {
    View v;
    Context context;
    List<clsUserMember> dataMember = null;
    List<clsMediaKontakDetail> dataParent, dataNoTelp, dataChild;
    List<clsMediaKontakDetail> dataChildTelp = null;
    clsMediaKontakDetailRepo repoKontak;
    clsUserMemberRepo repoUserMember = null;
    private ExpandableListAdapter mAdapter;

    List<String> expandableListTitle;
    HashMap<String, List<String>> expandableListDetail;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_new_detail_personal, container, false);
        context = getActivity().getApplicationContext();

        return v;
    }

    private static final String NAME = "NAME";
    private static final String IS_EVEN = "IS_EVEN";

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        List<Map<String, String>> groupData = new ArrayList<Map<String, String>>();
        List<List<Map<String, String>>> childData = new ArrayList<List<Map<String, String>>>();

        repoKontak = new clsMediaKontakDetailRepo(context.getApplicationContext());
        try {
            dataParent = (List<clsMediaKontakDetail>) repoKontak.findDataByParent();
            dataNoTelp = (List<clsMediaKontakDetail>) repoKontak.findbyTelpon();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for (clsMediaKontakDetail data : dataParent) {
            Map<String, String> curGroupMap = new HashMap<String, String>();
            groupData.add(curGroupMap);
            curGroupMap.put(NAME, "Data Kontak " +data.txtDeskripsi);
            curGroupMap.put(IS_EVEN, "This group is even" + "This group is odd");

            // child
            try {
                dataChild = (List<clsMediaKontakDetail>) repoKontak.findDataChild(data.getTxtDeskripsi().toString());
            } catch (SQLException e) {
                e.printStackTrace();
            }

            List<Map<String, String>> children = new ArrayList<Map<String, String>>();
            for (clsMediaKontakDetail dataChildTelpon : dataChild) {
                Map<String, String> curChildMap = new HashMap<String, String>();
                children.add(curChildMap);
                curChildMap.put(NAME, dataChildTelpon.txtDetailMedia);
                curChildMap.put(IS_EVEN, "Status : "+dataChildTelpon.getLttxtStatusAktif().toString()+ ", Priotitas Kontak : " +dataChildTelpon.getTxtPrioritasKontak().toString());
            }
            childData.add(children);
        }
        ExpandableListView lv = (ExpandableListView) getActivity().findViewById(R.id.list);
        // Set up our adapter
        mAdapter = new SimpleExpandableListAdapter(
                getActivity(),
                groupData,
                android.R.layout.simple_expandable_list_item_1,
                new String[] { NAME, IS_EVEN },
                new int[] { android.R.id.text1, android.R.id.text2 },
                childData,
                android.R.layout.simple_expandable_list_item_2,
                new String[] { NAME, IS_EVEN },
                new int[] { android.R.id.text1, android.R.id.text2 }
        );

//        expandableListAdapter = new CustomExpandableListAdapter(getActivity(), expandableListTitle, expandableListDetail);

        lv.setAdapter(mAdapter);
    }

}
