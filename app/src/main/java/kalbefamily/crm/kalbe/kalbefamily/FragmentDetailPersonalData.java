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
    List<clsMediaKontakDetail> dataNoTelp, dataSms, dataBBM, dataLine, dataWA, dataEmail, dataTwitter, dataFacebook, dataInstagram, dataFax, dataPath, dataMMS;
    clsMediaKontakDetailRepo repoKontak;
    private String txtKontakID;

    RelativeLayout layout1, layout2, layout3, layout4, layout5, layout6, layout7, layout8, layout9, layout10, layout11, layout12;
    TextView tvNoTelp, tvSms, tvBBM, tvLine, tvWA, tvEmail, tvTwitter, tvFacebook, tvInstagram, tvFax, tvMMS, tvPath;
    Button btnNoTelp;
    Spinner spinner, spinnerSms, spinnerBBM, spinnerLine, spinnerWA, spinnerEmail, spinnertwitter, spinnerFacebook, spinnerInstagram, spinnerFax, spinnerMMS, spinnerPath;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_detail_personal_data, container, false);
        context = getActivity().getApplicationContext();

        layout1 = (RelativeLayout) v.findViewById(R.id.layout1);
        layout2 = (RelativeLayout) v.findViewById(R.id.layout2);
        layout3 = (RelativeLayout) v.findViewById(R.id.layout3);
        layout4 = (RelativeLayout) v.findViewById(R.id.layout4);
        layout5 = (RelativeLayout) v.findViewById(R.id.layout5);
        layout6 = (RelativeLayout) v.findViewById(R.id.layout6);
        layout7 = (RelativeLayout) v.findViewById(R.id.layout7);
        layout8 = (RelativeLayout) v.findViewById(R.id.layout8);
        layout9 = (RelativeLayout) v.findViewById(R.id.layout9);
        layout10 = (RelativeLayout) v.findViewById(R.id.layout10);
        layout11 = (RelativeLayout) v.findViewById(R.id.layout11);
        layout12 = (RelativeLayout) v.findViewById(R.id.layout12);
        tvNoTelp = (TextView) v.findViewById(R.id.textViewNoTelp);
        tvSms = (TextView) v.findViewById(R.id.textViewSMS);
        tvBBM = (TextView) v.findViewById(R.id.textViewBBM);
        tvLine = (TextView) v.findViewById(R.id.textViewLine);
        tvWA = (TextView) v.findViewById(R.id.textViewWhatsApp);
        tvEmail = (TextView) v.findViewById(R.id.textViewEmail);
        tvTwitter = (TextView) v.findViewById(R.id.textViewTwitter);
        tvFacebook = (TextView) v.findViewById(R.id.textViewFacebook);
        tvInstagram = (TextView) v.findViewById(R.id.textViewInstagram);
        tvFax = (TextView) v.findViewById(R.id.textViewFax);
        tvPath = (TextView) v.findViewById(R.id.textViewPath);
        tvMMS = (TextView) v.findViewById(R.id.textViewMms);
        btnNoTelp = (Button) v.findViewById(R.id.btnEdit1);
        spinner = (Spinner) v.findViewById(R.id.spinnerTelpon);
        spinnerSms = (Spinner) v.findViewById(R.id.spinnerSMS);
        spinnerBBM = (Spinner) v.findViewById(R.id.spinnerBBM);
        spinnerLine = (Spinner) v.findViewById(R.id.spinnerLine);
        spinnerWA = (Spinner) v.findViewById(R.id.spinnerWA);
        spinnerEmail = (Spinner) v.findViewById(R.id.spinnerEmail);
        spinnertwitter = (Spinner) v.findViewById(R.id.spinnerTwitter);
        spinnerFacebook = (Spinner) v.findViewById(R.id.spinnerFacebook);
        spinnerInstagram = (Spinner) v.findViewById(R.id.spinnerInstagram);
        spinnerFax = (Spinner) v.findViewById(R.id.spinnerFax);
        spinnerPath = (Spinner) v.findViewById(R.id.spinnerPath);
        spinnerMMS = (Spinner) v.findViewById(R.id.spinnerMMS);

        layout1.setVisibility(View.GONE);
        layout2.setVisibility(View.GONE);
        layout6.setVisibility(View.GONE);

        repoKontak = new clsMediaKontakDetailRepo(context.getApplicationContext());
        try {
            dataNoTelp = (List<clsMediaKontakDetail>) repoKontak.findbyTelpon();
            dataSms = (List<clsMediaKontakDetail>) repoKontak.findbySms();
            dataEmail = (List<clsMediaKontakDetail>) repoKontak.findbyEmail();
            dataBBM = (List<clsMediaKontakDetail>) repoKontak.findbyBBM();
            dataLine = (List<clsMediaKontakDetail>) repoKontak.findbyLine();
            dataWA = (List<clsMediaKontakDetail>) repoKontak.findbyWA();
            dataTwitter = (List<clsMediaKontakDetail>) repoKontak.findbyTwitter();
            dataFacebook = (List<clsMediaKontakDetail>) repoKontak.findbyFacebook();
            dataInstagram = (List<clsMediaKontakDetail>) repoKontak.findbyInstagram();
            dataFax = (List<clsMediaKontakDetail>) repoKontak.findbyFax();
            dataPath = (List<clsMediaKontakDetail>) repoKontak.findbyPath();
            dataMMS = (List<clsMediaKontakDetail>) repoKontak.findbyMMS();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // visible or gone layout
        if (dataNoTelp.size() > 0) {
            layout1.setVisibility(View.VISIBLE);
        } if (dataSms.size() > 0) {
            layout2.setVisibility(View.VISIBLE);
        } if (dataEmail.size() > 0) {
            layout6.setVisibility(View.VISIBLE);
        } if (dataBBM.size() > 0) {
            layout3.setVisibility(View.VISIBLE);
        } if (dataLine.size() > 0) {
            layout4.setVisibility(View.VISIBLE);
        } if (dataWA.size() > 0) {
            layout5.setVisibility(View.VISIBLE);
        } if (dataTwitter.size() > 0) {
            layout7.setVisibility(View.VISIBLE);
        } if (dataFacebook.size() > 0) {
            layout8.setVisibility(View.VISIBLE);
        } if (dataInstagram.size() > 0) {
            layout9.setVisibility(View.VISIBLE);
        } if (dataFax.size() > 0) {
            layout10.setVisibility(View.VISIBLE);
        } if (dataPath.size() > 0) {
            layout11.setVisibility(View.VISIBLE);
        } if (dataMMS.size() > 0) {
            layout12.setVisibility(View.VISIBLE);
        }

        btnNoTelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectOption();
            }
        });

        // Spinner click listener
        spinner.setOnItemSelectedListener(this);
        spinnerSms.setOnItemSelectedListener(this);
        spinnerBBM.setOnItemSelectedListener(this);
        spinnerLine.setOnItemSelectedListener(this);
        spinnerWA.setOnItemSelectedListener(this);
        spinnerEmail.setOnItemSelectedListener(this);
        spinnertwitter.setOnItemSelectedListener(this);
        spinnerFacebook.setOnItemSelectedListener(this);
        spinnerInstagram.setOnItemSelectedListener(this);
        spinnerFax.setOnItemSelectedListener(this);
        spinnerPath.setOnItemSelectedListener(this);
        spinnerMMS.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List<String> noTelp = new ArrayList<String>();
        List<String> noSms = new ArrayList<String>();
        List<String> bbm = new ArrayList<String>();
        List<String> line = new ArrayList<String>();
        List<String> wa = new ArrayList<String>();
        List<String> email = new ArrayList<String>();
        List<String> twitter = new ArrayList<String>();
        List<String> facebook = new ArrayList<String>();
        List<String> instagram = new ArrayList<String>();
        List<String> fax = new ArrayList<String>();
        List<String> path = new ArrayList<String>();
        List<String> mms = new ArrayList<String>();
        if (dataNoTelp.size() > 0) {
            for (clsMediaKontakDetail kontakDetail : dataNoTelp) {
                noTelp.add(kontakDetail.getTxtDetailMedia());
            }
        } if (dataSms.size() > 0) {
            for (clsMediaKontakDetail kontakDetail : dataSms) {
                noSms.add(kontakDetail.getTxtDetailMedia());
            }
        } if (dataBBM.size() > 0) {
            for (clsMediaKontakDetail kontakDetail : dataBBM) {
                bbm.add(kontakDetail.getTxtDetailMedia());
            }
        } if (dataLine.size() > 0) {
            for (clsMediaKontakDetail kontakDetail : dataLine) {
                line.add(kontakDetail.getTxtDetailMedia());
            }
        } if (dataWA.size() > 0) {
            for (clsMediaKontakDetail kontakDetail : dataWA) {
                wa.add(kontakDetail.getTxtDetailMedia());
            }
        } if (dataEmail.size() > 0) {
            for (clsMediaKontakDetail kontakDetail : dataEmail) {
                email.add(kontakDetail.getTxtDetailMedia());
            }
        } if (dataTwitter.size() > 0) {
            for (clsMediaKontakDetail kontakDetail : dataTwitter) {
                twitter.add(kontakDetail.getTxtDetailMedia());
            }
        } if (dataFacebook.size() > 0) {
            for (clsMediaKontakDetail kontakDetail : dataFacebook) {
                facebook.add(kontakDetail.getTxtDetailMedia());
            }
        } if (dataInstagram.size() > 0) {
            for (clsMediaKontakDetail kontakDetail : dataInstagram) {
                instagram.add(kontakDetail.getTxtDetailMedia());
            }
        } if (dataFax.size() > 0) {
            for (clsMediaKontakDetail kontakDetail : dataFax) {
                fax.add(kontakDetail.getTxtDetailMedia());
            }
        } if (dataPath.size() > 0) {
            for (clsMediaKontakDetail kontakDetail : dataPath) {
                path.add(kontakDetail.getTxtDetailMedia());
            }
        } if (dataMMS.size() > 0) {
            for (clsMediaKontakDetail kontakDetail : dataMMS) {
                mms.add(kontakDetail.getTxtDetailMedia());
            }
        }

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context.getApplicationContext(), android.R.layout.simple_spinner_item, noTelp);
        ArrayAdapter<String> dataAdapterSms = new ArrayAdapter<String>(context.getApplicationContext(), android.R.layout.simple_spinner_item, noSms);
        ArrayAdapter<String> dataAdapterBBM = new ArrayAdapter<String>(context.getApplicationContext(), android.R.layout.simple_spinner_item, bbm);
        ArrayAdapter<String> dataAdapterLine = new ArrayAdapter<String>(context.getApplicationContext(), android.R.layout.simple_spinner_item, line);
        ArrayAdapter<String> dataAdapterWA = new ArrayAdapter<String>(context.getApplicationContext(), android.R.layout.simple_spinner_item, wa);
        ArrayAdapter<String> dataAdapterEmail = new ArrayAdapter<String>(context.getApplicationContext(), android.R.layout.simple_spinner_item, email);
        ArrayAdapter<String> dataAdapterTwitter = new ArrayAdapter<String>(context.getApplicationContext(), android.R.layout.simple_spinner_item, twitter);
        ArrayAdapter<String> dataAdapterFacebook = new ArrayAdapter<String>(context.getApplicationContext(), android.R.layout.simple_spinner_item, facebook);
        ArrayAdapter<String> dataAdapterInstagram = new ArrayAdapter<String>(context.getApplicationContext(), android.R.layout.simple_spinner_item, instagram);
        ArrayAdapter<String> dataAdapterFax = new ArrayAdapter<String>(context.getApplicationContext(), android.R.layout.simple_spinner_item, fax);
        ArrayAdapter<String> dataAdapterPath = new ArrayAdapter<String>(context.getApplicationContext(), android.R.layout.simple_spinner_item, path);
        ArrayAdapter<String> dataAdapterMMS = new ArrayAdapter<String>(context.getApplicationContext(), android.R.layout.simple_spinner_item, mms);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapterSms.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapterBBM.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapterLine.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapterWA.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapterEmail.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapterTwitter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapterFacebook.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapterInstagram.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapterFax.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapterPath.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapterMMS.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
        spinnerSms.setAdapter(dataAdapterSms);
        spinnerBBM.setAdapter(dataAdapterBBM);
        spinnerLine.setAdapter(dataAdapterLine);
        spinnerWA.setAdapter(dataAdapterWA);
        spinnerEmail.setAdapter(dataAdapterEmail);
        spinnertwitter.setAdapter(dataAdapterTwitter);
        spinnerFacebook.setAdapter(dataAdapterFacebook);
        spinnerInstagram.setAdapter(dataAdapterInstagram);
        spinnerFax.setAdapter(dataAdapterFax);
        spinnerPath.setAdapter(dataAdapterPath);
        spinnerMMS.setAdapter(dataAdapterMMS);

        return v;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String telpon = adapterView.getItemAtPosition(i).toString();
        String sms = adapterView.getItemAtPosition(i).toString();
        String txtDetailMedia = adapterView.getItemAtPosition(i).toString();

        Spinner spinner = (Spinner) adapterView;
        if(spinner.getId() == R.id.spinnerTelpon) {
            tvNoTelp.setText(telpon);
        } if(spinner.getId() == R.id.spinnerSMS) {
            tvSms.setText(sms);
        } if(spinner.getId() == R.id.spinnerBBM) {
            tvBBM.setText(txtDetailMedia);
        } if(spinner.getId() == R.id.spinnerLine) {
            tvLine.setText(txtDetailMedia);
        } if(spinner.getId() == R.id.spinnerWA) {
            tvWA.setText(txtDetailMedia);
        } if(spinner.getId() == R.id.spinnerEmail) {
            tvEmail.setText(txtDetailMedia);
        } if(spinner.getId() == R.id.spinnerTwitter) {
            tvTwitter.setText(txtDetailMedia);
        } if(spinner.getId() == R.id.spinnerFacebook) {
            tvFacebook.setText(txtDetailMedia);
        } if(spinner.getId() == R.id.spinnerInstagram) {
            tvInstagram.setText(txtDetailMedia);
        } if(spinner.getId() == R.id.spinnerFax) {
            tvFax.setText(txtDetailMedia);
        } if(spinner.getId() == R.id.spinnerPath) {
            tvPath.setText(txtDetailMedia);
        } if(spinner.getId() == R.id.spinnerMMS) {
            tvMMS.setText(txtDetailMedia);
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
