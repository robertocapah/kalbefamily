package kalbefamily.crm.kalbe.kalbefamily;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import kalbefamily.crm.kalbe.kalbefamily.BL.clsActivity;
import kalbefamily.crm.kalbe.kalbefamily.Common.clsDeviceInfoData;
import kalbefamily.crm.kalbe.kalbefamily.Common.clsUserMember;
import kalbefamily.crm.kalbe.kalbefamily.Common.clsWarning;
import kalbefamily.crm.kalbe.kalbefamily.Common.clsmVersionApp;
import kalbefamily.crm.kalbe.kalbefamily.Data.VolleyResponseListener;
import kalbefamily.crm.kalbe.kalbefamily.Data.VolleyUtils;
import kalbefamily.crm.kalbe.kalbefamily.Data.clsHardCode;
import kalbefamily.crm.kalbe.kalbefamily.Data.clsHelper;
import kalbefamily.crm.kalbe.kalbefamily.Repo.clsDeviceInfoRepo;
import kalbefamily.crm.kalbe.kalbefamily.Repo.clsUserMemberRepo;
import kalbefamily.crm.kalbe.kalbefamily.Repo.clsmVersionAppRepo;

/**
 * Created by Rian Andrivani on 7/17/2017.
 */

public class MemberActivity extends AppCompatActivity {
    private EditText txtNoTelp;
    private clsWarning _clsWarning;
    private GoogleApiClient client;

    private String txtTelp;
    private String role = "Role";
    private String[] roles = new String[1];
    ProgressDialog progress;
    long Delay = 3000;
    private Button btnSubmit, btnPing;
    private TextView txtVersionLogin;
    private TextView txtHDId, txtHDId2;
    private PackageInfo pInfo = null;
    //    private Spinner spnRole, spnOutlet;
    private int intSet = 1;
    private String[] arrdefaultBranch = new String[]{"-"};
    private String[] arrdefaultOutlet = new String[]{"-"};
    private static final String TAG = "MainActivity";
    clsDeviceInfoRepo repoDeviceInfo = null;
    clsmVersionAppRepo repoVersionApp = null;
    clsUserMemberRepo repoUserMember = null;
    int intProcesscancel = 0;

    @Override
    public void onBackPressed() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);

        builder.setTitle("Exit");
        builder.setMessage("Do you want to exit?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                finish();
                System.exit(0);
//                Intent intent = new Intent(Intent.ACTION_MAIN);
//                intent.addCategory(Intent.CATEGORY_HOME);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        android.app.AlertDialog alert = builder.create();
        alert.show();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e2) {
            // TODO Auto-generated catch block
            e2.printStackTrace();
        }

        ImageView imgBanner = (ImageView) findViewById(R.id.ivBannerLogin);
        imgBanner.setAdjustViewBounds(true);
        imgBanner.setScaleType(ImageView.ScaleType.CENTER_CROP);
        txtHDId = (TextView) findViewById(R.id.txtHDId);
        txtHDId2 = (TextView) findViewById(R.id.txtHDId2);
        txtNoTelp = (EditText) findViewById(R.id.txtNoTelp);
        txtNoTelp.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    intProcesscancel = 0;

                    return true;
                }
                return false;
            }
        });
        checkVersion();

        txtVersionLogin = (TextView) findViewById(R.id.txtVersionLogin);
        txtVersionLogin.setText(pInfo.versionName);

        btnSubmit = (Button) findViewById(R.id.buttonSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                intProcesscancel = 0;
                if (txtNoTelp.getText().length() == 0) {
//                    showToast(LoginActivity.this, "Please input username");
                    Toast.makeText(getApplicationContext(), "Please input Mobile phone", Toast.LENGTH_LONG).show();

                } else {
                    txtTelp = txtNoTelp.getText().toString();
                    UserMember();
                }
            }
        });

        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        Button btnExit = (Button) findViewById(R.id.buttonExit);
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(MemberActivity.this);

                builder.setTitle("Exit");
                builder.setMessage("Do you want to exit?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                android.app.AlertDialog alert = builder.create();
                alert.show();
            }
        });

        Button btnPing = (Button) findViewById(R.id.buttonPing);
        btnPing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //buat check isi table deviceInfo
                List<clsDeviceInfoData> items = null;
                try {
                    items = (List<clsDeviceInfoData>) repoDeviceInfo.findAll();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
//                clsDeviceInfo info = items.get(0);
//                Log.d("info",info.toString());

                //buat clear isi table
                /*try {
                    repoDeviceInfo.clearTable();
                } catch (SQLException e) {
                    e.printStackTrace();
                }*/

                //ini buat copy db ke luar
                try {
                    new clsHelper().createDatabase(getApplicationContext());
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    public void UserMember() {
        final ProgressDialog Dialog = new ProgressDialog(MemberActivity.this);
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        txtTelp = txtNoTelp.getText().toString();
        String strLinkAPI = new clsHardCode().linkGetDetailKontak;
//        String nameRole = selectedRole;
        JSONObject resJson = new JSONObject();
        List<clsmVersionApp> dataInfoVersion = null;
        List<clsDeviceInfoData> dataInfo = null;
        try {
            dataInfo = (List<clsDeviceInfoData>) repoDeviceInfo.findAll();
            dataInfoVersion = (List<clsmVersionApp>) repoVersionApp.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
//            resJson.put("TxtVersion", dataInfoVersion.get(0).getTxtVersion());
//            resJson.put("TxtGUI_mVersionApp", dataInfoVersion.get(0).getTxtGUI());
            resJson.put("txtMemberIdOrTelpId", txtTelp);
            resJson.put("TxtModel", dataInfo.get(0).getTxtModel());
            resJson.put("TxtDevice", dataInfo.get(0).getTxtDevice());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final String mRequestBody = "[" + resJson.toString() + "]";

        new VolleyUtils().makeJsonObjectRequest(MemberActivity.this, strLinkAPI, mRequestBody, "Please wait !", new VolleyResponseListener() {
            @Override
            public void onError(String response) {
                new clsActivity().showCustomToast(getApplicationContext(), response, false);
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
                                String txtKontakId = jsonobject.getString("TxtKontakId");
                                String memberID = jsonobject.getString("TxtMemberId");
                                String txtNama = jsonobject.getString("TxtNama");
                                String txtAlamat = jsonobject.getString("TxtAlamat");
                                String txtJenisKelamin = jsonobject.getString("TxtJenisKelamin");
                                String txtEmail = jsonobject.getString("TxtEmail");
                                String txtTelp = jsonobject.getString("TxtTelp");
                                String txtNoKTP = jsonobject.getString("TxtNoKTP");
                                String txtNamaKeluarga = jsonobject.getString("TxtNamaKeluarga");
                                String txtNamaPanggilan = jsonobject.getString("TxtNamaPanggilan");

                                clsUserMember dataUser = new clsUserMember();
                                dataUser.setTxtKontakId(txtKontakId);
                                dataUser.setTxtMemberId(memberID);
                                dataUser.setTxtNama(txtNama);
                                dataUser.setTxtAlamat(txtAlamat);
                                dataUser.setTxtJenisKelamin(txtJenisKelamin);
                                dataUser.setTxtEmail(txtEmail);
                                dataUser.setTxtNoTelp(txtTelp);
                                dataUser.setTxtNoKTP(txtNoKTP);
                                dataUser.setTxtNamaKeluarga(txtNamaKeluarga);
                                dataUser.setTxtNamaPanggilan(txtNamaPanggilan);

                                repoUserMember = new clsUserMemberRepo(getApplicationContext());
                                repoUserMember.createOrUpdate(dataUser);
                                Log.d("Data info", "Data member valid");
                            }
                            Intent intent = new Intent(MemberActivity.this, CardViewActivity.class);
                            finish();
                            startActivity(intent);
                        } else {
                            new clsActivity().showCustomToast(getApplicationContext(), warn, false);
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

    public void checkVersion() {
        final ProgressDialog Dialog = new ProgressDialog(MemberActivity.this);
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String strLinkAPI = new clsHardCode().linkCheckVersion;
        // http://prm.kalbenutritionals.web.id/VisitPlan/API/VisitPlanAPI/CheckVersionApp_J
        JSONObject resJson = new JSONObject();
        try {
            resJson.put("TxtNameApp", "Kalbe Family");
            resJson.put("TxtType", "Android");
            resJson.put("txtVersion", "1.0.0.1");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final String mRequestBody = "[" + resJson.toString() + "]";
//        String result = new clsHelper().volleyImplement(getApplicationContext(),mRequestBody,strLinkAPI,Login.this);
//

        new VolleyUtils().makeJsonObjectRequest(MemberActivity.this, strLinkAPI, mRequestBody, "Checking Version !", new VolleyResponseListener() {
            @Override
            public void onError(String response) {
                new clsActivity().showCustomToast(getApplicationContext(), response, false);
            }

            @Override
            public void onResponse(String response, Boolean status, String strErrorMsg) {
                if (response != null){
                    try {
                        JSONObject jsonObject1 = new JSONObject(response);
                        JSONObject jsonObject2 = jsonObject1.getJSONObject("validJson");

                        String result = jsonObject2.getString("IntResult");
                        String txtWarn = jsonObject2.getString("TxtMessage");
                        if (result.equals("1")){
                            JSONArray jsonObject3 = jsonObject2.getJSONArray("ListOfObjectData");
                            for(int i=0; i < jsonObject3.length(); i++) {
                                JSONObject jsonobject = jsonObject3.getJSONObject(i);
                                txtHDId.setText(String.valueOf(new clsActivity().GenerateGuid()));
                                txtHDId2.setText(String.valueOf(new clsActivity().GenerateGuid()));
                                String txtGUI = txtHDId.getText().toString();
                                String txtGUI2 = txtHDId2.getText().toString();
                                String intMApplication = jsonobject.getString("IntMApplication");
                                String txtName = jsonobject.getString("TxtName");
                                String txtType = jsonobject.getString("TxtType");
                                String txtVersion = jsonobject.getString("TxtVersion");
                                String txtLink = jsonobject.getString("TxtLink");
                                String bitActive = jsonobject.getString("BitActive");

                                clsDeviceInfoData data = new clsDeviceInfoData();
                                data.setTxtGUI(txtGUI);
                                data.setIntMApplication(intMApplication);
                                data.setTxtNameApp(txtName);
                                data.setTxtDevice(android.os.Build.DEVICE);
                                data.setTxtType(txtType);
                                data.setTxtVersion(txtVersion);
                                data.setBitActive(bitActive);
                                data.setIdDevice("");
                                data.setTxtLink(txtLink);
                                data.setTxtModel(android.os.Build.MANUFACTURER+" "+android.os.Build.MODEL);

                                clsmVersionApp dataVersion = new clsmVersionApp();
                                dataVersion.setTxtGUI(txtGUI2);
                                dataVersion.setIntMApplication(intMApplication);
                                dataVersion.setTxtNameApp(txtName);
                                dataVersion.setTxtVersion(txtVersion);
                                dataVersion.setTxtType(txtType);
                                dataVersion.setBitActive(bitActive);
                                dataVersion.setTxtLink(txtLink);

                                repoDeviceInfo =new clsDeviceInfoRepo(getApplicationContext());
                                repoVersionApp = new clsmVersionAppRepo(getApplicationContext());
                                int h = 0;
                                int j = 0;
                                try {
                                    h = repoDeviceInfo.createOrUpdate(data);
                                    j = repoVersionApp.createOrUpdate(dataVersion);
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                                if(h > -1)
                                {
                                    Log.d("Data info", "Data info berhasil di simpan");
                                    status = true;
                                }
                            }
//                            TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
//                            String imeiNumber = tm.getDeviceId();

                        }else{
                            Toast.makeText(getApplicationContext(), txtWarn, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if(!status){
                    new clsActivity().showCustomToast(getApplicationContext(), strErrorMsg, false);
                }
            }
        });
    }
}
