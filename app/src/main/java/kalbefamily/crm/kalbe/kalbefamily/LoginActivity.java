package kalbefamily.crm.kalbe.kalbefamily;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ParseException;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

//import com.activeandroid.query.From;
//import com.activeandroid.query.Select;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import kalbefamily.crm.kalbe.kalbefamily.BL.clsActivity;
import kalbefamily.crm.kalbe.kalbefamily.BL.tdeviceBL;
import kalbefamily.crm.kalbe.kalbefamily.Common.clsDeviceInfoData;
import kalbefamily.crm.kalbe.kalbefamily.Common.clsUserLoginData;
import kalbefamily.crm.kalbe.kalbefamily.Common.clsWarning;
import kalbefamily.crm.kalbe.kalbefamily.Data.VolleyResponseListener;
import kalbefamily.crm.kalbe.kalbefamily.Data.VolleyUtils;
import kalbefamily.crm.kalbe.kalbefamily.Data.clsHelper;
import kalbefamily.crm.kalbe.kalbefamily.Repo.clsDeviceInfoRepo;
import kalbefamily.crm.kalbe.kalbefamily.Repo.clsUserLoginRepo;
//import kalbefamily.crm.kalbe.kalbefamily.Common.tdeviceData;

public class LoginActivity extends clsActivity {
    private EditText txtMemberID;
    private Button btnMember;
    private clsWarning _clsWarning;
    private GoogleApiClient client;

    private String role = "Role";
    private String[] roles = new String[1];
    ProgressDialog progress;
    long Delay = 3000;
    private EditText txtLoginEmail;
    private EditText txtLoginPassword;
    private Button btnLogin, btnPing;
    private TextView txtVersionLogin;
    private PackageInfo pInfo = null;
    private List<String> arrrole, arroutlet;
    private HashMap<String, String> HMRole = new HashMap<String, String>();
    private HashMap<String, String> HMOutletCode = new HashMap<String, String>();
    private HashMap<String, String> HMOutletName = new HashMap<String, String>();
    private HashMap<String, String> HMBranchCode = new HashMap<String, String>();
    //    private Spinner spnRole, spnOutlet;
    private int intSet = 1;
    private String selectedRole;
    private String selectedOutlet;
    private String txtEmail;
    private String txtEmail1;
    private String txtPassword1;
    private String txtPassword;
    private String[] arrdefaultBranch = new String[]{"-"};
    private String[] arrdefaultOutlet = new String[]{"-"};
    private static final String TAG = "MainActivity";
    clsDeviceInfoRepo repoDeviceInfo = null;
    clsUserLoginRepo repoLogin = null;

    int intProcesscancel = 0;

    @Override
    public void onBackPressed() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
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
        txtLoginEmail = (EditText) findViewById(R.id.txtLoginEmail);
        txtLoginPassword = (EditText) findViewById(R.id.editTextPass);
        txtLoginEmail.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    intProcesscancel = 0;
                    txtLoginPassword.requestFocus();
//                    txtEmail1 = txtLoginEmail.getText().toString();
//                    txtPassword1 = txtLoginPassword.getText().toString();
                    return true;
                }
                return false;
            }
        });

        checkVersion();
        txtLoginPassword.setOnTouchListener(new DrawableClickListener.RightDrawableClickListener(txtLoginPassword) {
            public boolean onDrawableClick() {
                if (intSet == 1) {
                    txtLoginPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    intSet = 0;
                } else {
                    txtLoginPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    intSet = 1;
                }

                return true;
            }
        });

        txtLoginPassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER ||
                            keyCode == KeyEvent.KEYCODE_ENTER) {
                        btnLogin.performClick();
                        return true;
                    }
                }

                return false;
            }
        });

        txtLoginPassword.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    btnLogin.performClick();
                    return true;
                }
                return false;
            }
        });


        txtVersionLogin = (TextView) findViewById(R.id.txtVersionLogin);
        txtVersionLogin.setText(pInfo.versionName);

        btnLogin = (Button) findViewById(R.id.buttonLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                intProcesscancel = 0;
                if (txtLoginEmail.getText().length() == 0) {
//                    showToast(LoginActivity.this, "Please input username");
                    Toast.makeText(getApplicationContext(), "Please input Username", Toast.LENGTH_LONG).show();

                } else {
                    txtEmail1 = txtLoginEmail.getText().toString();
                    txtPassword1 = txtLoginPassword.getText().toString();
//                        AsyncCallLogin task = new AsyncCallLogin();
//                        task.execute();
                    userLogin();
                }
            }
        });

        txtMemberID=(EditText) findViewById(R.id.txtMemberID);

        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        Button btnExit = (Button) findViewById(R.id.buttonExit);
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(LoginActivity.this);

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

    private void userLogin() {
        final ProgressDialog Dialog = new ProgressDialog(LoginActivity.this);
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        txtEmail1 = txtLoginEmail.getText().toString();
        txtPassword1 = txtLoginPassword.getText().toString();
        String strLinkAPI = "http://prm.kalbenutritionals.web.id/VisitPlan/API/VisitPlanAPI/LogIn_J";
//        String nameRole = selectedRole;
        JSONObject resJson = new JSONObject();
        List<clsDeviceInfoData> dataInfo = null;
        try {
            dataInfo = (List<clsDeviceInfoData>) repoDeviceInfo.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            resJson.put("TxtVersion", dataInfo.get(0).getTxtVersion());
            resJson.put("TxtPegawaiID", txtEmail1);
            resJson.put("TxtPassword", txtPassword1);
            resJson.put("TxtModel", dataInfo.get(0).getTxtModel());
            resJson.put("TxtDevice", dataInfo.get(0).getTxtDevice());
            resJson.put("TxtGUI_mVersionApp", dataInfo.get(0).getTxtGUI());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final String mRequestBody = "[" + resJson.toString() + "]";

        new VolleyUtils().makeJsonObjectRequest(LoginActivity.this, strLinkAPI, mRequestBody, new VolleyResponseListener() {
            @Override
            public void onError(String response) {
                new clsActivity().showCustomToast(getApplicationContext(), response, false);
            }

            @Override
            public void onResponse(String response, Boolean status, String strErrorMsg) {
                if (response != null) {
                    try {
                        JSONObject jsonObject1 = new JSONObject(response);
                        String result = jsonObject1.getString("TxtResult");
                        String warn = jsonObject1.getString("TxtWarn");
                        if (result.equals("1")) {
                            JSONObject jsonObject2 = jsonObject1.getJSONObject("TxtData");
                            JSONObject jsonDataUserLogin = jsonObject2.getJSONObject("UserLogin");
                            String TxtNameApp = jsonDataUserLogin.getString("TxtNameApp");
                            String TxtGUI = jsonDataUserLogin.getString("TxtGUI");
                            String TxtUserID = jsonDataUserLogin.getString("TxtUserID");
                            String TxtUserName = jsonDataUserLogin.getString("TxtUserName");
                            String TxtName = jsonDataUserLogin.getString("TxtName");
                            String TxtEmail = jsonDataUserLogin.getString("TxtEmail");
                            String TxtEmpID = jsonDataUserLogin.getString("TxtEmpID");
                            String DtLastLogin = jsonDataUserLogin.getString("DtLastLogin");
                            String TxtDeviceId = jsonDataUserLogin.getString("TxtDeviceId");
                            clsUserLoginData data = new clsUserLoginData();
                            data.setTxtNameApp(TxtNameApp);
                            data.setTxtGUI(TxtGUI);
                            data.setTxtUserID(TxtUserID);
                            data.setIdUserLogin(1);
                            data.setTxtUserName(TxtUserName);
                            data.setTxtName(TxtName);
                            data.setTxtEmail(TxtEmail);
                            data.setEmployeeId(TxtEmpID);
                            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            Calendar cal = Calendar.getInstance();
                            data.setDtLastLogin(DtLastLogin);
                            data.setTxtDeviceId(TxtDeviceId);
//                            data.setTxtInsertedBy(TxtInsertedBy);
//                            data.setDtInserted(dateFormat.format(cal.getTime()));

                            repoLogin =new clsUserLoginRepo(getApplicationContext());
                            int i = 0;
                            i = repoLogin.create(data);
                            if(i > -1)
                            {
                                Log.d("Data info", "Data info berhasil di simpan");
//                                Intent myIntent = new Intent(LoginActivity.this, MainMenu.class);
//                                myIntent.putExtra("keyMainMenu", "main_menu");
//                                startActivity(myIntent);
                            }else{
                                new clsActivity().showCustomToast(getApplicationContext(),warn,false);
                            }
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

    public void checkVersion() {
        final ProgressDialog Dialog = new ProgressDialog(LoginActivity.this);
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String strLinkAPI = "http://prm.kalbenutritionals.web.id/VisitPlan/API/VisitPlanAPI/CheckVersionApp_J";
        JSONObject resJson = new JSONObject();
        try {
            resJson.put("TxtVersion", pInfo.versionName);
            resJson.put("TxtNameApp", "Android - Call Plan BR Mobile");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final String mRequestBody = "[" + resJson.toString() + "]";
//        String result = new clsHelper().volleyImplement(getApplicationContext(),mRequestBody,strLinkAPI,Login.this);
//

        new VolleyUtils().makeJsonObjectRequest(LoginActivity.this, strLinkAPI, mRequestBody, new VolleyResponseListener() {
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

                        String result = jsonObject2.getString("TxtResult");
                        String txtWarn = jsonObject2.getString("TxtWarn");
                        if (result.equals("1")){
                            JSONObject jsonObject3 = jsonObject2.getJSONObject("TxtData");
                            String txtGUI = jsonObject3.getString("TxtGUI");
                            String txtNameApp = jsonObject3.getString("TxtNameApp");
                            String txtVersion = jsonObject3.getString("TxtVersion");
                            String txtFile = jsonObject3.getString("TxtFile");
                            String bitActive = jsonObject3.getString("BitActive");
                            String txtInsertedBy = jsonObject3.getString("TxtInsertedBy");
//                            TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
//                            String imeiNumber = tm.getDeviceId();
                            clsDeviceInfoData data = new clsDeviceInfoData();
                            data.setTxtGUI(txtGUI);
                            data.setTxtNameApp(txtNameApp);
                            data.setTxtDevice(android.os.Build.DEVICE);
                            data.setTxtFile(txtFile);
                            data.setTxtVersion(txtVersion);
                            data.setBitActive(bitActive);
                            data.setIdDevice("");
                            data.setTxtModel(android.os.Build.MANUFACTURER+" "+android.os.Build.MODEL);
                            repoDeviceInfo =new clsDeviceInfoRepo(getApplicationContext());
                            int i = 0;
                            try {
                                i = repoDeviceInfo.createOrUpdate(data);
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                            if(i > -1)
                            {
                                Log.d("Data info", "Data info berhasil di simpan");
                            }
                        }else{
                            Toast.makeText(getApplicationContext(), txtWarn, Toast.LENGTH_SHORT).show();
                        }


//                        for (int i = 0; i < jsonArray.length(); i++) {
//                            JSONObject explrObject = jsonArray.getJSONObject(i);
//                        }
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

//    private class AsyncCallLogin extends AsyncTask<JSONObject, Void, JSONObject> {
//        @SuppressWarnings("WrongThread")
//        @Override
//        protected JSONObject doInBackground(JSONObject... params) {
//            JSONObject LoginData = null;
//            try {
//                LoginData = new JSONObject();
//                tdeviceData _tdeviceData=new tdeviceBL().getInfoDevice();
//                JSONObject JsonParam=new JSONObject();
//                String txtVersion= txtVersionApp();
//                JsonParam.put("txtDevice",_tdeviceData.txtDevice.toString());
//                JsonParam.put("txtModel",_tdeviceData.txtModel.toString());
//                JsonParam.put("txtGUIDeviceId",_tdeviceData.txtGUIDeviceId.toString());
//                JsonParam.put("txtVersion",_tdeviceData.txtVersion.toString());
//                JsonParam.put("txtParam",txtMemberID.getText().toString());
//                LoginData=PushData("GetDetailKontak",JsonParam.toString());
//
//                //EditText txt = (EditText) findViewById(R.id.txtLoginEmail);
//                //roledata = new mUserRoleBL().getRoleAndOutlet(txtEmail1, pInfo.versionName);
//            } catch (ParseException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//                showToastWarning(getApplicationContext() ,e.getMessage());
//            } catch (Exception e) {
//                e.printStackTrace();
//                showToastWarning(getApplicationContext()
//                        ,e.getMessage());
//            }
//
//            return LoginData;
//        }
//        private ProgressDialog Dialog = new ProgressDialog(LoginActivity.this);
//
//        @Override
//        protected void onPostExecute(JSONObject JsonRes) {
//            if(new clsActivity().ValidJSON(JsonRes)){
//                try {
//                    String txtvalidJson=(String.valueOf(JsonRes.get("validJson")));
//                    JSONObject validJson=new JSONObject(txtvalidJson);
//                    String intresult=(String.valueOf(validJson.get("TxtResult")));
//                    if(intresult.equals("1")){
//                        String txtGetDataJson = String.valueOf(validJson.get("TxtData"));
//                        JSONObject JsonData=new JSONObject(txtGetDataJson);
//                        String txtJSONUserPegawai = String.valueOf(JsonData.get("UserPegawai"));
//                        JSONObject JSONUserPegawai=new JSONObject(txtJSONUserPegawai);
//                        String txtJSONUserJabatan = String.valueOf(JsonData.get("UserJabatan"));
//                        JSONArray JSONUserJabatanArray=new JSONArray(txtJSONUserJabatan);
//
//                        LayoutInflater layoutInflater = LayoutInflater.from(LoginActivity.this);
//                        View promptView = layoutInflater.inflate(R.layout.listmember, null);
//                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LoginActivity.this);
//                        alertDialogBuilder.setView(promptView);
//                        final TextView  _tvName=new TextView(LoginActivity.this);
//                        _tvName.setText("asdasd");
//                        final LinearLayout llMemberID=(LinearLayout) promptView.findViewById(R.id.llMemberID);
//                        llMemberID.addView(_tvName);
//                    }else{
//                        String txtvalidJsonError=(String.valueOf(JsonRes.get("validJson")));
//                        JSONObject validJsonError=new JSONObject(txtvalidJsonError);
//                        String txtWarn=(String) validJsonError.get("TxtWarn");
//                        new clsActivity().showToastWarning(LoginActivity.this,txtWarn);
//                    }
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    new clsActivity().showToastWarning(LoginActivity.this,e.getMessage());
//                }
//
//
//            }
//            Dialog.dismiss();
//        }
//
//
//
//        int intProcesscancel = 0;
//
//        @Override
//        protected void onPreExecute() {
//            //Make ProgressBar invisible
//            //pg.setVisibility(View.VISIBLE);
//            Dialog.setCancelable(false);
//            Dialog.show();
//        }
//
//        @Override
//        protected void onProgressUpdate(Void... values) {
//            Dialog.dismiss();
//        }
//
//    }
}
