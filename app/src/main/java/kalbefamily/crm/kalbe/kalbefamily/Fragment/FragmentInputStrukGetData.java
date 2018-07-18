package kalbefamily.crm.kalbe.kalbefamily.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListAdapter;
import android.widget.ListView;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import kalbefamily.crm.kalbe.kalbefamily.Adapter.ListAdapterNew;
import kalbefamily.crm.kalbe.kalbefamily.Common.clsImageStruk;
import kalbefamily.crm.kalbe.kalbefamily.Common.clsUserMember;
import kalbefamily.crm.kalbe.kalbefamily.InputStrukDetailActivity;
import kalbefamily.crm.kalbe.kalbefamily.R;
import kalbefamily.crm.kalbe.kalbefamily.Repo.clsImageStrukRepo;

/**
 * Created by User on 2/26/2018.
 */

public class FragmentInputStrukGetData extends Fragment {
    View v;
    Context context;
    List<clsUserMember> dataMember = null;
    private ExpandableListAdapter mAdapter;
    private clsImageStrukRepo repoImageStruk;
    List<clsImageStruk> listData;
    List<clsImageStruk> listDataChild;

    List<String> listGuiId = new ArrayList<>();
    List<String> status = new ArrayList<>();
    List<Integer> icon = new ArrayList<>();
    List<String> strDate = new ArrayList<>();
    List<String> time = new ArrayList<>();

    String[] versionNumber = {"1.0", "1.1"};

    ListView lView;

    ListAdapterNew lAdapter;
    FloatingActionButton fabAddInputStruk;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_input_struk_get_data, container, false);
        context = getActivity().getApplicationContext();
        lView = (ListView) v.findViewById(R.id.androidList);
        fabAddInputStruk = (FloatingActionButton) v.findViewById(R.id.fabAddInputStruk);

        fabAddInputStruk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                FragmentInputStruk fragmentInputStruk = new FragmentInputStruk();
                FragmentTransaction fragmentTransactionInputStruk = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransactionInputStruk.replace(R.id.frame, fragmentInputStruk);
                fragmentTransactionInputStruk.commit();
            }
        });

        try {
            repoImageStruk = new clsImageStrukRepo(context);
            listData = (List<clsImageStruk>) repoImageStruk.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String valid = "";
        int img;
        for (clsImageStruk data : listData) {
            String strDate1 = data.getTxtDate();
            SimpleDateFormat format = new SimpleDateFormat("dd-MMMM-yyyy HH:mm");
            Date date = null;
            try {
                date = format.parse(strDate1);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MMMM-yyyy");
            SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm");
            String txtDate = formatter.format(date);
            String txtTime = formatTime.format(date);

            if (data.getTxtValidate().equals("true")) {
                valid = "Tervalidasi";
                img = R.drawable.ic_ceklis;
            } else if (data.getTxtValidate().equals("false") && data.getTxtActiveOcr().equals("0")) {
                valid = "On Progress";
                img = R.drawable.ic_onprogress;
            } else if (data.getTxtValidate().equals("false")) {
                valid = "Tidak Valid";
                img = R.drawable.ic_silang;
            } else {
                valid = "OnProgress";
                img = R.drawable.ic_onprogress;
            }
            status.add("Status : " + valid);
            icon.add(img);
            strDate.add(txtDate);
            time.add(txtTime);
            listGuiId.add(data.getTxtGuiId());
        }

        lAdapter = new ListAdapterNew(getActivity(), status, strDate, icon, time);

        lView.setAdapter(lAdapter);

        lView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(getActivity(), status.get(i), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, InputStrukDetailActivity.class);
                intent.putExtra("id", listGuiId.get(i));
                startActivity(intent);
//                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                getActivity().overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
            }
        });

        return v;
    }

//    private static final String NAME = "NAME";
//    private static final String IS_EVEN = "IS_EVEN";
//
//    @Override
//    public void onActivityCreated(Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//
//        List<Map<String, String>> groupData = new ArrayList<Map<String, String>>();
//        List<List<Map<String, String>>> childData = new ArrayList<List<Map<String, String>>>();
//
//        try {
//            repoImageStruk = new clsImageStrukRepo(context);
//            listData = (List<clsImageStruk>) repoImageStruk.findAll();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        // parent
//        int i = 1;
//        for (clsImageStruk data : listData) {
//            Map<String, String> curGroupMap = new HashMap<String, String>();
//            groupData.add(curGroupMap);
//            curGroupMap.put(NAME, " Image " + String.valueOf(i));
//            curGroupMap.put(IS_EVEN, "This group is even" + "This group is odd");
//
//            // child
//            try {
//                listDataChild = (List<clsImageStruk>) repoImageStruk.findDataChild(data.getTxtGuiId());
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//
//            List<Map<String, String>> children = new ArrayList<Map<String, String>>();
//            String valid = "";
//            for (clsImageStruk dataChild : listDataChild) {
//                if (dataChild.getTxtValidate().equals("true")) {
//                    valid = "Tervalidasi";
//                }else if (data.getTxtValidate().equals("false")) {
//                    valid = "Tidak Valid";
//                } else {
//                    valid = "OnProgress";
//                }
//                Map<String, String> curChildMap = new HashMap<String, String>();
//                children.add(curChildMap);
//                curChildMap.put(NAME, "Status Validasi");
//                curChildMap.put(IS_EVEN, valid);
//            }
//            childData.add(children);
//
//            i++;
//        }
//        ExpandableListView lv = (ExpandableListView) getActivity().findViewById(R.id.listInputStruk);
//        // Set up our adapter
//        String groupFrom[] = {NAME};
//        int groupTo[] = {R.id.heading};
//
//        mAdapter = new SimpleExpandableListAdapter(
//                getActivity(),
//                groupData,
//                R.layout.group_items,
//                groupFrom, groupTo,
//                childData,
//                android.R.layout.simple_expandable_list_item_2,
//                new String[] { NAME, IS_EVEN },
//                new int[] { android.R.id.text1, android.R.id.text2 }
//        );
//        lv.setAdapter(mAdapter);
//    }
}
