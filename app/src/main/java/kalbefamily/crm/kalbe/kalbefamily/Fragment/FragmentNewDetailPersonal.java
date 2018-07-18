package kalbefamily.crm.kalbe.kalbefamily.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import kalbefamily.crm.kalbe.kalbefamily.BL.clsActivity;
import kalbefamily.crm.kalbe.kalbefamily.Common.clsJenisMedia;
import kalbefamily.crm.kalbe.kalbefamily.Common.clsMediaKontakDetail;
import kalbefamily.crm.kalbe.kalbefamily.Common.clsMediaType;
import kalbefamily.crm.kalbe.kalbefamily.Common.clsSendData;
import kalbefamily.crm.kalbe.kalbefamily.Common.clsToken;
import kalbefamily.crm.kalbe.kalbefamily.Common.clsUserMember;
import kalbefamily.crm.kalbe.kalbefamily.Common.mConfigData;
import kalbefamily.crm.kalbe.kalbefamily.Data.DatabaseHelper;
import kalbefamily.crm.kalbe.kalbefamily.Data.DatabaseManager;
import kalbefamily.crm.kalbe.kalbefamily.Data.VolleyResponseListener;
import kalbefamily.crm.kalbe.kalbefamily.Data.VolleyUtils;
import kalbefamily.crm.kalbe.kalbefamily.Data.clsHardCode;
import kalbefamily.crm.kalbe.kalbefamily.Data.clsHelper;
import kalbefamily.crm.kalbe.kalbefamily.R;
import kalbefamily.crm.kalbe.kalbefamily.Repo.clsJenisMediaRepo;
import kalbefamily.crm.kalbe.kalbefamily.Repo.clsMediaKontakDetailRepo;
import kalbefamily.crm.kalbe.kalbefamily.Repo.clsMediaTypeRepo;
import kalbefamily.crm.kalbe.kalbefamily.Repo.clsTokenRepo;
import kalbefamily.crm.kalbe.kalbefamily.Repo.clsUserMemberRepo;
import kalbefamily.crm.kalbe.kalbefamily.Repo.mConfigRepo;
import kalbefamily.crm.kalbe.kalbefamily.addons.volley.VolleyMultipartRequest;

import static com.android.volley.VolleyLog.TAG;

/**
 * Created by Rian Andrivani on 9/7/2017.
 */

public class FragmentNewDetailPersonal extends Fragment implements AdapterView.OnItemSelectedListener {
    View v;
    Context context;
    TextView txtDeskripsi;
    FloatingActionButton fabAddKontak;
    List<clsUserMember> dataMember = null;
    List<clsMediaKontakDetail> dataParent, dataChildJoin, dataChild;
    List<clsMediaType> dataMediaType;
    List<clsJenisMedia> dataJenisMedia;
    List<clsToken> dataToken;
    clsMediaKontakDetailRepo repoKontak;
    clsUserMemberRepo repoUserMember = null;
    clsMediaTypeRepo mediaTypeRepo = null;
    clsJenisMediaRepo jenisMediaRepo = null;
    clsTokenRepo tokenRepo;
    private ExpandableListAdapter mAdapter;

    String child, deskripsi, lttxtMediaID, access_token;
    boolean validate = true;
    private String txtKontakID;
    private HashMap<String, String> hashMapSpinnerKategori = new HashMap<>();
    private HashMap<String, String> hashMapSpinnerKategoriMedia = new HashMap<>();
    DatabaseHelper helper = DatabaseManager.getInstance().getHelper();


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_new_detail_personal, container, false);
        context = getActivity().getApplicationContext();
        txtDeskripsi = (TextView) v.findViewById(R.id.txtDeskripsi);
        fabAddKontak = (FloatingActionButton) v.findViewById(R.id.fabAddKontak);

        fabAddKontak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupTambah();
            }
        });

        try {
            tokenRepo = new clsTokenRepo(context);
            dataToken = (List<clsToken>) tokenRepo.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

//        btnSimpan.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
//                alertDialog.setTitle("Konfirmasi");
//                alertDialog.setMessage("Apakah Anda yakin?");
//                alertDialog.setPositiveButton("SIMPAN", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int i) {
//                        sendData();
//                    }
//                });
//                alertDialog.setNegativeButton("BATAL", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//                alertDialog.show();
//            }
//        });

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
//            dataChildJoin = (List<clsMediaKontakDetail>) repoKontak.findbyTelpon();
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
        final TextView tvKontak, tvKategori;
        final EditText etKeterangan, etPrioritas, etExtension;
        final CheckBox checkBoxStatus = (CheckBox) promptView.findViewById(R.id.checkboxStatus);
        final Spinner spinnerKategoriMedia;
//        final String selectedItem = spinnerTelp.getSelectedItem().toString();

        tvKategori = (TextView) promptView.findViewById(R.id.tvKategori);
        tvKontak = (TextView) promptView.findViewById(R.id.etKontak);
        etKeterangan = (EditText) promptView.findViewById(R.id.etKeterangan);
        etPrioritas = (EditText) promptView.findViewById(R.id.etPrioritas);
        etExtension = (EditText) promptView.findViewById(R.id.etExtension);
        spinnerKategoriMedia = (Spinner) promptView.findViewById(R.id.spinnerKategoriMedia);

        // spinner listener
        spinnerKategoriMedia.setOnItemSelectedListener(this);

        try {
            jenisMediaRepo = new clsJenisMediaRepo(context);
            dataJenisMedia = (List<clsJenisMedia>) jenisMediaRepo.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Spinner Drop down elements
        final List<String> kategoriMedia = new ArrayList<String>();
        kategoriMedia.add("Pilih Salah satu");
        if (dataJenisMedia.size() > 0) {
            for (clsJenisMedia jenisMedia : dataJenisMedia) {
                kategoriMedia.add(jenisMedia.txtNamaMasterData);
                hashMapSpinnerKategoriMedia.put(jenisMedia.txtNamaMasterData, jenisMedia.txtGuiId);
            }
        }

        final ArrayAdapter<String> spinnerArrayAdapterKategoriMedia = new ArrayAdapter<String>(getActivity(),R.layout.spinner_item, kategoriMedia);

        // attaching data adapter to spinner
        spinnerKategoriMedia.setAdapter(spinnerArrayAdapterKategoriMedia);

        deskripsi = dataParent.get(groupPosition).getTxtDeskripsi().toString();
        lttxtMediaID = dataParent.get(groupPosition).getLttxtMediaID().toString();

        tvKategori.setText(deskripsi);


        List<clsMediaKontakDetail> ltdt = null;

        try {
            ltdt = (List<clsMediaKontakDetail>) new clsMediaKontakDetailRepo(getContext()).findWhere(dataParent.get(groupPosition).getTxtDeskripsi());
            dataChildJoin = (List<clsMediaKontakDetail>) repoKontak.findDataChildJoin(dataParent.get(groupPosition).getTxtDeskripsi());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        tvKontak.setText(ltdt.get(childPosition).getTxtDetailMedia().toString());
        etKeterangan.setText(ltdt.get(childPosition).getTxtKeterangan().toString());
        etPrioritas.setText(ltdt.get(childPosition).getTxtPrioritasKontak().toString());
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
        String itemJoin = "";
        if (dataChildJoin.get(childPosition).getTxtNamaMasterData() == null) {
            itemJoin = "";
        } else {
            itemJoin = dataChildJoin.get(childPosition).getTxtNamaMasterData().toString();
        }

        for (int i = 0; i < spinnerKategoriMedia.getAdapter().getCount(); i++) {
            if (spinnerKategoriMedia.getItemAtPosition(i).equals(itemJoin)) {
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
                final String guiID = finalLtdt.get(childPosition).getTxtGuiId().toString();
                final String kategoriMediaKontak = spinnerKategoriMedia.getSelectedItem().toString();

                txtDeskripsi.setText(deskripsi);
                if (tvKontak.getText().toString().equals("")) {
                    new clsActivity().showToast(context.getApplicationContext(), "Kontak Tidak boleh kosong", false);
                } else if (deskripsi.equals("Telepon")) {
                    if (!isValidMobile(tvKontak.getText().toString())) {
                        new clsActivity().showToast(context.getApplicationContext(), "No Telepon tidak Valid", false);
                        validate = false;
                    } else if (etPrioritas.getText().toString().equals("")) {
                        new clsActivity().showToast(context.getApplicationContext(), "Prioritas Tidak boleh kosong", false);
                    } else {
                        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                        alertDialog.setTitle("Konfirmasi");
                        alertDialog.setMessage("Apakah Anda yakin?");
                        alertDialog.setPositiveButton("SIMPAN", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
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
                                dt.setTxtDetailMedia(tvKontak.getText().toString());
                                dt.setTxtKeterangan(etKeterangan.getText().toString());
                                dt.setTxtExtension(etExtension.getText().toString());
                                if (kategoriMediaKontak.equals("Pilih Salah satu")) {
                                    dt.setTxtKategoriMedia("");
                                } else {
                                    dt.setTxtKategoriMedia(hashMapSpinnerKategoriMedia.get(kategoriMediaKontak));
                                }

                                if (checkBoxStatus.isChecked()) {
                                    dt.setLttxtStatusAktif("Aktif");
                                } else {
                                    dt.setLttxtStatusAktif("Tidak Aktif");
                                }
                                repoKontak = new clsMediaKontakDetailRepo(context.getApplicationContext());
                                helper.refreshData2();
                                repoKontak.createOrUpdate(dt);

                                alertD.dismiss();

                                sendDataMediaKontakDetail();

                                new clsActivity().showToast(context.getApplicationContext(), "Kontak Berhasil di perbarui", true);
                                Log.d("Data info", "Kontak berhasil di perbarui");
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
                } else if (deskripsi.equals("Email")) {
                    if (!isValidEmail(tvKontak.getText().toString())) {
                        new clsActivity().showToast(context.getApplicationContext(), "Email tidak valid", false);
                        validate = false;
                    } else if (etPrioritas.getText().toString().equals("")) {
                        new clsActivity().showToast(context.getApplicationContext(), "Prioritas Tidak boleh kosong", false);
                    } else {
                        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                        alertDialog.setTitle("Konfirmasi");
                        alertDialog.setMessage("Apakah Anda yakin?");
                        alertDialog.setPositiveButton("SIMPAN", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
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
                                dt.setTxtDetailMedia(tvKontak.getText().toString());
                                dt.setTxtKeterangan(etKeterangan.getText().toString());
                                dt.setTxtExtension(etExtension.getText().toString());
                                if (kategoriMediaKontak.equals("Pilih Salah satu")) {
                                    dt.setTxtKategoriMedia("");
                                } else {
                                    dt.setTxtKategoriMedia(hashMapSpinnerKategoriMedia.get(kategoriMediaKontak));
                                }

                                if (checkBoxStatus.isChecked()) {
                                    dt.setLttxtStatusAktif("Aktif");
                                } else {
                                    dt.setLttxtStatusAktif("Tidak Aktif");
                                }
                                repoKontak = new clsMediaKontakDetailRepo(context.getApplicationContext());
                                helper.refreshData2();
                                repoKontak.createOrUpdate(dt);

                                alertD.dismiss();

                                sendDataMediaKontakDetail();

                                new clsActivity().showToast(context.getApplicationContext(), "Kontak Berhasil di perbarui", true);
                                Log.d("Data info", "Kontak berhasil di perbarui");
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
                } else if (etPrioritas.getText().toString().equals("")) {
                    new clsActivity().showToast(context.getApplicationContext(), "Prioritas Tidak boleh kosong", false);
                } else {
                    final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                    alertDialog.setTitle("Konfirmasi");
                    alertDialog.setMessage("Apakah Anda yakin?");
                    alertDialog.setPositiveButton("SIMPAN", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int i) {
                            clsMediaKontakDetail dataKontak = new clsMediaKontakDetail();
                            dataKontak.setTxtGuiId(guiID);
                            dataKontak.setTxtKontakId(dataMember.get(0).txtKontakId);
                            dataKontak.setLttxtMediaID(lttxtMediaID);
                            dataKontak.setTxtDeskripsi(txtDeskripsi.getText().toString());
                            dataKontak.setTxtPrioritasKontak(etPrioritas.getText().toString());
                            dataKontak.setTxtDetailMedia(tvKontak.getText().toString());
                            dataKontak.setTxtKeterangan(etKeterangan.getText().toString());
                            dataKontak.setTxtExtension(etExtension.getText().toString());
                            if (kategoriMediaKontak.equals("Pilih Salah satu")) {
                                dataKontak.setTxtKategoriMedia("");
                            } else {
                                dataKontak.setTxtKategoriMedia(hashMapSpinnerKategoriMedia.get(kategoriMediaKontak));
                            }

                            if (checkBoxStatus.isChecked()) {
                                dataKontak.setLttxtStatusAktif("Aktif");
                            } else {
                                dataKontak.setLttxtStatusAktif("Tidak Aktif");
                            }
                            repoKontak = new clsMediaKontakDetailRepo(context.getApplicationContext());
                            helper.refreshData2();
                            repoKontak.createOrUpdate(dataKontak);

                            alertD.dismiss();

                            sendDataMediaKontakDetail();

                            new clsActivity().showToast(context.getApplicationContext(), "Kontak Berhasil di perbarui", true);
                            Log.d("Data info", "Kontak berhasil di perbarui");
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
            mediaTypeRepo = new clsMediaTypeRepo(context);
            dataMediaType = (List<clsMediaType>) mediaTypeRepo.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            jenisMediaRepo = new clsJenisMediaRepo(context);
            dataJenisMedia = (List<clsJenisMedia>) jenisMediaRepo.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Spinner click listener
        spinnerKategori.setOnItemSelectedListener(this);
        spinnerKategoriMedia.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("Pilih salah satu");
        if (dataMediaType.size() > 0) {
            for (clsMediaType mediaType : dataMediaType) {
                categories.add(mediaType.txtNamaMasterData);
                hashMapSpinnerKategori.put(mediaType.txtNamaMasterData, mediaType.txtGuiId);
            }
        }

        final List<String> kategoriMedia = new ArrayList<String>();
        kategoriMedia.add("Pilih salah satu");
        if (dataJenisMedia.size() > 0) {
            for (clsJenisMedia jenisMedia : dataJenisMedia) {
                kategoriMedia.add(jenisMedia.txtNamaMasterData);
                hashMapSpinnerKategoriMedia.put(jenisMedia.txtNamaMasterData, jenisMedia.txtGuiId);
            }
        }

        // Creating adapter for spinnerTelp
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, categories);
        ArrayAdapter<String> dataAdapterKategoriMedia = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, kategoriMedia);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapterKategoriMedia.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Initializing an ArrayAdapter with initial text like select one
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                getActivity(),R.layout.spinner_item, categories){
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                }
                else
                {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };

        final ArrayAdapter<String> spinnerArrayAdapterKategoriMedia = new ArrayAdapter<String>(getActivity(),R.layout.spinner_item, kategoriMedia);

        // attaching data adapter to spinner
        spinnerKategori.setAdapter(spinnerArrayAdapter);
        spinnerKategoriMedia.setAdapter(spinnerArrayAdapterKategoriMedia);

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
                    } else if (kategori.equals("PIlih salah satu")) {
                        etKontak.setHint("No Kontak");
                    } else {
                        int maxLength = 35;
                        InputFilter[] FilterArray = new InputFilter[1];
                        FilterArray[0] = new InputFilter.LengthFilter(maxLength);
                        etKontak.setFilters(FilterArray);
                        etKontak.setHint(kategori);

                        etKontak.setInputType(InputType.TYPE_CLASS_TEXT);
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
                final String kategori = spinnerKategori.getSelectedItem().toString();
                final String kategoriMediaKontak = spinnerKategoriMedia.getSelectedItem().toString();

                if (kategori.equals("Pilih salah satu")) {
                    new clsActivity().showToast(context.getApplicationContext(), "Anda harus memilih type kontak", false);
                } else if (etKontak.getText().toString().equals("")) {
                    new clsActivity().showToast(context.getApplicationContext(), "Kontak Tidak boleh kosong", false);
                } else if (etKontak.getText().toString().length() < 5) {
                    new clsActivity().showToast(context.getApplicationContext(), "Kontak Tidak boleh kurang dari 5 karakter", false);
                } else if (kategori.equals("Telepon")) {
                    if (!isValidMobile(etKontak.getText().toString())) {
                        new clsActivity().showToast(context.getApplicationContext(), "No Telepon tidak Valid", false);
                        validate = false;
                    } else if (etPrioritas.getText().toString().equals("")) {
                        new clsActivity().showToast(context.getApplicationContext(), "Prioritas Tidak boleh kosong", false);
                    } else {
                        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                        alertDialog.setTitle("Konfirmasi");
                        alertDialog.setMessage("Apakah Anda yakin?");
                        alertDialog.setPositiveButton("SIMPAN", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                clsMediaKontakDetail dataKontak = new clsMediaKontakDetail();
                                dataKontak.setTxtGuiId(new clsActivity().GenerateGuid());
                                dataKontak.setTxtKontakId(dataMember.get(0).txtKontakId);
                                dataKontak.setLttxtMediaID(hashMapSpinnerKategori.get(kategori));
                                dataKontak.setTxtDeskripsi(kategori);
                                dataKontak.setTxtPrioritasKontak(etPrioritas.getText().toString());
                                dataKontak.setTxtDetailMedia(etKontak.getText().toString());
                                dataKontak.setTxtKeterangan(etKeterangan.getText().toString());
                                dataKontak.setTxtExtension(etExtension.getText().toString());
                                if (kategoriMediaKontak.equals("Pilih Salah satu")) {
                                    dataKontak.setTxtKategoriMedia("");
                                } else {
                                    dataKontak.setTxtKategoriMedia(hashMapSpinnerKategoriMedia.get(kategoriMediaKontak));
                                }

                                if (checkBoxStatus.isChecked()) {
                                    dataKontak.setLttxtStatusAktif("Aktif");
                                } else {
                                    dataKontak.setLttxtStatusAktif("Tidak Aktif");
                                }
                                repoKontak = new clsMediaKontakDetailRepo(context.getApplicationContext());
                                repoKontak.createOrUpdate(dataKontak);

                                alertD.dismiss();

                                sendDataMediaKontakDetail();

                                new clsActivity().showToast(context.getApplicationContext(), "Kontak Berhasil di buat", true);
                                Log.d("Data info", "Kontak berhasil di buat");
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
                } else if (kategori.equals("Email")) {
                    if (!isValidEmail(etKontak.getText().toString())) {
                        new clsActivity().showToast(context.getApplicationContext(), "Email tidak valid", false);
                        validate = false;
                    } else if (etPrioritas.getText().toString().equals("")) {
                        new clsActivity().showToast(context.getApplicationContext(), "Prioritas Tidak boleh kosong", false);
                    } else {
                        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                        alertDialog.setTitle("Konfirmasi");
                        alertDialog.setMessage("Apakah Anda yakin?");
                        alertDialog.setPositiveButton("SIMPAN", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                clsMediaKontakDetail dataKontak = new clsMediaKontakDetail();
                                dataKontak.setTxtGuiId(new clsActivity().GenerateGuid());
                                dataKontak.setTxtKontakId(dataMember.get(0).txtKontakId);
                                dataKontak.setLttxtMediaID(hashMapSpinnerKategori.get(kategori));
                                dataKontak.setTxtDeskripsi(kategori);
                                dataKontak.setTxtPrioritasKontak(etPrioritas.getText().toString());
                                dataKontak.setTxtDetailMedia(etKontak.getText().toString());
                                dataKontak.setTxtKeterangan(etKeterangan.getText().toString());
                                dataKontak.setTxtExtension(etExtension.getText().toString());
                                if (kategoriMediaKontak.equals("Pilih Salah satu")) {
                                    dataKontak.setTxtKategoriMedia("");
                                } else {
                                    dataKontak.setTxtKategoriMedia(hashMapSpinnerKategoriMedia.get(kategoriMediaKontak));
                                }

                                if (checkBoxStatus.isChecked()) {
                                    dataKontak.setLttxtStatusAktif("Aktif");
                                } else {
                                    dataKontak.setLttxtStatusAktif("Tidak Aktif");
                                }
                                repoKontak = new clsMediaKontakDetailRepo(context.getApplicationContext());
                                repoKontak.createOrUpdate(dataKontak);

                                alertD.dismiss();

                                sendDataMediaKontakDetail();

                                new clsActivity().showToast(context.getApplicationContext(), "Kontak Berhasil di buat", true);
                                Log.d("Data info", "Kontak berhasil di buat");
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
                } else if (kategori.equals("SMS")) {
                    String noSMS = "";
                    noSMS = etKontak.getText().toString().substring(0, 2);
                    if (!noSMS.equals("08")) {
                        new clsActivity().showToast(context.getApplicationContext(), "No SMS harus diawali angka 08", false);
                        validate = false;
                    } else if (etKontak.getText().toString().length() < 8) {
                        new clsActivity().showToast(context.getApplicationContext(), "No SMS tidak boleh kurang dari 8 angka", false);
                    } else if (etPrioritas.getText().toString().equals("")) {
                        new clsActivity().showToast(context.getApplicationContext(), "Prioritas Tidak boleh kosong", false);
                    } else {
                        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                        alertDialog.setTitle("Konfirmasi");
                        alertDialog.setMessage("Apakah Anda yakin?");
                        alertDialog.setPositiveButton("SIMPAN", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                clsMediaKontakDetail dataKontak = new clsMediaKontakDetail();
                                dataKontak.setTxtGuiId(new clsActivity().GenerateGuid());
                                dataKontak.setTxtKontakId(dataMember.get(0).txtKontakId);
                                dataKontak.setLttxtMediaID(hashMapSpinnerKategori.get(kategori));
                                dataKontak.setTxtDeskripsi(kategori);
                                dataKontak.setTxtPrioritasKontak(etPrioritas.getText().toString());
                                dataKontak.setTxtDetailMedia(etKontak.getText().toString());
                                dataKontak.setTxtKeterangan(etKeterangan.getText().toString());
                                dataKontak.setTxtExtension(etExtension.getText().toString());
                                if (kategoriMediaKontak.equals("Pilih Salah satu")) {
                                    dataKontak.setTxtKategoriMedia("");
                                } else {
                                    dataKontak.setTxtKategoriMedia(hashMapSpinnerKategoriMedia.get(kategoriMediaKontak));
                                }

                                if (checkBoxStatus.isChecked()) {
                                    dataKontak.setLttxtStatusAktif("Aktif");
                                } else {
                                    dataKontak.setLttxtStatusAktif("Tidak Aktif");
                                }
                                repoKontak = new clsMediaKontakDetailRepo(context.getApplicationContext());
                                repoKontak.createOrUpdate(dataKontak);

                                alertD.dismiss();

                                sendDataMediaKontakDetail();

                                new clsActivity().showToast(context.getApplicationContext(), "Kontak Berhasil di buat", true);
                                Log.d("Data info", "Kontak berhasil di buat");
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
                } else if (etPrioritas.getText().toString().equals("")) {
                    new clsActivity().showToast(context.getApplicationContext(), "Prioritas Tidak boleh kosong", false);
                } else {
                    final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                    alertDialog.setTitle("Konfirmasi");
                    alertDialog.setMessage("Apakah Anda yakin?");
                    alertDialog.setPositiveButton("SIMPAN", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int i) {
                            clsMediaKontakDetail dataKontak = new clsMediaKontakDetail();
                            dataKontak.setTxtGuiId(new clsActivity().GenerateGuid());
                            dataKontak.setTxtKontakId(dataMember.get(0).txtKontakId);
                            dataKontak.setLttxtMediaID(hashMapSpinnerKategori.get(kategori));
                            dataKontak.setTxtDeskripsi(kategori);
                            dataKontak.setTxtPrioritasKontak(etPrioritas.getText().toString());
                            dataKontak.setTxtDetailMedia(etKontak.getText().toString());
                            dataKontak.setTxtKeterangan(etKeterangan.getText().toString());
                            dataKontak.setTxtExtension(etExtension.getText().toString());
                            if (kategoriMediaKontak.equals("Pilih Salah satu")) {
                                dataKontak.setTxtKategoriMedia("");
                            } else {
                                dataKontak.setTxtKategoriMedia(hashMapSpinnerKategoriMedia.get(kategoriMediaKontak));
                            }

                            if (checkBoxStatus.isChecked()) {
                                dataKontak.setLttxtStatusAktif("Aktif");
                            } else {
                                dataKontak.setLttxtStatusAktif("Tidak Aktif");
                            }
                            repoKontak = new clsMediaKontakDetailRepo(context.getApplicationContext());
                            repoKontak.createOrUpdate(dataKontak);

//                            new clsActivity().showToast(context.getApplicationContext(), "Kontak berhasil di buat", true);
                            alertD.dismiss();

                            sendDataMediaKontakDetail();

                            new clsActivity().showToast(context.getApplicationContext(), "Kontak Berhasil di buat", true);
                            Log.d("Data info", "Kontak berhasil di buat");
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

                dataUpdate();
            }
        });
    }

    private void sendDataMediaKontakDetail() {
        String versionName = "";
        clsSendData dtJson = new clsHelper().sendDataMediaKontak(versionName, context.getApplicationContext());
        if (dtJson != null) {
            try {
                String strLinkAPI = new clsHardCode().linkSendDataMediaKontak;
                final String mRequestBody = "[" + dtJson.toString() + "]";

                volleyRequestSendDataMediaKontak(strLinkAPI, dtJson, new VolleyResponseListener() {
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
                                Log.i(TAG, "Ski data from server - " + warn);
//                                Intent intent = new Intent(context.getApplicationContext(), HomeMenu.class);
//                                getActivity().finish();
//                                startActivity(intent);
                            } else {
//                                new clsActivity().showCustomToast(context.getApplicationContext(), warn, false);
                                popup();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Log.i(TAG, "Ski data from server - " + warn);
                        kontakDetail();
                    }
                });
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public void kontakDetail() {
        final ProgressDialog Dialog = new ProgressDialog(getActivity());
        RequestQueue queue = Volley.newRequestQueue(context.getApplicationContext());
        clsUserMemberRepo repo = new clsUserMemberRepo(context.getApplicationContext());
        helper.refreshData2();
        try {
            dataMember = (List<clsUserMember>) repo.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        txtKontakID = dataMember.get(0).getTxtKontakId();
        String strLinkAPI = new clsHardCode().linkGetDataKontakDetail;
//        String nameRole = selectedRole;
        JSONObject resJson = new JSONObject();

        try {
            resJson.put("txtKontakID", txtKontakID);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final String mRequestBody = "[" + resJson.toString() + "]";

        volleyJsonObjectRequest(strLinkAPI, mRequestBody, "Sinkronisasi Data...", new VolleyResponseListener() {
            @Override
            public void onError(String response) {
                new clsActivity().showCustomToast(context.getApplicationContext(), response, false);
            }

            @Override
            public void onResponse(String response, Boolean status, String strErrorMsg) {
                if (response != null) {
                    try {
                        JSONObject jsonObject1 = new JSONObject(response);
                        JSONObject jsn = jsonObject1.getJSONObject("validJson");
                        String warn = jsn.getString("TxtMessage");
                        String result = jsn.getString("IntResult");

                        if (result.equals("1")) {
                            JSONArray jsonDataUserMember = jsn.getJSONArray("ListOfObjectData");
                            for(int i=0; i < jsonDataUserMember.length(); i++) {
                                JSONObject jsonobject = jsonDataUserMember.getJSONObject(i);
                                String lttxtMediaID = jsonobject.getString("LttxtMediaID");
                                String txtDeskripsi = jsonobject.getString("TxtDeskripsi");
                                String txtPrioritasKontak = jsonobject.getString("IntPrioritasKontak");
                                String txtDetailMedia = jsonobject.getString("TxtDetailMedia");
                                String txtKeterangan = jsonobject.getString("TxtKeterangan");
                                String lttxtStatusAktif = jsonobject.getString("LttxtStatusAktif");
                                String txtKategoriMedia = jsonobject.getString("TxtKategoriMedia");
                                String txtExtension = jsonobject.getString("TxtExtension");
                                txtDeskripsi = txtDeskripsi.trim();

                                clsMediaKontakDetail dataKontak = new clsMediaKontakDetail();
                                dataKontak.setTxtGuiId(new clsActivity().GenerateGuid());
                                dataKontak.setTxtKontakId(dataMember.get(0).txtKontakId);
                                dataKontak.setLttxtMediaID(lttxtMediaID);
                                dataKontak.setTxtDeskripsi(txtDeskripsi);
                                dataKontak.setTxtPrioritasKontak(txtPrioritasKontak);
                                dataKontak.setTxtDetailMedia(txtDetailMedia);
                                dataKontak.setTxtKeterangan(txtKeterangan);
                                dataKontak.setLttxtStatusAktif(lttxtStatusAktif);
                                dataKontak.setTxtKategoriMedia(txtKategoriMedia);
                                dataKontak.setTxtExtension(txtExtension);

                                repoKontak = new clsMediaKontakDetailRepo(context.getApplicationContext());
                                repoKontak.createOrUpdate(dataKontak);
                            }
                            Log.d("Data info", "Data Kontak Detail berhasil di update");
                            dataUpdate();

                        } else {
                            new clsActivity().showCustomToast(context.getApplicationContext(), warn, false);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
//                if(!status){
//                    new clsMainActivity().showCustomToast(getApplicationContext(), strErrorMsg, false);
//                }
            }
        });
    }

    public void volleyRequestSendDataMediaKontak(String strLinkAPI, final clsSendData mRequestBody, final VolleyResponseListener listener) {
        String client = "";
        RequestQueue queue = Volley.newRequestQueue(context);
        ProgressDialog Dialog = new ProgressDialog(getActivity());

        Dialog = ProgressDialog.show(getActivity(), "", "Mohon Tunggu...", true);

        final ProgressDialog finalDialog = Dialog;
        final ProgressDialog finalDialog1 = Dialog;

        mConfigRepo configRepo = new mConfigRepo(context);
        try {
            mConfigData configDataClient = (mConfigData) configRepo.findById(5);
            client = configDataClient.getTxtDefaultValue().toString();
            dataToken = (List<clsToken>) tokenRepo.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        final String finalClient = client;
        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, strLinkAPI, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Boolean status = false;
                String errorMessage = null;
                listener.onResponse(response.toString(), status, errorMessage);
                finalDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String strLinkRequestToken = new clsHardCode().linkToken;
                final String refresh_token = dataToken.get(0).txtRefreshToken;
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_UNAUTHORIZED) {

                    new VolleyUtils().requestTokenWithRefresh(getActivity(), strLinkRequestToken, refresh_token, finalClient, new VolleyResponseListener() {
                        @Override
                        public void onError(String message) {
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onResponse(String response, Boolean status, String strErrorMsg) {
                            if (response != null) {
                                try {
                                    String accessToken = "";
                                    String newRefreshToken = "";
                                    String refreshToken = "";
                                    JSONObject jsonObject = new JSONObject(response);
                                    accessToken = jsonObject.getString("access_token");
                                    refreshToken = jsonObject.getString("refresh_token");
                                    String dtIssued = jsonObject.getString(".issued");

                                    clsToken data = new clsToken();
                                    data.setIntId("1");
                                    data.setDtIssuedToken(dtIssued);
                                    data.setTxtUserToken(accessToken);
                                    data.setTxtRefreshToken(refreshToken);

                                    tokenRepo.createOrUpdate(data);
                                    //Toast.makeText(getApplicationContext(), "Success get new Access Token", Toast.LENGTH_SHORT).show();
                                    newRefreshToken = refreshToken;
                                    if (refreshToken == newRefreshToken && !newRefreshToken.equals("")) {
                                        sendDataMediaKontakDetail();
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                Toast.makeText(context, strErrorMsg, Toast.LENGTH_SHORT).show();
                            }
                            finalDialog1.dismiss();
                        }
                    });
                } else if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_INTERNAL_SERVER_ERROR ){
                    new clsActivity().showCustomToast(context, "Error 500, Server Error", false);
                    finalDialog1.dismiss();
                } else {
                    popup();
                    finalDialog1.dismiss();
                }
            }
            public void popup() {
                final SweetAlertDialog dialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE);
                dialog.setTitleText("Oops...");
                dialog.setContentText("Mohon check kembali koneksi internet anda");
                dialog.setCancelable(false);
                dialog.show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                try {
                    final String mRequestBody2 = "[" +  mRequestBody.getDtdataJson().txtJSONmediaKontak().toString() + "]";
                    params.put("txtParam", mRequestBody2);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                try {
                    tokenRepo = new clsTokenRepo(context);
                    dataToken = (List<clsToken>) tokenRepo.findAll();
                    access_token = dataToken.get(0).getTxtUserToken();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + access_token);

                return headers;
            }

        };
        multipartRequest.setRetryPolicy(new
                DefaultRetryPolicy(500000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(multipartRequest);
    }

    private void volleyJsonObjectRequest(String strLinkAPI, final String mRequestBody, String progressBarType, final VolleyResponseListener listener) {
        String client = "";
        RequestQueue queue = Volley.newRequestQueue(context);
        ProgressDialog Dialog = new ProgressDialog(getActivity());
        Dialog = ProgressDialog.show(getActivity(), "", progressBarType, false);

        final ProgressDialog finalDialog = Dialog;
        final ProgressDialog finalDialog1 = Dialog;

        mConfigRepo configRepo = new mConfigRepo(context);
        try {
            mConfigData configDataClient = (mConfigData) configRepo.findById(5);
            client = configDataClient.getTxtDefaultValue().toString();
            dataToken = (List<clsToken>) tokenRepo.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        final String finalClient = client;
        StringRequest req = new StringRequest(Request.Method.POST, strLinkAPI, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Boolean status = false;
                String errorMessage = null;
                listener.onResponse(response, status, errorMessage);
                finalDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String strLinkRequestToken = new clsHardCode().linkToken;
                final String refresh_token = dataToken.get(0).txtRefreshToken;
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_UNAUTHORIZED) {
                    // HTTP Status Code: 401 Unauthorized
                    //new clsActivity().showCustomToast(getApplicationContext(), "401, Authorization has been denied for this request", false);

                    new VolleyUtils().requestTokenWithRefresh(getActivity(), strLinkRequestToken, refresh_token, finalClient, new VolleyResponseListener() {
                        @Override
                        public void onError(String message) {
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onResponse(String response, Boolean status, String strErrorMsg) {
                            if (response != null) {
                                try {
                                    String accessToken = "";
                                    String newRefreshToken = "";
                                    String refreshToken = "";
                                    JSONObject jsonObject = new JSONObject(response);
                                    accessToken = jsonObject.getString("access_token");
                                    refreshToken = jsonObject.getString("refresh_token");
                                    String dtIssued = jsonObject.getString(".issued");

                                    clsToken data = new clsToken();
                                    data.setIntId("1");
                                    data.setDtIssuedToken(dtIssued);
                                    data.setTxtUserToken(accessToken);
                                    data.setTxtRefreshToken(refreshToken);

                                    tokenRepo.createOrUpdate(data);
                                    //Toast.makeText(getApplicationContext(), "Success get new Access Token", Toast.LENGTH_SHORT).show();
                                    newRefreshToken = refreshToken;
                                    if (refreshToken == newRefreshToken && !newRefreshToken.equals("")) {
                                        kontakDetail();
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                Toast.makeText(context, strErrorMsg, Toast.LENGTH_SHORT).show();
                            }
                            finalDialog1.dismiss();
                        }
                    });
                } else if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_INTERNAL_SERVER_ERROR ){
                    new clsActivity().showCustomToast(context, "Error 500, Server Error", false);
                    finalDialog1.dismiss();
                } else {
                    popup();
                    finalDialog1.dismiss();
                }
            }
            public void popup() {
                final SweetAlertDialog dialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE);
                dialog.setTitleText("Oops...");
                dialog.setContentText("Mohon check kembali koneksi internet anda");
                dialog.setCancelable(false);
                dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        dialog.dismiss();
                        sweetAlertDialog.dismiss();
                    }
                });
                dialog.show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("txtParam", mRequestBody);
                return params;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                try {
                    tokenRepo = new clsTokenRepo(context);
                    dataToken = (List<clsToken>) tokenRepo.findAll();
                    access_token = dataToken.get(0).getTxtUserToken();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + access_token);

                return headers;
            }
        };
        req.setRetryPolicy(new
                DefaultRetryPolicy(60000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


        queue.add(req);
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
