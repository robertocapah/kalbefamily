package kalbefamily.crm.kalbe.kalbefamily.Fragment;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
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
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import kalbefamily.crm.kalbe.kalbefamily.BL.clsActivity;
import kalbefamily.crm.kalbe.kalbefamily.Common.clsMediaKontakDetail;
import kalbefamily.crm.kalbe.kalbefamily.Common.clsUserMember;
import kalbefamily.crm.kalbe.kalbefamily.R;
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
    List<clsMediaKontakDetail> dataAll;
    clsMediaKontakDetailRepo repoKontak;
    clsUserMemberRepo repoUserMember = null;

    RelativeLayout layout1, layout2, layout3, layout4, layout5, layout6, layout7, layout8, layout9, layout10, layout11, layout12;
    TextView txtDeskripsi, lttxtMediaID, tvNoTelp, tvSms, tvBBM, tvLine, tvWA, tvEmail, tvTwitter, tvFacebook, tvInstagram, tvFax, tvMMS, tvPath;
    Button btnNoTelp, btnSms, btnBBM, btnLine, btnWA,  btnEmail, btnTwitter, btnFacebook, btnInstagram, btnFax, btnPath, btnMMS;
    Spinner spinnerTelp, spinnerSms, spinnerBBM, spinnerLine, spinnerWA, spinnerEmail, spinnertwitter, spinnerFacebook, spinnerInstagram, spinnerFax, spinnerMMS, spinnerPath;
    Spinner spinner;
    private HashMap<String, String> hashMapTelp = new HashMap<>();
    private HashMap<String, String> hashMapSms = new HashMap<>();
    private HashMap<String, String> hashMapBBM = new HashMap<>();
    private HashMap<String, String> hashMapLine = new HashMap<>();
    private HashMap<String, String> hashMapWA = new HashMap<>();
    private HashMap<String, String> hashMapEmail = new HashMap<>();
    private HashMap<String, String> hashMapTwitter = new HashMap<>();
    private HashMap<String, String> hashMapFacebook = new HashMap<>();
    private HashMap<String, String> hashMapInstagram = new HashMap<>();
    private HashMap<String, String> hashMapFax = new HashMap<>();
    private HashMap<String, String> hashMapPath = new HashMap<>();
    private HashMap<String, String> hashMapMms = new HashMap<>();
    private HashMap<String, String> hashMapTelpPrioritas = new HashMap<>();
    private HashMap<String, String> hashMapSmsPrioritas = new HashMap<>();
    private HashMap<String, String> hashMapBBMPrioritas = new HashMap<>();
    private HashMap<String, String> hashMapLinePrioritas = new HashMap<>();
    private HashMap<String, String> hashMapWAPrioritas = new HashMap<>();
    private HashMap<String, String> hashMapEmailPrioritas = new HashMap<>();
    private HashMap<String, String> hashMapTwitterPrioritas = new HashMap<>();
    private HashMap<String, String> hashMapFacebookPrioritas = new HashMap<>();
    private HashMap<String, String> hashMapInstagramPrioritas = new HashMap<>();
    private HashMap<String, String> hashMapFaxPrioritas = new HashMap<>();
    private HashMap<String, String> hashMapPathPrioritas = new HashMap<>();
    private HashMap<String, String> hashMapMmsPrioritas = new HashMap<>();
    private HashMap<String, String> hashMapTelpAktif = new HashMap<>();
    private HashMap<String, String> hashMapSmsAktif = new HashMap<>();
    private HashMap<String, String> hashMapBBMAktif = new HashMap<>();
    private HashMap<String, String> hashMapLineAktif = new HashMap<>();
    private HashMap<String, String> hashMapWAAktif = new HashMap<>();
    private HashMap<String, String> hashMapEmailAktif = new HashMap<>();
    private HashMap<String, String> hashMapTwitterAktif = new HashMap<>();
    private HashMap<String, String> hashMapFacebookAktif = new HashMap<>();
    private HashMap<String, String> hashMapInstagramAktif = new HashMap<>();
    private HashMap<String, String> hashMapPathAktif = new HashMap<>();
    private HashMap<String, String> hashMapFaxAktif = new HashMap<>();
    private HashMap<String, String> hashMapMmsAktif = new HashMap<>();
    private HashMap<String, String> hashMapIdTelp = new HashMap<>();
    private HashMap<String, String> hashMapIdSms = new HashMap<>();
    private HashMap<String, String> hashMapIdBBM = new HashMap<>();
    private HashMap<String, String> hashMapIdLine = new HashMap<>();
    private HashMap<String, String> hashMapIdWA = new HashMap<>();
    private HashMap<String, String> hashMapIdEmail = new HashMap<>();
    private HashMap<String, String> hashMapIdTwitter = new HashMap<>();
    private HashMap<String, String> hashMapIdFacebook = new HashMap<>();
    private HashMap<String, String> hashMapIdInstagram = new HashMap<>();
    private HashMap<String, String> hashMapIdPath = new HashMap<>();
    private HashMap<String, String> hashMapIdFax = new HashMap<>();
    private HashMap<String, String> hashMapIdMMS = new HashMap<>();

    boolean validate = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_detail_personal_data, container, false);
        context = getActivity().getApplicationContext();

        txtDeskripsi = (TextView) v.findViewById(R.id.txtDeskripsi);
        lttxtMediaID = (TextView) v.findViewById(R.id.lttxtMediaID);
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
        spinnerTelp = (Spinner) v.findViewById(R.id.spinnerTelpon);
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
        btnNoTelp = (Button) v.findViewById(R.id.btnEdit1);
        btnEmail = (Button) v.findViewById(R.id.btnEdit6);
        btnSms = (Button) v.findViewById(R.id.btnEdit2);
        btnBBM = (Button) v.findViewById(R.id.btnEdit3);
        btnLine = (Button) v.findViewById(R.id.btnEdit4);
        btnWA = (Button) v.findViewById(R.id.btnEdit5);
        btnTwitter = (Button) v.findViewById(R.id.btnEdit7);
        btnFacebook = (Button) v.findViewById(R.id.btnEdit8);
        btnInstagram = (Button) v.findViewById(R.id.btnEdit9);
        btnFax = (Button) v.findViewById(R.id.btnEdit10);
        btnPath = (Button) v.findViewById(R.id.btnEdit11);
        btnMMS = (Button) v.findViewById(R.id.btnEdit12);

        layout1.setVisibility(View.GONE);
        layout2.setVisibility(View.GONE);
        layout6.setVisibility(View.GONE);

        repoKontak = new clsMediaKontakDetailRepo(context.getApplicationContext());
        try {
            dataAll = (List<clsMediaKontakDetail>) repoKontak.findAll();
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
                selectOptionTelpon();
            }
        });

        btnSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectOptionSms();
            }
        });

        btnBBM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectOptionBBM();
            }
        });

        btnLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectOptionLine();
            }
        });

        btnWA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectOptionWA();
            }
        });

        btnEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectOptionEmail();
            }
        });

        btnTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectOptionTwitter();
            }
        });

        btnFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectOptionFacebook();
            }
        });

        btnInstagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectOptionInstagram();
            }
        });

        btnFax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectOptionFax();
            }
        });

        btnPath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectOptionPath();
            }
        });

        btnMMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectOptionMMS();
            }
        });

        dataListview();

        return v;
    }

    private void dataListview() {
        repoKontak = new clsMediaKontakDetailRepo(context.getApplicationContext());
        try {
            dataAll = (List<clsMediaKontakDetail>) repoKontak.findAll();
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

        // Spinner click listener
        spinnerTelp.setOnItemSelectedListener(this);
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
                hashMapIdTelp.put(kontakDetail.getTxtDetailMedia(), kontakDetail.getTxtGuiId());
                hashMapTelp.put(kontakDetail.getTxtDetailMedia(), kontakDetail.getTxtKeterangan());
                hashMapTelpAktif.put(kontakDetail.getTxtDetailMedia(), kontakDetail.getLttxtStatusAktif());
                hashMapTelpPrioritas.put(kontakDetail.getTxtDetailMedia(), kontakDetail.getTxtPrioritasKontak());
            }
        } if (dataSms.size() > 0) {
            for (clsMediaKontakDetail kontakDetail : dataSms) {
                noSms.add(kontakDetail.getTxtDetailMedia());
                hashMapIdSms.put(kontakDetail.getTxtDetailMedia(), kontakDetail.getTxtGuiId());
                hashMapSms.put(kontakDetail.getTxtDetailMedia(), kontakDetail.getTxtKeterangan());
                hashMapSmsAktif.put(kontakDetail.getTxtDetailMedia(), kontakDetail.getLttxtStatusAktif());
                hashMapSmsPrioritas.put(kontakDetail.getTxtDetailMedia(), kontakDetail.getTxtPrioritasKontak());
            }
        } if (dataBBM.size() > 0) {
            for (clsMediaKontakDetail kontakDetail : dataBBM) {
                bbm.add(kontakDetail.getTxtDetailMedia());
                hashMapIdBBM.put(kontakDetail.getTxtDetailMedia(), kontakDetail.getTxtGuiId());
                hashMapBBM.put(kontakDetail.getTxtDetailMedia(), kontakDetail.getTxtKeterangan());
                hashMapBBMAktif.put(kontakDetail.getTxtDetailMedia(), kontakDetail.getLttxtStatusAktif());
                hashMapBBMPrioritas.put(kontakDetail.getTxtDetailMedia(), kontakDetail.getTxtPrioritasKontak());
            }
        } if (dataLine.size() > 0) {
            for (clsMediaKontakDetail kontakDetail : dataLine) {
                line.add(kontakDetail.getTxtDetailMedia());
                hashMapIdLine.put(kontakDetail.getTxtDetailMedia(), kontakDetail.getTxtGuiId());
                hashMapLine.put(kontakDetail.getTxtDetailMedia(), kontakDetail.getTxtKeterangan());
                hashMapLineAktif.put(kontakDetail.getTxtDetailMedia(), kontakDetail.getLttxtStatusAktif());
                hashMapLinePrioritas.put(kontakDetail.getTxtDetailMedia(), kontakDetail.getTxtPrioritasKontak());
            }
        } if (dataWA.size() > 0) {
            for (clsMediaKontakDetail kontakDetail : dataWA) {
                wa.add(kontakDetail.getTxtDetailMedia());
                hashMapIdWA.put(kontakDetail.getTxtDetailMedia(), kontakDetail.getTxtGuiId());
                hashMapWA.put(kontakDetail.getTxtDetailMedia(), kontakDetail.getTxtKeterangan());
                hashMapWAAktif.put(kontakDetail.getTxtDetailMedia(), kontakDetail.getLttxtStatusAktif());
                hashMapWAPrioritas.put(kontakDetail.getTxtDetailMedia(), kontakDetail.getTxtPrioritasKontak());
            }
        } if (dataEmail.size() > 0) {
            for (clsMediaKontakDetail kontakDetail : dataEmail) {
                email.add(kontakDetail.getTxtDetailMedia());
                hashMapIdEmail.put(kontakDetail.getTxtDetailMedia(), kontakDetail.getTxtGuiId());
                hashMapEmail.put(kontakDetail.getTxtDetailMedia(), kontakDetail.getTxtKeterangan());
                hashMapEmailAktif.put(kontakDetail.getTxtDetailMedia(), kontakDetail.getLttxtStatusAktif());
                hashMapEmailPrioritas.put(kontakDetail.getTxtDetailMedia(), kontakDetail.getTxtPrioritasKontak());
            }
        } if (dataTwitter.size() > 0) {
            for (clsMediaKontakDetail kontakDetail : dataTwitter) {
                twitter.add(kontakDetail.getTxtDetailMedia());
                hashMapIdTwitter.put(kontakDetail.getTxtDetailMedia(), kontakDetail.getTxtGuiId());
                hashMapTwitter.put(kontakDetail.getTxtDetailMedia(), kontakDetail.getTxtKeterangan());
                hashMapTwitterAktif.put(kontakDetail.getTxtDetailMedia(), kontakDetail.getLttxtStatusAktif());
                hashMapTwitterPrioritas.put(kontakDetail.getTxtDetailMedia(), kontakDetail.getTxtPrioritasKontak());
            }
        } if (dataFacebook.size() > 0) {
            for (clsMediaKontakDetail kontakDetail : dataFacebook) {
                facebook.add(kontakDetail.getTxtDetailMedia());
                hashMapIdFacebook.put(kontakDetail.getTxtDetailMedia(), kontakDetail.getTxtGuiId());
                hashMapFacebook.put(kontakDetail.getTxtDetailMedia(), kontakDetail.getTxtKeterangan());
                hashMapFacebookAktif.put(kontakDetail.getTxtDetailMedia(), kontakDetail.getLttxtStatusAktif());
                hashMapFacebookPrioritas.put(kontakDetail.getTxtDetailMedia(), kontakDetail.getTxtPrioritasKontak());
            }
        } if (dataInstagram.size() > 0) {
            for (clsMediaKontakDetail kontakDetail : dataInstagram) {
                instagram.add(kontakDetail.getTxtDetailMedia());
                hashMapIdInstagram.put(kontakDetail.getTxtDetailMedia(), kontakDetail.getTxtGuiId());
                hashMapInstagram.put(kontakDetail.getTxtDetailMedia(), kontakDetail.getTxtKeterangan());
                hashMapInstagramAktif.put(kontakDetail.getTxtDetailMedia(), kontakDetail.getLttxtStatusAktif());
                hashMapInstagramPrioritas.put(kontakDetail.getTxtDetailMedia(), kontakDetail.getTxtPrioritasKontak());
            }
        } if (dataFax.size() > 0) {
            for (clsMediaKontakDetail kontakDetail : dataFax) {
                fax.add(kontakDetail.getTxtDetailMedia());
                hashMapIdFax.put(kontakDetail.getTxtDetailMedia(), kontakDetail.getTxtGuiId());
                hashMapFax.put(kontakDetail.getTxtDetailMedia(), kontakDetail.getTxtKeterangan());
                hashMapFaxAktif.put(kontakDetail.getTxtDetailMedia(), kontakDetail.getLttxtStatusAktif());
                hashMapFaxPrioritas.put(kontakDetail.getTxtDetailMedia(), kontakDetail.getTxtPrioritasKontak());
            }
        } if (dataPath.size() > 0) {
            for (clsMediaKontakDetail kontakDetail : dataPath) {
                path.add(kontakDetail.getTxtDetailMedia());
                hashMapIdPath.put(kontakDetail.getTxtDetailMedia(), kontakDetail.getTxtGuiId());
                hashMapPath.put(kontakDetail.getTxtDetailMedia(), kontakDetail.getTxtKeterangan());
                hashMapPathAktif.put(kontakDetail.getTxtDetailMedia(), kontakDetail.getLttxtStatusAktif());
                hashMapPathPrioritas.put(kontakDetail.getTxtDetailMedia(), kontakDetail.getTxtPrioritasKontak());
            }
        } if (dataMMS.size() > 0) {
            for (clsMediaKontakDetail kontakDetail : dataMMS) {
                mms.add(kontakDetail.getTxtDetailMedia());
                hashMapIdMMS.put(kontakDetail.getTxtDetailMedia(), kontakDetail.getTxtGuiId());
                hashMapMms.put(kontakDetail.getTxtDetailMedia(), kontakDetail.getTxtKeterangan());
                hashMapMmsAktif.put(kontakDetail.getTxtDetailMedia(), kontakDetail.getLttxtStatusAktif());
                hashMapMmsPrioritas.put(kontakDetail.getTxtDetailMedia(), kontakDetail.getTxtPrioritasKontak());
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

        // attaching data adapter to spinnerTelp
        spinnerTelp.setAdapter(dataAdapter);
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
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String telpon = adapterView.getItemAtPosition(i).toString();
        String sms = adapterView.getItemAtPosition(i).toString();
        String txtDetailMedia = adapterView.getItemAtPosition(i).toString();

        spinner = (Spinner) adapterView;
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

    private void popupTambahTelp() {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        final View promptView = layoutInflater.inflate(R.layout.popup_add_edit_data, null);
        final EditText etKontak, etKeterangan, etPrioritas;
        final CheckBox checkBoxStatus = (CheckBox) promptView.findViewById(R.id.checkboxStatus);

        etKontak =(EditText) promptView.findViewById(R.id.etKontak);
        etKeterangan = (EditText) promptView.findViewById(R.id.etKeterangan);
        etPrioritas = (EditText) promptView.findViewById(R.id.etPrioritas);

        int maxLength = 14;
        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(maxLength);
        etKontak.setFilters(FilterArray);

        etKontak.setInputType(InputType.TYPE_CLASS_PHONE);
        etKontak.setHint("* wajib diisi");
        etPrioritas.setHint("* wajib diisi");

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
                                txtDeskripsi.setText("Telepon");
                                lttxtMediaID.setText("0001");
                                if (etKontak.getText().toString().equals("")){
                                    new clsActivity().showCustomToast(context.getApplicationContext(), "No Telepon Tidak boleh kosong", false);
                                } else if (!isValidMobile(etKontak.getText().toString())) {
                                    new clsActivity().showCustomToast(context.getApplicationContext(), "No Telepon tidak Valid", false);
                                    validate = false;
                                } else if (etPrioritas.getText().toString().equals("")) {
                                    new clsActivity().showCustomToast(context.getApplicationContext(), "Prioritas Tidak boleh kosong", false);
                                } else {
                                    clsMediaKontakDetail dataKontak = new clsMediaKontakDetail();
                                    dataKontak.setTxtGuiId(new clsActivity().GenerateGuid());
                                    dataKontak.setTxtKontakId(dataMember.get(0).txtKontakId);
                                    dataKontak.setLttxtMediaID(lttxtMediaID.getText().toString());
                                    dataKontak.setTxtDeskripsi(txtDeskripsi.getText().toString());
                                    dataKontak.setTxtPrioritasKontak(etPrioritas.getText().toString());
                                    dataKontak.setTxtDetailMedia(etKontak.getText().toString());
                                    dataKontak.setTxtKeterangan(etKeterangan.getText().toString());

                                    if (checkBoxStatus.isChecked()) {
                                        dataKontak.setLttxtStatusAktif("Aktif");
                                    } else {
                                        dataKontak.setLttxtStatusAktif("Tidak Aktif");
                                    }
                                    repoKontak = new clsMediaKontakDetailRepo(context.getApplicationContext());
                                    repoKontak.createOrUpdate(dataKontak);

                                    new clsActivity().showCustomToast(context.getApplicationContext(), "Kontak baru berhasil dibuat", true);
                                    Log.d("Data info", "Kontak baru berhasil di buat");
                                }

                                dialog.dismiss();
                                dataListview();
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

    private void popupTambahSms() {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        final View promptView = layoutInflater.inflate(R.layout.popup_add_edit_data, null);
        final EditText etKontak, etKeterangan, etPrioritas;
        final CheckBox checkBoxStatus = (CheckBox) promptView.findViewById(R.id.checkboxStatus);

        etKontak =(EditText) promptView.findViewById(R.id.etKontak);
        etKeterangan = (EditText) promptView.findViewById(R.id.etKeterangan);
        etPrioritas = (EditText) promptView.findViewById(R.id.etPrioritas);

        int maxLength = 14;
        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(maxLength);
        etKontak.setFilters(FilterArray);

        etKontak.setInputType(InputType.TYPE_CLASS_PHONE);
        etKontak.setHint("* wajib diisi");
        etPrioritas.setHint("* wajib diisi");

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
                                txtDeskripsi.setText("SMS");
                                lttxtMediaID.setText("0004");
                                if (etKontak.getText().toString().equals("")){
                                    new clsActivity().showCustomToast(context.getApplicationContext(), "No Sms Tidak boleh kosong", false);
                                } else if (!isValidMobile(etKontak.getText().toString())) {
                                    new clsActivity().showCustomToast(context.getApplicationContext(), "No SMS tidak Valid", false);
                                    validate = false;
                                } else if (etPrioritas.getText().toString().equals("")) {
                                    new clsActivity().showCustomToast(context.getApplicationContext(), "Prioritas Tidak boleh kosong", false);
                                } else {
                                    clsMediaKontakDetail dataKontak = new clsMediaKontakDetail();
                                    dataKontak.setTxtGuiId(new clsActivity().GenerateGuid());
                                    dataKontak.setTxtKontakId(dataMember.get(0).txtKontakId);
                                    dataKontak.setLttxtMediaID(lttxtMediaID.getText().toString());
                                    dataKontak.setTxtDeskripsi(txtDeskripsi.getText().toString());
                                    dataKontak.setTxtPrioritasKontak(etPrioritas.getText().toString());
                                    dataKontak.setTxtDetailMedia(etKontak.getText().toString());
                                    dataKontak.setTxtKeterangan(etKeterangan.getText().toString());

                                    if (checkBoxStatus.isChecked()) {
                                        dataKontak.setLttxtStatusAktif("Aktif");
                                    } else {
                                        dataKontak.setLttxtStatusAktif("Tidak Aktif");
                                    }
                                    repoKontak = new clsMediaKontakDetailRepo(context.getApplicationContext());
                                    repoKontak.createOrUpdate(dataKontak);

                                    new clsActivity().showCustomToast(context.getApplicationContext(), "Kontak baru berhasil dibuat", true);
                                    Log.d("Data info", "Kontak baru berhasil di buat");
                                }

                                dialog.dismiss();
                                dataListview();
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

    private void popupTambahBBM() {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        final View promptView = layoutInflater.inflate(R.layout.popup_add_edit_data, null);
        final EditText etKontak, etKeterangan, etPrioritas;
        final CheckBox checkBoxStatus = (CheckBox) promptView.findViewById(R.id.checkboxStatus);

        etKontak =(EditText) promptView.findViewById(R.id.etKontak);
        etKeterangan = (EditText) promptView.findViewById(R.id.etKeterangan);
        etPrioritas = (EditText) promptView.findViewById(R.id.etPrioritas);

        int maxLength = 8;
        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(maxLength);
        etKontak.setFilters(FilterArray);

        etKontak.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
        etKontak.setHint("* wajib diisi");
        etPrioritas.setHint("* wajib diisi");

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
                                txtDeskripsi.setText("BLACKBERRY");
                                lttxtMediaID.setText("0497");
                                if (etKontak.getText().toString().equals("")){
                                    new clsActivity().showCustomToast(context.getApplicationContext(), "Pin BBM Tidak boleh kosong", false);
                                } else if (etKontak.getText().toString().length() < 6) {
                                    new clsActivity().showCustomToast(context.getApplicationContext(), "Pin BBM tidak valid", false);
                                } else if (etPrioritas.getText().toString().equals("")) {
                                    new clsActivity().showCustomToast(context.getApplicationContext(), "Prioritas Tidak boleh kosong", false);
                                } else {
                                    clsMediaKontakDetail dataKontak = new clsMediaKontakDetail();
                                    dataKontak.setTxtGuiId(new clsActivity().GenerateGuid());
                                    dataKontak.setTxtKontakId(dataMember.get(0).txtKontakId);
                                    dataKontak.setLttxtMediaID(lttxtMediaID.getText().toString());
                                    dataKontak.setTxtDeskripsi(txtDeskripsi.getText().toString());
                                    dataKontak.setTxtPrioritasKontak(etPrioritas.getText().toString());
                                    dataKontak.setTxtDetailMedia(etKontak.getText().toString().toUpperCase());
                                    dataKontak.setTxtKeterangan(etKeterangan.getText().toString());

                                    if (checkBoxStatus.isChecked()) {
                                        dataKontak.setLttxtStatusAktif("Aktif");
                                    } else {
                                        dataKontak.setLttxtStatusAktif("Tidak Aktif");
                                    }
                                    repoKontak = new clsMediaKontakDetailRepo(context.getApplicationContext());
                                    repoKontak.createOrUpdate(dataKontak);

                                    new clsActivity().showCustomToast(context.getApplicationContext(), "Kontak baru berhasil dibuat", true);
                                    Log.d("Data info", "Kontak baru berhasil di buat");
                                }

                                dialog.dismiss();
                                dataListview();
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

    private void popupTambahLine() {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        final View promptView = layoutInflater.inflate(R.layout.popup_add_edit_data, null);
        final EditText etKontak, etKeterangan, etPrioritas;
        final CheckBox checkBoxStatus = (CheckBox) promptView.findViewById(R.id.checkboxStatus);

        etKontak =(EditText) promptView.findViewById(R.id.etKontak);
        etKeterangan = (EditText) promptView.findViewById(R.id.etKeterangan);
        etPrioritas = (EditText) promptView.findViewById(R.id.etPrioritas);

        int maxLength = 55;
        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(maxLength);
        etKontak.setFilters(FilterArray);

        etKontak.setHint("* wajib diisi");
        etPrioritas.setHint("* wajib diisi");

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
                                txtDeskripsi.setText("LINE");
                                lttxtMediaID.setText("1205");
                                if (etKontak.getText().toString().equals("")){
                                    new clsActivity().showCustomToast(context.getApplicationContext(), "ID Line Tidak boleh kosong", false);
                                } else if (etPrioritas.getText().toString().equals("")) {
                                    new clsActivity().showCustomToast(context.getApplicationContext(), "Prioritas Tidak boleh kosong", false);
                                } else {
                                    clsMediaKontakDetail dataKontak = new clsMediaKontakDetail();
                                    dataKontak.setTxtGuiId(new clsActivity().GenerateGuid());
                                    dataKontak.setTxtKontakId(dataMember.get(0).txtKontakId);
                                    dataKontak.setLttxtMediaID(lttxtMediaID.getText().toString());
                                    dataKontak.setTxtDeskripsi(txtDeskripsi.getText().toString());
                                    dataKontak.setTxtPrioritasKontak(etPrioritas.getText().toString());
                                    dataKontak.setTxtDetailMedia(etKontak.getText().toString().toUpperCase());
                                    dataKontak.setTxtKeterangan(etKeterangan.getText().toString());

                                    if (checkBoxStatus.isChecked()) {
                                        dataKontak.setLttxtStatusAktif("Aktif");
                                    } else {
                                        dataKontak.setLttxtStatusAktif("Tidak Aktif");
                                    }
                                    repoKontak = new clsMediaKontakDetailRepo(context.getApplicationContext());
                                    repoKontak.createOrUpdate(dataKontak);

                                    new clsActivity().showCustomToast(context.getApplicationContext(), "Kontak baru berhasil dibuat", true);
                                    Log.d("Data info", "Kontak baru berhasil di buat");
                                }

                                dialog.dismiss();
                                dataListview();
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

    private void popupTambahWA() {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        final View promptView = layoutInflater.inflate(R.layout.popup_add_edit_data, null);
        final EditText etKontak, etKeterangan, etPrioritas;
        final CheckBox checkBoxStatus = (CheckBox) promptView.findViewById(R.id.checkboxStatus);

        etKontak =(EditText) promptView.findViewById(R.id.etKontak);
        etKeterangan = (EditText) promptView.findViewById(R.id.etKeterangan);
        etPrioritas = (EditText) promptView.findViewById(R.id.etPrioritas);

        int maxLength = 14;
        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(maxLength);
        etKontak.setFilters(FilterArray);

        etKontak.setInputType(InputType.TYPE_CLASS_PHONE);
        etKontak.setHint("* wajib diisi");
        etPrioritas.setHint("* wajib diisi");

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
                                txtDeskripsi.setText("WHATSAPP");
                                lttxtMediaID.setText("1208");
                                if (etKontak.getText().toString().equals("")){
                                    new clsActivity().showCustomToast(context.getApplicationContext(), "No WhatsApp Tidak boleh kosong", false);
                                } else if (!isValidMobile(etKontak.getText().toString())) {
                                    new clsActivity().showCustomToast(context.getApplicationContext(), "No WhatsApp tidak Valid", false);
                                    validate = false;
                                } else if (etPrioritas.getText().toString().equals("")) {
                                    new clsActivity().showCustomToast(context.getApplicationContext(), "Prioritas Tidak boleh kosong", false);
                                } else {
                                    clsMediaKontakDetail dataKontak = new clsMediaKontakDetail();
                                    dataKontak.setTxtGuiId(new clsActivity().GenerateGuid());
                                    dataKontak.setTxtKontakId(dataMember.get(0).txtKontakId);
                                    dataKontak.setLttxtMediaID(lttxtMediaID.getText().toString());
                                    dataKontak.setTxtDeskripsi(txtDeskripsi.getText().toString());
                                    dataKontak.setTxtPrioritasKontak(etPrioritas.getText().toString());
                                    dataKontak.setTxtDetailMedia(etKontak.getText().toString().toUpperCase());
                                    dataKontak.setTxtKeterangan(etKeterangan.getText().toString());

                                    if (checkBoxStatus.isChecked()) {
                                        dataKontak.setLttxtStatusAktif("Aktif");
                                    } else {
                                        dataKontak.setLttxtStatusAktif("Tidak Aktif");
                                    }
                                    repoKontak = new clsMediaKontakDetailRepo(context.getApplicationContext());
                                    repoKontak.createOrUpdate(dataKontak);

                                    new clsActivity().showCustomToast(context.getApplicationContext(), "Kontak baru berhasil dibuat", true);
                                    Log.d("Data info", "Kontak baru berhasil di buat");
                                }

                                dialog.dismiss();
                                dataListview();
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

    private void popupTambahEmail() {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        final View promptView = layoutInflater.inflate(R.layout.popup_add_edit_data, null);
        final EditText etKontak, etKeterangan, etPrioritas;
        final CheckBox checkBoxStatus = (CheckBox) promptView.findViewById(R.id.checkboxStatus);

        etKontak =(EditText) promptView.findViewById(R.id.etKontak);
        etKeterangan = (EditText) promptView.findViewById(R.id.etKeterangan);
        etPrioritas = (EditText) promptView.findViewById(R.id.etPrioritas);

        int maxLength = 55;
        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(maxLength);
        etKontak.setFilters(FilterArray);

        etKontak.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        etKontak.setHint("* wajib diisi");
        etPrioritas.setHint("* wajib diisi");

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
                                txtDeskripsi.setText("Email");
                                lttxtMediaID.setText("0002");
                                if (etKontak.getText().toString().equals("")){
                                    new clsActivity().showCustomToast(context.getApplicationContext(), "Email Tidak boleh kosong", false);
                                } else if (!isValidEmail(etKontak.getText().toString())) {
                                    new clsActivity().showCustomToast(context.getApplicationContext(), "Email tidak valid", false);
                                    validate = false;
                                } else if (etPrioritas.getText().toString().equals("")) {
                                    new clsActivity().showCustomToast(context.getApplicationContext(), "Prioritas Tidak boleh kosong", false);
                                } else {
                                    clsMediaKontakDetail dataKontak = new clsMediaKontakDetail();
                                    dataKontak.setTxtGuiId(new clsActivity().GenerateGuid());
                                    dataKontak.setTxtKontakId(dataMember.get(0).txtKontakId);
                                    dataKontak.setLttxtMediaID(lttxtMediaID.getText().toString());
                                    dataKontak.setTxtDeskripsi(txtDeskripsi.getText().toString());
                                    dataKontak.setTxtPrioritasKontak(etPrioritas.getText().toString());
                                    dataKontak.setTxtDetailMedia(etKontak.getText().toString());
                                    dataKontak.setTxtKeterangan(etKeterangan.getText().toString());

                                    if (checkBoxStatus.isChecked()) {
                                        dataKontak.setLttxtStatusAktif("Aktif");
                                    } else {
                                        dataKontak.setLttxtStatusAktif("Tidak Aktif");
                                    }
                                    repoKontak = new clsMediaKontakDetailRepo(context.getApplicationContext());
                                    repoKontak.createOrUpdate(dataKontak);

                                    new clsActivity().showCustomToast(context.getApplicationContext(), "Kontak baru berhasil dibuat", true);
                                    Log.d("Data info", "Kontak baru berhasil di buat");
                                }

                                dialog.dismiss();
                                dataListview();
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

    private void popupTambahTwitter() {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        final View promptView = layoutInflater.inflate(R.layout.popup_add_edit_data, null);
        final EditText etKontak, etKeterangan, etPrioritas;
        final CheckBox checkBoxStatus = (CheckBox) promptView.findViewById(R.id.checkboxStatus);

        etKontak =(EditText) promptView.findViewById(R.id.etKontak);
        etKeterangan = (EditText) promptView.findViewById(R.id.etKeterangan);
        etPrioritas = (EditText) promptView.findViewById(R.id.etPrioritas);

        int maxLength = 35;
        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(maxLength);
        etKontak.setFilters(FilterArray);

        etKontak.setHint("* wajib diisi");
        etPrioritas.setHint("* wajib diisi");

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
                                txtDeskripsi.setText("TWITTER");
                                lttxtMediaID.setText("0837");
                                if (etKontak.getText().toString().equals("")){
                                    new clsActivity().showCustomToast(context.getApplicationContext(), "Id Twitter Tidak boleh kosong", false);
                                } else if (etPrioritas.getText().toString().equals("")) {
                                    new clsActivity().showCustomToast(context.getApplicationContext(), "Prioritas Tidak boleh kosong", false);
                                } else {
                                    clsMediaKontakDetail dataKontak = new clsMediaKontakDetail();
                                    dataKontak.setTxtGuiId(new clsActivity().GenerateGuid());
                                    dataKontak.setTxtKontakId(dataMember.get(0).txtKontakId);
                                    dataKontak.setLttxtMediaID(lttxtMediaID.getText().toString());
                                    dataKontak.setTxtDeskripsi(txtDeskripsi.getText().toString());
                                    dataKontak.setTxtPrioritasKontak(etPrioritas.getText().toString());
                                    dataKontak.setTxtDetailMedia(etKontak.getText().toString());
                                    dataKontak.setTxtKeterangan(etKeterangan.getText().toString());

                                    if (checkBoxStatus.isChecked()) {
                                        dataKontak.setLttxtStatusAktif("Aktif");
                                    } else {
                                        dataKontak.setLttxtStatusAktif("Tidak Aktif");
                                    }
                                    repoKontak = new clsMediaKontakDetailRepo(context.getApplicationContext());
                                    repoKontak.createOrUpdate(dataKontak);

                                    new clsActivity().showCustomToast(context.getApplicationContext(), "Kontak baru berhasil dibuat", true);
                                    Log.d("Data info", "Kontak baru berhasil di buat");
                                }

                                dialog.dismiss();
                                dataListview();
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

    private void popupTambahFacebook() {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        final View promptView = layoutInflater.inflate(R.layout.popup_add_edit_data, null);
        final EditText etKontak, etKeterangan, etPrioritas;
        final CheckBox checkBoxStatus = (CheckBox) promptView.findViewById(R.id.checkboxStatus);

        etKontak =(EditText) promptView.findViewById(R.id.etKontak);
        etKeterangan = (EditText) promptView.findViewById(R.id.etKeterangan);
        etPrioritas = (EditText) promptView.findViewById(R.id.etPrioritas);

        int maxLength = 35;
        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(maxLength);
        etKontak.setFilters(FilterArray);

        etKontak.setHint("* wajib diisi");
        etPrioritas.setHint("* wajib diisi");

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
                                txtDeskripsi.setText("FACEBOOK");
                                lttxtMediaID.setText("0838");
                                if (etKontak.getText().toString().equals("")){
                                    new clsActivity().showCustomToast(context.getApplicationContext(), "Id Facebook Tidak boleh kosong", false);
                                } else if (etPrioritas.getText().toString().equals("")) {
                                    new clsActivity().showCustomToast(context.getApplicationContext(), "Prioritas Tidak boleh kosong", false);
                                } else {
                                    clsMediaKontakDetail dataKontak = new clsMediaKontakDetail();
                                    dataKontak.setTxtGuiId(new clsActivity().GenerateGuid());
                                    dataKontak.setTxtKontakId(dataMember.get(0).txtKontakId);
                                    dataKontak.setLttxtMediaID(lttxtMediaID.getText().toString());
                                    dataKontak.setTxtDeskripsi(txtDeskripsi.getText().toString());
                                    dataKontak.setTxtPrioritasKontak(etPrioritas.getText().toString());
                                    dataKontak.setTxtDetailMedia(etKontak.getText().toString());
                                    dataKontak.setTxtKeterangan(etKeterangan.getText().toString());

                                    if (checkBoxStatus.isChecked()) {
                                        dataKontak.setLttxtStatusAktif("Aktif");
                                    } else {
                                        dataKontak.setLttxtStatusAktif("Tidak Aktif");
                                    }
                                    repoKontak = new clsMediaKontakDetailRepo(context.getApplicationContext());
                                    repoKontak.createOrUpdate(dataKontak);

                                    new clsActivity().showCustomToast(context.getApplicationContext(), "Kontak baru berhasil dibuat", true);
                                    Log.d("Data info", "Kontak baru berhasil di buat");
                                }

                                dialog.dismiss();
                                dataListview();
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

    private void popupTambahInstagram() {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        final View promptView = layoutInflater.inflate(R.layout.popup_add_edit_data, null);
        final EditText etKontak, etKeterangan, etPrioritas;
        final CheckBox checkBoxStatus = (CheckBox) promptView.findViewById(R.id.checkboxStatus);

        etKontak =(EditText) promptView.findViewById(R.id.etKontak);
        etKeterangan = (EditText) promptView.findViewById(R.id.etKeterangan);
        etPrioritas = (EditText) promptView.findViewById(R.id.etPrioritas);

        int maxLength = 35;
        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(maxLength);
        etKontak.setFilters(FilterArray);

        etKontak.setHint("* wajib diisi");
        etPrioritas.setHint("* wajib diisi");

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
                                txtDeskripsi.setText("INSTAGRAM");
                                lttxtMediaID.setText("1207");
                                if (etKontak.getText().toString().equals("")){
                                    new clsActivity().showCustomToast(context.getApplicationContext(), "Id Instagram Tidak boleh kosong", false);
                                } else if (etPrioritas.getText().toString().equals("")) {
                                    new clsActivity().showCustomToast(context.getApplicationContext(), "Prioritas Tidak boleh kosong", false);
                                } else {
                                    clsMediaKontakDetail dataKontak = new clsMediaKontakDetail();
                                    dataKontak.setTxtGuiId(new clsActivity().GenerateGuid());
                                    dataKontak.setTxtKontakId(dataMember.get(0).txtKontakId);
                                    dataKontak.setLttxtMediaID(lttxtMediaID.getText().toString());
                                    dataKontak.setTxtDeskripsi(txtDeskripsi.getText().toString());
                                    dataKontak.setTxtPrioritasKontak(etPrioritas.getText().toString());
                                    dataKontak.setTxtDetailMedia(etKontak.getText().toString());
                                    dataKontak.setTxtKeterangan(etKeterangan.getText().toString());

                                    if (checkBoxStatus.isChecked()) {
                                        dataKontak.setLttxtStatusAktif("Aktif");
                                    } else {
                                        dataKontak.setLttxtStatusAktif("Tidak Aktif");
                                    }
                                    repoKontak = new clsMediaKontakDetailRepo(context.getApplicationContext());
                                    repoKontak.createOrUpdate(dataKontak);

                                    new clsActivity().showCustomToast(context.getApplicationContext(), "Kontak baru berhasil dibuat", true);
                                    Log.d("Data info", "Kontak baru berhasil di buat");
                                }

                                dialog.dismiss();
                                dataListview();
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

    private void popupTambahFax() {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        final View promptView = layoutInflater.inflate(R.layout.popup_add_edit_data, null);
        final EditText etKontak, etKeterangan, etPrioritas;
        final CheckBox checkBoxStatus = (CheckBox) promptView.findViewById(R.id.checkboxStatus);

        etKontak =(EditText) promptView.findViewById(R.id.etKontak);
        etKeterangan = (EditText) promptView.findViewById(R.id.etKeterangan);
        etPrioritas = (EditText) promptView.findViewById(R.id.etPrioritas);

        int maxLength = 14;
        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(maxLength);
        etKontak.setFilters(FilterArray);

        etKontak.setInputType(InputType.TYPE_CLASS_PHONE);
        etKontak.setHint("* wajib diisi");
        etPrioritas.setHint("* wajib diisi");

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
                                txtDeskripsi.setText("Fax");
                                lttxtMediaID.setText("0003");
                                if (etKontak.getText().toString().equals("")){
                                    new clsActivity().showCustomToast(context.getApplicationContext(), "No Fax Tidak boleh kosong", false);
                                } else if (etPrioritas.getText().toString().equals("")) {
                                    new clsActivity().showCustomToast(context.getApplicationContext(), "Prioritas Tidak boleh kosong", false);
                                } else {
                                    clsMediaKontakDetail dataKontak = new clsMediaKontakDetail();
                                    dataKontak.setTxtGuiId(new clsActivity().GenerateGuid());
                                    dataKontak.setTxtKontakId(dataMember.get(0).txtKontakId);
                                    dataKontak.setLttxtMediaID(lttxtMediaID.getText().toString());
                                    dataKontak.setTxtDeskripsi(txtDeskripsi.getText().toString());
                                    dataKontak.setTxtPrioritasKontak(etPrioritas.getText().toString());
                                    dataKontak.setTxtDetailMedia(etKontak.getText().toString());
                                    dataKontak.setTxtKeterangan(etKeterangan.getText().toString());

                                    if (checkBoxStatus.isChecked()) {
                                        dataKontak.setLttxtStatusAktif("Aktif");
                                    } else {
                                        dataKontak.setLttxtStatusAktif("Tidak Aktif");
                                    }
                                    repoKontak = new clsMediaKontakDetailRepo(context.getApplicationContext());
                                    repoKontak.createOrUpdate(dataKontak);

                                    new clsActivity().showCustomToast(context.getApplicationContext(), "Kontak baru berhasil dibuat", true);
                                    Log.d("Data info", "Kontak baru berhasil di buat");
                                }

                                dialog.dismiss();
                                dataListview();
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

    private void popupTambahPath() {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        final View promptView = layoutInflater.inflate(R.layout.popup_add_edit_data, null);
        final EditText etKontak, etKeterangan, etPrioritas;
        final CheckBox checkBoxStatus = (CheckBox) promptView.findViewById(R.id.checkboxStatus);

        etKontak =(EditText) promptView.findViewById(R.id.etKontak);
        etKeterangan = (EditText) promptView.findViewById(R.id.etKeterangan);
        etPrioritas = (EditText) promptView.findViewById(R.id.etPrioritas);

        int maxLength = 35;
        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(maxLength);
        etKontak.setFilters(FilterArray);

        etKontak.setHint("* wajib diisi");
        etPrioritas.setHint("* wajib diisi");

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
                                txtDeskripsi.setText("PATH");
                                lttxtMediaID.setText("1206");
                                if (etKontak.getText().toString().equals("")){
                                    new clsActivity().showCustomToast(context.getApplicationContext(), "Id Path Tidak boleh kosong", false);
                                } else if (etPrioritas.getText().toString().equals("")) {
                                    new clsActivity().showCustomToast(context.getApplicationContext(), "Prioritas Tidak boleh kosong", false);
                                } else {
                                    clsMediaKontakDetail dataKontak = new clsMediaKontakDetail();
                                    dataKontak.setTxtGuiId(new clsActivity().GenerateGuid());
                                    dataKontak.setTxtKontakId(dataMember.get(0).txtKontakId);
                                    dataKontak.setLttxtMediaID(lttxtMediaID.getText().toString());
                                    dataKontak.setTxtDeskripsi(txtDeskripsi.getText().toString());
                                    dataKontak.setTxtPrioritasKontak(etPrioritas.getText().toString());
                                    dataKontak.setTxtDetailMedia(etKontak.getText().toString());
                                    dataKontak.setTxtKeterangan(etKeterangan.getText().toString());

                                    if (checkBoxStatus.isChecked()) {
                                        dataKontak.setLttxtStatusAktif("Aktif");
                                    } else {
                                        dataKontak.setLttxtStatusAktif("Tidak Aktif");
                                    }
                                    repoKontak = new clsMediaKontakDetailRepo(context.getApplicationContext());
                                    repoKontak.createOrUpdate(dataKontak);

                                    new clsActivity().showCustomToast(context.getApplicationContext(), "Kontak baru berhasil dibuat", true);
                                    Log.d("Data info", "Kontak baru berhasil di buat");
                                }

                                dialog.dismiss();
                                dataListview();
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

    private void popupTambahMMS() {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        final View promptView = layoutInflater.inflate(R.layout.popup_add_edit_data, null);
        final EditText etKontak, etKeterangan, etPrioritas;
        final CheckBox checkBoxStatus = (CheckBox) promptView.findViewById(R.id.checkboxStatus);

        etKontak =(EditText) promptView.findViewById(R.id.etKontak);
        etKeterangan = (EditText) promptView.findViewById(R.id.etKeterangan);
        etPrioritas = (EditText) promptView.findViewById(R.id.etPrioritas);

        int maxLength = 14;
        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(maxLength);
        etKontak.setFilters(FilterArray);

        etKontak.setInputType(InputType.TYPE_CLASS_PHONE);
        etKontak.setHint("* wajib diisi");
        etPrioritas.setHint("* wajib diisi");

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
                                txtDeskripsi.setText("MMS");
                                lttxtMediaID.setText("0006");
                                if (etKontak.getText().toString().equals("")){
                                    new clsActivity().showCustomToast(context.getApplicationContext(), "No MMS Tidak boleh kosong", false);
                                } else if (etPrioritas.getText().toString().equals("")) {
                                    new clsActivity().showCustomToast(context.getApplicationContext(), "Prioritas Tidak boleh kosong", false);
                                } else {
                                    clsMediaKontakDetail dataKontak = new clsMediaKontakDetail();
                                    dataKontak.setTxtGuiId(new clsActivity().GenerateGuid());
                                    dataKontak.setTxtKontakId(dataMember.get(0).txtKontakId);
                                    dataKontak.setLttxtMediaID(lttxtMediaID.getText().toString());
                                    dataKontak.setTxtDeskripsi(txtDeskripsi.getText().toString());
                                    dataKontak.setTxtPrioritasKontak(etPrioritas.getText().toString());
                                    dataKontak.setTxtDetailMedia(etKontak.getText().toString());
                                    dataKontak.setTxtKeterangan(etKeterangan.getText().toString());

                                    if (checkBoxStatus.isChecked()) {
                                        dataKontak.setLttxtStatusAktif("Aktif");
                                    } else {
                                        dataKontak.setLttxtStatusAktif("Tidak Aktif");
                                    }
                                    repoKontak = new clsMediaKontakDetailRepo(context.getApplicationContext());
                                    repoKontak.createOrUpdate(dataKontak);

                                    new clsActivity().showCustomToast(context.getApplicationContext(), "Kontak baru berhasil dibuat", true);
                                    Log.d("Data info", "Kontak baru berhasil di buat");
                                }

                                dialog.dismiss();
                                dataListview();
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

    private void popupEditNoTelp() {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        final View promptView = layoutInflater.inflate(R.layout.popup_add_edit_data, null);
        final EditText etKontak, etKeterangan, etPrioritas;
        final CheckBox checkBoxStatus = (CheckBox) promptView.findViewById(R.id.checkboxStatus);
        final String selectedItem = spinnerTelp.getSelectedItem().toString();

        etKontak =(EditText) promptView.findViewById(R.id.etKontak);
        etKeterangan = (EditText) promptView.findViewById(R.id.etKeterangan);
        etPrioritas = (EditText) promptView.findViewById(R.id.etPrioritas);

        int maxLength = 14;
        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(maxLength);
        etKontak.setFilters(FilterArray);

        etKontak.setInputType(InputType.TYPE_CLASS_PHONE);
        etKontak.setText(tvNoTelp.getText().toString());
        etKeterangan.setText(hashMapTelp.get(selectedItem));
        etPrioritas.setText(hashMapTelpPrioritas.get(selectedItem));

        if (hashMapTelpAktif.get(selectedItem).toString().equals("Aktif")){
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

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this.getContext());
        alertDialogBuilder.setView(promptView);
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                txtDeskripsi.setText("Telepon");
                                lttxtMediaID.setText("0001");
                                if (etKontak.getText().toString().equals("")){
                                    new clsActivity().showCustomToast(context.getApplicationContext(), "No Telepon Tidak boleh kosong", false);
                                } else if (!isValidMobile(etKontak.getText().toString())) {
                                    new clsActivity().showCustomToast(context.getApplicationContext(), "No Telepon tidak Valid", false);
                                    validate = false;
                                } else if (etPrioritas.getText().toString().equals("")) {
                                    new clsActivity().showCustomToast(context.getApplicationContext(), "Prioritas Tidak boleh kosong", false);
                                } else {
                                    clsMediaKontakDetail dataKontak = new clsMediaKontakDetail();
                                    dataKontak.setTxtGuiId(hashMapIdTelp.get(selectedItem).toString());
                                    dataKontak.setTxtKontakId(dataMember.get(0).txtKontakId);
                                    dataKontak.setLttxtMediaID(lttxtMediaID.getText().toString());
                                    dataKontak.setTxtDeskripsi(txtDeskripsi.getText().toString());
                                    dataKontak.setTxtPrioritasKontak(etPrioritas.getText().toString());
                                    dataKontak.setTxtDetailMedia(etKontak.getText().toString());
                                    dataKontak.setTxtKeterangan(etKeterangan.getText().toString());

                                    if (checkBoxStatus.isChecked()) {
                                        dataKontak.setLttxtStatusAktif("Aktif");
                                    } else {
                                        dataKontak.setLttxtStatusAktif("Tidak Aktif");
                                    }
                                    repoKontak = new clsMediaKontakDetailRepo(context.getApplicationContext());
                                    repoKontak.createOrUpdate(dataKontak);

                                    new clsActivity().showCustomToast(context.getApplicationContext(), "Kontak berhasil di perbarui", true);
                                    Log.d("Data info", "Kontak berhasil di perbarui");
                                }

                                dialog.dismiss();
                                dataListview();
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

    private void popupEditSms() {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        final View promptView = layoutInflater.inflate(R.layout.popup_add_edit_data, null);
        final EditText etKontak, etKeterangan, etPrioritas;
        final CheckBox checkBoxStatus = (CheckBox) promptView.findViewById(R.id.checkboxStatus);
        final String selectedItem = spinnerSms.getSelectedItem().toString();

        etKontak =(EditText) promptView.findViewById(R.id.etKontak);
        etKeterangan = (EditText) promptView.findViewById(R.id.etKeterangan);
        etPrioritas = (EditText) promptView.findViewById(R.id.etPrioritas);

        int maxLength = 14;
        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(maxLength);
        etKontak.setFilters(FilterArray);

        etKontak.setInputType(InputType.TYPE_CLASS_PHONE);
        etKontak.setText(tvSms.getText().toString());
        etKeterangan.setText(hashMapSms.get(selectedItem));
        etPrioritas.setText(hashMapSmsPrioritas.get(selectedItem));
        if (hashMapSmsAktif.get(selectedItem).toString().equals("Aktif")){
            checkBoxStatus.setChecked(true);
        } else {
            checkBoxStatus.setChecked(false);
            checkBoxStatus.setText("Tidak AKtif");
        }
//        etKeterangan.setText(hashMapSms.get(selectedItem));
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
                                txtDeskripsi.setText("SMS");
                                lttxtMediaID.setText("0004");
                                if (etKontak.getText().toString().equals("")){
                                    new clsActivity().showCustomToast(context.getApplicationContext(), "No Sms Tidak boleh kosong", false);
                                } else if (!isValidMobile(etKontak.getText().toString())) {
                                    new clsActivity().showCustomToast(context.getApplicationContext(), "No SMS tidak Valid", false);
                                    validate = false;
                                } else if (etPrioritas.getText().toString().equals("")) {
                                    new clsActivity().showCustomToast(context.getApplicationContext(), "Prioritas Tidak boleh kosong", false);
                                } else {
                                    clsMediaKontakDetail dataKontak = new clsMediaKontakDetail();
                                    dataKontak.setTxtGuiId(hashMapIdSms.get(selectedItem).toString());
                                    dataKontak.setTxtKontakId(dataMember.get(0).txtKontakId);
                                    dataKontak.setLttxtMediaID(lttxtMediaID.getText().toString());
                                    dataKontak.setTxtDeskripsi(txtDeskripsi.getText().toString());
                                    dataKontak.setTxtPrioritasKontak(etPrioritas.getText().toString());
                                    dataKontak.setTxtDetailMedia(etKontak.getText().toString());
                                    dataKontak.setTxtKeterangan(etKeterangan.getText().toString());

                                    if (checkBoxStatus.isChecked()) {
                                        dataKontak.setLttxtStatusAktif("Aktif");
                                    } else {
                                        dataKontak.setLttxtStatusAktif("Tidak Aktif");
                                    }
                                    repoKontak = new clsMediaKontakDetailRepo(context.getApplicationContext());
                                    repoKontak.createOrUpdate(dataKontak);

                                    new clsActivity().showCustomToast(context.getApplicationContext(), "Kontak berhasil di perbarui", true);
                                    Log.d("Data info", "Kontak berhasil di perbarui");
                                }

                                dialog.dismiss();
                                dataListview();
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

    private void popupEditBBM() {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        final View promptView = layoutInflater.inflate(R.layout.popup_add_edit_data, null);
        final EditText etKontak, etKeterangan, etPrioritas;
        final CheckBox checkBoxStatus = (CheckBox) promptView.findViewById(R.id.checkboxStatus);
        final String selectedItem = spinnerBBM.getSelectedItem().toString();

        etKontak =(EditText) promptView.findViewById(R.id.etKontak);
        etKeterangan = (EditText) promptView.findViewById(R.id.etKeterangan);
        etPrioritas = (EditText) promptView.findViewById(R.id.etPrioritas);

        int maxLength = 8;
        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(maxLength);
        etKontak.setFilters(FilterArray);

        etKontak.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
        etKontak.setText(tvBBM.getText().toString());
        etKeterangan.setText(hashMapBBM.get(selectedItem));
        etPrioritas.setText(hashMapBBMPrioritas.get(selectedItem));
        if (hashMapBBMAktif.get(selectedItem).toString().equals("Aktif")){
            checkBoxStatus.setChecked(true);
        } else {
            checkBoxStatus.setChecked(false);
            checkBoxStatus.setText("Tidak AKtif");
        }
//        etKeterangan.setText(hashMapSms.get(selectedItem));
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
                                txtDeskripsi.setText("BLACKBERRY");
                                lttxtMediaID.setText("0497");
                                if (etKontak.getText().toString().equals("")){
                                    new clsActivity().showCustomToast(context.getApplicationContext(), "Pin BBM Tidak boleh kosong", false);
                                } else if (etKontak.getText().toString().length() < 6) {
                                    new clsActivity().showCustomToast(context.getApplicationContext(), "Pin BBM tidak valid", false);
                                } else if (etPrioritas.getText().toString().equals("")) {
                                    new clsActivity().showCustomToast(context.getApplicationContext(), "Prioritas Tidak boleh kosong", false);
                                } else {
                                    clsMediaKontakDetail dataKontak = new clsMediaKontakDetail();
                                    dataKontak.setTxtGuiId(hashMapIdBBM.get(selectedItem).toString());
                                    dataKontak.setTxtKontakId(dataMember.get(0).txtKontakId);
                                    dataKontak.setLttxtMediaID(lttxtMediaID.getText().toString());
                                    dataKontak.setTxtDeskripsi(txtDeskripsi.getText().toString());
                                    dataKontak.setTxtPrioritasKontak(etPrioritas.getText().toString());
                                    dataKontak.setTxtDetailMedia(etKontak.getText().toString().toUpperCase());
                                    dataKontak.setTxtKeterangan(etKeterangan.getText().toString());

                                    if (checkBoxStatus.isChecked()) {
                                        dataKontak.setLttxtStatusAktif("Aktif");
                                    } else {
                                        dataKontak.setLttxtStatusAktif("Tidak Aktif");
                                    }
                                    repoKontak = new clsMediaKontakDetailRepo(context.getApplicationContext());
                                    repoKontak.createOrUpdate(dataKontak);

                                    new clsActivity().showCustomToast(context.getApplicationContext(), "Kontak berhasil di perbarui", true);
                                    Log.d("Data info", "Kontak berhasil di perbarui");
                                }

                                dialog.dismiss();
                                dataListview();
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

    private void popupEditLine() {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        final View promptView = layoutInflater.inflate(R.layout.popup_add_edit_data, null);
        final EditText etKontak, etKeterangan, etPrioritas;
        final CheckBox checkBoxStatus = (CheckBox) promptView.findViewById(R.id.checkboxStatus);
        final String selectedItem = spinnerLine.getSelectedItem().toString();

        etKontak =(EditText) promptView.findViewById(R.id.etKontak);
        etKeterangan = (EditText) promptView.findViewById(R.id.etKeterangan);
        etPrioritas = (EditText) promptView.findViewById(R.id.etPrioritas);

        int maxLength = 55;
        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(maxLength);
        etKontak.setFilters(FilterArray);

        etKontak.setText(tvLine.getText().toString());
        etKeterangan.setText(hashMapLine.get(selectedItem));
        etPrioritas.setText(hashMapLinePrioritas.get(selectedItem));
        if (hashMapLineAktif.get(selectedItem).toString().equals("Aktif")){
            checkBoxStatus.setChecked(true);
        } else {
            checkBoxStatus.setChecked(false);
            checkBoxStatus.setText("Tidak AKtif");
        }
//        etKeterangan.setText(hashMapSms.get(selectedItem));
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
                                txtDeskripsi.setText("LINE");
                                lttxtMediaID.setText("1205");
                                if (etKontak.getText().toString().equals("")){
                                    new clsActivity().showCustomToast(context.getApplicationContext(), "ID Line Tidak boleh kosong", false);
                                } else if (etPrioritas.getText().toString().equals("")) {
                                    new clsActivity().showCustomToast(context.getApplicationContext(), "Prioritas Tidak boleh kosong", false);
                                } else {
                                    clsMediaKontakDetail dataKontak = new clsMediaKontakDetail();
                                    dataKontak.setTxtGuiId(hashMapIdLine.get(selectedItem).toString());
                                    dataKontak.setTxtKontakId(dataMember.get(0).txtKontakId);
                                    dataKontak.setLttxtMediaID(lttxtMediaID.getText().toString());
                                    dataKontak.setTxtDeskripsi(txtDeskripsi.getText().toString());
                                    dataKontak.setTxtPrioritasKontak(etPrioritas.getText().toString());
                                    dataKontak.setTxtDetailMedia(etKontak.getText().toString().toUpperCase());
                                    dataKontak.setTxtKeterangan(etKeterangan.getText().toString());

                                    if (checkBoxStatus.isChecked()) {
                                        dataKontak.setLttxtStatusAktif("Aktif");
                                    } else {
                                        dataKontak.setLttxtStatusAktif("Tidak Aktif");
                                    }
                                    repoKontak = new clsMediaKontakDetailRepo(context.getApplicationContext());
                                    repoKontak.createOrUpdate(dataKontak);

                                    new clsActivity().showCustomToast(context.getApplicationContext(), "Kontak berhasil di perbarui", true);
                                    Log.d("Data info", "Kontak berhasil di perbarui");
                                }

                                dialog.dismiss();
                                dataListview();
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

    private void popupEditWA() {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        final View promptView = layoutInflater.inflate(R.layout.popup_add_edit_data, null);
        final EditText etKontak, etKeterangan, etPrioritas;
        final CheckBox checkBoxStatus = (CheckBox) promptView.findViewById(R.id.checkboxStatus);
        final String selectedItem = spinnerWA.getSelectedItem().toString();

        etKontak =(EditText) promptView.findViewById(R.id.etKontak);
        etKeterangan = (EditText) promptView.findViewById(R.id.etKeterangan);
        etPrioritas = (EditText) promptView.findViewById(R.id.etPrioritas);

        int maxLength = 14;
        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(maxLength);
        etKontak.setFilters(FilterArray);

        etKontak.setInputType(InputType.TYPE_CLASS_PHONE);
        etKontak.setText(tvWA.getText().toString());
        etKeterangan.setText(hashMapWA.get(selectedItem));
        etPrioritas.setText(hashMapWAPrioritas.get(selectedItem));
        if (hashMapWAAktif.get(selectedItem).toString().equals("Aktif")){
            checkBoxStatus.setChecked(true);
        } else {
            checkBoxStatus.setChecked(false);
            checkBoxStatus.setText("Tidak AKtif");
        }
//        etKeterangan.setText(hashMapSms.get(selectedItem));
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
                                txtDeskripsi.setText("WHATSAPP");
                                lttxtMediaID.setText("1208");
                                if (etKontak.getText().toString().equals("")){
                                    new clsActivity().showCustomToast(context.getApplicationContext(), "No WhatsApp Tidak boleh kosong", false);
                                } else if (!isValidMobile(etKontak.getText().toString())) {
                                    new clsActivity().showCustomToast(context.getApplicationContext(), "No WhatsApp tidak Valid", false);
                                    validate = false;
                                } else if (etPrioritas.getText().toString().equals("")) {
                                    new clsActivity().showCustomToast(context.getApplicationContext(), "Prioritas Tidak boleh kosong", false);
                                } else {
                                    clsMediaKontakDetail dataKontak = new clsMediaKontakDetail();
                                    dataKontak.setTxtGuiId(hashMapIdWA.get(selectedItem).toString());
                                    dataKontak.setTxtKontakId(dataMember.get(0).txtKontakId);
                                    dataKontak.setLttxtMediaID(lttxtMediaID.getText().toString());
                                    dataKontak.setTxtDeskripsi(txtDeskripsi.getText().toString());
                                    dataKontak.setTxtPrioritasKontak(etPrioritas.getText().toString());
                                    dataKontak.setTxtDetailMedia(etKontak.getText().toString().toUpperCase());
                                    dataKontak.setTxtKeterangan(etKeterangan.getText().toString());

                                    if (checkBoxStatus.isChecked()) {
                                        dataKontak.setLttxtStatusAktif("Aktif");
                                    } else {
                                        dataKontak.setLttxtStatusAktif("Tidak Aktif");
                                    }
                                    repoKontak = new clsMediaKontakDetailRepo(context.getApplicationContext());
                                    repoKontak.createOrUpdate(dataKontak);

                                    new clsActivity().showCustomToast(context.getApplicationContext(), "Kontak berhasil di perbarui", true);
                                    Log.d("Data info", "Kontak berhasil di perbarui");
                                }

                                dialog.dismiss();
                                dataListview();
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

    private void popupEditEmail() {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        final View promptView = layoutInflater.inflate(R.layout.popup_add_edit_data, null);
        final EditText etKontak, etKeterangan, etPrioritas;
        final CheckBox checkBoxStatus = (CheckBox) promptView.findViewById(R.id.checkboxStatus);
        final String selectedItem = spinnerEmail.getSelectedItem().toString();

        etKontak =(EditText) promptView.findViewById(R.id.etKontak);
        etKeterangan = (EditText) promptView.findViewById(R.id.etKeterangan);
        etPrioritas = (EditText) promptView.findViewById(R.id.etPrioritas);

        int maxLength = 55;
        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(maxLength);
        etKontak.setFilters(FilterArray);

        etKontak.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        etKontak.setText(tvEmail.getText().toString());
        etKeterangan.setText(hashMapEmail.get(selectedItem));
        etPrioritas.setText(hashMapEmailPrioritas.get(selectedItem));
        if (hashMapEmailAktif.get(selectedItem).toString().equals("Aktif")){
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

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this.getContext());
        alertDialogBuilder.setView(promptView);
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                txtDeskripsi.setText("Email");
                                lttxtMediaID.setText("0002");
                                if (etKontak.getText().toString().equals("")){
                                    new clsActivity().showCustomToast(context.getApplicationContext(), "Email Tidak boleh kosong", false);
                                } else if (!isValidEmail(etKontak.getText().toString())) {
                                    new clsActivity().showCustomToast(context.getApplicationContext(), "Email tidak valid", false);
                                    validate = false;
                                } else if (etPrioritas.getText().toString().equals("")) {
                                    new clsActivity().showCustomToast(context.getApplicationContext(), "Prioritas Tidak boleh kosong", false);
                                } else {
                                    clsMediaKontakDetail dataKontak = new clsMediaKontakDetail();
                                    dataKontak.setTxtGuiId(hashMapIdEmail.get(selectedItem).toString());
                                    dataKontak.setTxtKontakId(dataMember.get(0).txtKontakId);
                                    dataKontak.setLttxtMediaID(lttxtMediaID.getText().toString());
                                    dataKontak.setTxtDeskripsi(txtDeskripsi.getText().toString());
                                    dataKontak.setTxtPrioritasKontak(etPrioritas.getText().toString());
                                    dataKontak.setTxtDetailMedia(etKontak.getText().toString());
                                    dataKontak.setTxtKeterangan(etKeterangan.getText().toString());

                                    if (checkBoxStatus.isChecked()) {
                                        dataKontak.setLttxtStatusAktif("Aktif");
                                    } else {
                                        dataKontak.setLttxtStatusAktif("Tidak Aktif");
                                    }
                                    repoKontak = new clsMediaKontakDetailRepo(context.getApplicationContext());
                                    repoKontak.createOrUpdate(dataKontak);

                                    new clsActivity().showCustomToast(context.getApplicationContext(), "Kontak berhasil di perbarui", true);
                                    Log.d("Data info", "Kontak berhasil di perbarui");
                                }

                                dialog.dismiss();
                                dataListview();
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

    private void popupEditTwitter() {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        final View promptView = layoutInflater.inflate(R.layout.popup_add_edit_data, null);
        final EditText etKontak, etKeterangan, etPrioritas;
        final CheckBox checkBoxStatus = (CheckBox) promptView.findViewById(R.id.checkboxStatus);
        final String selectedItem = spinnertwitter.getSelectedItem().toString();

        etKontak =(EditText) promptView.findViewById(R.id.etKontak);
        etKeterangan = (EditText) promptView.findViewById(R.id.etKeterangan);
        etPrioritas = (EditText) promptView.findViewById(R.id.etPrioritas);

        int maxLength = 35;
        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(maxLength);
        etKontak.setFilters(FilterArray);

        etKontak.setText(tvTwitter.getText().toString());
        etKeterangan.setText(hashMapTwitter.get(selectedItem));
        etPrioritas.setText(hashMapTwitterPrioritas.get(selectedItem));
        if (hashMapTwitterAktif.get(selectedItem).toString().equals("Aktif")){
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

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this.getContext());
        alertDialogBuilder.setView(promptView);
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                txtDeskripsi.setText("TWITTER");
                                lttxtMediaID.setText("0837");
                                if (etKontak.getText().toString().equals("")){
                                    new clsActivity().showCustomToast(context.getApplicationContext(), "Id Twitter Tidak boleh kosong", false);
                                } else if (etPrioritas.getText().toString().equals("")) {
                                    new clsActivity().showCustomToast(context.getApplicationContext(), "Prioritas Tidak boleh kosong", false);
                                } else {
                                    clsMediaKontakDetail dataKontak = new clsMediaKontakDetail();
                                    dataKontak.setTxtGuiId(hashMapIdTwitter.get(selectedItem).toString());
                                    dataKontak.setTxtKontakId(dataMember.get(0).txtKontakId);
                                    dataKontak.setLttxtMediaID(lttxtMediaID.getText().toString());
                                    dataKontak.setTxtDeskripsi(txtDeskripsi.getText().toString());
                                    dataKontak.setTxtPrioritasKontak(etPrioritas.getText().toString());
                                    dataKontak.setTxtDetailMedia(etKontak.getText().toString());
                                    dataKontak.setTxtKeterangan(etKeterangan.getText().toString());

                                    if (checkBoxStatus.isChecked()) {
                                        dataKontak.setLttxtStatusAktif("Aktif");
                                    } else {
                                        dataKontak.setLttxtStatusAktif("Tidak Aktif");
                                    }
                                    repoKontak = new clsMediaKontakDetailRepo(context.getApplicationContext());
                                    repoKontak.createOrUpdate(dataKontak);

                                    new clsActivity().showCustomToast(context.getApplicationContext(), "Kontak berhasil di perbarui", true);
                                    Log.d("Data info", "Kontak berhasil di perbarui");
                                }

                                dialog.dismiss();
                                dataListview();
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

    private void popupEditFacebook() {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        final View promptView = layoutInflater.inflate(R.layout.popup_add_edit_data, null);
        final EditText etKontak, etKeterangan, etPrioritas;
        final CheckBox checkBoxStatus = (CheckBox) promptView.findViewById(R.id.checkboxStatus);
        final String selectedItem = spinnerFacebook.getSelectedItem().toString();

        etKontak =(EditText) promptView.findViewById(R.id.etKontak);
        etKeterangan = (EditText) promptView.findViewById(R.id.etKeterangan);
        etPrioritas = (EditText) promptView.findViewById(R.id.etPrioritas);

        int maxLength = 35;
        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(maxLength);
        etKontak.setFilters(FilterArray);

        etKontak.setText(tvFacebook.getText().toString());
        etKeterangan.setText(hashMapFacebook.get(selectedItem));
        etPrioritas.setText(hashMapFacebookPrioritas.get(selectedItem));
        if (hashMapFacebookAktif.get(selectedItem).toString().equals("Aktif")){
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

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this.getContext());
        alertDialogBuilder.setView(promptView);
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                txtDeskripsi.setText("FACEBOOK");
                                lttxtMediaID.setText("0838");
                                if (etKontak.getText().toString().equals("")){
                                    new clsActivity().showCustomToast(context.getApplicationContext(), "Id Facebook Tidak boleh kosong", false);
                                } else if (etPrioritas.getText().toString().equals("")) {
                                    new clsActivity().showCustomToast(context.getApplicationContext(), "Prioritas Tidak boleh kosong", false);
                                } else {
                                    clsMediaKontakDetail dataKontak = new clsMediaKontakDetail();
                                    dataKontak.setTxtGuiId(hashMapIdFacebook.get(selectedItem).toString());
                                    dataKontak.setTxtKontakId(dataMember.get(0).txtKontakId);
                                    dataKontak.setLttxtMediaID(lttxtMediaID.getText().toString());
                                    dataKontak.setTxtDeskripsi(txtDeskripsi.getText().toString());
                                    dataKontak.setTxtPrioritasKontak(etPrioritas.getText().toString());
                                    dataKontak.setTxtDetailMedia(etKontak.getText().toString());
                                    dataKontak.setTxtKeterangan(etKeterangan.getText().toString());

                                    if (checkBoxStatus.isChecked()) {
                                        dataKontak.setLttxtStatusAktif("Aktif");
                                    } else {
                                        dataKontak.setLttxtStatusAktif("Tidak Aktif");
                                    }
                                    repoKontak = new clsMediaKontakDetailRepo(context.getApplicationContext());
                                    repoKontak.createOrUpdate(dataKontak);

                                    new clsActivity().showCustomToast(context.getApplicationContext(), "Kontak berhasil di perbarui", true);
                                    Log.d("Data info", "Kontak berhasil di perbarui");
                                }

                                dialog.dismiss();
                                dataListview();
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

    private void popupEditInstagram() {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        final View promptView = layoutInflater.inflate(R.layout.popup_add_edit_data, null);
        final EditText etKontak, etKeterangan, etPrioritas;
        final CheckBox checkBoxStatus = (CheckBox) promptView.findViewById(R.id.checkboxStatus);
        final String selectedItem = spinnerInstagram.getSelectedItem().toString();

        etKontak =(EditText) promptView.findViewById(R.id.etKontak);
        etKeterangan = (EditText) promptView.findViewById(R.id.etKeterangan);
        etPrioritas = (EditText) promptView.findViewById(R.id.etPrioritas);

        int maxLength = 35;
        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(maxLength);
        etKontak.setFilters(FilterArray);

        etKontak.setText(tvInstagram.getText().toString());
        etKeterangan.setText(hashMapInstagram.get(selectedItem));
        etPrioritas.setText(hashMapInstagramPrioritas.get(selectedItem));
        if (hashMapInstagramAktif.get(selectedItem).toString().equals("Aktif")){
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

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this.getContext());
        alertDialogBuilder.setView(promptView);
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                txtDeskripsi.setText("INSTAGRAM");
                                lttxtMediaID.setText("1207");
                                if (etKontak.getText().toString().equals("")){
                                    new clsActivity().showCustomToast(context.getApplicationContext(), "Id Instagram Tidak boleh kosong", false);
                                } else if (etPrioritas.getText().toString().equals("")) {
                                    new clsActivity().showCustomToast(context.getApplicationContext(), "Prioritas Tidak boleh kosong", false);
                                } else {
                                    clsMediaKontakDetail dataKontak = new clsMediaKontakDetail();
                                    dataKontak.setTxtGuiId(hashMapIdInstagram.get(selectedItem).toString());
                                    dataKontak.setTxtKontakId(dataMember.get(0).txtKontakId);
                                    dataKontak.setLttxtMediaID(lttxtMediaID.getText().toString());
                                    dataKontak.setTxtDeskripsi(txtDeskripsi.getText().toString());
                                    dataKontak.setTxtPrioritasKontak(etPrioritas.getText().toString());
                                    dataKontak.setTxtDetailMedia(etKontak.getText().toString());
                                    dataKontak.setTxtKeterangan(etKeterangan.getText().toString());

                                    if (checkBoxStatus.isChecked()) {
                                        dataKontak.setLttxtStatusAktif("Aktif");
                                    } else {
                                        dataKontak.setLttxtStatusAktif("Tidak Aktif");
                                    }
                                    repoKontak = new clsMediaKontakDetailRepo(context.getApplicationContext());
                                    repoKontak.createOrUpdate(dataKontak);

                                    new clsActivity().showCustomToast(context.getApplicationContext(), "Kontak berhasil di perbarui", true);
                                    Log.d("Data info", "Kontak berhasil di perbarui");
                                }

                                dialog.dismiss();
                                dataListview();
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

    private void popupEditFax() {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        final View promptView = layoutInflater.inflate(R.layout.popup_add_edit_data, null);
        final EditText etKontak, etKeterangan, etPrioritas;
        final CheckBox checkBoxStatus = (CheckBox) promptView.findViewById(R.id.checkboxStatus);
        final String selectedItem = spinnerFax.getSelectedItem().toString();

        etKontak =(EditText) promptView.findViewById(R.id.etKontak);
        etKeterangan = (EditText) promptView.findViewById(R.id.etKeterangan);
        etPrioritas = (EditText) promptView.findViewById(R.id.etPrioritas);

        int maxLength = 14;
        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(maxLength);
        etKontak.setFilters(FilterArray);

        etKontak.setInputType(InputType.TYPE_CLASS_PHONE);
        etKontak.setText(tvFax.getText().toString());
        etKeterangan.setText(hashMapFax.get(selectedItem));
        etPrioritas.setText(hashMapFaxPrioritas.get(selectedItem));
        if (hashMapFaxAktif.get(selectedItem).toString().equals("Aktif")){
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

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this.getContext());
        alertDialogBuilder.setView(promptView);
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                txtDeskripsi.setText("Fax");
                                lttxtMediaID.setText("0003");
                                if (etKontak.getText().toString().equals("")){
                                    new clsActivity().showCustomToast(context.getApplicationContext(), "No Fax Tidak boleh kosong", false);
                                } else if (etPrioritas.getText().toString().equals("")) {
                                    new clsActivity().showCustomToast(context.getApplicationContext(), "Prioritas Tidak boleh kosong", false);
                                } else {
                                    clsMediaKontakDetail dataKontak = new clsMediaKontakDetail();
                                    dataKontak.setTxtGuiId(hashMapIdFax.get(selectedItem).toString());
                                    dataKontak.setTxtKontakId(dataMember.get(0).txtKontakId);
                                    dataKontak.setLttxtMediaID(lttxtMediaID.getText().toString());
                                    dataKontak.setTxtDeskripsi(txtDeskripsi.getText().toString());
                                    dataKontak.setTxtPrioritasKontak(etPrioritas.getText().toString());
                                    dataKontak.setTxtDetailMedia(etKontak.getText().toString());
                                    dataKontak.setTxtKeterangan(etKeterangan.getText().toString());

                                    if (checkBoxStatus.isChecked()) {
                                        dataKontak.setLttxtStatusAktif("Aktif");
                                    } else {
                                        dataKontak.setLttxtStatusAktif("Tidak Aktif");
                                    }
                                    repoKontak = new clsMediaKontakDetailRepo(context.getApplicationContext());
                                    repoKontak.createOrUpdate(dataKontak);

                                    new clsActivity().showCustomToast(context.getApplicationContext(), "Kontak berhasil di perbarui", true);
                                    Log.d("Data info", "Kontak berhasil di perbarui");
                                }

                                dialog.dismiss();
                                dataListview();
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

    private void popupEditPath() {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        final View promptView = layoutInflater.inflate(R.layout.popup_add_edit_data, null);
        final EditText etKontak, etKeterangan, etPrioritas;
        final CheckBox checkBoxStatus = (CheckBox) promptView.findViewById(R.id.checkboxStatus);
        final String selectedItem = spinnerPath.getSelectedItem().toString();

        etKontak =(EditText) promptView.findViewById(R.id.etKontak);
        etKeterangan = (EditText) promptView.findViewById(R.id.etKeterangan);
        etPrioritas = (EditText) promptView.findViewById(R.id.etPrioritas);

        int maxLength = 35;
        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(maxLength);
        etKontak.setFilters(FilterArray);

        etKontak.setText(tvPath.getText().toString());
        etKeterangan.setText(hashMapPath.get(selectedItem));
        etPrioritas.setText(hashMapPathPrioritas.get(selectedItem));
        if (hashMapPathAktif.get(selectedItem).toString().equals("Aktif")){
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

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this.getContext());
        alertDialogBuilder.setView(promptView);
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                txtDeskripsi.setText("PATH");
                                lttxtMediaID.setText("1206");
                                if (etKontak.getText().toString().equals("")){
                                    new clsActivity().showCustomToast(context.getApplicationContext(), "Id Path Tidak boleh kosong", false);
                                } else if (etPrioritas.getText().toString().equals("")) {
                                    new clsActivity().showCustomToast(context.getApplicationContext(), "Prioritas Tidak boleh kosong", false);
                                } else {
                                    clsMediaKontakDetail dataKontak = new clsMediaKontakDetail();
                                    dataKontak.setTxtGuiId(hashMapIdPath.get(selectedItem).toString());
                                    dataKontak.setTxtKontakId(dataMember.get(0).txtKontakId);
                                    dataKontak.setLttxtMediaID(lttxtMediaID.getText().toString());
                                    dataKontak.setTxtDeskripsi(txtDeskripsi.getText().toString());
                                    dataKontak.setTxtPrioritasKontak(etPrioritas.getText().toString());
                                    dataKontak.setTxtDetailMedia(etKontak.getText().toString());
                                    dataKontak.setTxtKeterangan(etKeterangan.getText().toString());

                                    if (checkBoxStatus.isChecked()) {
                                        dataKontak.setLttxtStatusAktif("Aktif");
                                    } else {
                                        dataKontak.setLttxtStatusAktif("Tidak Aktif");
                                    }
                                    repoKontak = new clsMediaKontakDetailRepo(context.getApplicationContext());
                                    repoKontak.createOrUpdate(dataKontak);

                                    new clsActivity().showCustomToast(context.getApplicationContext(), "Kontak berhasil di perbarui", true);
                                    Log.d("Data info", "Kontak berhasil di perbarui");
                                }

                                dialog.dismiss();
                                dataListview();
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

    private void popupEditMMS() {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        final View promptView = layoutInflater.inflate(R.layout.popup_add_edit_data, null);
        final EditText etKontak, etKeterangan, etPrioritas;
        final CheckBox checkBoxStatus = (CheckBox) promptView.findViewById(R.id.checkboxStatus);
        final String selectedItem = spinnerMMS.getSelectedItem().toString();

        etKontak =(EditText) promptView.findViewById(R.id.etKontak);
        etKeterangan = (EditText) promptView.findViewById(R.id.etKeterangan);
        etPrioritas = (EditText) promptView.findViewById(R.id.etPrioritas);

        int maxLength = 14;
        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(maxLength);
        etKontak.setFilters(FilterArray);

        etKontak.setInputType(InputType.TYPE_CLASS_PHONE);
        etKontak.setText(tvMMS.getText().toString());
        etKeterangan.setText(hashMapMms.get(selectedItem));
        etPrioritas.setText(hashMapMmsPrioritas.get(selectedItem));
        if (hashMapMmsAktif.get(selectedItem).toString().equals("Aktif")){
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

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this.getContext());
        alertDialogBuilder.setView(promptView);
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                txtDeskripsi.setText("MMS");
                                lttxtMediaID.setText("0006");
                                if (etKontak.getText().toString().equals("")){
                                    new clsActivity().showCustomToast(context.getApplicationContext(), "No MMS Tidak boleh kosong", false);
                                } else if (etPrioritas.getText().toString().equals("")) {
                                    new clsActivity().showCustomToast(context.getApplicationContext(), "Prioritas Tidak boleh kosong", false);
                                } else {
                                    clsMediaKontakDetail dataKontak = new clsMediaKontakDetail();
                                    dataKontak.setTxtGuiId(hashMapIdMMS.get(selectedItem).toString());
                                    dataKontak.setTxtKontakId(dataMember.get(0).txtKontakId);
                                    dataKontak.setLttxtMediaID(lttxtMediaID.getText().toString());
                                    dataKontak.setTxtDeskripsi(txtDeskripsi.getText().toString());
                                    dataKontak.setTxtPrioritasKontak(etPrioritas.getText().toString());
                                    dataKontak.setTxtDetailMedia(etKontak.getText().toString());
                                    dataKontak.setTxtKeterangan(etKeterangan.getText().toString());

                                    if (checkBoxStatus.isChecked()) {
                                        dataKontak.setLttxtStatusAktif("Aktif");
                                    } else {
                                        dataKontak.setLttxtStatusAktif("Tidak Aktif");
                                    }
                                    repoKontak = new clsMediaKontakDetailRepo(context.getApplicationContext());
                                    repoKontak.createOrUpdate(dataKontak);

                                    new clsActivity().showCustomToast(context.getApplicationContext(), "Kontak berhasil di perbarui", true);
                                    Log.d("Data info", "Kontak berhasil di perbarui");
                                }

                                dialog.dismiss();
                                dataListview();
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

    private void selectOptionTelpon() {
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
                        popupTambahTelp();
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

    private void selectOptionSms() {
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
                        popupTambahSms();
                } else if (items[item].equals("Edit")) {
                    if(result)
                        popupEditSms();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void selectOptionBBM() {
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
                        popupTambahBBM();
                } else if (items[item].equals("Edit")) {
                    if(result)
                        popupEditBBM();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void selectOptionLine() {
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
                        popupTambahLine();
                } else if (items[item].equals("Edit")) {
                    if(result)
                        popupEditLine();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void selectOptionWA() {
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
                        popupTambahWA();
                } else if (items[item].equals("Edit")) {
                    if(result)
                        popupEditWA();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void selectOptionEmail() {
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
                        popupTambahEmail();
                } else if (items[item].equals("Edit")) {
                    if(result)
                        popupEditEmail();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void selectOptionTwitter() {
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
                        popupTambahTwitter();
                } else if (items[item].equals("Edit")) {
                    if(result)
                        popupEditTwitter();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void selectOptionFacebook() {
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
                        popupTambahFacebook();
                } else if (items[item].equals("Edit")) {
                    if(result)
                        popupEditFacebook();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void selectOptionInstagram() {
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
                        popupTambahInstagram();
                } else if (items[item].equals("Edit")) {
                    if(result)
                        popupEditInstagram();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void selectOptionFax() {
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
                        popupTambahFax();
                } else if (items[item].equals("Edit")) {
                    if(result)
                        popupEditFax();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void selectOptionPath() {
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
                        popupTambahPath();
                } else if (items[item].equals("Edit")) {
                    if(result)
                        popupEditPath();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    private void selectOptionMMS() {
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
                        popupTambahMMS();
                } else if (items[item].equals("Edit")) {
                    if(result)
                        popupEditMMS();
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
