package kalbefamily.crm.kalbe.kalbefamily;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import kalbefamily.crm.kalbe.kalbefamily.BL.clsActivity;
import kalbefamily.crm.kalbe.kalbefamily.Common.clsMediaKontakDetail;
import kalbefamily.crm.kalbe.kalbefamily.Common.clsUserMember;
import kalbefamily.crm.kalbe.kalbefamily.Data.DatabaseHelper;
import kalbefamily.crm.kalbe.kalbefamily.Data.DatabaseManager;
import kalbefamily.crm.kalbe.kalbefamily.Data.VolleyResponseListener;
import kalbefamily.crm.kalbe.kalbefamily.Data.VolleyUtils;
import kalbefamily.crm.kalbe.kalbefamily.Data.clsHardCode;
import kalbefamily.crm.kalbe.kalbefamily.Repo.clsMediaKontakDetailRepo;
import kalbefamily.crm.kalbe.kalbefamily.Repo.clsUserMemberRepo;

/**
 * Created by Rian Andrivani on 8/28/2017.
 */

public class FragmentDetailPersonalData extends Fragment implements AdapterView.OnItemSelectedListener {
    View v;
    Context context;
    List<clsUserMember> dataMember = null;
    List<clsMediaKontakDetail> dataNoTelp, dataSms;
    clsMediaKontakDetailRepo repoKontakDetail;
    clsMediaKontakDetailRepo repoKontak;
    private String txtKontakID;

    RelativeLayout layout1, layout2, layout3, layout4, layout5, layout6;
    TextView tvNoTelp, tvSms;
    Button btnNoTelp;
    Spinner spinner, spinnerSms;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_detail_personal_data, container, false);
        context = getActivity().getApplicationContext();

        tvNoTelp = (TextView) v.findViewById(R.id.textViewNoTelp);
        tvSms = (TextView) v.findViewById(R.id.textViewSMS);
        btnNoTelp = (Button) v.findViewById(R.id.btnEdit1);
        spinner = (Spinner) v.findViewById(R.id.spinnerTelpon);
        spinnerSms = (Spinner) v.findViewById(R.id.spinnerSMS);

        btnNoTelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectOption();
            }
        });

        repoKontak = new clsMediaKontakDetailRepo(context.getApplicationContext());
        try {
            dataNoTelp = (List<clsMediaKontakDetail>) repoKontak.findbyTelpon();
            dataSms = (List<clsMediaKontakDetail>) repoKontak.findbySms();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Spinner click listener
        spinner.setOnItemSelectedListener(this);
        spinnerSms.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List<String> noTelp = new ArrayList<String>();
        List<String> noSms = new ArrayList<String>();
        if (dataNoTelp.size() > 0) {
            for (clsMediaKontakDetail kontakDetail : dataNoTelp) {
                noTelp.add(kontakDetail.getTxtDetailMedia());
            }
        } if (dataSms.size() > 0) {
            for (clsMediaKontakDetail kontakDetail : dataSms) {
                noSms.add(kontakDetail.getTxtDetailMedia());
            }
        }

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context.getApplicationContext(), android.R.layout.simple_spinner_item, noTelp);
        ArrayAdapter<String> dataAdapterSms = new ArrayAdapter<String>(context.getApplicationContext(), android.R.layout.simple_spinner_item, noSms);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapterSms.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
        spinnerSms.setAdapter(dataAdapterSms);

        return v;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String telpon = adapterView.getItemAtPosition(i).toString();
        String sms = adapterView.getItemAtPosition(i).toString();

        Spinner spinner = (Spinner) adapterView;
        if(spinner.getId() == R.id.spinnerTelpon) {
            tvNoTelp.setText(telpon);
        } if(spinner.getId() == R.id.spinnerSMS) {
            tvSms.setText(sms);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void popupEditNoTelp() {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        final View promptView = layoutInflater.inflate(R.layout.popup_add_edit_data, null);
        final EditText etKontak =(EditText) promptView.findViewById(R.id.etKontak);
        final CheckBox checkBoxStatus = (CheckBox) promptView.findViewById(R.id.checkboxStatus);
        final RadioButton radioButtonPrioritas, radioButtonBknPrioritas;
        final RadioGroup radioGenderradioGroupPrioritas;
        radioGenderradioGroupPrioritas = (RadioGroup) v.findViewById(R.id.radioGroupPrioritas);
        radioButtonPrioritas = (RadioButton) v.findViewById(R.id.radioButtonPrioritas);
        radioButtonBknPrioritas = (RadioButton) v.findViewById(R.id.radioButtonBknPrioritas);

        etKontak.setText(tvNoTelp.getText().toString());
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
//        radioButtonActive.setChecked(true);
//        radioButtonPrioritas.setChecked(true);
//        radioButtonInActive.setChecked(false);
//        radioButtonBknPrioritas.setChecked(false);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this.getContext());
        alertDialogBuilder.setView(promptView);
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
//                                etTelpon.setText(etKontak.getText().toString());
                                dialog.dismiss();
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

    private void selectOption() {
        final CharSequence[] items = { "Tambah Baru", "Edit",
                "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Pilihan");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result= Utility.checkPermission(getActivity());
                if (items[item].equals("Tambah Baru")) {
                    if(result)
                        dialog.dismiss();
                } else if (items[item].equals("Edit")) {
                    if(result)
                        popupEditNoTelp();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    public static class Utility {
        public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        public static boolean checkPermission(final Context context)
        {
            int currentAPIVersion = Build.VERSION.SDK_INT;
            if(currentAPIVersion>= Build.VERSION_CODES.M)
            {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                        alertBuilder.setCancelable(true);
                        alertBuilder.setTitle("Permission necessary");
                        alertBuilder.setMessage("External storage permission is necessary");
                        alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                            }
                        });
                        AlertDialog alert = alertBuilder.create();
                        alert.show();
                    } else {
                        ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                    }
                    return false;
                } else {
                    return true;
                }
            } else {
                return true;
            }
        }
    }
}
