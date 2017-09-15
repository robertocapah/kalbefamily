package kalbefamily.crm.kalbe.kalbefamily;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import kalbefamily.crm.kalbe.kalbefamily.BL.clsActivity;
import kalbefamily.crm.kalbe.kalbefamily.Common.clsMediaKontakDetail;
import kalbefamily.crm.kalbe.kalbefamily.Common.clsSendData;
import kalbefamily.crm.kalbe.kalbefamily.Common.clsUserMember;
import kalbefamily.crm.kalbe.kalbefamily.Data.VolleyResponseListener;
import kalbefamily.crm.kalbe.kalbefamily.Data.VolleyUtils;
import kalbefamily.crm.kalbe.kalbefamily.Data.clsHardCode;
import kalbefamily.crm.kalbe.kalbefamily.Data.clsHelper;
import kalbefamily.crm.kalbe.kalbefamily.Repo.clsMediaKontakDetailRepo;
import kalbefamily.crm.kalbe.kalbefamily.Repo.clsUserMemberRepo;

import static com.android.volley.VolleyLog.TAG;

/**
 * Created by Rian Andrivani on 9/7/2017.
 */

public class FragmentNewDetailPersonal extends Fragment implements AdapterView.OnItemSelectedListener {
    View v;
    Context context;
    TextView txtDeskripsi;
    Button btnSimpan, btnTambah;
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
        btnSimpan = (Button) v.findViewById(R.id.btnSimpan);
        btnTambah = (Button) v.findViewById(R.id.btnTambah);

        btnTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupTambah();
            }
        });

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                alertDialog.setTitle("Konfirmasi");
                alertDialog.setMessage("Apakah Anda yakin?");
                alertDialog.setPositiveButton("SIMPAN", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        sendData();
                    }
                });
                alertDialog.setNegativeButton("BATAL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alertDialog.show();
            }
        });

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
        final EditText etKontak, etKeterangan, etPrioritas, etExtension;
        final CheckBox checkBoxStatus = (CheckBox) promptView.findViewById(R.id.checkboxStatus);
        final Spinner spinnerKategoriMedia;
//        final String selectedItem = spinnerTelp.getSelectedItem().toString();

        etKontak = (EditText) promptView.findViewById(R.id.etKontak);
        etKeterangan = (EditText) promptView.findViewById(R.id.etKeterangan);
        etPrioritas = (EditText) promptView.findViewById(R.id.etPrioritas);
        etExtension = (EditText) promptView.findViewById(R.id.etExtension);
        spinnerKategoriMedia = (Spinner) promptView.findViewById(R.id.spinnerKategoriMedia);

        // spinner listener
        spinnerKategoriMedia.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List<String> kategoriMedia = new ArrayList<String>();
        kategoriMedia.add("");
        kategoriMedia.add("Home");
        kategoriMedia.add("Office");
        kategoriMedia.add("NOMOR BAPAK");
        kategoriMedia.add("NOMOR IBU");
        kategoriMedia.add("NOMOR ORANG TUA");
        kategoriMedia.add("NOMOR KELUARGA");
        kategoriMedia.add("EMAIL PIC");
        kategoriMedia.add("NOMOR PIC");
        kategoriMedia.add("NOMOR TOKO");
        kategoriMedia.add("EMAIL VERIFICATION");
        kategoriMedia.add("PATH");
        kategoriMedia.add("fACEBOOK");
        kategoriMedia.add("TWITTER");
        kategoriMedia.add("EMAIL PELANGGAN");
        kategoriMedia.add("ID LINE");
        kategoriMedia.add("BLACKBERRY");
        kategoriMedia.add("BLOG");
        kategoriMedia.add("INSTAGRAM");
        kategoriMedia.add("WHATS APP");

        // Creating adapter for spinnerTelp
        ArrayAdapter<String> dataAdapterKategoriMedia = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, kategoriMedia);

        // Drop down layout style - list view with radio button
        dataAdapterKategoriMedia.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinnerKategoriMedia.setAdapter(dataAdapterKategoriMedia);

        deskripsi = dataParent.get(groupPosition).getTxtDeskripsi().toString();
        lttxtMediaID = dataParent.get(groupPosition).getLttxtMediaID().toString();
        if (deskripsi.equals("Telepon") || deskripsi.equals("SMS") || deskripsi.equals("WHATSAPP") || deskripsi.equals("Fax") || deskripsi.equals("MMS")) {
            if (deskripsi.equals("Telepon")) {
                etKontak.setHint("No Telepon");
            } else if (deskripsi.equals("SMS")) {
                etKontak.setHint("No SMS");
            } else if (deskripsi.equals("WHATSAPP")) {
                etKontak.setHint("No WhatsApp");
            } else if (deskripsi.equals("Fax")) {
                etKontak.setHint("No Fax");
            } else if (deskripsi.equals("MMS")) {
                etKontak.setHint("No MMS");
            }
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
            etKontak.setHint("ID BBM");
        } else if (deskripsi.equals("Email")) {
            int maxLength = 55;
            InputFilter[] FilterArray = new InputFilter[1];
            FilterArray[0] = new InputFilter.LengthFilter(maxLength);
            etKontak.setFilters(FilterArray);

            etKontak.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
            etKontak.setHint("Akun Email");
        } else {
            if (deskripsi.equals("FACEBOOK")) {
                etKontak.setHint("Akun Facebook");
            } else if (deskripsi.equals("INSTAGRAM")) {
                etKontak.setHint("Akun Instagram");
            } else if (deskripsi.equals("TWITTER")) {
                etKontak.setHint("Akun Twitter");
            } else if (deskripsi.equals("LINE")) {
                etKontak.setHint("Akun Line");
            } else if (deskripsi.equals("PATH")) {
                etKontak.setHint("Akun Path");
            }
            int maxLength = 35;
            InputFilter[] FilterArray = new InputFilter[1];
            FilterArray[0] = new InputFilter.LengthFilter(maxLength);
            etKontak.setFilters(FilterArray);

            etKontak.setInputType(InputType.TYPE_CLASS_TEXT);
        }

        etKontak.setText(kontak);

        List<clsMediaKontakDetail> ltdt = null;

        try {
            ltdt = (List<clsMediaKontakDetail>) new clsMediaKontakDetailRepo(getContext()).findWhere(dataParent.get(groupPosition).getTxtDeskripsi());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        etKeterangan.setText(ltdt.get(childPosition).getTxtKeterangan().toString());
        etPrioritas.setText(prioritasKontak);
        etExtension.setText(ltdt.get(childPosition).getTxtExtension().toString());

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

        int index = 0;
        String item = ltdt.get(childPosition).getTxtKategoriMedia().toString();
        for (int i = 0; i < spinnerKategoriMedia.getAdapter().getCount() - 1; i++) {
            if (spinnerKategoriMedia.getItemAtPosition(i).equals(item)) {
                index = i;
            }
        }
        spinnerKategoriMedia.setSelection(index);

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

                            }
                        })
                .setNegativeButton("Tutup", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alertD = alertDialogBuilder.create();
        alertD.show();

        alertD.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String guiID = finalLtdt.get(childPosition).getTxtGuiId().toString();
                String kategoriMediaKontak = spinnerKategoriMedia.getSelectedItem().toString();
                txtDeskripsi.setText(deskripsi);
                if (etKontak.getText().toString().equals("")) {
                    new clsActivity().showToast(context.getApplicationContext(), "Kontak Tidak boleh kosong", false);
                } else if (deskripsi.equals("Telepon")) {
                    if (!isValidMobile(etKontak.getText().toString())) {
                        new clsActivity().showToast(context.getApplicationContext(), "No Telepon tidak Valid", false);
                        validate = false;
                    } else if (etPrioritas.getText().toString().equals("")) {
                        new clsActivity().showToast(context.getApplicationContext(), "Prioritas Tidak boleh kosong", false);
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
                        dt.setTxtExtension(etExtension.getText().toString());
                        dt.setTxtKategoriMedia(kategoriMediaKontak);

                        if (checkBoxStatus.isChecked()) {
                            dt.setLttxtStatusAktif("Aktif");
                        } else {
                            dt.setLttxtStatusAktif("Tidak Aktif");
                        }
                        repoKontak = new clsMediaKontakDetailRepo(context.getApplicationContext());
                        repoKontak.createOrUpdate(dt);

                        new clsActivity().showToast(context.getApplicationContext(), "Kontak berhasil di perbarui", true);
                        Log.d("Data info", "Kontak berhasil di perbarui");
                        alertD.dismiss();
                    }
                } else if (deskripsi.equals("Email")) {
                    if (!isValidEmail(etKontak.getText().toString())) {
                        new clsActivity().showToast(context.getApplicationContext(), "Email tidak valid", false);
                        validate = false;
                    } else if (etPrioritas.getText().toString().equals("")) {
                        new clsActivity().showToast(context.getApplicationContext(), "Prioritas Tidak boleh kosong", false);
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
                        dt.setTxtExtension(etExtension.getText().toString());
                        dt.setTxtKategoriMedia(kategoriMediaKontak);

                        if (checkBoxStatus.isChecked()) {
                            dt.setLttxtStatusAktif("Aktif");
                        } else {
                            dt.setLttxtStatusAktif("Tidak Aktif");
                        }
                        repoKontak = new clsMediaKontakDetailRepo(context.getApplicationContext());
                        repoKontak.createOrUpdate(dt);

                        new clsActivity().showToast(context.getApplicationContext(), "Kontak berhasil di perbarui", true);
                        Log.d("Data info", "Kontak berhasil di perbarui");
                        alertD.dismiss();
                    }
                } else if (etPrioritas.getText().toString().equals("")) {
                    new clsActivity().showToast(context.getApplicationContext(), "Prioritas Tidak boleh kosong", false);
                } else {
                    clsMediaKontakDetail dataKontak = new clsMediaKontakDetail();
                    dataKontak.setTxtGuiId(guiID);
                    dataKontak.setTxtKontakId(dataMember.get(0).txtKontakId);
                    dataKontak.setLttxtMediaID(lttxtMediaID);
                    dataKontak.setTxtDeskripsi(txtDeskripsi.getText().toString());
                    dataKontak.setTxtPrioritasKontak(etPrioritas.getText().toString());
                    dataKontak.setTxtDetailMedia(etKontak.getText().toString());
                    dataKontak.setTxtKeterangan(etKeterangan.getText().toString());
                    dataKontak.setTxtExtension(etExtension.getText().toString());
                    dataKontak.setTxtKategoriMedia(kategoriMediaKontak);

                    if (checkBoxStatus.isChecked()) {
                        dataKontak.setLttxtStatusAktif("Aktif");
                    } else {
                        dataKontak.setLttxtStatusAktif("Tidak Aktif");
                    }
                    repoKontak = new clsMediaKontakDetailRepo(context.getApplicationContext());
                    repoKontak.createOrUpdate(dataKontak);

                    new clsActivity().showToast(context.getApplicationContext(), "Kontak berhasil di perbarui", true);
                    Log.d("Data info", "Kontak berhasil di perbarui");
                    alertD.dismiss();
                }

                dataUpdate();
            }
        });
    }

    private void popupTambah() {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        final View promptView = layoutInflater.inflate(R.layout.popup_add_data_kontak, null);
        final EditText etKontak, etKeterangan, etPrioritas, etExtension;
        final CheckBox checkBoxStatus = (CheckBox) promptView.findViewById(R.id.checkboxStatus);
        final Spinner spinnerKategori, spinnerKategoriMedia;

        etKontak = (EditText) promptView.findViewById(R.id.etKontak);
        spinnerKategori = (Spinner) promptView.findViewById(R.id.spinnerKategori);
        spinnerKategoriMedia = (Spinner) promptView.findViewById(R.id.spinnerKategoriMedia);
        etKeterangan = (EditText) promptView.findViewById(R.id.etKeterangan);
        etPrioritas = (EditText) promptView.findViewById(R.id.etPrioritas);
        etExtension = (EditText) promptView.findViewById(R.id.etExtension);

        // Spinner click listener
        spinnerKategori.setOnItemSelectedListener(this);
        spinnerKategoriMedia.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("BLACKBERRY");
        categories.add("Email");
        categories.add("FACEBOOK");
        categories.add("Fax");
        categories.add("INSTAGRAM");
        categories.add("LINE");
        categories.add("MMS");
        categories.add("PATH");
        categories.add("SMS");
        categories.add("Telepon");
        categories.add("TWITTER");
        categories.add("WHATSAPP");

        List<String> kategoriMedia = new ArrayList<String>();
        kategoriMedia.add("");
        kategoriMedia.add("Home");
        kategoriMedia.add("Office");
        kategoriMedia.add("NOMOR BAPAK");
        kategoriMedia.add("NOMOR IBU");
        kategoriMedia.add("NOMOR ORANG TUA");
        kategoriMedia.add("NOMOR KELUARGA");
        kategoriMedia.add("EMAIL PIC");
        kategoriMedia.add("NOMOR PIC");
        kategoriMedia.add("NOMOR TOKO");
        kategoriMedia.add("EMAIL VERIFICATION");
        kategoriMedia.add("PATH");
        kategoriMedia.add("fACEBOOK");
        kategoriMedia.add("TWITTER");
        kategoriMedia.add("EMAIL PELANGGAN");
        kategoriMedia.add("ID LINE");
        kategoriMedia.add("BLACKBERRY");
        kategoriMedia.add("BLOG");
        kategoriMedia.add("INSTAGRAM");
        kategoriMedia.add("WHATS APP");

        // Creating adapter for spinnerTelp
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, categories);
        ArrayAdapter<String> dataAdapterKategoriMedia = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, kategoriMedia);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapterKategoriMedia.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinnerKategori.setAdapter(dataAdapter);
        spinnerKategoriMedia.setAdapter(dataAdapterKategoriMedia);

        spinnerKategori.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String kategori = spinnerKategori.getSelectedItem().toString();

                if (kategori.equals("Telepon") || kategori.equals("SMS") || kategori.equals("WHATSAPP") || kategori.equals("Fax") || kategori.equals("MMS")) {
                    if (kategori.equals("Telepon")) {
                        etKontak.setHint("No Telepon");
                    } else if (kategori.equals("SMS")) {
                        etKontak.setHint("No SMS");
                    } else if (kategori.equals("WHATSAPP")) {
                        etKontak.setHint("No WhatsApp");
                    } else if (kategori.equals("Fax")) {
                        etKontak.setHint("No Fax");
                    } else if (kategori.equals("MMS")) {
                        etKontak.setHint("No MMS");
                    }
                    int maxLength = 14;
                    InputFilter[] FilterArray = new InputFilter[1];
                    FilterArray[0] = new InputFilter.LengthFilter(maxLength);
                    etKontak.setFilters(FilterArray);

                    etKontak.setInputType(InputType.TYPE_CLASS_PHONE);
                } else if (kategori.equals("BLACKBERRY")) {
                    int maxLength = 8;
                    InputFilter[] FilterArray = new InputFilter[1];
                    FilterArray[0] = new InputFilter.LengthFilter(maxLength);
                    etKontak.setFilters(FilterArray);

                    etKontak.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
                    etKontak.setHint("ID BBM");
                } else if (kategori.equals("Email")) {
                    int maxLength = 55;
                    InputFilter[] FilterArray = new InputFilter[1];
                    FilterArray[0] = new InputFilter.LengthFilter(maxLength);
                    etKontak.setFilters(FilterArray);

                    etKontak.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                    etKontak.setHint("Akun Email");
                } else {
                    if (kategori.equals("FACEBOOK")) {
                        etKontak.setHint("Akun Facebook");
                    } else if (kategori.equals("INSTAGRAM")) {
                        etKontak.setHint("Akun Instagram");
                    } else if (kategori.equals("TWITTER")) {
                        etKontak.setHint("Akun Twitter");
                    } else if (kategori.equals("LINE")) {
                        etKontak.setHint("Akun Line");
                    } else if (kategori.equals("PATH")) {
                        etKontak.setHint("Akun Path");
                    }
                    int maxLength = 35;
                    InputFilter[] FilterArray = new InputFilter[1];
                    FilterArray[0] = new InputFilter.LengthFilter(maxLength);
                    etKontak.setFilters(FilterArray);

                    etKontak.setInputType(InputType.TYPE_CLASS_TEXT);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

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
                            @Override
                            public void onClick(DialogInterface dialog, int i) {

                            }
                        })
                .setNegativeButton("Tutup", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

        final AlertDialog alertD = alertDialogBuilder.create();
        alertD.show();

        alertD.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String kategori = spinnerKategori.getSelectedItem().toString();
                String kategoriMediaKontak = spinnerKategoriMedia.getSelectedItem().toString();
                String mediaID = null;

                if (etKontak.getText().toString().equals("")) {
                    new clsActivity().showToast(context.getApplicationContext(), "Kontak Tidak boleh kosong", false);
                } else if (kategori.equals("Telepon")) {
                    if (!isValidMobile(etKontak.getText().toString())) {
                        new clsActivity().showToast(context.getApplicationContext(), "No Telepon tidak Valid", false);
                        validate = false;
                    } else if (etPrioritas.getText().toString().equals("")) {
                        new clsActivity().showToast(context.getApplicationContext(), "Prioritas Tidak boleh kosong", false);
                    } else {
                        mediaID = "0001";
                        clsMediaKontakDetail dataKontak = new clsMediaKontakDetail();
                        dataKontak.setTxtGuiId(new clsActivity().GenerateGuid());
                        dataKontak.setTxtKontakId(dataMember.get(0).txtKontakId);
                        dataKontak.setLttxtMediaID(mediaID);
                        dataKontak.setTxtDeskripsi(kategori);
                        dataKontak.setTxtPrioritasKontak(etPrioritas.getText().toString());
                        dataKontak.setTxtDetailMedia(etKontak.getText().toString());
                        dataKontak.setTxtKeterangan(etKeterangan.getText().toString());
                        dataKontak.setTxtExtension(etExtension.getText().toString());
                        dataKontak.setTxtKategoriMedia(kategoriMediaKontak);

                        if (checkBoxStatus.isChecked()) {
                            dataKontak.setLttxtStatusAktif("Aktif");
                        } else {
                            dataKontak.setLttxtStatusAktif("Tidak Aktif");
                        }
                        repoKontak = new clsMediaKontakDetailRepo(context.getApplicationContext());
                        repoKontak.createOrUpdate(dataKontak);

                        new clsActivity().showToast(context.getApplicationContext(), "Kontak berhasil di buat", true);
                        Log.d("Data info", "Kontak berhasil di buat");
                        alertD.dismiss();
                    }
                } else if (kategori.equals("Email")) {
                    if (!isValidEmail(etKontak.getText().toString())) {
                        new clsActivity().showToast(context.getApplicationContext(), "Email tidak valid", false);
                        validate = false;
                    } else if (etPrioritas.getText().toString().equals("")) {
                        new clsActivity().showToast(context.getApplicationContext(), "Prioritas Tidak boleh kosong", false);
                    } else {
                        mediaID = "0002";
                        clsMediaKontakDetail dataKontak = new clsMediaKontakDetail();
                        dataKontak.setTxtGuiId(new clsActivity().GenerateGuid());
                        dataKontak.setTxtKontakId(dataMember.get(0).txtKontakId);
                        dataKontak.setLttxtMediaID(mediaID);
                        dataKontak.setTxtDeskripsi(kategori);
                        dataKontak.setTxtPrioritasKontak(etPrioritas.getText().toString());
                        dataKontak.setTxtDetailMedia(etKontak.getText().toString());
                        dataKontak.setTxtKeterangan(etKeterangan.getText().toString());
                        dataKontak.setTxtExtension(etExtension.getText().toString());
                        dataKontak.setTxtKategoriMedia(kategoriMediaKontak);

                        if (checkBoxStatus.isChecked()) {
                            dataKontak.setLttxtStatusAktif("Aktif");
                        } else {
                            dataKontak.setLttxtStatusAktif("Tidak Aktif");
                        }
                        repoKontak = new clsMediaKontakDetailRepo(context.getApplicationContext());
                        repoKontak.createOrUpdate(dataKontak);

                        new clsActivity().showToast(context.getApplicationContext(), "Kontak berhasil di buat", true);
                        Log.d("Data info", "Kontak berhasil di buat");
                        alertD.dismiss();
                    }
                } else if (etPrioritas.getText().toString().equals("")) {
                    new clsActivity().showToast(context.getApplicationContext(), "Prioritas Tidak boleh kosong", false);
                } else {
                    if (kategori.equals("Fax")) {
                        mediaID = "0003";
                    } else if (kategori.equals("SMS")) {
                        mediaID = "0004";
                    } else if (kategori.equals("MMS")) {
                        mediaID = "0006";
                    } else if (kategori.equals("BLACKBERRY")) {
                        mediaID = "0497";
                    } else if (kategori.equals("TWITTER")) {
                        mediaID = "0837";
                    } else if (kategori.equals("FACEBOOK")) {
                        mediaID = "0838";
                    } else if (kategori.equals("LINE")) {
                        mediaID = "1205";
                    } else if (kategori.equals("PATH")) {
                        mediaID = "1206";
                    } else if (kategori.equals("INSTAGRAM")) {
                        mediaID = "1207";
                    } else if (kategori.equals("WHATSAPP")) {
                        mediaID = "1208";
                    }
                    clsMediaKontakDetail dataKontak = new clsMediaKontakDetail();
                    dataKontak.setTxtGuiId(new clsActivity().GenerateGuid());
                    dataKontak.setTxtKontakId(dataMember.get(0).txtKontakId);
                    dataKontak.setLttxtMediaID(mediaID);
                    dataKontak.setTxtDeskripsi(kategori);
                    dataKontak.setTxtPrioritasKontak(etPrioritas.getText().toString());
                    dataKontak.setTxtDetailMedia(etKontak.getText().toString());
                    dataKontak.setTxtKeterangan(etKeterangan.getText().toString());
                    dataKontak.setTxtExtension(etExtension.getText().toString());
                    dataKontak.setTxtKategoriMedia(kategoriMediaKontak);

                    if (checkBoxStatus.isChecked()) {
                        dataKontak.setLttxtStatusAktif("Aktif");
                    } else {
                        dataKontak.setLttxtStatusAktif("Tidak Aktif");
                    }
                    repoKontak = new clsMediaKontakDetailRepo(context.getApplicationContext());
                    repoKontak.createOrUpdate(dataKontak);

                    new clsActivity().showToast(context.getApplicationContext(), "Kontak berhasil di buat", true);
                    Log.d("Data info", "Kontak berhasil di buat");
                    alertD.dismiss();
                }

                dataUpdate();
            }
        });
    }

    private void sendData() {
        String versionName = "";
        clsSendData dtJson = new clsHelper().sendData(versionName, context.getApplicationContext());
        if (dtJson != null) {
            try {
                String strLinkAPI = new clsHardCode().linkSendData;
                final String mRequestBody = "[" + dtJson.toString() + "]";

                new VolleyUtils().makeJsonObjectRequestSendData(getActivity(), strLinkAPI, dtJson, new VolleyResponseListener() {
                    @Override
                    public void onError(String message) {
                        String error;
                    }

                    @Override
                    public void onResponse(String response, Boolean status, String strErrorMsg) {
                        String warn = null;
                        try {
                            JSONObject jsonObject1 = new JSONObject(response);
                            JSONObject jsn = jsonObject1.getJSONObject("validJson");
                            warn = jsn.getString("TxtMessage");
                            String result = jsn.getString("IntResult");
                            String res = response;

                            if (result.equals("1")) {
                                sendDataMediaKontakDetail();
                            } else {
//                                new clsActivity().showCustomToast(context.getApplicationContext(), warn, false);
                                popup();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Log.i(TAG, "Ski data from server - " + warn);
                        clsUserMember userMemberData = new clsUserMember();
                    }
                });
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private void sendDataMediaKontakDetail() {
        String versionName = "";
        clsSendData dtJson = new clsHelper().sendDataMediaKontak(versionName, context.getApplicationContext());
        if (dtJson != null) {
            try {
                String strLinkAPI = new clsHardCode().linkSendDataMediaKontak;
                final String mRequestBody = "[" + dtJson.toString() + "]";

                new VolleyUtils().makeJsonObjectRequestSendDataMediaKontak(context.getApplicationContext(), strLinkAPI, dtJson, new VolleyResponseListener() {
                    @Override
                    public void onError(String message) {
                        String error;
                    }

                    @Override
                    public void onResponse(String response, Boolean status, String strErrorMsg) {
                        String warn = null;
                        try {
                            JSONObject jsonObject1 = new JSONObject(response);
                            JSONObject jsn = jsonObject1.getJSONObject("validJson");
                            warn = jsn.getString("TxtMessage");
                            String result = jsn.getString("IntResult");
                            String res = response;

                            if (result.equals("1")) {
                                new clsActivity().showCustomToast(context.getApplicationContext(), "Saved", true);
                                Intent intent = new Intent(context.getApplicationContext(), HomeMenu.class);
                                getActivity().finish();
                                startActivity(intent);
                            } else {
//                                new clsActivity().showCustomToast(context.getApplicationContext(), warn, false);
                                popup();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Log.i(TAG, "Ski data from server - " + warn);
                        clsUserMember userMemberData = new clsUserMember();
                    }
                });
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private void popup() {
        new SweetAlertDialog(getActivity(), SweetAlertDialog.NORMAL_TYPE)
                .setTitleText("Oops...")
                .setContentText("Data Gagal disimpan, silahkan coba lagi...")
                .show();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

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
