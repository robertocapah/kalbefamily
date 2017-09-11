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
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kalbefamily.crm.kalbe.kalbefamily.BL.clsActivity;
import kalbefamily.crm.kalbe.kalbefamily.Common.clsMediaKontakDetail;
import kalbefamily.crm.kalbe.kalbefamily.Common.clsUserMember;
import kalbefamily.crm.kalbe.kalbefamily.Data.clsHelper;
import kalbefamily.crm.kalbe.kalbefamily.Repo.clsMediaKontakDetailRepo;
import kalbefamily.crm.kalbe.kalbefamily.Repo.clsUserMemberRepo;

/**
 * Created by Rian Andrivani on 9/7/2017.
 */

public class FragmentNewDetailPersonal extends Fragment {
    View v;
    Context context;
    TextView txtDeskripsi;
    List<clsUserMember> dataMember = null;
    List<clsMediaKontakDetail> dataParent, dataNoTelp, dataChild;
    clsMediaKontakDetailRepo repoKontak;
    clsUserMemberRepo repoUserMember = null;
    private ExpandableListAdapter mAdapter;

    String child, deskripsi, lttxtMediaID;
    boolean validate = true;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_new_detail_personal, container, false);
        context = getActivity().getApplicationContext();
        txtDeskripsi = (TextView) v.findViewById(R.id.txtDeskripsi);

        return v;
    }

    private static final String NAME = "NAME";
    private static final String IS_EVEN = "IS_EVEN";

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        dataUpdate();
    }

    private void dataUpdate() {
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
            curGroupMap.put(NAME, "Data Kontak " + data.txtDeskripsi);
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
                curChildMap.put(IS_EVEN, "Status : " + dataDetail.getLttxtStatusAktif().toString() + ", Priotitas Kontak : " + dataDetail.getTxtPrioritasKontak().toString());
            }
            childData.add(children);
        }
        ExpandableListView lv = (ExpandableListView) getActivity().findViewById(R.id.list);
        lv.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View view, int groupPosition, int childPosition, long l) {
                child = parent.getExpandableListAdapter().getChild(groupPosition, childPosition).toString();
                popupEdit(groupPosition, childPosition);
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
                new String[]{NAME, IS_EVEN},
                new int[]{android.R.id.text1, android.R.id.text2}
        );

        lv.setAdapter(mAdapter);
    }

    private void popupEdit(final int groupPosition, final int childPosition) {
        String CurrentString = child;
        String[] separated = CurrentString.split(",");
        String kontak = separated[0].substring(separated[0].lastIndexOf('=') + 1, separated[0].length()).trim();
        String status = separated[1].substring(separated[1].lastIndexOf(':') + 1, separated[1].length()).trim();
        String prioritas = separated[2].substring(separated[2].lastIndexOf(':') + 1, separated[2].length()).trim();
        String prioritasKontak = prioritas.substring(0, prioritas.lastIndexOf('}')).trim();

        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        final View promptView = layoutInflater.inflate(R.layout.popup_add_edit_data, null);
        final EditText etKontak, etKeterangan, etPrioritas;
        final CheckBox checkBoxStatus = (CheckBox) promptView.findViewById(R.id.checkboxStatus);
//        final String selectedItem = spinnerTelp.getSelectedItem().toString();

        etKontak = (EditText) promptView.findViewById(R.id.etKontak);
        etKeterangan = (EditText) promptView.findViewById(R.id.etKeterangan);
        etPrioritas = (EditText) promptView.findViewById(R.id.etPrioritas);
        deskripsi = dataParent.get(groupPosition).getTxtDeskripsi().toString();
        lttxtMediaID = dataParent.get(groupPosition).getLttxtMediaID().toString();
        if (deskripsi.equals("Telepon") || deskripsi.equals("SMS") || deskripsi.equals("WHATSAPP") || deskripsi.equals("Fax") || deskripsi.equals("MMS")) {
            int maxLength = 14;
            InputFilter[] FilterArray = new InputFilter[1];
            FilterArray[0] = new InputFilter.LengthFilter(maxLength);
            etKontak.setFilters(FilterArray);

            etKontak.setInputType(InputType.TYPE_CLASS_PHONE);
        } else if (deskripsi.equals("BLACKBERRY")) {
            int maxLength = 8;
            InputFilter[] FilterArray = new InputFilter[1];
            FilterArray[0] = new InputFilter.LengthFilter(maxLength);
            etKontak.setFilters(FilterArray);

            etKontak.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
        } else if (deskripsi.equals("Email")) {
            int maxLength = 55;
            InputFilter[] FilterArray = new InputFilter[1];
            FilterArray[0] = new InputFilter.LengthFilter(maxLength);
            etKontak.setFilters(FilterArray);

            etKontak.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        } else {
            int maxLength = 35;
            InputFilter[] FilterArray = new InputFilter[1];
            FilterArray[0] = new InputFilter.LengthFilter(maxLength);
            etKontak.setFilters(FilterArray);
        }

        etKontak.setHint("* wajib diisi");
        etPrioritas.setHint("* wajib diisi");

        etKontak.setText(kontak);

        List<clsMediaKontakDetail> ltdt = null;

        try {
            ltdt = (List<clsMediaKontakDetail>) new clsMediaKontakDetailRepo(getContext()).findWhere(dataParent.get(groupPosition).getTxtDeskripsi());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        etKeterangan.setText(ltdt.get(childPosition).getTxtKeterangan().toString());
        etPrioritas.setText(prioritasKontak);

        if (status.equals("Aktif")) {
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
        final List<clsMediaKontakDetail> finalLtdt = ltdt;

        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                String guiID = finalLtdt.get(childPosition).getTxtGuiId().toString();
                                txtDeskripsi.setText(deskripsi);
                                if (etKontak.getText().toString().equals("")) {
                                    new clsActivity().showCustomToast(context.getApplicationContext(), "Kontak Tidak boleh kosong", false);
                                } else if (deskripsi.equals("Telepon")) {
                                    if (!isValidMobile(etKontak.getText().toString())) {
                                        new clsActivity().showCustomToast(context.getApplicationContext(), "No Telepon tidak Valid", false);
                                        validate = false;
                                    } else if (etPrioritas.getText().toString().equals("")) {
                                        new clsActivity().showCustomToast(context.getApplicationContext(), "Prioritas Tidak boleh kosong", false);
                                    } else {
                                        List<clsMediaKontakDetail> dataKontak = null;
                                        try {
                                            dataKontak = new clsMediaKontakDetailRepo(getContext()).findByIdString(guiID);
                                        } catch (SQLException e) {
                                            e.printStackTrace();
                                        }
                                        clsMediaKontakDetail dt = dataKontak.get(0);

                                        dt.setTxtKontakId(dataMember.get(0).txtKontakId);
                                        dt.setLttxtMediaID(lttxtMediaID);
                                        dt.setTxtDeskripsi(txtDeskripsi.getText().toString());
                                        dt.setTxtPrioritasKontak(etPrioritas.getText().toString());
                                        dt.setTxtDetailMedia(etKontak.getText().toString());
                                        dt.setTxtKeterangan(etKeterangan.getText().toString());

                                        if (checkBoxStatus.isChecked()) {
                                            dt.setLttxtStatusAktif("Aktif");
                                        } else {
                                            dt.setLttxtStatusAktif("Tidak Aktif");
                                        }
                                        repoKontak = new clsMediaKontakDetailRepo(context.getApplicationContext());
                                        repoKontak.createOrUpdate(dt);

                                        new clsActivity().showCustomToast(context.getApplicationContext(), "Kontak berhasil di perbarui", true);
                                        Log.d("Data info", "Kontak berhasil di perbarui");
                                    }
                                } else if (deskripsi.equals("Email")) {
                                    if (!isValidEmail(etKontak.getText().toString())) {
                                        new clsActivity().showCustomToast(context.getApplicationContext(), "Email tidak valid", false);
                                        validate = false;
                                    } else if (etPrioritas.getText().toString().equals("")) {
                                        new clsActivity().showCustomToast(context.getApplicationContext(), "Prioritas Tidak boleh kosong", false);
                                    } else {
                                        List<clsMediaKontakDetail> dataKontak = null;
                                        try {
                                            dataKontak = new clsMediaKontakDetailRepo(getContext()).findByIdString(guiID);
                                        } catch (SQLException e) {
                                            e.printStackTrace();
                                        }
                                        clsMediaKontakDetail dt = dataKontak.get(0);

                                        dt.setTxtKontakId(dataMember.get(0).txtKontakId);
                                        dt.setLttxtMediaID(lttxtMediaID);
                                        dt.setTxtDeskripsi(txtDeskripsi.getText().toString());
                                        dt.setTxtPrioritasKontak(etPrioritas.getText().toString());
                                        dt.setTxtDetailMedia(etKontak.getText().toString());
                                        dt.setTxtKeterangan(etKeterangan.getText().toString());

                                        if (checkBoxStatus.isChecked()) {
                                            dt.setLttxtStatusAktif("Aktif");
                                        } else {
                                            dt.setLttxtStatusAktif("Tidak Aktif");
                                        }
                                        repoKontak = new clsMediaKontakDetailRepo(context.getApplicationContext());
                                        repoKontak.createOrUpdate(dt);

                                        new clsActivity().showCustomToast(context.getApplicationContext(), "Kontak berhasil di perbarui", true);
                                        Log.d("Data info", "Kontak berhasil di perbarui");
                                    }
                                } else if (etPrioritas.getText().toString().equals("")) {
                                    new clsActivity().showCustomToast(context.getApplicationContext(), "Prioritas Tidak boleh kosong", false);
                                } else {
                                    List<clsMediaKontakDetail> dataKontak = null;
                                    try {
                                        dataKontak = new clsMediaKontakDetailRepo(getContext()).findByIdString(guiID);
                                    } catch (SQLException e) {
                                        e.printStackTrace();
                                    }
                                    clsMediaKontakDetail dt = dataKontak.get(0);

                                    dt.setTxtKontakId(dataMember.get(0).txtKontakId);
                                    dt.setLttxtMediaID(lttxtMediaID);
                                    dt.setTxtDeskripsi(txtDeskripsi.getText().toString());
                                    dt.setTxtPrioritasKontak(etPrioritas.getText().toString());
                                    dt.setTxtDetailMedia(etKontak.getText().toString());
                                    dt.setTxtKeterangan(etKeterangan.getText().toString());

                                    if (checkBoxStatus.isChecked()) {
                                        dt.setLttxtStatusAktif("Aktif");
                                    } else {
                                        dt.setLttxtStatusAktif("Tidak Aktif");
                                    }
                                    repoKontak = new clsMediaKontakDetailRepo(context.getApplicationContext());
                                    repoKontak.createOrUpdate(dt);

                                    new clsActivity().showCustomToast(context.getApplicationContext(), "Kontak berhasil di perbarui", true);
                                    Log.d("Data info", "Kontak berhasil di perbarui");
                                }

                                dialog.dismiss();
                                dataUpdate();
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

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    private boolean isValidMobile(String phone) {
        return android.util.Patterns.PHONE.matcher(phone).matches();
    }

}