package kalbefamily.crm.kalbe.kalbefamily;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import kalbefamily.crm.kalbe.kalbefamily.BL.clsActivity;
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

        final List<Map<String, String>> groupData = new ArrayList<Map<String, String>>();
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
            for (clsMediaKontakDetail dataDetail : dataChild) {
                Map<String, String> curChildMap = new HashMap<String, String>();
                children.add(curChildMap);
                curChildMap.put(NAME, dataDetail.txtDetailMedia);
                curChildMap.put(IS_EVEN, "Status : "+dataDetail.getLttxtStatusAktif().toString()+ ", Priotitas Kontak : " +dataDetail.getTxtPrioritasKontak().toString());
            }
            childData.add(children);
        }
        ExpandableListView lv = (ExpandableListView) getActivity().findViewById(R.id.list);
        lv.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View view, int groupPosition, int childPosition, long l) {
                String childData = parent.getExpandableListAdapter().getChild(groupPosition, childPosition).toString();

                String CurrentString = childData;
                String[] separated = CurrentString.split(",");
                String kontak = separated[0].substring(separated[0].lastIndexOf('=')+1, separated[0].length()).trim();
                String status = separated[1].substring(separated[1].lastIndexOf(':')+1, separated[1].length()).trim();
                String prioritas = separated[2].substring(separated[2].lastIndexOf(':')+1, separated[2].length()).trim();
                String prioritasKontak = prioritas.substring(0, prioritas.lastIndexOf('}')).trim();

//                Toast.makeText(getActivity(), parent.getExpandableListAdapter().getChild(groupPosition, childPosition).toString(), Toast.LENGTH_LONG).show();
                LayoutInflater layoutInflater = LayoutInflater.from(getContext());
                final View promptView = layoutInflater.inflate(R.layout.popup_add_edit_data, null);
                final EditText etKontak, etKeterangan, etPrioritas;
                final CheckBox checkBoxStatus = (CheckBox) promptView.findViewById(R.id.checkboxStatus);
//        final String selectedItem = spinnerTelp.getSelectedItem().toString();

                etKontak =(EditText) promptView.findViewById(R.id.etKontak);
                etKeterangan = (EditText) promptView.findViewById(R.id.etKeterangan);
                etPrioritas = (EditText) promptView.findViewById(R.id.etPrioritas);

//        int maxLength = 14;
//        InputFilter[] FilterArray = new InputFilter[1];
//        FilterArray[0] = new InputFilter.LengthFilter(maxLength);
//        etKontak.setFilters(FilterArray);
//
//        etKontak.setInputType(InputType.TYPE_CLASS_PHONE);
        etKontak.setText(kontak);
//        etKeterangan.setText(hashMapTelp.get(selectedItem));
        etPrioritas.setText(prioritasKontak);
//
        if (status.equals("Aktif")){
            checkBoxStatus.setChecked(true);
        } else {
            checkBoxStatus.setChecked(false);
            checkBoxStatus.setText("Tidak AKtif");
        }

                checkBoxStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (b == true) {
                            checkBoxStatus.setText("Aktif");
                        } else {
                            checkBoxStatus.setText("Tidak Aktif");
                        }
                    }
                });

                try {
                    repoUserMember = new clsUserMemberRepo(context);
                    dataMember = (List<clsUserMember>) repoUserMember.findAll();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                alertDialogBuilder.setView(promptView);
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
//                                txtDeskripsi.setText("Telepon");
//                                lttxtMediaID.setText("0001");
//                                if (etKontak.getText().toString().equals("")){
//                                    new clsActivity().showCustomToast(context.getApplicationContext(), "No Telepon Tidak boleh kosong", false);
//                                } else if (!isValidMobile(etKontak.getText().toString())) {
//                                    new clsActivity().showCustomToast(context.getApplicationContext(), "No Telepon tidak Valid", false);
//                                    validate = false;
//                                } else if (etPrioritas.getText().toString().equals("")) {
//                                    new clsActivity().showCustomToast(context.getApplicationContext(), "Prioritas Tidak boleh kosong", false);
//                                } else {
//                                    clsMediaKontakDetail dataKontak = new clsMediaKontakDetail();
//                                    dataKontak.setTxtGuiId(hashMapIdTelp.get(selectedItem).toString());
//                                    dataKontak.setTxtKontakId(dataMember.get(0).txtKontakId);
//                                    dataKontak.setLttxtMediaID(lttxtMediaID.getText().toString());
//                                    dataKontak.setTxtDeskripsi(txtDeskripsi.getText().toString());
//                                    dataKontak.setTxtPrioritasKontak(etPrioritas.getText().toString());
//                                    dataKontak.setTxtDetailMedia(etKontak.getText().toString());
//                                    dataKontak.setTxtKeterangan(etKeterangan.getText().toString());
//
//                                    if (checkBoxStatus.isChecked()) {
//                                        dataKontak.setLttxtStatusAktif("Aktif");
//                                    } else {
//                                        dataKontak.setLttxtStatusAktif("Tidak Aktif");
//                                    }
//                                    repoKontak = new clsMediaKontakDetailRepo(context.getApplicationContext());
//                                    repoKontak.createOrUpdate(dataKontak);
//
//                                    new clsActivity().showCustomToast(context.getApplicationContext(), "Kontak berhasil di perbarui", true);
//                                    Log.d("Data info", "Kontak berhasil di perbarui");
//                                }
//
//                                dialog.dismiss();
//                                dataListview();
                                    }
                                })
                        .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                final AlertDialog alertD = alertDialogBuilder.create();
                alertD.show();
                return false;
            }
        });

        String groupFrom[] = {NAME};
        int groupTo[] = {R.id.heading};

        // Set up our adapter
        mAdapter = new SimpleExpandableListAdapter(getActivity(), groupData,
                R.layout.group_items,
                groupFrom, groupTo,
                childData,
                android.R.layout.simple_expandable_list_item_2,
                new String[] { NAME, IS_EVEN },
                new int[] { android.R.id.text1, android.R.id.text2 }
        );

//        mAdapter = new CustomExpandableListAdapter(getActivity(), expandableListTitle, expandableListDetail);

        lv.setAdapter(mAdapter);
    }

    private void popupEdit() {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        final View promptView = layoutInflater.inflate(R.layout.popup_add_edit_data, null);
        final EditText etKontak, etKeterangan, etPrioritas;
        final CheckBox checkBoxStatus = (CheckBox) promptView.findViewById(R.id.checkboxStatus);
//        final String selectedItem = spinnerTelp.getSelectedItem().toString();

        etKontak =(EditText) promptView.findViewById(R.id.etKontak);
        etKeterangan = (EditText) promptView.findViewById(R.id.etKeterangan);
        etPrioritas = (EditText) promptView.findViewById(R.id.etPrioritas);

//        int maxLength = 14;
//        InputFilter[] FilterArray = new InputFilter[1];
//        FilterArray[0] = new InputFilter.LengthFilter(maxLength);
//        etKontak.setFilters(FilterArray);
//
//        etKontak.setInputType(InputType.TYPE_CLASS_PHONE);
//        etKontak.setText(tvNoTelp.getText().toString());
//        etKeterangan.setText(hashMapTelp.get(selectedItem));
//        etPrioritas.setText(hashMapTelpPrioritas.get(selectedItem));
//
//        if (hashMapTelpAktif.get(selectedItem).toString().equals("Aktif")){
//            checkBoxStatus.setChecked(true);
//        } else {
//            checkBoxStatus.setChecked(false);
//            checkBoxStatus.setText("Tidak AKtif");
//        }

        checkBoxStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b == true) {
                    checkBoxStatus.setText("Aktif");
                } else {
                    checkBoxStatus.setText("Tidak Aktif");
                }
            }
        });

        try {
            repoUserMember = new clsUserMemberRepo(context);
            dataMember = (List<clsUserMember>) repoUserMember.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this.getContext());
        alertDialogBuilder.setView(promptView);
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
//                                txtDeskripsi.setText("Telepon");
//                                lttxtMediaID.setText("0001");
//                                if (etKontak.getText().toString().equals("")){
//                                    new clsActivity().showCustomToast(context.getApplicationContext(), "No Telepon Tidak boleh kosong", false);
//                                } else if (!isValidMobile(etKontak.getText().toString())) {
//                                    new clsActivity().showCustomToast(context.getApplicationContext(), "No Telepon tidak Valid", false);
//                                    validate = false;
//                                } else if (etPrioritas.getText().toString().equals("")) {
//                                    new clsActivity().showCustomToast(context.getApplicationContext(), "Prioritas Tidak boleh kosong", false);
//                                } else {
//                                    clsMediaKontakDetail dataKontak = new clsMediaKontakDetail();
//                                    dataKontak.setTxtGuiId(hashMapIdTelp.get(selectedItem).toString());
//                                    dataKontak.setTxtKontakId(dataMember.get(0).txtKontakId);
//                                    dataKontak.setLttxtMediaID(lttxtMediaID.getText().toString());
//                                    dataKontak.setTxtDeskripsi(txtDeskripsi.getText().toString());
//                                    dataKontak.setTxtPrioritasKontak(etPrioritas.getText().toString());
//                                    dataKontak.setTxtDetailMedia(etKontak.getText().toString());
//                                    dataKontak.setTxtKeterangan(etKeterangan.getText().toString());
//
//                                    if (checkBoxStatus.isChecked()) {
//                                        dataKontak.setLttxtStatusAktif("Aktif");
//                                    } else {
//                                        dataKontak.setLttxtStatusAktif("Tidak Aktif");
//                                    }
//                                    repoKontak = new clsMediaKontakDetailRepo(context.getApplicationContext());
//                                    repoKontak.createOrUpdate(dataKontak);
//
//                                    new clsActivity().showCustomToast(context.getApplicationContext(), "Kontak berhasil di perbarui", true);
//                                    Log.d("Data info", "Kontak berhasil di perbarui");
//                                }
//
//                                dialog.dismiss();
//                                dataListview();
                            }
                        })
                .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alertD = alertDialogBuilder.create();
        alertD.show();
    }

    private void prepareListData() {
        expandableListTitle = new ArrayList<String>();
        expandableListDetail = new HashMap<String, List<String>>();
        List<List<Map<String, String>>> childData = new ArrayList<List<Map<String, String>>>();

        repoKontak = new clsMediaKontakDetailRepo(context.getApplicationContext());
        try {
            dataParent = (List<clsMediaKontakDetail>) repoKontak.findDataByParent();
            dataNoTelp = (List<clsMediaKontakDetail>) repoKontak.findbyTelpon();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for (clsMediaKontakDetail dataHeader : dataParent) {
            expandableListTitle.add(dataHeader.getTxtDeskripsi().toString());

            // child
            try {
                dataChild = (List<clsMediaKontakDetail>) repoKontak.findDataChild(dataHeader.getTxtDeskripsi().toString());
            } catch (SQLException e) {
                e.printStackTrace();
            }

//            for (clsMediaKontakDetail dataDetail : dataChild) {
//                expandableListDetail.add("Status : "+dataDetail.getLttxtStatusAktif().toString()+ ", Priotitas Kontak : " +dataDetail.getTxtPrioritasKontak().toString());
//            }

        }

//        // Adding child data
//        expandableListTitle.add("Top 250");
//        expandableListTitle.add("Now Showing");
//        expandableListTitle.add("Coming Soon..");
//
//        // Adding child data
//        List<String> top250 = new ArrayList<String>();
//        top250.add("The Shawshank Redemption");
//        top250.add("The Godfather");
//        top250.add("The Godfather: Part II");
//        top250.add("Pulp Fiction");
//        top250.add("The Good, the Bad and the Ugly");
//        top250.add("The Dark Knight");
//        top250.add("12 Angry Men");
//
//        List<String> nowShowing = new ArrayList<String>();
//        nowShowing.add("The Conjuring");
//        nowShowing.add("Despicable Me 2");
//        nowShowing.add("Turbo");
//        nowShowing.add("Grown Ups 2");
//        nowShowing.add("Red 2");
//        nowShowing.add("The Wolverine");
//
//        List<String> comingSoon = new ArrayList<String>();
//        comingSoon.add("2 Guns");
//        comingSoon.add("The Smurfs 2");
//        comingSoon.add("The Spectacular Now");
//        comingSoon.add("The Canyons");
//        comingSoon.add("Europa Report");
//
//        expandableListDetail.put(expandableListTitle.get(0), top250); // Header, Child data
//        expandableListDetail.put(expandableListTitle.get(1), nowShowing);
//        expandableListDetail.put(expandableListTitle.get(2), comingSoon);
    }

}
