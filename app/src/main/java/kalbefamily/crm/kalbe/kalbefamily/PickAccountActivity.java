package kalbefamily.crm.kalbe.kalbefamily;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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
import com.oktaviani.dewi.mylibrary.authenticator.AccountGeneral;

import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import kalbefamily.crm.kalbe.kalbefamily.Adapter.CardAppAdapter;
import kalbefamily.crm.kalbe.kalbefamily.BL.AuthenticatorUtil;
import kalbefamily.crm.kalbe.kalbefamily.BL.clsActivity;
import kalbefamily.crm.kalbe.kalbefamily.Common.clsToken;
import kalbefamily.crm.kalbe.kalbefamily.Common.clsUserMember;
import kalbefamily.crm.kalbe.kalbefamily.Common.mConfigData;
import kalbefamily.crm.kalbe.kalbefamily.Data.VolleyResponseListener;
import kalbefamily.crm.kalbe.kalbefamily.Data.VolleyUtils;
import kalbefamily.crm.kalbe.kalbefamily.Data.clsHardCode;
import kalbefamily.crm.kalbe.kalbefamily.Repo.clsTokenRepo;
import kalbefamily.crm.kalbe.kalbefamily.Repo.clsUserLoginRepo;
import kalbefamily.crm.kalbe.kalbefamily.Repo.clsUserMemberRepo;
import kalbefamily.crm.kalbe.kalbefamily.Repo.mConfigRepo;

import static com.oktaviani.dewi.mylibrary.authenticator.AccountGeneral.ARG_ARRAY_ACCOUNT_AVAILABLE;
import static com.oktaviani.dewi.mylibrary.authenticator.AccountGeneral.ARG_ARRAY_ACCOUNT_NAME;
import static com.oktaviani.dewi.mylibrary.authenticator.AccountGeneral.ARG_AUTH_TYPE;
import static com.oktaviani.dewi.mylibrary.authenticator.AccountGeneral.ARG_IS_ADDING_NEW_ACCOUNT;
import static com.oktaviani.dewi.mylibrary.authenticator.AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS;
import static com.oktaviani.dewi.mylibrary.authenticator.AccountGeneral.PARAM_USER_PASS;

public class PickAccountActivity extends AccountAuthenticatorActivity {

    Context context;
    Account availableAccounts[];
    String name[];
    private String mAuthTokenType;
    private AlertDialog mAlertDialog;
    private AccountManager mAccountManager;
    private final String TAG = this.getClass().getSimpleName();
    //batas aman

    private static final int REQUEST_READ_PHONE_STATE = 0;
    EditText etUsername, etPassword;
    String txtUsername, imeiNumber, deviceName;
    String clientId = "";
    Button btnSubmit, btnExit;
    Spinner spnRole;
    private Context mContext;

    private int intSet = 1;
    int intProcesscancel = 0;

//    List<clsLogin> dataLogin;
    List<clsToken> dataToken;
    clsUserLoginRepo loginRepo;
    clsTokenRepo tokenRepo;
//    mMenuRepo menuRepo;
    ListView listView;
    private TextView txtVersionLogin,txtLink;
    private PackageInfo pInfo = null;

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Exit");
        builder.setMessage("Are you sure to exit?");

        builder.setPositiveButton("EXIT", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                finish();
                System.exit(0);
            }
        });

        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorAccent));
        }
        mContext = this;
        setContentView(R.layout.activity_pick_account);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_READ_PHONE_STATE);

            imeiNumber = tm.getDeviceId().toString();
            deviceName = Build.MANUFACTURER+" "+ Build.MODEL;
        } else {
            //TODO
            imeiNumber = tm.getDeviceId().toString();
            deviceName = Build.MANUFACTURER+" "+ Build.MODEL;
        }

        mAccountManager = AccountManager.get(getBaseContext());
        try {
            tokenRepo = new clsTokenRepo(getApplicationContext());
            dataToken = (List<clsToken>) tokenRepo.findAll();
            if (dataToken.size() == 0) {
                new VolleyUtils().requestToken(this);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        mAuthTokenType = getIntent().getStringExtra(ARG_AUTH_TYPE);
        if (mAuthTokenType == null)
            mAuthTokenType = AUTHTOKEN_TYPE_FULL_ACCESS;

        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e2) {
            // TODO Auto-generated catch block
            e2.printStackTrace();
        }
        txtVersionLogin = (TextView) findViewById(R.id.tv_version);
        txtLink = (TextView) findViewById(R.id.tv_link);
        txtVersionLogin.setText(pInfo.versionName);
        txtLink.setText(new mConfigRepo(context).API );


        listView = (ListView) findViewById(R.id.lvPickAccount);

        String[] names = getIntent().getStringArrayExtra(ARG_ARRAY_ACCOUNT_NAME);
        Parcelable[] parceAccount = getIntent().getParcelableArrayExtra(ARG_ARRAY_ACCOUNT_AVAILABLE);
        final List<String> account = new ArrayList<>();
        List<Integer> icon = new ArrayList<>();
        if (parceAccount!=null){
            availableAccounts = Arrays.copyOf(parceAccount, parceAccount.length, Account[].class);
            if (availableAccounts.length>0){
                name = new String[names.length+1];
                for (int i=0; i<names.length; i++){
                    account.add(names[i]);
                    icon.add(R.drawable.profile);
                }
                account.add("Add New Account");
                icon.add(R.drawable.add_pick_acount);
            }
        }

        listView.setAdapter(new CardAppAdapter(this,  account, icon));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (account.get(position).equals("Add New Account")){
                    new AuthenticatorUtil().addNewAccount(PickAccountActivity.this, mAccountManager, AccountGeneral.ACCOUNT_TYPE, AUTHTOKEN_TYPE_FULL_ACCESS);
                } else {
                    new AuthenticatorUtil().getExistingAccountAuthToken(PickAccountActivity.this, mAccountManager,availableAccounts[position], AUTHTOKEN_TYPE_FULL_ACCESS);
                }
            }
        });
    }

    String txtMember, txtPassword, access_token;
    clsUserMemberRepo repoUserMember = null;
    private static String[] data_token_s;
    private static Activity activity_s;
    private static AccountManager accountManager_s;
    public void userMember(final String[] data_token, final Activity activity, final AccountManager mAccountManager) {
        data_token_s = data_token;
        activity_s = activity;
        accountManager_s = mAccountManager;
//        final ProgressDialog Dialog = new ProgressDialog(PickAccountActivity.this);
        txtMember = data_token[0];
        txtPassword = data_token[1];
        String strLinkAPI = new clsHardCode().linkGetDatadMembership;
        JSONObject resJson = new JSONObject();
        try {
            resJson.put("txtMemberID", txtMember);
            resJson.put("txtPassword", txtPassword);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String mRequestBody = "[" + resJson.toString() + "]";

//        Intent mm = getIntent();
//        Bundle bbb = mm.getExtras();
//        String ccc = bbb.getString(AccountManager.KEY_ACCOUNT_TYPE);

//        final String accountType = getIntent().getExtras().getString(AccountManager.KEY_ACCOUNT_TYPE);
//        final boolean newAccount = getIntent().getExtras().getBoolean(ARG_IS_ADDING_NEW_ACCOUNT, false);

        final Bundle datum = new Bundle();

        volleyUserMember(strLinkAPI, mRequestBody, "Mohon Tunggu Beberapa Saat...", new VolleyResponseListener() {
            @Override
            public void onError(String response) {
                new clsActivity().showCustomToast(activity_s, response, false);
            }
            Intent res = null;
            @Override
            public void onResponse(String response, Boolean status, String strErrorMsg) {
                if (response != null) {
                    try {
                        JSONObject jsonObject1 = new JSONObject(response);
                        JSONObject jsn = jsonObject1.getJSONObject("validJson");
                        String warn = jsn.getString("TxtMessage");
                        String result = jsn.getString("IntResult");
                        String accessToken = "dummy_access_token";

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
                                String txtBasePoint = jsonobject.getString("IntBasePoin");

                                clsUserMember dataUser = new clsUserMember();
                                dataUser.setTxtKontakId(txtKontakId);
                                dataUser.setTxtMemberId(txtMemberID);
                                dataUser.setTxtNama(txtNama);
                                dataUser.setTxtAlamat(txtAlamat);
                                dataUser.setTxtJenisKelamin(txtJenisKelamin);
                                dataUser.setTxtEmail(txtEmail);
                                dataUser.setTxtNoTelp(txtTelp);
                                dataUser.setTxtNoKTP(txtNoKTP);
                                dataUser.setTxtBasePoin(txtBasePoint);
                                dataUser.setTxtNamaBelakang(txtNamaKeluarga);
                                dataUser.setTxtNamaPanggilan(txtNamaPanggilan);

                                repoUserMember = new clsUserMemberRepo(activity_s);
                                repoUserMember.createOrUpdate(dataUser);
                                Log.d("Data info", "Data member valid");

//                                datum.putString(AccountManager.KEY_ACCOUNT_NAME, txtMember);
//                                datum.putString(AccountManager.KEY_ACCOUNT_TYPE, accountType);
//                                datum.putString(AccountManager.KEY_AUTHTOKEN, accessToken);
//                                datum.putString(PARAM_USER_PASS, txtPassword);
//                                datum.putString(ARG_AUTH_TYPE, mAuthTokenType);
//                                datum.putBoolean(ARG_IS_ADDING_NEW_ACCOUNT, newAccount);
//                                res = new Intent();
//                                res.putExtras(datum);
//                                finishLogin(res, mAccountManager);
                            }
                            Toast.makeText(activity_s, "Your Member ID is valid", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(activity_s, HomeMenu.class);
                            finish();
                            activity_s.startActivity(intent);
                        } else {
                            new clsActivity().showCustomToast(activity_s, warn + ", Password Anda salah ", false);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void volleyUserMember(String strLinkAPI, final String mRequestBody, String progressBarType, final VolleyResponseListener listener) {
        String client = "";
        RequestQueue queue = Volley.newRequestQueue(activity_s);
        ProgressDialog Dialog = new ProgressDialog(activity_s);
        Dialog = ProgressDialog.show(activity_s, "", progressBarType, false);

        final ProgressDialog finalDialog = Dialog;
        final ProgressDialog finalDialog1 = Dialog;

        mConfigRepo configRepo = new mConfigRepo(activity_s);
        try {
            tokenRepo = new clsTokenRepo(activity_s);
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
//                    new clsActivity().showCustomToast(getApplicationContext(), "401, Authorization has been denied for this request", false);

                    new VolleyUtils().requestTokenWithRefresh(activity_s, strLinkRequestToken, refresh_token, finalClient, new VolleyResponseListener() {
                        @Override
                        public void onError(String message) {
                            Toast.makeText(activity_s, message, Toast.LENGTH_SHORT).show();
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
                                        userMember(data_token_s, activity_s, accountManager_s);
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                Toast.makeText(activity_s, strErrorMsg, Toast.LENGTH_SHORT).show();
                            }
                            finalDialog1.dismiss();
                        }
                    });
                } else if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_INTERNAL_SERVER_ERROR ){
                    new clsActivity().showCustomToast(activity_s, "Error 500, Server Error", false);
                    finalDialog1.dismiss();
                } else {
                    popup();
                    finalDialog1.dismiss();
                }
            }
            public void popup() {
                final SweetAlertDialog dialog = new SweetAlertDialog(activity_s, SweetAlertDialog.WARNING_TYPE);
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
                    tokenRepo = new clsTokenRepo(activity_s);
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

//    public void finishLogin(Intent intent, AccountManager mAccountManager) {
//        Log.d("kalbe", TAG + "> finishLogin");
//
//        String accountName = intent.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
//        String accountPassword = intent.getStringExtra(PARAM_USER_PASS);
//        final Account account = new Account(accountName, intent.getStringExtra(AccountManager.KEY_ACCOUNT_TYPE));
//        String authtoken = intent.getStringExtra(AccountManager.KEY_AUTHTOKEN);
//        String authtokenType = intent.getStringExtra(ARG_AUTH_TYPE);
//        if (intent.getBooleanExtra(ARG_IS_ADDING_NEW_ACCOUNT, false)) {
//            Log.d("kalbe", TAG + "> finishLogin > addAccountExplicitly");
//            // Creating the account on the device and setting the auth token we got
//            // (Not setting the auth token will cause another call to the server to authenticate the user)
//            mAccountManager.addAccountExplicitly(account, accountPassword, null);
//            mAccountManager.setAuthToken(account, authtokenType, authtoken);
//        } else {
//            Log.d("kalbe", TAG + "> finishLogin > setPassword");
//            mAccountManager.setPassword(account, accountPassword);
//        }
//
//        setAccountAuthenticatorResult(intent.getExtras());
//        setResult(RESULT_OK, intent);
//        finish();
//    }

//    public void login(final String[] data_token, final Activity activity, final AccountManager mAccountManager) {
//        txtUsername = data_token[0];
//        txtPassword = data_token[1];
//        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        Calendar cal = Calendar.getInstance();
//        final String now = dateFormat.format(cal.getTime()).toString();
//        final String currentDateTime = DateFormat.getDateTimeInstance().format(new Date());
//        String strLinkAPI = new clsHardCode().linkLogin;
//        JSONObject resJson = new JSONObject();
//        this.mAccountManager = mAccountManager;
//        try {
//            loginRepo = new clsLoginRepo(activity.getApplicationContext());
//            tokenRepo = new clsTokenRepo(activity.getApplicationContext());
//            dataToken = (List<clsToken>) tokenRepo.findAll();
//            resJson.put("txtUsername", txtUsername);
//            resJson.put("txtPassword", txtPassword);
//            resJson.put("txtRefreshToken", dataToken.get(0).txtRefreshToken.toString());
//        } catch (JSONException e) {
//            e.printStackTrace();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        final String mRequestBody = resJson.toString();
//        final String accountType = data_token[2];
//        final boolean newAccount = false;
//        final Bundle datum = new Bundle();
//        volleyLogin(activity, strLinkAPI, mRequestBody, data_token, "Please Wait....", new VolleyResponseListener() {
//            @Override
//            public void onError(String message) {
//                Toast.makeText(activity.getApplicationContext(), message, Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onResponse(String response, Boolean status, String strErrorMsg) {
//                Intent res = null;
//                if (response != null) {
//                    try {
//                        JSONObject jsonObject = new JSONObject(response);
//                        JSONObject jsn = jsonObject.getJSONObject("result");
//                        String warn = jsn.getString("txtMessage");
//                        String result = jsn.getString("intResult");
//                        String accessToken = "dummy_access_token";
//
//                        if (result.equals("1")){
//                            clsLogin data = new clsLogin();
//                            data.setTxtGuiId("1");
//                            data.setTxtUsername(txtUsername);
//                            data.setTxtPassword(txtPassword);
//                            data.setDtLogin(now);
//                            data.setTxtImei(imeiNumber);
//                            data.setTxtDeviceName(deviceName);
//
//                            loginRepo.createOrUpdate(data);
//                            Log.d("Data info", "Login Success");
//                            new LoginActivity().listMenu(activity);
//
//                            datum.putString(AccountManager.KEY_ACCOUNT_NAME, txtUsername);
//                            datum.putString(AccountManager.KEY_ACCOUNT_TYPE, accountType);
//                            datum.putString(AccountManager.KEY_AUTHTOKEN, accessToken);
//                            datum.putString(PARAM_USER_PASS, txtPassword);
//                            datum.putString(ARG_AUTH_TYPE, data_token[3]);
//                            datum.putBoolean(ARG_IS_ADDING_NEW_ACCOUNT, newAccount);
//                            res = new Intent();
//                            res.putExtras(datum);
//                            new LoginActivity().finishLogin(res, mAccountManager);
//
//                            Intent intent = new Intent(activity, MainMenu.class);
//                            activity.finish();
//                            activity.startActivity(intent);
//
//                        } else {
//                            Toast.makeText(activity.getApplicationContext(), warn, Toast.LENGTH_SHORT).show();
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        });
//    }


//    private void volleyLogin(final Activity activity, String strLinkAPI, final String mRequestBody, final String[] data_token, String progressBarType, final VolleyResponseListener listener) {
//        RequestQueue queue = Volley.newRequestQueue(activity.getApplicationContext());
//        final String[] body = new String[1];
//        final String[] message = new String[1];
//        final ProgressDialog Dialog = new ProgressDialog(activity);
//        Dialog.setMessage(progressBarType);
//        Dialog.setCancelable(false);
//        Dialog.show();
//
//        final ProgressDialog finalDialog = Dialog;
//        final ProgressDialog finalDialog1 = Dialog;
//
//        mConfigRepo configRepo = new mConfigRepo(activity.getApplicationContext());
//        try {
//            mConfigData configDataClient = (mConfigData) configRepo.findById(4);
//            clientId = configDataClient.getTxtDefaultValue().toString();
//            dataToken = (List<clsToken>) tokenRepo.findAll();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        StringRequest request = new StringRequest(Request.Method.POST, strLinkAPI, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                Boolean status = false;
//                String errorMessage = null;
//                listener.onResponse(response, status, errorMessage);
//                finalDialog.dismiss();
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                String strLinkAPI = new clsHardCode().linkToken;
//                final String refresh_token = dataToken.get(0).txtRefreshToken;
//                NetworkResponse networkResponse = error.networkResponse;
//                if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_UNAUTHORIZED) {
//                    // HTTP Status Code: 401 Unauthorized
//                    try {
//                        // body for value error response
//                        body[0] = new String(error.networkResponse.data,"UTF-8");
//                        JSONObject jsonObject = new JSONObject(body[0]);
//                        message[0] = jsonObject.getString("Message");
//                        //Toast.makeText(context, "Error 401, " + message[0], Toast.LENGTH_SHORT).show();
//                    } catch (UnsupportedEncodingException e) {
//                        e.printStackTrace();
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//
//                    new VolleyUtils().requestTokenWithRefresh(activity, strLinkAPI, refresh_token, clientId, new VolleyResponseListener() {
//                        @Override
//                        public void onError(String message) {
//                            Toast.makeText(activity.getApplicationContext(), message, Toast.LENGTH_SHORT).show();
//                        }
//
//                        @Override
//                        public void onResponse(String response, Boolean status, String strErrorMsg) {
//                            if (response != null) {
//                                try {
//                                    String accessToken = "";
//                                    String newRefreshToken = "";
//                                    String refreshToken = "";
//                                    JSONObject jsonObject = new JSONObject(response);
//                                    accessToken = jsonObject.getString("access_token");
//                                    refreshToken = jsonObject.getString("refresh_token");
//                                    String dtIssued = jsonObject.getString(".issued");
//
//                                    clsToken data = new clsToken();
//                                    data.setIntId("1");
//                                    data.setDtIssuedToken(dtIssued);
//                                    data.setTxtUserToken(accessToken);
//                                    data.setTxtRefreshToken(refreshToken);
//
//                                    tokenRepo.createOrUpdate(data);
//                                    Toast.makeText(activity.getApplicationContext(), "Success get new Access Token", Toast.LENGTH_SHORT).show();
//                                    newRefreshToken = refreshToken;
//                                    if (refreshToken == newRefreshToken && !newRefreshToken.equals("")) {
//                                        login(data_token, activity, mAccountManager);
//                                    }
//
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        }
//                    });
//
//                    finalDialog1.dismiss();
//
//                } else {
//                    Toast.makeText(activity.getApplicationContext(), "Error 500, Server Error", Toast.LENGTH_SHORT).show();
//                    finalDialog1.dismiss();
//                }
//            }
//        }) {
//            @Override
//            public String getBodyContentType() {
//                return "application/json; charset=utf-8";
//            }
//
//            @Override
//            public byte[] getBody() throws AuthFailureError {
//                try {
//                    return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
//                } catch (UnsupportedEncodingException uee) {
//                    return null;
//                }
//            }
//
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                try {
//                    tokenRepo = new clsTokenRepo(activity.getApplicationContext());
//                    dataToken = (List<clsToken>) tokenRepo.findAll();
//                    access_token = dataToken.get(0).getTxtUserToken();
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
//                HashMap<String, String> headers = new HashMap<>();
//                headers.put("Authorization", "Bearer " + access_token);
//
//                return headers;
//            }
//        };
//        request.setRetryPolicy(new
//                DefaultRetryPolicy(60000,
//                0,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//
//        queue.add(request);
//    }

//    private void logout() {
//        Intent intent = new Intent(PickAccountActivity.this, SplashActivity.class);
//        finish();
//        startActivity(intent);
//    }

//    @Override
//    protected void onResume() {
//        Account[] tes = new AuthenticatorUtil().countingAccount(mAccountManager);
//        int countlistView = listView.getAdapter().getCount() -1;
//        if (tes.length<countlistView){
//            logout();
//        }
//        super.onResume();
//    }

}
