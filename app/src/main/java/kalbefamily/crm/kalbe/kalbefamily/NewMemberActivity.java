package kalbefamily.crm.kalbe.kalbefamily;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class NewMemberActivity extends AppCompatActivity {
    private EditText txtMemberId;
    private clsWarning _clsWarning;
    private GoogleApiClient client;

    private String txtMember;
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

        builder.setTitle("Keluar");
        builder.setMessage("Apakah Anda ingin keluar?");

        builder.setPositiveButton("KELUAR", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                finish();
                System.exit(0);
            }
        });

        builder.setNegativeButton("BATAL", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        android.app.AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_member);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.primary_color_theme));
        }

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
        txtMemberId = (EditText) findViewById(R.id.txtMember);
        txtMemberId.setOnKeyListener(new View.OnKeyListener() {
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
                if (txtMemberId.getText().length() == 0) {
//                    showToast(LoginActivity.this, "Please input username");
                    Toast.makeText(getApplicationContext(), "Please input Member ID", Toast.LENGTH_LONG).show();

                } else {
                    txtMember = txtMemberId.getText().toString();
                    userMember();
                }
            }
        });

        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        Button btnExit = (Button) findViewById(R.id.buttonExit);
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(NewMemberActivity.this);

                builder.setTitle("Keluar");
                builder.setMessage("Apakah Anda ingin keluar?");

                builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });

                builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                android.app.AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

    private void userMember() {
        final ProgressDialog Dialog = new ProgressDialog(NewMemberActivity.this);
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        txtMember = txtMemberId.getText().toString();
        String strLinkAPI = new clsHardCode().linkGetDatadMembership;
        JSONObject resJson = new JSONObject();
        List<clsmVersionApp> dataInfoVersion = null;
        List<clsDeviceInfoData> dataInfo = null;

        try {
            resJson.put("txtMemberID", txtMember);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final String mRequestBody = "[" + resJson.toString() + "]";

        new VolleyUtils().makeJsonObjectRequest(NewMemberActivity.this, strLinkAPI, mRequestBody, "Mohon Tunggu...", new VolleyResponseListener() {
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
                                String txtMemberID = jsonobject.getString("TxtMemberId");
                                String txtNama = jsonobject.getString("txtNamaKartu");
                                String txtAlamat = jsonobject.getString("TxtAlamat");
                                String txtJenisKelamin = jsonobject.getString("TxtJenisKelamin");
                                String txtEmail = jsonobject.getString("TxtEmail");
                                String txtTelp = jsonobject.getString("TxtTelp");
                                String txtNoKTP = jsonobject.getString("TxtNoKTP");
                                String txtNamaKeluarga = jsonobject.getString("TxtNamaKeluarga");
                                String txtNamaPanggilan = jsonobject.getString("TxtNamaPanggilan");

                                clsUserMember dataUser = new clsUserMember();
                                dataUser.setTxtKontakId(txtKontakId);
                                dataUser.setTxtMemberId(txtMemberID);
                                dataUser.setTxtNama(txtNama);
                                dataUser.setTxtAlamat(txtAlamat);
                                dataUser.setTxtJenisKelamin(txtJenisKelamin);
                                dataUser.setTxtEmail(txtEmail);
                                dataUser.setTxtNoTelp(txtTelp);
                                dataUser.setTxtNoKTP(txtNoKTP);
                                dataUser.setTxtNamaBelakang(txtNamaKeluarga);
                                dataUser.setTxtNamaPanggilan(txtNamaPanggilan);

//                                repoUserMember = new clsUserMemberRepo(getApplicationContext());
//                                repoUserMember.createOrUpdate(dataUser);
//                                Log.d("Data info", "Data member valid");
                            }
//                            Toast.makeText(getApplicationContext(), "Your Member ID is valid", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(NewMemberActivity.this, NewLoginActivity.class);
                            intent.putExtra("memberID", txtMember);
                            finish();
                            startActivity(intent);
                        } else {
                            new clsActivity().showCustomToast(getApplicationContext(), warn, false);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void checkVersion() {
        final ProgressDialog Dialog = new ProgressDialog(NewMemberActivity.this);
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

        new VolleyUtils().makeJsonObjectRequest(NewMemberActivity.this, strLinkAPI, mRequestBody, "Checking Version !", new VolleyResponseListener() {
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
                                String intMApplication = jsonobject.getString("IntMApplication");
                                String txtName = jsonobject.getString("TxtName");
                                String txtType = jsonobject.getString("TxtType");
                                String txtVersion = jsonobject.getString("TxtVersion");
                                String txtLink = jsonobject.getString("TxtLink");
                                String bitActive = jsonobject.getString("BitActive");

                                clsDeviceInfoData data = new clsDeviceInfoData();
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
